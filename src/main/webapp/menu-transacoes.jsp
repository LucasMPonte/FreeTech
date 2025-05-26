<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
  <title>Transações - FreeTech</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container mt-5">
  <h1 class="text-center mb-4">Minhas Transações</h1>

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
    <a href="transacoes?acao=abrir-form-cadastro" class="btn btn-success">+ Nova Transação</a>
  </div>

  <c:if test="${empty transacoes}">
    <div class="alert alert-info text-center">Nenhuma transação encontrada.</div>
  </c:if>

  <c:if test="${not empty transacoes}">
    <div class="card-body">
      <h5 class="card-title">Histórico de Transações</h5>
      <table class="table table-striped table-bordered align-middle">
        <thead>
        <tr>
          <th>Tipo</th>
          <th>Descrição</th>
          <th class="text-end">Valor</th>
          <th class="text-center">Data</th>
          <th>Conta</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${transacoes}" var="transacao">
          <tr>
            <td>
              <c:choose>
                <c:when test="${fn:toUpperCase(transacao.tipoTransacao) == 'CREDITO'}">
                  <span class="text-success">Crédito</span>
                </c:when>
                <c:when test="${fn:toUpperCase(transacao.tipoTransacao) == 'DEBITO'}">
                  <span class="text-danger">Débito</span>
                </c:when>
                <c:otherwise>
                  <span class="text-warning">Tipo Desconhecido</span>
                </c:otherwise>
              </c:choose>
            </td>

            <td>${transacao.descricaoTransacao}</td>
            <td class="text-end">
              <fmt:formatNumber value="${transacao.valorTransacao}" type="currency" currencySymbol="R$"/>
            </td>
            <td class="text-center">
              <c:set var="pattern" value="dd/MM/yyyy"/>
                ${transacao.dataTransacao.format(DateTimeFormatter.ofPattern(pattern))}
            </td>
            <td>${transacao.idContaBancaria.idConta}</td>
            <td class="text-center">
              <a href="transacoes?acao=abrir-form-edicao&id-transacao=${transacao.idTransacao}"
                 class="btn btn-primary btn-sm">Editar</a>

              <button type="button"
                      class="btn btn-danger btn-sm"
                      data-bs-toggle="modal"
                      data-bs-target="#excluirModal"
                      onclick="prepararExclusao(${transacao.idTransacao})">
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

<div class="modal fade" id="excluirModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="modalLabel">Confirmar Exclusão</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
      </div>
      <div class="modal-body">
        <h4>Deseja realmente excluir esta transação?</h4>
        <p><strong>Atenção:</strong> Esta ação não pode ser desfeita.</p>
      </div>
      <div class="modal-footer">
        <form action="transacoes" method="post">
          <input type="hidden" name="acao" value="excluir">
          <input type="hidden" name="idTransacao" id="idExcluir">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="submit" class="btn btn-danger">Confirmar</button>
        </form>
      </div>
    </div>
  </div>
</div>
<%@include file="footer.jsp" %>
<script src="resources/js/bootstrap.bundle.js"></script>
<script>
  function prepararExclusao(id) {
    document.getElementById('idExcluir').value = id;
  }
</script>
</body>
</html>