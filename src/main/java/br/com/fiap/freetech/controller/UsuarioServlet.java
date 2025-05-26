package br.com.fiap.freetech.controller;

import br.com.fiap.freetech.dao.UsuarioDao;
import br.com.fiap.freetech.exceptions.DBException;
import br.com.fiap.freetech.factory.DaoFactory;
import br.com.fiap.freetech.models.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {
    private UsuarioDao usuarioDao;

    @Override
    public void init() throws ServletException {
        usuarioDao = DaoFactory.getUsuarioDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");

        switch (acao) {
            case "cadastro-usuario":
                cadastrar(req, resp);
                break;
            case "login-usuario":
                login(req, resp);
                break;
            case "logout":
                req.setAttribute("msgLogout", "Você saiu.");
                HttpSession session = req.getSession();
                session.invalidate();
                req.getRequestDispatcher("home.jsp").forward(req, resp);
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
            case "cadastro-usuario":
                req.getRequestDispatcher("cadastro.jsp").forward(req, resp);
                break;
            case "login-usuario":
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                break;
            case "abrir-form-edicao":
                abrirFormEdicao(req, resp);
                break;
        }
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");
        LocalDate dataNascimento = LocalDate.parse(req.getParameter("dtnascimento"));
        String senha = req.getParameter("senha");
        String confirmarSenha = req.getParameter("confirmar-senha");

        if (!senha.equals(confirmarSenha)) {
            req.getSession().setAttribute("erroSenha", "As senhas precisam ser iguais.");
            req.getRequestDispatcher("cadastro.jsp").forward(req, resp);
            return;
        }

        Usuario usuario = new Usuario(
                0,
                nome,
                email,
                senha,
                dataNascimento
        );

        if (usuarioDao.emailExistente(email)) {
            req.setAttribute("erroEmail", "E-mail já cadastrado!");
            req.getRequestDispatcher("cadastro.jsp").forward(req, resp);
            return;
        }

        try {
            usuarioDao.cadastrarUsuario(usuario);
            HttpSession session = req.getSession();
            session.setAttribute("user", email);

            req.getRequestDispatcher("home.jsp?mensagem=Bem+vindo").forward(req, resp);
        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao cadastrar usuário");
            req.getRequestDispatcher("cadastro.jsp").forward(req, resp);
        }
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String emailSessao = (String) session.getAttribute("user");

        if (emailSessao == null) {
            resp.sendRedirect("login.jsp?erroFilter=1");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
            String nome = req.getParameter("nome");
            String email = req.getParameter("email");
            LocalDate dataNascimento = LocalDate.parse(req.getParameter("dataNascimento"));

            Usuario usuarioAtual = usuarioDao.buscarUsuarioPorEmail(emailSessao);
            if (usuarioAtual == null) {
                session.invalidate();
                resp.sendRedirect("login.jsp?erroFilter=1");
                return;
            }

            Usuario usuarioAtualizado = new Usuario(
                    idUsuario,
                    nome,
                    email,
                    usuarioAtual.getSenha(),
                    dataNascimento
            );

            usuarioDao.alterarDadosUsuario(usuarioAtualizado);

            if (!email.equals(usuarioAtual.getEmail())) {
                session.setAttribute("user", email);
            }

            req.setAttribute("mensagem", "Perfil atualizado com sucesso!");
            req.setAttribute("usuario", usuarioAtualizado);
            req.getRequestDispatcher("perfil.jsp").forward(req, resp);

        } catch (DBException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao atualizar o perfil: " + e.getMessage());
            req.getRequestDispatcher("perfil.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro nos dados enviados. Por favor, revise.");
            req.getRequestDispatcher("perfil.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            req.getRequestDispatcher("perfil.jsp").forward(req, resp);
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String emailSessao = (String) session.getAttribute("user");

        if (emailSessao == null) {
            resp.sendRedirect("login.jsp?erroFilter=1");
            return;
        }

       try {
           int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
           String senhaConfirmacao = req.getParameter("senhaConfirmacao");

           if (!usuarioDao.validarSenha(emailSessao, senhaConfirmacao)) {
               req.setAttribute("erroSenha", "Senha incorreta. Não foi possível excluir o perfil.");
               req.getRequestDispatcher("perfil.jsp").forward(req, resp);
               return;
           }

           usuarioDao.excluirUsuario(idUsuario);

           session.invalidate();
           resp.sendRedirect("home.jsp?mensagem=perfilExcluido");
       } catch (DBException | SQLException e) {
           e.printStackTrace();
           req.setAttribute("erro", "Erro ao excluir perfil: " + e.getMessage());
           req.getRequestDispatcher("perfil.jsp").forward(req, resp);
       }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        Usuario usuario = new Usuario(email, senha);

        if (usuarioDao.validarUsuario(usuario)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", email);
            resp.sendRedirect("home.jsp");
        } else {
            req.setAttribute("erro", "Usuário e/ou senha inválidos");
            resp.sendRedirect("login.jsp?erro=login");
        }
    }

    private void abrirFormEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email != null) {
            try {
                Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);

                if (usuario != null) {
                    req.setAttribute("usuario", usuario);

                    req.getRequestDispatcher("editar-perfil.jsp").forward(req, resp);
                } else {
                    req.setAttribute("erro", "Usuário não encontrado");
                    req.getRequestDispatcher("perfil.jsp").forward(req, resp);
                }
            } catch (DBException e) {
                e.printStackTrace();
                req.setAttribute("erro", "Erro ao carregar dados do usuário: " + e.getMessage());
                req.getRequestDispatcher("perfil.jsp").forward(req, resp);
            }
        } else {
            resp.sendRedirect("login.jsp?erro=login");
        }
    }
}
