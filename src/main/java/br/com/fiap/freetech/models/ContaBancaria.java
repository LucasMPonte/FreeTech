package br.com.fiap.freetech.models;

public class ContaBancaria {
    private int idConta;
    private Banco idBanco;
    private Usuario idUsuario;
    private String tipoConta;
    private double saldo;

    public ContaBancaria() {
    }

    public ContaBancaria(int idConta, Banco idBanco, Usuario idUsuario, String tipoConta, double saldo) {
        this.idConta = idConta;
        this.idBanco = idBanco;
        this.idUsuario = idUsuario;
        this.tipoConta = tipoConta;
        this.saldo = saldo;
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public Banco getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Banco idBanco) {
        this.idBanco = idBanco;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
