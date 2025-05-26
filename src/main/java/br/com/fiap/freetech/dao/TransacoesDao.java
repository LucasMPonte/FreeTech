package br.com.fiap.freetech.dao;

import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Transacoes;

import java.util.List;

public interface TransacoesDao {
    void cadastrarTransacao (Transacoes transacoes) throws DBException;
    void atualizarTransacao (Transacoes transacoes) throws DBException;
    void removerTransacao (int idTransacao) throws DBException;
    Transacoes buscarTransacao (int idTransacao);
    List<Transacoes> listarPorContaBancaria (int idContaBancaria);
    List<Transacoes> listarPorUsuario (int idUsuario);
}
