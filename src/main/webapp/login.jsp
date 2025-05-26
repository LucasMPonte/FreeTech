<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Entrar - FreeTech</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container d-flex justify-content-center align-items-center">
    <section id="menu-login" class="border rounded p-4 bg-white shadow">
        <c:if test="${param.erro == 'login'}">
            <div class="alert alert-danger ms-2 me-2 m-auto my-5">Usuário e/ou senha inválido.</div>
        </c:if>
        <form action="usuario" method="post">
            <input type="hidden" name="acao" value="login-usuario">
            <h3>Acesse sua conta!</h3>
            <div class="mb-3">
                <label for="user" class="fw-bold form-label">E-mail:</label>
                <input
                        type="email"
                        name="email"
                        id="email-login"
                        class="form-control rounded-2 border-1 border-dark"
                />
            </div>
            <div class="mb-3">
                <label for="senha" class="fw-bold form-label">Senha:</label>
                <input
                        type="password"
                        name="senha"
                        id="senha-cadastro"
                        class="form-control rounded-2 border-1 border-dark"
                />
            </div>
            <button type="submit" class="btn btn-success w-100 mt-2" value="submit">
                Entrar
            </button>
        </form>
    </section>
</div>
<script src="resources/js/bootstrap.bundle.js"></script>
<%@include file="footer.jsp" %>
</body>
</html>