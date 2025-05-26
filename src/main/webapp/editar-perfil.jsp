<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Editar Perfil - FreeTech</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <div class="mt-5 ms-5 me-5">
        <div class="card mb-3">
            <div class="card-header">
                EDITAR PERFIL
            </div>
            <c:if test="${not empty mensagem}">
                <div class="alert alert-success ms-2 me-2 m-auto">${mensagem}</div>
            </c:if>
            <c:if test="${not empty erro}">
                <div class="alert alert-danger ms-2 me-2 m-auto">${erro}</div>
            </c:if>
            <div class="card-body">
                <form action="usuario?acao=editar" method="post">
                    <input type="hidden" name="idUsuario" value="${usuario.id}"/>
                    <div class="form-group mb-3">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" id="nome" name="nome" value="${usuario.nome}" required>
                    </div>
                    <div class="form-group mb-3">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="${usuario.email}"
                               required>
                    </div>
                    <div class="form-group mb-3">
                        <label for="dataNascimento">Data de Nascimento</label>
                        <input type="date" class="form-control" id="dataNascimento" name="dataNascimento"
                               value="${usuario.dataNascimento}" required>
                    </div>
                    <div class="d-flex justify-content-between">
                        <input type="submit" value="Salvar Alterações" class="btn btn-primary">
                        <a href="perfil?acao=abrir-perfil" class="btn btn-secondary mt-3 ms-2">Voltar</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
