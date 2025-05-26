<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home - FreeTech</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>

<div class="container mt-5 text-center">
  <c:if test="${param.mensagem == 'perfilExcluido'}">
    <div class="alert alert-success text-center">Seu perfil foi excluído com sucesso.</div>
  </c:if>
  <c:if test="${param.erroFilter == 1}">
    <div class="alert alert-danger alert-dismissible fade show mt-4" role="alert">
        Você precisa estar logado para acessar esta página.
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar"></button>
    </div>
  </c:if>
  <c:if test="${not empty msgLogout}">
    <div class="alert alert-success alert-dismissible fade show mt-4" role="alert">
      ${msgLogout}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar"></button>
    </div>
  </c:if>
  <h1 class="display-4">Bem-vindo ao FreeTech!</h1>
  <p class="lead mt-3">
    Sua jornada para uma vida financeira mais organizada começa aqui. <br>
    Registre suas transações, acompanhe seus ganhos e defina seus objetivos.
  </p>

  <c:if test="${not empty user}">
    <div class="row mt-4 justify-content-center">
      <div class="col-md-3 my-2">
        <a href="transacoes?acao=listar-por-usuario" class="btn btn-outline-success w-100">Minhas Transações</a>
      </div>
      <div class="col-md-3 my-2">
        <a href="investimentos?acao=listar" class="btn btn-outline-primary w-100">Meus Investimentos</a>
      </div>
      <c:if test="${not empty erro}">
        <div class="alert alert-danger">
          ${erro}
        </div>
      </c:if>
      <div class="col-md-3 my-2">
        <a href="conta-bancaria?acao=listar" class="btn btn-outline-info w-100">Minhas Contas</a>
      </div>
      <div class="col-md-3 my-2">
        <a href="perfil?acao=abrir-perfil" class="btn btn-outline-dark w-100">Perfil</a>
      </div>
    </div>
  </c:if>

  <c:if test="${empty user}">
    <div class="mt-4">
      <a href="usuario?acao=cadastro-usuario" class="btn btn-success mx-2">Começar Agora</a>
      <a href="usuario?acao=login-usuario" class="btn btn-outline-light bg-dark mx-2">Já tenho conta</a>
    </div>
  </c:if>
</div>

<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
