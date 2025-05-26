package br.com.fiap.freetech.factory;

import br.com.fiap.freetech.dao.*;
import br.com.fiap.freetech.dao.impl.*;

public class DaoFactory {

    public static UsuarioDao getUsuarioDao() {
        return new OracleUsuarioDao();
    }
    public static BancoDao getBancoDao() {
        return new OracleBancoDao();
    }
    public static ContaBancariaDao getContaBancariaDao() {
        return new OracleContaBancariaDao();
    }
    public static TransacoesDao getTransacoesDao() {
        return new OracleTransacoesDao();
    }
    public static InvestimentosDao getInvestimentosDao() {
        return new OracleInvestimentosDao();
    }
}
