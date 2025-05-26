package br.com.fiap.freetech.controller;

import br.com.fiap.freetech.dao.BancoDao;
import br.com.fiap.freetech.dao.ContaBancariaDao;
import br.com.fiap.freetech.dao.UsuarioDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.factory.DaoFactory;
import br.com.fiap.freetech.models.Banco;
import br.com.fiap.freetech.models.ContaBancaria;
import br.com.fiap.freetech.models.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/conta-bancaria")
public class ContaBancariaServlet extends HttpServlet {
    private ContaBancariaDao contaBancariaDao;
    private BancoDao bancoDao;
    private UsuarioDao usuarioDao;

    @Override
    public void init() throws ServletException {
        contaBancariaDao = DaoFactory.getContaBancariaDao();
        bancoDao = DaoFactory.getBancoDao();
        usuarioDao = DaoFactory.getUsuarioDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");

        switch (acao) {
            case "cadastrar":
                cadastrar(req, resp);
                break;
            case "editar":
                editar(req, resp);
                break;
            case "excluir":
                excluir(req, resp);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");

        switch (acao) {
            case "abrir-form-cadastro-conta":
                abrirFormCadastroConta(req, resp);
                break;
            case "listar":
                listar(req, resp);
                break;
            case "abrir-form-edicao-conta":
                abrirFormEdicao(req, resp);
                break;
        }
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email != null) {
            try {
                Usuario usuarioLogado = usuarioDao.buscarUsuarioPorEmail(email);
                if (usuarioLogado != null) {
                    int idBancoSelecionado = Integer.parseInt(req.getParameter("banco"));
                    String tipoConta = req.getParameter("tipo-conta");
                    double saldo = Double.parseDouble(req.getParameter("saldo"));
                    Banco bancoSelecionado = new Banco();
                    bancoSelecionado.setIdBanco(idBancoSelecionado);
                    ContaBancaria contaBancaria = new ContaBancaria(
                            0,
                            bancoSelecionado,
                            usuarioLogado,
                            tipoConta,
                            saldo
                    );
                    try {
                        contaBancariaDao.cadastrarContaBancaria(contaBancaria);
                        req.setAttribute("mensagem", "Conta cadastrada com sucesso!");
                    } catch (DBException e) {
                        e.printStackTrace();
                        req.setAttribute("erro", "Erro ao cadastrar conta");
                    }
                }
            } catch (DBException e) {
                throw new RuntimeException(e);
            }
            abrirFormCadastroConta(req, resp);
        }
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        try {
            String idContaStr = req.getParameter("id-conta");
            String idBancoStr = req.getParameter("bancos");
            String tipoContaStr = req.getParameter("tipo-conta");
            String saldoStr = req.getParameter("saldo");

            if (idContaStr == null || idBancoStr == null || tipoContaStr == null || saldoStr == null) {
                throw new DBException("Todos os campos devem ser preenchidos.");
            }

            System.out.println("ID Conta recebido: " + idContaStr);
            System.out.println("ID Banco recebido: " + idBancoStr);
            System.out.println("Tipo Conta recebido: " + tipoContaStr);
            System.out.println("Saldo recebido: " + saldoStr);

            int idConta = Integer.parseInt(idContaStr);
            int idBanco = Integer.parseInt(idBancoStr);
            String tipoConta = tipoContaStr;
            double saldo = Double.parseDouble(saldoStr);

            if (idBanco == 0) {
                throw new DBException("Selecione um banco.");
            }

            Banco banco = new Banco();
            banco.setIdBanco(idBanco);

            ContaBancaria conta = new ContaBancaria();
            conta.setIdConta(idConta);
            conta.setIdBanco(banco);
            conta.setTipoConta(tipoConta);
            conta.setSaldo(saldo);

            contaBancariaDao.atualizarContaBancaria(conta);

            req.setAttribute("mensagem", "Conta atualizada com sucesso!");
            resp.sendRedirect("conta-bancaria?acao=abrir-form-edicao-conta&id-conta=" + idConta);
        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao atualizar conta: " + e.getMessage());
            abrirFormEdicao(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email != null) {
            try {
                Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
                if (usuario != null) {
                    List<ContaBancaria> listaContas = contaBancariaDao.listarPorUsuario(usuario.getId());
                    req.setAttribute("contas", listaContas);
                    req.getRequestDispatcher("menu-conta.jsp").forward(req, resp);
                    return;
                }
            } catch (DBException e) {
                e.printStackTrace();
                req.setAttribute("erro", "Erro ao listar contas. Tente novamente mais tarde.");
            }
        }
        req.getRequestDispatcher("menu-conta.jsp").forward(req, resp);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("idExcluir"));
        try {
            contaBancariaDao.removerContaBancaria(id);
            req.setAttribute("mensagem", "Conta removida!");
            listar(req, resp);
        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao remover conta");
        }
        listar(req, resp);
    }

    private void abrirFormCadastroConta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        carregarOpcoesBanco(req);
        req.getRequestDispatcher("cadastro-conta-bancaria.jsp").forward(req, resp);
    }

    private void abrirFormEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idContaStr = req.getParameter("id-conta");
            System.out.println("ID recebido no abrirFormEdicao: " + idContaStr);
            if (idContaStr == null || idContaStr.isEmpty()) {
                throw new DBException("ID da conta não especificado");
            }
            int idConta = Integer.parseInt(idContaStr);
            ContaBancaria contaBancaria = contaBancariaDao.buscarContaBancaria(idConta);

            System.out.println("ID da conta encontrada: " + contaBancaria.getIdConta());
            if (contaBancaria == null) {
                throw new DBException("Conta bancária não encontrada");
            }

            carregarOpcoesBanco(req);
            req.setAttribute("contaBancaria", contaBancaria);
            req.getRequestDispatcher("editar-conta-bancaria.jsp").forward(req, resp);

        } catch (DBException e) {
            throw new RuntimeException(e);
        }

    }

    private void carregarOpcoesBanco(HttpServletRequest req) {
        List<Banco> bancoList = bancoDao.listar();
        req.setAttribute("bancos", bancoList);
    }

}