package com.naranjatradicionaldegandia.elias.ambos;

import android.util.Log;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.broker;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.clientId;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.qos;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.topicRoot;

public class Robot {

    public static MqttClient client;


    public static void girarDerecha(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando GirarDerecha a robot");
            String mensaje = "giroderecha";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void girarIzquierda(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando GirarIzquierda a robot");
            String mensaje = "giroizquierda";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void avanzar(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando avanzar a robot");
            String mensaje = "giroderecha";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void activarModoAutomatico(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando modo automatico ");
            String mensaje = "0x0001";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot, message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }
    public static void parar(){

        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Enviando avanzar a robot");
            String mensaje = "parar";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"estado", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
    }

}
