package br.com.fiap.freetech.dao.impl;

import br.com.fiap.freetech.dao.ConnectionManager;
import br.com.fiap.freetech.dao.ContaBancariaDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Banco;
import br.com.fiap.freetech.models.ContaBancaria;
import br.com.fiap.freetech.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleContaBancariaDao implements ContaBancariaDao {
    @Override
    public void cadastrarContaBancaria(ContaBancaria contaBancaria) throws DBException {
        String sql = "INSERT INTO T_FIN_CONTA_BANCARIA " +
                "(ID_CONTA, ID_BANCO, ID_USUARIO, TIPO_CONTA, SALDO) " +
                "VALUES (SEQ_CONTA_BANCARIA.NEXTVAL, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contaBancaria.getIdBanco().getIdBanco());
            stmt.setInt(2, contaBancaria.getIdUsuario().getId());
            stmt.setString(3, contaBancaria.getTipoConta());
            stmt.setDouble(4, contaBancaria.getSaldo());
            stmt.executeUpdate();

            System.out.println("Conta cadastrada com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao cadastrar conta bancária");
        }
    }

    @Override
    public void atualizarContaBancaria(ContaBancaria contaBancaria) throws DBException {
        String sql = "UPDATE T_FIN_CONTA_BANCARIA SET " +
                "ID_BANCO = ?, " +
                "TIPO_CONTA = ?, " +
                "SALDO = ? " +
                "WHERE ID_CONTA = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contaBancaria.getIdBanco().getIdBanco());
            stmt.setString(2, contaBancaria.getTipoConta());
            stmt.setDouble(3, contaBancaria.getSaldo());
            stmt.setInt(4, contaBancaria.getIdConta());
            stmt.executeUpdate();

            System.out.println("Conta atualizada com sucesso");
        } catch (SQLException e) {
            throw new DBException("Erro ao atualizar conta");
        }
    }

    @Override
    public void removerContaBancaria(int idConta) throws DBException {
        String sql = "DELETE FROM T_FIN_CONTA_BANCARIA WHERE ID_CONTA = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            stmt.executeUpdate();

            System.out.println("Conta removida com sucesso");
        } catch (SQLException e) {
            throw new DBException("Erro ao remover conta");
        }
    }

    @Override
    public ContaBancaria buscarContaBancaria(int idConta) {
        ContaBancaria contaBancaria = null;
        String sql = "SELECT * FROM T_FIN_CONTA_BANCARIA cb " +
                "INNER JOIN T_FIN_BANCO b on cb.ID_BANCO = b.ID_BANCO " +
                "WHERE cb.ID_CONTA = ?";

        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idConta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("ID_CONTA");
                    String tipoConta = rs.getString("TIPO_CONTA");
                    double saldo = rs.getDouble("SALDO");
                    int idBanco = rs.getInt("ID_BANCO");
                    String nomeBanco = rs.getString("NOME_BANCO");
                    int compe = rs.getInt("COMPE");

                    Banco banco = new Banco(idBanco, nomeBanco, compe);

                    contaBancaria = new ContaBancaria();
                    contaBancaria.setIdConta(id);
                    contaBancaria.setTipoConta(tipoConta);
                    contaBancaria.setSaldo(saldo);
                    contaBancaria.setIdBanco(banco);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contaBancaria;
    }

    @Override
    public List<ContaBancaria> listarPorUsuario(int idUsuario) {
        List<ContaBancaria> listaContas = new ArrayList<ContaBancaria>();
        String sql = "SELECT cb.ID_CONTA, cb.TIPO_CONTA, cb.SALDO, " +
                "b.ID_BANCO, b.NOME_BANCO, b.COMPE " +
                "FROM T_FIN_CONTA_BANCARIA cb " +
                "JOIN T_FIN_BANCO b on cb.ID_BANCO = b.ID_BANCO " +
                "WHERE cb.ID_USUARIO = ?";

        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Banco banco = new Banco(
                            rs.getInt("ID_BANCO"),
                            rs.getString("NOME_BANCO"),
                            rs.getInt("COMPE")
                    );

                    ContaBancaria conta = new ContaBancaria();
                    conta.setIdConta(rs.getInt("ID_CONTA"));
                    conta.setIdBanco(banco);
                    conta.setTipoConta(rs.getString("TIPO_CONTA"));
                    conta.setSaldo(rs.getDouble("SALDO"));

                    Usuario usuario = new Usuario();
                    usuario.setId(idUsuario);
                    conta.setIdUsuario(usuario);

                    listaContas.add(conta);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contas bancárias", e);
        }
        return listaContas;
    }
}
