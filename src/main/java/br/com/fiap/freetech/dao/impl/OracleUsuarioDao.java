package br.com.fiap.freetech.dao.impl;

import br.com.fiap.freetech.dao.ConnectionManager;
import br.com.fiap.freetech.dao.UsuarioDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Usuario;
import br.com.fiap.freetech.utils.CriptografiaUtils;

import java.sql.*;

public class OracleUsuarioDao implements UsuarioDao {

    @Override
    public void cadastrarUsuario(Usuario usuario) throws DBException {

        String sql = "INSERT INTO T_FIN_USUARIO (ID_USUARIO, NOME_USUARIO, EMAIL, SENHA, DATA_NASCIMENTO) " +
                "VALUES (SEQ_USUARIO.NEXTVAL, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, criptografarSenha(usuario.getSenha()));
            stmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao cadastrar usuário");
        }
    }

    @Override
    public void alterarDadosUsuario(Usuario usuario) throws DBException {
        String sql = "UPDATE T_FIN_USUARIO SET " +
                "NOME_USUARIO = ?," +
                "EMAIL = ?, " +
                "SENHA = ?, " +
                "DATA_NASCIMENTO = ?" +
                "WHERE ID_USUARIO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, criptografarSenha(usuario.getSenha()));
            stmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao atualizar usuário");
        }
    }

    @Override
    public void excluirUsuario(int id) throws DBException {
        Connection conexao = null;
        PreparedStatement stmtTransacoes = null;
        PreparedStatement stmtContaBancaria = null;
        PreparedStatement stmtInvestimentos = null;
        PreparedStatement stmtUsuario = null;

        try {
            conexao = ConnectionManager.getInstance().getConnection();
            conexao.setAutoCommit(false);

            String sqlTransacoes = "DELETE FROM T_FIN_TRANSACOES WHERE ID_USUARIO = ?";
            stmtTransacoes = conexao.prepareStatement(sqlTransacoes);
            stmtTransacoes.setInt(1, id);
            stmtTransacoes.executeUpdate();

            String sqlContaBancaria = "DELETE FROM T_FIN_CONTA_BANCARIA WHERE ID_USUARIO = ?";
            stmtContaBancaria = conexao.prepareStatement(sqlContaBancaria);
            stmtContaBancaria.setInt(1, id);
            stmtContaBancaria.executeUpdate();

            String sqlInvestimentos = "DELETE FROM T_FIN_INVESTIMENTOS WHERE ID_USUARIO = ?";
            stmtInvestimentos = conexao.prepareStatement(sqlInvestimentos);
            stmtInvestimentos.setInt(1, id);
            stmtInvestimentos.executeUpdate();

            String sqlUsuario = "DELETE FROM T_FIN_USUARIO WHERE ID_USUARIO = ?";
            stmtUsuario = conexao.prepareStatement(sqlUsuario);
            stmtUsuario.setInt(1, id);
            stmtUsuario.executeUpdate();

            conexao.commit();
        } catch (SQLException e) {
            try {
                if (conexao != null) {
                    conexao.rollback();
                }
            } catch (SQLException ex) {
                e.printStackTrace();
                throw new DBException("Erro ao realizar rollback");
            }
            e.printStackTrace();
            throw new DBException("Erro ao excluir usuário");
        }finally {
            try {
                if (stmtTransacoes != null) stmtTransacoes.close();
                if (stmtContaBancaria != null) stmtContaBancaria.close();
                if (stmtInvestimentos != null) stmtInvestimentos.close();
                if (stmtUsuario != null) stmtUsuario.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DBException("Erro ao fechar conexão");
            }
        }
    }

    @Override
    public boolean validarSenha(String email, String senha) throws DBException, SQLException {
        String sql = "SELECT SENHA FROM T_FIN_USUARIO WHERE EMAIL = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String senhaArmazenada = rs.getString("SENHA");
                    String senhaCriptografada = criptografarSenha(senha);

                    return senhaArmazenada.equals(senhaCriptografada);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DBException("Erro ao validar senha: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean validarUsuario(Usuario usuario) {
        String sql = "SELECT * FROM T_FIN_USUARIO WHERE EMAIL = ? AND SENHA = ?";

        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, criptografarSenha(usuario.getSenha()));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException | DBException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean emailExistente(String email) {
        String sql = "SELECT 1 FROM T_FIN_USUARIO WHERE EMAIL = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ;
        }
        return false;
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) throws DBException {
        String sql = "SELECT * FROM T_FIN_USUARIO WHERE EMAIL = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("ID_USUARIO"),
                            rs.getString("NOME_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getString("SENHA"),
                            rs.getDate("DATA_NASCIMENTO").toLocalDate()
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DBException("Erro ao buscar usuário por email");
        }
    }

    private String criptografarSenha(String senha) throws DBException {
        try {
            return CriptografiaUtils.criptografar(senha);
        } catch (Exception e) {
            throw new DBException("Erro ao crtiptografar a senha", e);
        }
    }
}
