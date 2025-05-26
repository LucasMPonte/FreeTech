<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Contato - FreeTech</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>

<div class="container mt-5 text-center">
  <h1 class="display-5 mb-4">Fale Comigo</h1>
  <p class="lead">Tem dúvidas, sugestões ou quer saber mais sobre o projeto? Entre em contato!</p>

  <div class="mt-4">
    <p><strong>Email:</strong> ponte.lucasm@gmail.com</p>
    <p><strong>LinkedIn:</strong> <a href="https://www.linkedin.com/in/lucasmanicaponte/" target="_blank">linkedin.com/in/lucasmanicaponte</a></p>
    <p><strong>GitHub:</strong> <a href="https://github.com/LucasMPonte" target="_blank">github.com/LucasMPonte</a></p>
  </div>

  <div class="mt-5">
    <a href="home.jsp" class="btn btn-outline-secondary">Voltar para a Home</a>
  </div>
</div>

<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
