<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contas Bancárias - FreeTech</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container mt-5">
    <h1 class="text-center mb-4">Minhas Contas Bancárias</h1>

    <div class="mb-3 text-end">
        <a href="conta-bancaria?acao=abrir-form-cadastro-conta" class="btn btn-success">+ Nova Conta</a>
    </div>

    <c:if test="${empty contas}">
        <div class="alert alert-info text-center">Você ainda não cadastrou nenhuma conta bancária.</div>
    </c:if>

    <c:if test="${not empty contas}">
        <div class="card-body">
            <h5 class="card-title">Suas contas em um só lugar</h5>
            <table class="table table-striped table-bordered align-middle">
                <thead>
                <tr>
                    <th>Banco</th>
                    <th class="text-end">Tipo da Conta</th>
                    <th class="text-center">Saldo</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${contas}" var="conta">
                    <tr>
                        <td>${conta.idBanco.nomeBanco}</td>
                        <td class="text-center">${conta.tipoConta}</td>
                        <td class="text-center"><fmt:formatNumber value="${conta.saldo}" type="currency" currencySymbol="R$"/></td>
                        <td class="text-center">
                            <c:url value="conta-bancaria" var="link">
                                <c:param name="acao" value="abrir-form-edicao-conta"/>
                                <c:param name="id-conta" value="${conta.idConta}"/>
                            </c:url>
                            <a href="${link}" class="btn btn-primary">Editar</a>

                            <c:url value="transacoes" var="link">
                                <c:param name="acao" value="listar-por-conta-bancaria"/>
                                <c:param name="idConta" value="${conta.idConta}"/>
                            </c:url>
                            <a href="${link}" class="btn btn-primary">Transações</a>
                            <c:url value="investimentos" var="link">
                                <c:param name="acao" value="listar"/>
                            </c:url>
                            <a href="${link}" class="btn btn-primary">Investimentos</a>
                            <button type="button"
                                    class="btn btn-danger"
                                    data-bs-toggle="modal"
                                    data-bs-target="#excluirModal"
                                    onclick="document.getElementById('idExcluir').value = '${conta.idConta}'">
                                Excluir
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>
<div class="modal fade" id="excluirModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5"
                    id="exampleModalLabel">
                    Confirmar Exclusão
                </h1>
                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="close">
                </button>
            </div>
            <div class="modal-body">
                <h4>Você confirma a exclusão desta conta?</h4>
                <p><strong>Atenção!</strong> Esta ação é irreversível.</p>
            </div>
            <div class="modal-footer">
                <form action="conta-bancaria" method="post">
                    <input
                            type="hidden"
                            name="acao"
                            value="excluir">
                    <input
                            type="hidden"
                            name="idExcluir"
                            id="idExcluir">
                    <button
                            type="button"
                            class="btn btn-secondary"
                            data-bs-dismiss="modal"> Não
                    </button>
                    <button
                            type="submit"
                            class="btn btn-danger"> Sim
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
