<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Meu Perfil - FreeTech</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>

<div class="container mt-5">
    <h1 class="text-center mb-4">Meu Perfil</h1>
    <c:if test="${not empty usuario}">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Informações Pessoais</h5>
                <p><strong>Nome:</strong> ${usuario.nome}</p>
                <p><strong>Email:</strong> ${usuario.email}</p>
                <p><strong>Data de Nascimento:</strong> <fmt:parseDate value="${usuario.dataNascimento}"
                                                                       pattern="yyyy-MM-dd" var="dataNascimentoFmt"/>
                    <fmt:formatDate value="${dataNascimentoFmt}" pattern="dd/MM/yyyy"/></p>
                <div class="mt-4 text-end">
                    <c:url value="usuario" var="linkEditar">
                        <c:param name="acao" value="abrir-form-edicao"/>
                        <c:param name="idUsuario" value="${usuario.id}"/>
                    </c:url>
                    <a href="${linkEditar}" class="btn btn-primary">Editar Perfil</a>

                    <button
                            type="button"
                            class="btn btn-danger"
                            data-bs-toggle="modal"
                            data-bs-target="#excluirModal">
                        Excluir Perfil
                    </button>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${empty usuario}">
        <div class="alert alert-warning text-center mt-4">
            Nenhuma informação de perfil disponível.
        </div>
    </c:if>
</div>
<div class="modal fade" id="excluirModal" tabindex="-1" aria-labelledby="excluirModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="excluirModalLabel">Confirmar Exclusão</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
            </div>
            <form action="usuario" method="post">
                <div class="modal-body">
                    <p>Tem certeza de que deseja excluir seu perfil?</p>
                    <p class="text-danger"><strong>Atenção:</strong> Essa ação é irreversível e removerá todos os seus
                        dados.</p>

                    <div class="mb-3">
                        <label for="senhaConfirmacao" class="form-label">Confirme sua senha:</label>
                        <input
                                type="password"
                                class="form-control"
                                id="senhaConfirmacao"
                                name="senhaConfirmacao"
                                required
                                placeholder="Digite sua senha para confirmar">
                    </div>
                    <input type="hidden" name="acao" value="excluir"/>
                    <input type="hidden" name="idUsuario" value="${usuario.id}"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger">Confirmar</button>
                </div>
            </form>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
