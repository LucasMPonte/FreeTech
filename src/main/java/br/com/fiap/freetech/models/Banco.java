package br.com.fiap.freetech.models;

public class Banco {
    private int idBanco;
    private String nomeBanco;
    private int compe;

    public Banco(int idBanco, String nomeBanco, int compe) {
        this.idBanco = idBanco;
        this.nomeBanco = nomeBanco;
        this.compe = compe;
    }

    public Banco() {
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public int getCompe() {
        return compe;
    }

    public void setCompe(int compe) {
        this.compe = compe;
    }
}
