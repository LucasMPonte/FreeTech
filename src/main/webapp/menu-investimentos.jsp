<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.*" %>
<!doctype html>
<html>
<head>
    <title>Meus Investimentos</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
    <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container mt-5">
    <h1 class="text-center mb-4">Meus Investimentos</h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger" role="alert">
                ${erro}
        </div>
    </c:if>
    <c:if test="${not empty mensagem}">
        <div class="alert alert-success" role="alert">
                ${mensagem}
        </div>
    </c:if>

    <div class="mb-3 text-end">
        <a href="cadastro-investimento.jsp" class="btn btn-success">+ Novo Investimento</a>
    </div>

    <c:if test="${empty investimentos}">
        <div class="alert alert-info text-center">Nenhum investimento encontrado.</div>
    </c:if>
    <c:if test="${not empty investimentos}">
        <div class="card-body">
            <h5 class="card-title">Investimentos</h5>
            <table class="table table-striped table-bordered align-middle">
                <thead>
                <tr>
                    <th>Tipo</th>
                    <th class="text-end">Valor</th>
                    <th class="text-end">Taxa de Rendimento</th>
                    <th class="text-center">Data do investimento</th>
                    <th class="text-center">Data do resgate</th>
                    <th class="text-end">Rendimento estimado</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${investimentos}" var="investimento">
                    <tr>
                        <td>${investimento.tipoInvestimento}</td>
                        <td class="text-end"><fmt:formatNumber value="${investimento.valorInvestimento}" type="currency" currencySymbol="R$"/></td>
                        <td class="text-end"><fmt:formatNumber value="${investimento.taxaRendimento / 100}" type="percent" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td class="text-center">
                            <fmt:parseDate value="${investimento.dataInvestimento}" pattern="yyyy-MM-dd"
                                           var="dataInvestimentoFmt"/>
                            <fmt:formatDate value="${dataInvestimentoFmt}"
                                            pattern="dd/MM/yyyy"/>
                        </td>
                        <td class="text-center">
                            <fmt:parseDate value="${investimento.dataResgate}" pattern="yyyy-MM-dd"
                                           var="dataResgateFmt"/>
                            <fmt:formatDate value="${dataResgateFmt}" pattern="dd/MM/yyyy"/>
                        </td>
                        <td class="text-end">
                            <fmt:formatNumber value="${investimento.getRendimentoEstimado()}" type="currency" currencySymbol="R$"/>
                        </td>
                        <td class="text-center">
                            <c:url value="investimentos" var="link">
                                <c:param name="acao" value="abrir-form-edicao"/>
                                <c:param name="idInvestimento" value="${investimento.idInvestimento}"/>
                            </c:url>
                            <a href="${link}" class="btn btn-primary">Editar</a>

                            <button type="button"
                                    class="btn btn-danger"
                                    data-bs-toggle="modal"
                                    data-bs-target="#excluirModal"
                                    onclick="setIdExcluir(${investimento.idInvestimento})">
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
                <h4>Você confirma a exclusão deste investimento?</h4>
                <p><strong>Atenção!</strong> Esta ação é irreversível.</p>
            </div>
            <div class="modal-footer">
                <form action="investimentos" method="post">
                    <input type="hidden"
                           name="acao"
                           value="excluir">
                    <input type="hidden"
                           name="idExcluir"
                           id="idExcluir">
                    <button type="button"
                            class="btn btn-secondary"
                            data-bs-dismiss="modal">Não
                    </button>
                    <button type="submit"
                            class="btn btn-danger">Sim
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
<script>
    function setIdExcluir(id) {
        document.getElementById('idExcluir').value = id;
    }
</script>
</body>
</html>