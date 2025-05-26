package br.com.fiap.freetech.controller;

import br.com.fiap.freetech.dao.ContaBancariaDao;
import br.com.fiap.freetech.dao.TransacoesDao;
import br.com.fiap.freetech.dao.UsuarioDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.factory.DaoFactory;
import br.com.fiap.freetech.models.ContaBancaria;
import br.com.fiap.freetech.models.Transacoes;
import br.com.fiap.freetech.models.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/transacoes")
public class TransacoesServlet extends HttpServlet {
    private UsuarioDao usuarioDao;
    private TransacoesDao transacoesDao;
    private ContaBancariaDao contaBancariaDao;

    @Override
    public void init() throws ServletException {
        usuarioDao = DaoFactory.getUsuarioDao();
        transacoesDao = DaoFactory.getTransacoesDao();
        contaBancariaDao = DaoFactory.getContaBancariaDao();
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
            case "listar-por-usuario":
                listarPorUsuario(req, resp);
                break;
            case "listar-por-conta-bancaria":
                listarPorContaBancaria(req, resp);
                break;
            case "abrir-form-cadastro":
                abrirFormCadastro(req, resp);
                break;
            case "abrir-form-edicao":
                try {
                    abrirFormEdicao(req, resp);
                } catch (DBException e) {
                    throw new RuntimeException(e);
                }
                break;

        }
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email == null) {
            req.setAttribute("erro", "Sessão expirada. Faça login novamente.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        try {
            Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
            int idConta = Integer.parseInt(req.getParameter("idConta"));
            String tipo = req.getParameter("tipoTransacao");
            double valor = Double.parseDouble(req.getParameter("valorTransacao"));
            String dataStr = req.getParameter("dataTransacao");
            String descricao = req.getParameter("descricaoTransacao");

            if (valor <= 0) {
                throw new DBException("Valor da transação deve ser positivo");
            }

            LocalDate data = LocalDate.parse(dataStr);

            ContaBancaria conta = contaBancariaDao.buscarContaBancaria(idConta);
            if (conta == null) {
                throw new DBException("Conta bancária não encontrada");
            }

            Transacoes transacao = new Transacoes();
            transacao.setTipoTransacao(tipo);
            transacao.setValorTransacao(valor);
            transacao.setDataTransacao(data);
            transacao.setDescricaoTransacao(descricao);
            transacao.setIdUsuario(usuario);
            transacao.setIdContaBancaria(conta);

            if ("credito".equalsIgnoreCase(tipo)) {
                conta.setSaldo(conta.getSaldo() + valor);
            } else if ("debito".equalsIgnoreCase(tipo)) {
                if (conta.getSaldo() < valor) {
                    throw new DBException("Saldo insuficiente para débito");
                }
                conta.setSaldo(conta.getSaldo() - valor);
            }

            contaBancariaDao.atualizarContaBancaria(conta);
            transacoesDao.cadastrarTransacao(transacao);

            req.setAttribute("mensagem", "Transação cadastrada com sucesso!");
            req.setAttribute("contaSelecionada", conta);

        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", e.getMessage());
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Valor inválido para transação");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao cadastrar transação.");
        }

        try {
            Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
            List<ContaBancaria> contas = contaBancariaDao.listarPorUsuario(usuario.getId());
            req.setAttribute("contas", contas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("cadastro-transacoes.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");
        Usuario usuario = null;

        try {
            usuario = usuarioDao.buscarUsuarioPorEmail(email);

            int idTransacao = Integer.parseInt(req.getParameter("idTransacao"));
            int idConta = Integer.parseInt(req.getParameter("idConta"));
            String tipoNovo = req.getParameter("tipoTransacao");
            double valorNovo = Double.parseDouble(req.getParameter("valorTransacao"));
            String dataStr = req.getParameter("dataTransacao");
            String descricao = req.getParameter("descricaoTransacao");

            Transacoes transacaoOriginal = transacoesDao.buscarTransacao(idTransacao);

            if (transacaoOriginal == null) {
                throw new DBException("Transação original não encontrada.");
            }

            ContaBancaria conta = contaBancariaDao.buscarContaBancaria(idConta);

            if (conta == null) {
                throw new DBException("Conta bancária não encontrada.");
            }

            if ("CREDITO".equalsIgnoreCase(transacaoOriginal.getTipoTransacao())) {
                conta.setSaldo(conta.getSaldo() - transacaoOriginal.getValorTransacao());
            } else if ("DEBITO".equalsIgnoreCase(transacaoOriginal.getTipoTransacao())) {
                conta.setSaldo(conta.getSaldo() + transacaoOriginal.getValorTransacao());
            }

            if ("CREDITO".equalsIgnoreCase(tipoNovo)) {
                conta.setSaldo(conta.getSaldo() + valorNovo);
            } else if ("DEBITO".equalsIgnoreCase(tipoNovo)) {
                if (conta.getSaldo() < valorNovo) {
                    throw new DBException("Saldo insuficiente para realizar o débito.");
                }
                conta.setSaldo(conta.getSaldo() - valorNovo);
            }

            contaBancariaDao.atualizarContaBancaria(conta);

            transacaoOriginal.setTipoTransacao(tipoNovo);
            transacaoOriginal.setValorTransacao(valorNovo);
            transacaoOriginal.setDescricaoTransacao(descricao);
            transacaoOriginal.setDataTransacao(LocalDate.parse(dataStr));

            transacoesDao.atualizarTransacao(transacaoOriginal);

            resp.sendRedirect("transacoes?acao=listar-por-usuario");

        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", e.getMessage());
            if (usuario != null) {
                List<ContaBancaria> contas = contaBancariaDao.listarPorUsuario(usuario.getId());
                req.setAttribute("contas", contas);
            }
            req.getRequestDispatcher("editar-transacoes.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao atualizar transação: " + e.getMessage());
            req.getRequestDispatcher("editar-transacoes.jsp").forward(req, resp);
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int idTransacao = Integer.parseInt(req.getParameter("idTransacao"));

            Transacoes transacao = transacoesDao.buscarTransacao(idTransacao);
            if (transacao == null) {
                req.setAttribute("erro", "Transação não encontrada.");
                listarPorUsuario(req, resp);
                return;
            }

            ContaBancaria conta = contaBancariaDao.buscarContaBancaria(transacao.getIdContaBancaria().getIdConta());
            if (conta == null) {
                req.setAttribute("erro", "Conta bancária associada à transação não encontrada.");
                listarPorUsuario(req, resp);
                return;
            }

            if ("CREDITO".equalsIgnoreCase(transacao.getTipoTransacao())) {
                conta.setSaldo(conta.getSaldo() - transacao.getValorTransacao());
            } else if ("DEBITO".equalsIgnoreCase(transacao.getTipoTransacao())) {
                conta.setSaldo(conta.getSaldo() + transacao.getValorTransacao());
            }

            contaBancariaDao.atualizarContaBancaria(conta);

            transacoesDao.removerTransacao(idTransacao);

            req.setAttribute("mensagem", "Transação excluída com sucesso!");
            listarPorUsuario(req, resp);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("erro", "ID inválido para a transação.");
            listarPorUsuario(req, resp);

        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao excluir a transação: " + e.getMessage());
            listarPorUsuario(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro inesperado ao processar a exclusão.");
            listarPorUsuario(req, resp);
        }
    }

    private void listarPorUsuario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email != null) {
            try {
                Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
                if (usuario != null) {
                    List<Transacoes> listarTransacoes = transacoesDao.listarPorUsuario(usuario.getId());
                    req.setAttribute("transacoes", listarTransacoes);
                    req.getRequestDispatcher("menu-transacoes.jsp").forward(req, resp);
                    return;
                }
            } catch (DBException e) {
                e.printStackTrace();
                req.setAttribute("erro", "Erro ao listar transações: " + e.getMessage());
            }
        } else {
            req.setAttribute("erro", "Usuário não está logado");
        }
        req.getRequestDispatcher("menu-transacoes.jsp").forward(req, resp);
    }


    private void listarPorContaBancaria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int idConta = Integer.parseInt(req.getParameter("idConta"));

            List<Transacoes> transacoes = transacoesDao.listarPorContaBancaria(idConta);
            req.setAttribute("transacoes", transacoes);
            req.getRequestDispatcher("menu-transacoes.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao listar transações");
        }
    }

    private void abrirFormEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, DBException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        int idTransacao = Integer.parseInt(req.getParameter("id-transacao"));
        Transacoes transacao = transacoesDao.buscarTransacao(idTransacao);
        Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
        List<ContaBancaria> contas = contaBancariaDao.listarPorUsuario(usuario.getId());

        req.setAttribute("transacao", transacao);
        req.setAttribute("contas", contas);
        req.getRequestDispatcher("editar-transacoes.jsp").forward(req, resp);
    }

    private void abrirFormCadastro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");
        Usuario usuario = null;

        if (email == null) {
            req.setAttribute("erro", "Sessão expirada. Faça login novamente.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        try {
            usuario = usuarioDao.buscarUsuarioPorEmail(email);
        } catch (DBException e) {
            e.printStackTrace();
        }
        String idContaStr = req.getParameter("idConta");

        if (idContaStr != null && !idContaStr.isEmpty()) {
            try {
                int idConta = Integer.parseInt(idContaStr);
                ContaBancaria conta = contaBancariaDao.buscarContaBancaria(idConta);

                if (conta != null && conta.getIdUsuario().getId() == usuario.getId()) {
                    req.setAttribute("contaSelecionada", conta);
                } else {
                    req.setAttribute("erro", "Conta inválida");
                }
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID de conta inválido.");
            }
        } else {
            List<ContaBancaria> contas = contaBancariaDao.listarPorUsuario(usuario.getId());
            req.setAttribute("contas", contas);
        }
        req.getRequestDispatcher("cadastro-transacoes.jsp").forward(req, resp);
    }
}
