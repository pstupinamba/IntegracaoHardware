package com.senai.integracaohardware;

import java.util.Arrays;

/**
 * Classe que representa uma mensagem CAN (Controller Area Network).
 * Contém um identificador (ID) e um array de bytes com os dados da mensagem.
 */
public class CanMessage {

    // Identificador da mensagem CAN, geralmente um valor numérico único
    private int id;

    // Dados da mensagem, representados como array de bytes
    private byte[] data;

    /**
     * Construtor da mensagem CAN.
     * @param id identificador da mensagem
     * @param data array de bytes com os dados da mensagem
     */
    public CanMessage(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    /**
     * Retorna o ID da mensagem.
     * @return inteiro representando o ID
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o array de dados da mensagem.
     * @return array de bytes com os dados
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Define o ID da mensagem.
     * @param id novo valor para o ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Define os dados da mensagem.
     * @param data novo array de bytes para os dados
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Método sobrescrito para comparar se dois objetos CanMessage são iguais.
     * São iguais se o ID e os dados forem iguais.
     * @param o objeto a ser comparado
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // mesma referência, logo iguais
        if (!(o instanceof CanMessage)) return false; // tipo diferente, logo não iguais
        CanMessage that = (CanMessage) o;
        // compara ID e conteúdo do array de dados
        return id == that.id && Arrays.equals(data, that.data);
    }

    /**
     * Gera um código hash para o objeto, baseado no ID e no conteúdo do array de dados.
     * Útil para armazenar em estruturas baseadas em hash (exemplo: HashMap).
     * @return código hash inteiro
     */
    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
