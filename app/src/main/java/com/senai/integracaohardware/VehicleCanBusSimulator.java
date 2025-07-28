package com.senai.integracaohardware;

import android.util.Log;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class VehicleCanBusSimulator {
    // Tag para logs, facilita a identificação das mensagens no Logcat
    private static final String TAG = "VehicleCanBusSimulator";

    // Fila bloqueante para armazenar as mensagens CAN recebidas
    // Esta fila garante que as mensagens sejam processadas em ordem e com controle de concorrência
    private final BlockingQueue<CanMessage> messageQueue = new LinkedBlockingQueue<>();

    // ExecutorService gerencia um pool de threads para processar tarefas assincronamente
    // Usamos um pool fixo com 2 threads para enviar e processar mensagens CAN simultaneamente
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    // Listener que será notificado sempre que uma mensagem CAN for processada
    // Funciona como uma callback para atualizar UI ou outras partes do sistema
    private Consumer<CanMessage> canMessageListener;

    // Construtor da classe, inicia o simulador CAN numa thread separada
    public VehicleCanBusSimulator() {
        executorService.submit(() -> {
            Log.d(TAG, "Simulador CAN iniciado");

            try {
                // Loop infinito para processar mensagens CAN da fila
                while (true) {
                    // Espera e retira a próxima mensagem da fila (bloqueante)
                    CanMessage message = messageQueue.take();

                    // Log detalhado da mensagem recebida: ID e dados formatados em hex
                    Log.d(TAG, "Mensagem CAN recebida (ID: 0x" + Integer.toHexString(message.getId())
                            + ", Dados: " + formatData(message.getData()) + ")");

                    // Se houver listener registrado, notifica-o com a mensagem recebida
                    if (canMessageListener != null) {
                        canMessageListener.accept(message);
                    }

                    // Simula o processamento da mensagem baseada no seu ID
                    // Aqui exemplificamos com o ID 0x123 que representa o volume mestre
                    switch (message.getId()) {
                        case 0x123:
                            if (message.getData().length > 0) {
                                // Extrai o volume do primeiro byte da mensagem (converte para int)
                                int volume = message.getData()[0] & 0xFF;

                                // Log do volume processado
                                Log.d(TAG, "Processando mensagem CAN: Novo Volume Mestre: " + volume);

                                // Aqui você poderia implementar mais lógica para manipular esse volume
                            }
                            break;

                        // Outros IDs podem ser tratados aqui adicionando mais cases
                    }
                }
            } catch (InterruptedException e) {
                // Quando a thread for interrompida, sai do loop e encerra o simulador
                Log.d(TAG, "Thread do simulador CAN interrompida.");
            }
        });
    }

    /**
     * Método para enviar uma mensagem CAN para o simulador.
     * A mensagem é colocada na fila para ser processada pela thread do simulador.
     * @param message mensagem CAN a ser enviada
     */
    public void sendMessage(CanMessage message) {
        Log.d(TAG, "Chamou sendMessage() no simulador CAN");

        // Envia a mensagem assincronamente usando o executor
        executorService.submit(() -> {
            Log.d(TAG, "Enviando mensagem CAN (ID: 0x" + Integer.toHexString(message.getId())
                    + ", Dados: " + formatData(message.getData()) + ")");
            try {
                // Coloca a mensagem na fila bloqueante (pode bloquear se a fila estiver cheia)
                messageQueue.put(message);
            } catch (InterruptedException e) {
                // Se a thread for interrompida durante a espera, marca o estado de interrupção e loga erro
                Thread.currentThread().interrupt();
                Log.e(TAG, "Falha ao enviar mensagem CAN.");
            }
        });
    }

    /**
     * Método para parar o simulador.
     * Encerra as threads do executor imediatamente e libera os recursos.
     */
    public void stopSimulator() {
        executorService.shutdownNow();
        Log.d(TAG, "Simulador CAN parado.");
    }

    /**
     * Registra um listener para ser notificado quando uma mensagem CAN for processada.
     * Normalmente usado para atualizar UI ou outras partes do sistema.
     * @param listener implementação da interface Consumer para receber as mensagens CAN
     */
    public void setCanMessageListener(Consumer<CanMessage> listener) {
        this.canMessageListener = listener;
    }

    /**
     * Método auxiliar para formatar dados da mensagem CAN em string hexadecimal legível.
     * @param data array de bytes da mensagem CAN
     * @return string formatada, ex: "0A 1F 4B"
     */
    private String formatData(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
