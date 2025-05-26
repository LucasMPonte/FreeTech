package br.com.fiap.freetech.models;

import java.time.LocalDate;

public class Transacoes {
    private int idTransacao;
    private Usuario idUsuario;
    private ContaBancaria idContaBancaria;
    private String tipoTransacao;
    private double valorTransacao;
    private LocalDate dataTransacao;
    private String descricaoTransacao;

    public Transacoes(int idTransacao, Usuario idUsuario, ContaBancaria idContaBancaria, String tipoTransacao, double valorTransacao, LocalDate dataTransacao, String descricaoTransacao) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.idContaBancaria = idContaBancaria;
        this.tipoTransacao = tipoTransacao;
        this.valorTransacao = valorTransacao;
        this.dataTransacao = dataTransacao;
        this.descricaoTransacao = descricaoTransacao;
    }

    public Transacoes() {
    }

    public int getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ContaBancaria getIdContaBancaria() {
        return idContaBancaria;
    }

    public void setIdContaBancaria(ContaBancaria idContaBancaria) {
        this.idContaBancaria = idContaBancaria;
    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public double getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(double valorTransacao) {
        this.valorTransacao = valorTransacao;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getDescricaoTransacao() {
        return descricaoTransacao;
    }

    public void setDescricaoTransacao(String descricaoTransacao) {
        this.descricaoTransacao = descricaoTransacao;
    }
}
