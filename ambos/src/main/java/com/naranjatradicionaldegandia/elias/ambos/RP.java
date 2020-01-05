package com.naranjatradicionaldegandia.elias.ambos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.broker;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.clientId;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.qos;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.topicRoot;

public class RP {

    public static MqttClient client;

    public static void tomarFoto(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando foto a RP");
            String mensaje = "foto";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
            client.publish(topicRoot, message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void tomarFotoTemporal(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando fototemporal a RP");
            String mensaje = "fototemporal";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void apagar(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando apagar a RP");
            String mensaje = "apagar";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void encender(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando encender a RP");
            String mensaje = "encender";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
}
