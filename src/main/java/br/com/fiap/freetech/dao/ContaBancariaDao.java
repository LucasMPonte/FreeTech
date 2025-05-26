package br.com.fiap.freetech.dao;

import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.ContaBancaria;

import java.util.List;

public interface ContaBancariaDao {
    void cadastrarContaBancaria (ContaBancaria contaBancaria) throws DBException;
    void atualizarContaBancaria (ContaBancaria contaBancaria) throws DBException;
    void removerContaBancaria (int idConta) throws DBException;
    ContaBancaria buscarContaBancaria (int idConta);
    List<ContaBancaria> listarPorUsuario (int idUsuario);

}
