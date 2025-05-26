<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sobre Nós - FreeTech</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>

<div class="container mt-5 text-center">
    <h1 class="display-5 mb-4">Sobre o FreeTech</h1>
    <p class="lead">
        O FreeTech é uma plataforma desenvolvida com o objetivo de ajudar as pessoas a organizarem sua vida financeira de maneira simples e eficiente.
    </p>
    <p class="mt-3">
        Este projeto foi criado por Lucas Manica Ponte como parte de um trabalho acadêmico no curso de Análise e Desenvolvimento de Sistemas, unindo boas práticas de programação web, design e usabilidade.
    </p>
    <p class="mt-3">
        Através da plataforma, você pode registrar transações, acompanhar investimentos, controlar contas bancárias e planejar metas financeiras.
    </p>
    <p class="mt-4">
        Quer falar comigo? <a href="contato.jsp" class="btn btn-outline-primary ms-2">Entre em Contato</a>
    </p>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
