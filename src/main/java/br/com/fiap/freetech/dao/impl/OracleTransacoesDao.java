package br.com.fiap.freetech.dao.impl;

import br.com.fiap.freetech.dao.ConnectionManager;
import br.com.fiap.freetech.dao.TransacoesDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Banco;
import br.com.fiap.freetech.models.ContaBancaria;
import br.com.fiap.freetech.models.Transacoes;
import br.com.fiap.freetech.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleTransacoesDao implements TransacoesDao {
    @Override
    public void cadastrarTransacao(Transacoes transacoes) throws DBException {
        String sql = "INSERT INTO T_FIN_TRANSACOES (ID_TRANSACAO, ID_USUARIO, ID_CONTA, TIPO_TRANSACAO, VALOR_TRANSACAO, DATA_TRANSACAO, DESCRICAO_TRANSACAO) " +
                "VALUES (SEQ_TRANSACOES.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, transacoes.getIdUsuario().getId());
            stmt.setInt(2, transacoes.getIdContaBancaria().getIdConta());
            stmt.setString(3, transacoes.getTipoTransacao());
            stmt.setDouble(4, transacoes.getValorTransacao());
            stmt.setDate(5, java.sql.Date.valueOf(transacoes.getDataTransacao()));
            stmt.setString(6, transacoes.getDescricaoTransacao());
            stmt.executeUpdate();

            System.out.println("Transação cadastrada com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao cadastrar transação");
        }
    }

    @Override
    public void atualizarTransacao(Transacoes transacoes) throws DBException {
        String sql = "UPDATE T_FIN_TRANSACOES SET " +
                "TIPO_TRANSACAO = ?, " +
                "VALOR_TRANSACAO = ?, " +
                "DESCRICAO_TRANSACAO = ?, " +
                "DATA_TRANSACAO = ? " +
                "WHERE ID_TRANSACAO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, transacoes.getTipoTransacao());
            stmt.setDouble(2, transacoes.getValorTransacao());
            stmt.setString(3, transacoes.getDescricaoTransacao());
            stmt.setDate(4, java.sql.Date.valueOf(transacoes.getDataTransacao()));
            stmt.setInt(5, transacoes.getIdTransacao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao atualizar transação" + e.getMessage(), e);
        }
    }

    @Override
    public void removerTransacao(int idTransacao) throws DBException {
        String sql = "DELETE FROM T_FIN_TRANSACOES WHERE ID_TRANSACAO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idTransacao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Erro ao remover transação");
        }
    }

    @Override
    public Transacoes buscarTransacao(int idTransacao) {
        Transacoes transacoes = null;
        String sql = "SELECT t.ID_TRANSACAO, t.TIPO_TRANSACAO, t.VALOR_TRANSACAO, t.DATA_TRANSACAO, t.DESCRICAO_TRANSACAO, " +
                "cb.ID_CONTA, cb.SALDO, b.NOME_BANCO " +
                "FROM T_FIN_TRANSACOES t " +
                "INNER JOIN T_FIN_CONTA_BANCARIA cb ON t.ID_CONTA = cb.ID_CONTA " +
                "INNER JOIN T_FIN_BANCO b ON cb.ID_BANCO = b.ID_BANCO " +
                "WHERE t.ID_TRANSACAO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idTransacao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    transacoes = new Transacoes();
                    transacoes.setIdTransacao(rs.getInt("ID_TRANSACAO"));
                    transacoes.setTipoTransacao(rs.getString("TIPO_TRANSACAO"));
                    transacoes.setValorTransacao(rs.getDouble("VALOR_TRANSACAO"));
                    transacoes.setDataTransacao(rs.getDate("DATA_TRANSACAO").toLocalDate());
                    transacoes.setDescricaoTransacao(rs.getString("DESCRICAO_TRANSACAO"));

                    int idConta = rs.getInt("ID_CONTA");
                    double saldoConta = rs.getDouble("SALDO");
                    String nomeBanco = rs.getString("NOME_BANCO");

                    Banco banco = new Banco();
                    banco.setNomeBanco(nomeBanco);

                    ContaBancaria contaBancaria = new ContaBancaria();
                    contaBancaria.setIdConta(idConta);
                    contaBancaria.setSaldo(saldoConta);
                    contaBancaria.setIdBanco(banco);

                    transacoes.setIdContaBancaria(contaBancaria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transacoes;
    }

    @Override
    public List<Transacoes> listarPorContaBancaria(int idContaBancaria) {
        List<Transacoes> lista = new ArrayList<>();
        String sql = "SELECT t.ID_TRANSACAO, t.TIPO_TRANSACAO, t.VALOR_TRANSACAO, t.DATA_TRANSACAO, t.DESCRICAO_TRANSACAO, " +
                "cb.ID_CONTA, cb.SALDO, b.NOME_BANCO " +
                "FROM T_FIN_TRANSACOES t " +
                "INNER JOIN T_FIN_CONTA_BANCARIA cb ON t.ID_CONTA = cb.ID_CONTA " +
                "INNER JOIN T_FIN_BANCO b ON cb.ID_BANCO = b.ID_BANCO " +
                "WHERE t.ID_CONTA = ?";

        try(Connection conexao = ConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idContaBancaria);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacoes transacoes = new Transacoes();
                    transacoes.setIdTransacao(rs.getInt("ID_TRANSACAO"));
                    transacoes.setTipoTransacao(rs.getString("TIPO_TRANSACAO"));
                    transacoes.setValorTransacao(rs.getDouble("VALOR_TRANSACAO"));
                    transacoes.setDataTransacao(rs.getDate("DATA_TRANSACAO").toLocalDate());
                    transacoes.setDescricaoTransacao(rs.getString("DESCRICAO_TRANSACAO"));

                    ContaBancaria conta = new ContaBancaria();
                    conta.setIdConta(idContaBancaria);
                    conta.setSaldo(rs.getDouble("SALDO"));

                    lista.add(transacoes);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public List<Transacoes> listarPorUsuario(int idUsuario) {
        List<Transacoes> lista = new ArrayList<>();
        String sql = "SELECT t.ID_TRANSACAO, t.TIPO_TRANSACAO, t.VALOR_TRANSACAO, t.DATA_TRANSACAO, t.DESCRICAO_TRANSACAO, " +
                "cb.ID_CONTA, cb.SALDO, b.NOME_BANCO " +
                "FROM T_FIN_TRANSACOES t " +
                "INNER JOIN T_FIN_CONTA_BANCARIA cb ON t.ID_CONTA = cb.ID_CONTA " +
                "INNER JOIN T_FIN_BANCO b ON cb.ID_BANCO = b.ID_BANCO " +
                "INNER JOIN T_FIN_USUARIO u ON t.ID_USUARIO = u.ID_USUARIO " +
                "WHERE t.ID_USUARIO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacoes transacoes = new Transacoes();
                    transacoes.setIdTransacao(rs.getInt("ID_TRANSACAO"));
                    transacoes.setTipoTransacao(rs.getString("TIPO_TRANSACAO"));
                    transacoes.setValorTransacao(rs.getDouble("VALOR_TRANSACAO"));
                    transacoes.setDataTransacao(rs.getDate("DATA_TRANSACAO").toLocalDate());
                    transacoes.setDescricaoTransacao(rs.getString("DESCRICAO_TRANSACAO"));

                    ContaBancaria conta = new ContaBancaria();
                    conta.setIdConta(rs.getInt("ID_CONTA"));
                    transacoes.setIdContaBancaria(conta);

                    Usuario usuario = new Usuario();
                    usuario.setId(idUsuario);

                    lista.add(transacoes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
