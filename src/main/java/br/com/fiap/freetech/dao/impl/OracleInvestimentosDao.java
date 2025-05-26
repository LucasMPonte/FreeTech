package br.com.fiap.freetech.dao.impl;

import br.com.fiap.freetech.dao.ConnectionManager;
import br.com.fiap.freetech.dao.InvestimentosDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.models.Investimentos;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OracleInvestimentosDao implements InvestimentosDao {
    @Override
    public void cadastrarInvestimento(Investimentos investimentos) throws DBException {
        String sql = "INSERT INTO T_FIN_INVESTIMENTOS " +
                "(ID_INVESTIMENTO, ID_USUARIO, TIPO_INVESTIMENTO, VALOR_INVESTIDO, DATA_INVESTIMENTO, TAXA_RENDIMENTO, DATA_RESGATE)" +
                "VALUES (SEQ_INVESTIMENTOS.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, investimentos.getIdUsuario().getId());
            stmt.setString(2, investimentos.getTipoInvestimento());
            stmt.setDouble(3, investimentos.getValorInvestimento());
            stmt.setDate(4, Date.valueOf(investimentos.getDataInvestimento()));
            stmt.setDouble(5, investimentos.getTaxaRendimento());

            if (investimentos.getDataResgate() != null) {
                stmt.setDate(6, Date.valueOf(investimentos.getDataResgate()));
            } else {
                stmt.setDate(6, null);
            }

            stmt.executeUpdate();

            System.out.println("Investimento cadastrado com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao cadastrar investimento");
        }
    }

    @Override
    public void atualizarInvestimento(Investimentos investimentos) throws DBException {
        String sql = "UPDATE T_FIN_INVESTIMENTOS " +
                "SET TIPO_INVESTIMENTO = ?, " +
                "VALOR_INVESTIDO = ?, " +
                "DATA_INVESTIMENTO = ?, " +
                "TAXA_RENDIMENTO = ?, " +
                "DATA_RESGATE = ? " +
                "WHERE ID_INVESTIMENTO = ?";

        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, investimentos.getTipoInvestimento());
            stmt.setDouble(2, investimentos.getValorInvestimento());
            stmt.setDate(3, Date.valueOf(investimentos.getDataInvestimento()));
            stmt.setDouble(4, investimentos.getTaxaRendimento());

            if (investimentos.getDataResgate() != null &&
                    !investimentos.getDataResgate().isBefore(investimentos.getDataInvestimento())) {
                stmt.setDate(5, Date.valueOf(investimentos.getDataResgate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setInt(6, investimentos.getIdInvestimento());

            stmt.executeUpdate();
            System.out.println("Investimento atualizado com sucesso");
        } catch (SQLException e) {
            if (e.getMessage().contains("CK_TAXA_RENDIMENTO")) {
                System.err.println("Erro SQL ao atualizar investimento: ");
                e.printStackTrace();
            }
            throw new DBException("Erro ao atualizar investimento: ");
        }
    }

    @Override
    public void removerInvestimento(int idInvestimento) throws DBException {
        String sql = "DELETE FROM T_FIN_INVESTIMENTOS WHERE ID_INVESTIMENTO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idInvestimento);
            stmt.executeUpdate();

            System.out.println("Investimento removido com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Erro ao remover investimento");
        }
    }

    @Override
    public Investimentos buscarInvestimento(int idInvestimento) {
        Investimentos investimentos = null;
        String sql = "SELECT * FROM T_FIN_INVESTIMENTOS WHERE ID_INVESTIMENTO = ?";

        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idInvestimento);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Executando a consulta" + sql);
                if (rs.next()) {
                    String tipoInvestimento = rs.getString("TIPO_INVESTIMENTO");
                    double valorInvestido = rs.getDouble("VALOR_INVESTIDO");
                    LocalDate dataInvestimento = rs.getDate("DATA_INVESTIMENTO").toLocalDate();
                    double taxaRendimento = rs.getDouble("TAXA_RENDIMENTO");
                    LocalDate dataResgate = rs.getDate("DATA_RESGATE") != null ? rs.getDate("DATA_RESGATE").toLocalDate() : null;

                    investimentos = new Investimentos(idInvestimento, tipoInvestimento, valorInvestido, taxaRendimento, dataInvestimento, dataResgate);
                    return investimentos;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao executar consulta: " + e.getMessage());
        }
        return investimentos;
    }

    @Override
    public List<Investimentos> listarPorUsuario(int idUsuario) {
        List<Investimentos> lista = new ArrayList<>();
        Investimentos investimentos = null;
        String sql = "SELECT i.ID_INVESTIMENTO, i.TIPO_INVESTIMENTO, i.VALOR_INVESTIDO, " +
                "i.TAXA_RENDIMENTO, i.DATA_INVESTIMENTO, i.DATA_RESGATE " +
                "FROM T_FIN_INVESTIMENTOS i " +
                "JOIN T_FIN_USUARIO u ON i.ID_USUARIO = u.ID_USUARIO " +
                "WHERE i.ID_USUARIO = ?";
        try (Connection conexao = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idInvestimento = rs.getInt("ID_INVESTIMENTO");
                    String tipoInvestimento = rs.getString("TIPO_INVESTIMENTO");
                    double valorInvestido = rs.getDouble("VALOR_INVESTIDO");
                    double taxaRendimento = rs.getDouble("TAXA_RENDIMENTO");
                    LocalDate dataInvestimento = rs.getDate("DATA_INVESTIMENTO").toLocalDate();
                    LocalDate dataResgate = rs.getDate("DATA_RESGATE") != null ? rs.getDate("DATA_RESGATE").toLocalDate() : null;
                    investimentos = new Investimentos();
                    investimentos.setIdInvestimento(idInvestimento);
                    investimentos.setTipoInvestimento(tipoInvestimento);
                    investimentos.setValorInvestimento(valorInvestido);
                    investimentos.setTaxaRendimento(taxaRendimento);
                    investimentos.setDataInvestimento(dataInvestimento);
                    investimentos.setDataResgate(dataResgate);

                    lista.add(investimentos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
