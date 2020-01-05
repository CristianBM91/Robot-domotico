package com.naranjatradicionaldegandia.elias.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naranjatradicionaldegandia.elias.ambos.Dato;
import com.naranjatradicionaldegandia.elias.ambos.DatosFirestore;
import com.naranjatradicionaldegandia.elias.ambos.Estado;
import com.naranjatradicionaldegandia.elias.ambos.Imagen;
import com.naranjatradicionaldegandia.elias.ambos.Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.broker;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.clientId;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.qos;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.topicRoot;


/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends AppCompatActivity {
    private DoorbellCamera mCamera;
    private Handler mCameraHandler;
    public static Dato dato;
    public static Estado estado;
    private HandlerThread mCameraThread;
    private Handler temporizadorHandler = new Handler();
    public static DatosFirestore datos;
    public static MqttClient client;
    private Imagen imagen;
    public static String correo = "elies1324@gmail.com";
    private Handler mCloudHandler;
    private HandlerThread mCloudThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = new DatosFirestore();

        // Creates new handlers and associated threads for camera
        mCameraThread = new HandlerThread("CameraBackground");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
        // Camera code is complicated, so we've shoved it all in this closet class for you.
        mCamera = DoorbellCamera.getInstance();
        mCamera.initializeCamera(this, mCameraHandler, mOnImageAvailableListener);
        temporizadorHandler.postDelayed(tomaFoto, 3 * 1000); //llamamos en 3 seg.
        mCloudThread = new HandlerThread("CloudThread");
        mCloudThread.start();
        mCloudHandler = new Handler(mCloudThread.getLooper());
        //Arduino UART




        imagen = new Imagen();
        //mqtt
        //MQTT
        try {
            Log.i(TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, "Test21", new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            MyCallback callback = new MyCallback();
            client.setCallback(callback);
            connOpts.setKeepAliveInterval(300);
            connOpts.setWill(topicRoot + "WillTopic", "App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);


        } catch (MqttException e) {
            Log.e(TAG, "Error al conectar.", e);
        }







        try {
            Log.i(TAG, "Publicando mensaje: " + "hola desde rp");
            MqttMessage message = new MqttMessage("hola desde rp".getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + "Raspberry Pi", message);
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar.", e);
        }
        Log.v("TAG", "Publicando en MQTT");


    }

    void registrarImagen(String titulo, String url, String correo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        imagen.setTitulo(titulo);
        imagen.setUrl(url);
        imagen.setTiempo();
        imagen.setCorreo(correo);
        db.collection("Grabaciones").document().set(imagen);
    }

    private Runnable tomaFoto = new Runnable() {
        @Override
        public void run() {
            mCamera.takePicture();
            temporizadorHandler.postDelayed(tomaFoto, 60 * 1000);
            //Programamos siguiente llamada dentro de 60 segundos
        }
    };
    private ImageReader.OnImageAvailableListener
            mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
                    final byte[] imageBytes = new byte[imageBuf.remaining()];
                    imageBuf.get(imageBytes);
                    image.close();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    annotateImage(db.collection("Grabaciones"), imageBytes);
                    onPictureTaken(imageBytes);
                }
            };


    private void onPictureTaken(final byte[] imageBytes) {
        if (imageBytes != null) {
            String nombreFichero = UUID.randomUUID().toString();
            subirBytes(imageBytes, "imagenes/" + correo + "/" + nombreFichero);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(
                    imageBytes, 0, imageBytes.length);

        }
    }

    private void subirBytes(final byte[] bytes, String referencia) {
        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageRef.child(referencia);
        UploadTask uploadTask = ref.putBytes(bytes);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull
                                          Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) throw task.getException();
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.e("Almacenamiento", "URL: " + downloadUri.toString());
                    registrarImagen(correo, downloadUri.toString(), correo);
                } else {
                    Log.e("Almacenamiento", "ERROR: subiendo bytes");
                }
            }
        });
    }


    private void annotateImage(final CollectionReference ref, final byte[] imageBytes) {
        mCloudHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "sending image to cloud vision");
                // annotate image by uploading to Cloud Vision API
                try {
                    Map<String, Float> annotations = CloudVisionUtils.annotateImage(imageBytes);
                    Log.d(TAG, "cloud vision annotations:" + annotations);
                    if (annotations != null) {
                        imagen.setAnotaciones(annotations);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Cloud Vison API error: ", e);
                }
            }
        });
    }


}