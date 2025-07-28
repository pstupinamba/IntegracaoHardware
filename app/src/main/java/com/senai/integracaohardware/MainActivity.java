package com.senai.integracaohardware;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IntegracaoHardware";

    // Componentes visuais da interface
    private TextView speedLabel;           // Label para mostrar a velocidade simulada
    private Button readSpeedButton;        // Botão para atualizar a velocidade
    private TextView canVolumeLabel;       // Label para mostrar o volume recebido via CAN
    private Button sendCanVolumeButton;    // Botão para enviar volume via CAN (simulado)

    // Simuladores usados para gerar dados e simular a rede CAN
    private final VehicleSensorSimulator vehicleSensorSimulator = new VehicleSensorSimulator("Velocidade", new Random());
    private final VehicleCanBusSimulator vehicleCanBusSimulator = new VehicleCanBusSimulator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita o modo Edge to Edge para aproveitar toda a área da tela
        EdgeToEdge.enable(this);

        // Define o layout principal da Activity
        setContentView(R.layout.activity_main);

        // Ajusta o padding para considerar áreas como status bar e barra de navegação
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencia os componentes visuais pelo ID definido no layout XML
        speedLabel = findViewById(R.id.speedLabel);
        readSpeedButton = findViewById(R.id.readSpeedButton);
        canVolumeLabel = findViewById(R.id.canVolumeLabel);
        sendCanVolumeButton = findViewById(R.id.sendCanVolumeButton);

        // Configura a ação do botão para ler velocidade simulada
        readSpeedButton.setOnClickListener(v -> {
            // Lê um valor aleatório de velocidade pelo simulador
            int currentSpeed = vehicleSensorSimulator.readSensorData();

            // Atualiza a label na UI com o valor lido
            speedLabel.setText("Velocidade Atual: " + currentSpeed + " km/h");

            // Log para debug
            Log.d(TAG, "Velocidade lida: " + currentSpeed + " km/h");
        });

        // Configura a ação do botão para enviar uma mensagem CAN simulada
        sendCanVolumeButton.setOnClickListener(v -> {
            // Gera um volume aleatório entre 0 e 100
            int randomVolume = new Random().nextInt(101);

            // Prepara os dados para enviar: um array de bytes com o volume
            byte[] data = new byte[]{(byte) randomVolume};

            // Cria a mensagem CAN com ID 0x123 e os dados do volume
            CanMessage message = new CanMessage(0x123, data);

            Log.d(TAG, "Botão de envio CAN clicado!");

            // Envia a mensagem pelo simulador de CAN
            vehicleCanBusSimulator.sendMessage(message);
        });

        // Registra um listener para receber mensagens CAN simuladas
        vehicleCanBusSimulator.setCanMessageListener(message -> {
            // Atualiza a UI na thread principal
            runOnUiThread(() -> {
                // Se a mensagem recebida for a do volume mestre (ID 0x123)
                if (message.getId() == 0x123 && message.getData().length > 0) {
                    // Extrai o volume do primeiro byte dos dados
                    int volume = message.getData()[0] & 0xFF;

                    // Atualiza a label com o volume recebido
                    canVolumeLabel.setText("Volume CAN: " + volume);
                }
            });
        });

    }
}
