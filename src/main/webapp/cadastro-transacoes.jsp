<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
  <title>Cadastrar Transação - FreeTech</title>
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
  <link rel="stylesheet" href="./resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp"%>
<div class="container">
  <div class="mt-5 ms-5 me-5">
    <div class="card mb-3">
      <div class="card-header">
        CADASTRO DE TRANSAÇÃO
      </div>

      <c:if test="${not empty mensagem}">
        <div class="alert alert-success mx-auto my-3 text-center">${mensagem}</div>
      </c:if>

      <c:if test="${not empty erro}">
        <div class="alert alert-danger mx-auto my-3 text-center">${erro}</div>
      </c:if>

      <div class="card-body">
        <form action="transacoes?acao=cadastrar" method="post">

          <div class="form-group mb-3">
            <label for="id-conta">Conta Bancária</label>
            <select name="idConta" id="id-conta" class="form-control" required>
              <c:choose>
                <c:when test="${not empty contaSelecionada}">
                  <option value="${contaSelecionada.idConta}" selected>
                      ${contaSelecionada.idBanco.nomeBanco} - ${contaSelecionada.tipoConta} - Saldo: R$ ${contaSelecionada.saldo}
                  </option>
                </c:when>
                <c:otherwise>
                  <option value="" disabled selected>Selecione a conta</option>
                  <c:forEach items="${contas}" var="conta">
                    <option value="${conta.idConta}" ${param.idConta == conta.idConta ? 'selected' : ''}>
                        ${conta.idBanco.nomeBanco} - ${conta.tipoConta} - Saldo: R$ ${conta.saldo}
                    </option>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
            </select>
          </div>

          <div class="form-group mb-3">
            <label for="tipo-transacao">Tipo de Transação</label>
            <select name="tipoTransacao" id="tipo-transacao" class="form-control" required>
              <option value="" disabled ${empty tipoTransacao ? "selected" : ""}>Selecione o tipo</option>
              <option value="credito" ${tipoTransacao == "C" ? "selected" : ""}>Crédito</option>
              <option value="debito" ${tipoTransacao == "D" ? "selected" : ""}>Débito</option>
            </select>
          </div>

          <div class="form-group mb-3">
            <label for="valor-transacao">Valor da Transação</label>
            <input type="number" step="0.01" name="valorTransacao" id="valor-transacao" class="form-control" required
                   value="${param.valorTransacao != null ? param.valorTransacao : ''}">
          </div>

          <div class="form-group mb-3">
            <label for="data-transacao">Data da Transação</label>
            <input type="date" name="dataTransacao" id="data-transacao" class="form-control" required
                   value="${param.dataTransacao != null ? param.dataTransacao : ''}">
          </div>

          <div class="form-group mb-3">
            <label for="descricao-transacao">Descrição</label>
            <textarea name="descricaoTransacao" id="descricao-transacao" class="form-control" rows="3">${param.descricaoTransacao != null ? param.descricaoTransacao : ''}</textarea>
          </div>

          <input type="submit" value="Salvar" class="btn btn-primary mt-3">
          <a href="transacoes?acao=listar-por-usuario" class="btn btn-secondary mt-3 ms-2">Voltar</a>
        </form>
      </div>
    </div>
  </div>
</div>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>
