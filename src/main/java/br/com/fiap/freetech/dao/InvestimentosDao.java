package br.com.fiap.freetech.dao;

import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Investimentos;

import java.util.List;

public interface InvestimentosDao {
    void cadastrarInvestimento(Investimentos investimentos) throws DBException;
    void atualizarInvestimento(Investimentos investimentos) throws DBException;
    void removerInvestimento(int idInvestimento) throws DBException;
    Investimentos buscarInvestimento(int idInvestimento);
    List<Investimentos> listarPorUsuario(int idUsuario);
}
