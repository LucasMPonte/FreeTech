package br.com.fiap.freetech.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Investimentos {
    private int idInvestimento;
    private Usuario idUsuario;
    private String tipoInvestimento;
    private double valorInvestimento;
    private double taxaRendimento;
    private LocalDate dataInvestimento;
    private LocalDate dataResgate;

    public Investimentos(int idInvestimento, Usuario idUsuario, String tipoInvestimento, double valorInvestimento, double taxaRendimento, LocalDate dataInvestimento, LocalDate dataResgate) {
        this.idInvestimento = idInvestimento;
        this.idUsuario = idUsuario;
        this.tipoInvestimento = tipoInvestimento;
        this.valorInvestimento = valorInvestimento;
        this.taxaRendimento = taxaRendimento;
        this.dataInvestimento = dataInvestimento;
        this.dataResgate = dataResgate;
    }

    public Investimentos(int idInvestimento, String tipoInvestimento, double valorInvestimento, double taxaRendimento, LocalDate dataResgate, LocalDate dataInvestimento) {
        this.idInvestimento = idInvestimento;
        this.tipoInvestimento = tipoInvestimento;
        this.valorInvestimento = valorInvestimento;
        this.taxaRendimento = taxaRendimento;
        this.dataInvestimento = dataInvestimento;
        this.dataResgate = dataResgate;
    }

    public Investimentos() {
    }

    public int getIdInvestimento() {
        return idInvestimento;
    }

    public void setIdInvestimento(int idInvestimento) {
        this.idInvestimento = idInvestimento;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoInvestimento() {
        return tipoInvestimento;
    }

    public void setTipoInvestimento(String tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    public double getValorInvestimento() {
        return valorInvestimento;
    }

    public void setValorInvestimento(double valorInvestimento) {
        this.valorInvestimento = valorInvestimento;
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    public LocalDate getDataInvestimento() {
        return dataInvestimento;
    }

    public void setDataInvestimento(LocalDate dataInvestimento) {
        this.dataInvestimento = dataInvestimento;
    }

    public LocalDate getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDate dataResgate) {
        this.dataResgate = dataResgate;
    }

    public double getRendimentoEstimado() {
        if (dataInvestimento == null || dataResgate == null) {
            return valorInvestimento * (taxaRendimento / 100.0);
        }

        double anos = ChronoUnit.DAYS.between(dataInvestimento, dataResgate) / 365.0;

        double taxaDecimal = taxaRendimento / 100.0;

        double montante = valorInvestimento * Math.pow(1 + taxaDecimal, anos);
        double rendimento = montante - valorInvestimento;

        return rendimento;
    }
}
