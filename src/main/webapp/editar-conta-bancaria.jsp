<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Document</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
    <link rel="stylesheet" href="./resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <div class="mt-5 ms-5 me-5">
        <div class="card mb-3">
            <div class="card-header">
                ATUALIZAR CONTA BANCÁRIA
            </div>
            <c:if test="${not empty mensagem}">
                <div class="alert alert-success ms-2 me-2 m-auto">${mensagem}</div>
            </c:if>
            <c:if test="${not empty erro}">
                <div class="alert alert-danger ms-2 me-2 m-auto">${erro}</div>
            </c:if>
            <div class="card-body">
                <form action="conta-bancaria?acao=editar" method="post">
                    <c:if test="${empty contaBancaria}">
                        <div class="alert alert-warning">Nenhuma conta selecionada para edição</div>
                    </c:if>
                    <input type="hidden" name="id-conta" value="${contaBancaria.idConta}">
                    <div class="form-group">
                        <label for="id-banco">Banco</label>
                        <select name="bancos" id="id-banco" class="form-control">
                            <option value="0">Selecione</option>
                            <c:forEach items="${bancos}" var="banco">
                                <option value="${banco.idBanco}"
                                    ${contaBancaria.idBanco != null && banco.idBanco == contaBancaria.idBanco.idBanco ? 'selected' : ''}>
                                        ${banco.nomeBanco}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="id-tipo-conta">Tipo da Conta</label>
                        <select name="tipo-conta" id="id-tipo-conta" class="form-control">
                            <option value="PF" ${contaBancaria.tipoConta == 'PF' ? 'selected' : ''}>Pessoa Física</option>
                            <option value="PJ" ${contaBancaria.tipoConta == 'PJ' ? 'selected' : ''}>Pessoa Jurídica</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="id-saldo">Saldo</label>
                        <input type="number" step="0.01" name="saldo" id="id-saldo" class="form-control"
                               value="${contaBancaria.saldo}" required>
                    </div>
                    <input type="submit" value="Salvar" class="btn btn-primary mt-3">
                    <a href="conta-bancaria?acao=listar" class="btn btn-secondary mt-3">Voltar</a>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>