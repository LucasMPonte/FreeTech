package br.com.fiap.freetech.dao;

import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Usuario;

import java.sql.SQLException;

public interface UsuarioDao {

    void cadastrarUsuario(Usuario usuario) throws DBException;
    void alterarDadosUsuario(Usuario usuario) throws DBException;
    void excluirUsuario(int id) throws DBException;
    boolean validarSenha(String email, String senha) throws DBException, SQLException;
    boolean validarUsuario(Usuario usuario);
    boolean emailExistente(String email);
    Usuario buscarUsuarioPorEmail(String email) throws DBException;
}
