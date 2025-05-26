package br.com.fiap.freetech.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        String url = req.getRequestURI();

        boolean usuarioLogado = session.getAttribute("user") != null;

        boolean acessoPublico =
                url.endsWith("/") ||
                url.endsWith("home.jsp") ||
                url.contains("home") ||
                url.contains("/usuario") ||
                url.contains("sobre") ||
                url.contains("contato") ||
                url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg");

        if (!usuarioLogado && !acessoPublico) {
            resp.sendRedirect(req.getContextPath() + "/home.jsp?erroFilter=1");
            return;
        }
            filterChain.doFilter(req, resp);
    }
}
