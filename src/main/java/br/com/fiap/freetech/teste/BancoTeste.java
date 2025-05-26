package br.com.fiap.freetech.teste;

import br.com.fiap.freetech.dao.BancoDao;
import br.com.fiap.freetech.factory.DaoFactory;
import br.com.fiap.freetech.models.Banco;

import java.util.List;

public class BancoTeste {
    public static void main(String[] args) {
        BancoDao dao = DaoFactory.getBancoDao();

        List<Banco> lista = dao.listar();
        for (Banco banco : lista) {
            System.out.println(banco.getIdBanco() + " " + banco.getNomeBanco() + " " + banco.getCompe());
        }
    }
}
