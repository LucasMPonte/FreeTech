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

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {
    private UsuarioDao usuarioDao;
    @Override
    public void init() throws ServletException {
        usuarioDao = DaoFactory.getUsuarioDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");

        switch (acao) {
            case "abrir-perfil":
                abrirPerfil (req, resp);
                break;
        }
    }

    private void abrirPerfil(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("user");

        if (email != null) {
            try {
                Usuario usuario = usuarioDao.buscarUsuarioPorEmail(email);
                req.setAttribute("usuario", usuario);
                req.getRequestDispatcher("perfil.jsp").forward(req, resp);
            } catch (DBException e) {
                req.setAttribute("erro", "Erro ao buscar dados do usuário");
                req.getRequestDispatcher("home.jsp").forward(req, resp);
                throw new RuntimeException(e);
            }
        } else {
            resp.sendRedirect("login.jsp?erro=login");
        }
    }
}
