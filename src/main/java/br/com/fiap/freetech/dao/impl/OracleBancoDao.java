package br.com.fiap.freetech.dao.impl;

import br.com.fiap.freetech.dao.BancoDao;
import br.com.fiap.freetech.dao.ConnectionManager;
import br.com.fiap.freetech.models.Banco;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleBancoDao implements BancoDao {
    @Override
    public List<Banco> listar() {
        List<Banco> lista = new ArrayList<Banco>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try (Connection conexao = ConnectionManager.getInstance().getConnection()) {
            String sql = "SELECT * FROM T_FIN_BANCO";
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID_BANCO");
                String nome = rs.getString("NOME_BANCO");
                int compe = rs.getInt("COMPE");
                Banco banco = new Banco(id, nome, compe);
                lista.add(banco);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                stmt.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }
}
