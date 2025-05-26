<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <p class="display-4">Seu primeiro passo rumo ao controle financeiro!</p>
    <c:if test="${not empty mensagem}">
        <div class="alert alert-success ms-2 me-2 m-auto my-5">${mensagem}</div>
    </c:if>
    <c:if test="${not empty erroEmail}">
        <div class="alert alert-danger ms-2 me-2 m-auto my-5">${erroEmail}</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-danger ms-2 me-2 m-auto my-5">${erro}</div>
    </c:if>
    <c:if test="${not empty erroSenha}">
        <div class="alert alert-danger ms-2 me-2 m-auto my-5">${erroSenha}</div>
    </c:if>
    <form action="usuario" method="post">
        <input type="hidden" name="acao" value="cadastro-usuario">
        <section id="menu-cadastro" class="row border rounded p-3 d-flex">
            <div class="col-12 my-2">
                <label for="nome" class="fw-bold form-label">Nome Completo:</label>
                <input
                        type="text"
                        name="nome"
                        id="nome-cadastro"
                        class="form-control rounded-2 border-1 border-dark"
                        placeholder="Digite seu nome completo"
                        required
                />
            </div>
            <div class="col-12 my-2">
                <label for="email" class="fw-bold form-label">E-mail:</label>
                <input
                        type="email"
                        name="email"
                        id="email-cadastro"
                        class="form-control rounded-2 border-1 border-dark"
                        placeholder="Digite seu e-mail"
                        required
                />
            </div>
            <div class="col-12 my-2">
                <label for="dtnascimento" class="fw-bold form-label"
                >Data de Nascimento:</label
                >
                <input
                        type="date"
                        name="dtnascimento"
                        id="dtnascimento-cadastro"
                        class="form-control border-1 border-dark"
                        placeholder="Informe a data de nascimento"
                />
            </div>
            <div class="col-12 my-2">
                <label for="senha" class="fw-bold form-label">Senha:</label>
                <input
                        type="password"
                        name="senha"
                        id="senha-cadastro"
                        class="form-control rounded-2 border-1 border-dark"
                        placeholder="Digite sua senha"
                        required
                />
            </div>
            <div class="col-12 my-2">
                <label for="confirmar-senha" class="fw-bold form-label"
                >Confirme sua Senha:</label
                >
                <input
                        type="password"
                        name="confirmar-senha"
                        id="confirmar-senha"
                        class="form-control rounded-2 border-1 border-dark"
                        required
                />
            </div>
            <button type="submit" class="btn btn-success mt-3" value="submit">
                Cadastrar
            </button>
        </section>
    </form>
</div>

<div class="modal fade" id="cadastroModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5"
                    id="exampleModalLabel">
                    Usuário cadastrado com sucesso!
                </h1>
                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="close">
                </button>
            </div>
            <div class="modal-body">
                <h4>Bem-vindo ao FreeTech!</h4>
                <h5>Você já pode fazer login e começar sua jornada na nossa plataforma!</h5>
            </div>
            <a href="login.jsp" class="btn btn-success my-5 mx-5">Prosseguir!</a>
            </form>
        </div>
    </div>
</div>
<c:if test="${cadastroSucesso}">
    <script>
        window.onload = function () {
            var modalSucesso = new bootstrap.Modal(document.getElementById("cadastroModal"));
            modalSucesso.show();
        }
    </script>
</c:if>
<% session.removeAttribute("cadastroSucesso"); %>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
<head>
    <title>Cadastro - FreeTech</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
</html>
