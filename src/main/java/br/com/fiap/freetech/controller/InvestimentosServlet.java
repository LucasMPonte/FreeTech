package br.com.fiap.freetech.controller;

import br.com.fiap.freetech.dao.InvestimentosDao;
import br.com.fiap.freetech.dao.UsuarioDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.factory.DaoFactory;
import br.com.fiap.freetech.models.Investimentos;
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

@WebServlet("/investimentos")
public class InvestimentosServlet extends HttpServlet {
    private InvestimentosDao investimentosDao;
    private UsuarioDao usuarioDao;

    @Override
    public void init() throws ServletException {
        investimentosDao = DaoFactory.getInvestimentosDao();
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
            case "listar":
                listar(req, resp);
                break;
            case "abrir-form-edicao":
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
                    String tipoInvestimento = req.getParameter("tipoInvestimento");
                    double valorInvestimento = Double.parseDouble(req.getParameter("valorInvestimento"));
                    double taxaRendimento = Double.parseDouble(req.getParameter("taxaRendimento"));
                    LocalDate dataInvestimento = LocalDate.parse(req.getParameter("dataInvestimento"));
                    String dataResgateStr = req.getParameter("dataResgate");
                    LocalDate dataResgate = null;


                    if (dataResgateStr != null && !dataResgateStr.isEmpty()) {
                        try {
                            dataResgate = LocalDate.parse(dataResgateStr);
                        } catch (Exception e) {
                            req.setAttribute("erro", "Data de Resgate inválida");
                            req.getRequestDispatcher("cadastro-investimento.jsp").forward(req, resp);
                            return;
                        }
                    }

                    Investimentos novoInvestimento = new Investimentos();
                    novoInvestimento.setTipoInvestimento(tipoInvestimento);
                    novoInvestimento.setValorInvestimento(valorInvestimento);
                    novoInvestimento.setTaxaRendimento(taxaRendimento);
                    novoInvestimento.setDataInvestimento(dataInvestimento);
                    novoInvestimento.setDataResgate(dataResgate);
                    novoInvestimento.setIdUsuario(usuarioLogado);

                    investimentosDao.cadastrarInvestimento(novoInvestimento);

                    req.setAttribute("mensagem", "Investimento cadastrado com sucesso!");
                    req.getRequestDispatcher("cadastro-investimento.jsp").forward(req, resp);
                }
            } catch (DBException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        System.out.println("Iniciando edição do investimento");

        if (email != null) {
            try {
                Usuario usuarioLogado = usuarioDao.buscarUsuarioPorEmail(email);

                if (usuarioLogado != null) {
                    String idInvestimentoStr = req.getParameter("idInvestimento");
                    String tipoInvestimento = req.getParameter("tipoInvestimento");
                    double valorInvestimento = Double.parseDouble(req.getParameter("valorInvestimento"));
                    double taxaRendimento = Double.parseDouble(req.getParameter("taxaRendimento"));

                    if (taxaRendimento < 0 || taxaRendimento > 100) {
                        req.setAttribute("erro", "A taxa de rendimento deve estar entre 0 e 100%");
                        abrirFormEdicao(req, resp);
                        return;
                    }

                    LocalDate dataInvestimento = LocalDate.parse(req.getParameter("dataInvestimento"));

                    LocalDate dataResgate = null;
                    String dataResgateStr = req.getParameter("dataResgate");

                    if (dataResgateStr != null && !dataResgateStr.trim().isEmpty()) {
                        dataResgate = LocalDate.parse(dataResgateStr);

                        if (dataResgate.isBefore(dataInvestimento)) {
                            req.setAttribute("erro", "A data de resgate deve ser posterior à data de investimento.");
                            abrirFormEdicao(req, resp);
                            return;
                        }
                    }

                    int idInvestimento = Integer.parseInt(idInvestimentoStr);
                    Investimentos investimento = new Investimentos();
                    investimento.setIdInvestimento(idInvestimento);
                    investimento.setTipoInvestimento(tipoInvestimento);
                    investimento.setValorInvestimento(valorInvestimento);
                    investimento.setTaxaRendimento(taxaRendimento);
                    investimento.setDataInvestimento(dataInvestimento);
                    investimento.setDataResgate(dataResgate);
                    investimento.setIdUsuario(usuarioLogado);

                    investimentosDao.atualizarInvestimento(investimento);

                    req.setAttribute("mensagem", "Investimento atualizado com sucesso!");
                    listar(req, resp);
                    return;
                }
            } catch (DBException e) {
                req.setAttribute("erro", "Erro ao atualizar o investimento");
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "Erro no formato dos valores numéricos informados.");
            }
        }
        abrirFormEdicao(req, resp);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idStr = req.getParameter("idExcluir");
            System.out.println("ID recebido para exclusão: " + idStr); // Log para debug

            if (idStr != null && !idStr.trim().isEmpty()) {
                int id = Integer.parseInt(idStr);
                investimentosDao.removerInvestimento(id);
                resp.sendRedirect("investimentos?acao=listar&mensagem=Investimento removido com sucesso");
            } else {
                resp.sendRedirect("investimentos?acao=listar&erro=ID do investimento não fornecido");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect("investimentos?acao=listar&erro=ID do investimento inválido");
        } catch (DBException e) {
            resp.sendRedirect("investimentos?acao=listar&erro=Erro ao remover o investimento");
        }
    }


    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email != null) {
            try {
                Usuario usuarioLogado = usuarioDao.buscarUsuarioPorEmail(email);
                if (usuarioLogado != null) {
                    List<Investimentos> listarInvestimentos = investimentosDao.listarPorUsuario(usuarioLogado.getId());
                    req.setAttribute("investimentos", listarInvestimentos);
                    req.getRequestDispatcher("menu-investimentos.jsp").forward(req, resp);
                    return;
                }
            } catch (DBException e) {
                e.printStackTrace();
                req.setAttribute("erro", "Erro ao listar investimentos. Tente novamente mais tarde.");
            }
        }
        req.getRequestDispatcher("menu-investimentos.jsp").forward(req, resp);
    }

    private void abrirFormEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idInvestimentoStr = req.getParameter("idInvestimento");

        System.out.println("idInvestimento: " + idInvestimentoStr);

        if (idInvestimentoStr == null || idInvestimentoStr.isEmpty()) {
            req.setAttribute("erro", "ID do investimento não foi fornecido");
            req.getRequestDispatcher("menu-investimentos.jsp").forward(req, resp);
            return;
        }

        try {
            int idInvestimento = Integer.parseInt(idInvestimentoStr);
            System.out.println("ID recebido: " + idInvestimento);
            Investimentos investimento = investimentosDao.buscarInvestimento(idInvestimento);
            System.out.println("Investimento encontrado: " + investimento);

            if (investimento == null) {
                req.setAttribute("erro", "Investimento não encontrado");
                req.getRequestDispatcher("menu-investimentos.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("investimento", investimento);
            req.getRequestDispatcher("editar-investimento.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "ID do investimento inválido");
            req.getRequestDispatcher("menu-investimentos.jsp").forward(req, resp);
        }
    }
}
