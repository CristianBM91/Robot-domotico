package com.naranjatradicionaldegandia.elias.iot;

import android.util.Log;

import com.naranjatradicionaldegandia.elias.ambos.Dato;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import static android.content.ContentValues.TAG;
import static com.naranjatradicionaldegandia.elias.iot.MainActivity.dato;
import static com.naranjatradicionaldegandia.elias.iot.MainActivity.datos;

public class MyCallback implements MqttCallback {

    @Override public void connectionLost(Throwable throwable) {
        Log.d("MQTT", "CONEXION PERDIDA");
    }

    @Override public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.d("MQTT", "HA LLEGADO");
        // **Do something with the message**
        String payload = new String(mqttMessage.getPayload());
        Log.d(TAG, "Recibiendo: " + topic + "->" + payload);
        if(payload.equals("0x1000")) {
           // mCamera.takePicture();
            //estado = new Estado(payload);
            //estados.anyade(estado);
        }
        else if (topic.equals("distancia")){
            dato = new Dato(topic, payload);
            datos.anyade(dato);
        }
        else if (topic.equals("luz")){
            dato = new Dato(topic, payload);
            datos.anyade(dato);
        } else if(topic.equals("modo/correo")) {
            //  correo = payload;
        }
    }


    @Override public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d("MQTT", "DELIVE");
    }
}