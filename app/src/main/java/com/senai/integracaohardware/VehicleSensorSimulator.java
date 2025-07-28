package com.senai.integracaohardware;

import android.util.Log;

import java.util.Random;

/**
 * Classe que simula um sensor de veículo para fins de teste.
 * Pode simular sensores como velocidade, temperatura externa e nível de combustível.
 */
public class VehicleSensorSimulator {

    // Nome do sensor que está sendo simulado
    private final String vehicleSensor;

    private static final String TAG = "VehicleSensorSimulator";

    // Gerador de números aleatórios para simular os dados do sensor
    private final Random random;

    /**
     * Construtor do simulador de sensor.
     * @param vehicleSensor nome do sensor a ser simulado
     * @param random objeto Random para geração de valores aleatórios
     */
    public VehicleSensorSimulator(String vehicleSensor, Random random) {
        this.vehicleSensor = vehicleSensor;
        this.random = random;  // usar o Random passado como parâmetro
    }

    /**
     * Simula a leitura de dados do sensor do veículo.
     * Dependendo do tipo de sensor, retorna um valor aleatório dentro de um intervalo plausível.
     * @return valor inteiro simulando a leitura do sensor (ex: velocidade em km/h)
     */
    public int readSensorData() {
        int data;
        switch (vehicleSensor) {
            case "Velocidade":
                data = random.nextInt(201); // 0 a 200 km/h
                break;
            case "Temperatura Externa":
                data = random.nextInt(50) - 10; // -10 a 39 °C
                break;
            case "Nível Combustível":
                data = random.nextInt(101); // 0 a 100 %
                break;
            default:
                data = random.nextInt(101); // valor genérico 0 a 100
        }

        Log.d(TAG, "[" + vehicleSensor + "] Lendo dados do sensor: " + data);
        return data;
    }

    /**
     * Simula a calibração do sensor, com um atraso para representar o processo.
     */
    public void calibrateSensor() {
        Log.i(TAG, "[" + vehicleSensor + "] Calibrando sensor...");
        try {
            Thread.sleep(1000); // pausa de 1 segundo simulando calibração
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // respeita a interrupção da thread
        }
        Log.i(TAG, "[" + vehicleSensor + "] Sensor calibrado.");
    }
}
