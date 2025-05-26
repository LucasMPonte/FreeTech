package br.com.fiap.freetech.teste;

import br.com.fiap.freetech.dao.TransacoesDao;
import br.com.fiap.freetech.factory.DaoFactory;

public class Transacoesteste {
    public static void main(String[] args) {
        TransacoesDao dao = DaoFactory.getTransacoesDao();

        dao.buscarTransacao(5);
    }
}
