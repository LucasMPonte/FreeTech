<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
  <title>Editar Transações</title>
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
  <link rel="stylesheet" href="./resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp"%>
<div class="container">
  <div class="mt-5 ms-5 me-5">
    <div class="card mb-3">
      <div class="card-header">
        ATUALIZAR TRANSAÇÃO
      </div>
      <c:if test="${not empty mensagem}">
        <div class="alert alert-success ms-2 me-2 m-auto">${mensagem}</div>
      </c:if>
      <c:if test="${not empty erro}">
        <div class="alert alert-danger ms-2 me-2 m-auto"> ${erro}</div>
      </c:if>
      <div class="card-body">
        <form action="transacoes?acao=editar" method="post">
          <input type="hidden" name="idTransacao" value="${transacao.idTransacao}">
          <div class="form-group">
            <div class="form-group">
              <label for="id-conta">Conta</label>
              <select name="idConta" id="id-conta" class="form-control">
                <option value="0">Selecione</option>
                <c:forEach items="${contas}" var="conta">
                  <option value="${conta.idConta}"
                    ${transacao.idContaBancaria.idConta == conta.idConta ? 'selected' : ''}>
                      ${conta.idBanco.nomeBanco}
                  </option>
                </c:forEach>
              </select>
            </div>

          </div>
          <div class="form-group">
            <label for="id-tipo-transacao">Tipo da Transação</label>
            <select name="tipoTransacao" id="id-tipo-transacao" class="form-control">
              <option value="CREDITO" ${transacao.tipoTransacao == 'CREDITO' ? 'selected' : ''}>Crédito</option>
              <option value="DEBITO" ${transacao.tipoTransacao == 'DEBITO' ? 'selected' : ''}>Débito</option>
            </select>
          </div>
          <div class="form-group">
            <label for="id-valor">Valor</label>
            <input type="number" step="0.01" name="valorTransacao" id="id-valor" class="form-control" value="${transacoes.valorTransacao}" required>
          </div>
          <div class="form-group">
            <label for="id-data">Data da transação</label>
            <input type="date" name="dataTransacao" id="id-data" class="form-control" value="${transacao.dataTransacao}" required>
          </div>
          <div class="form-group">
            <label for="id-descricao">Descrição</label>
            <textarea name="descricaoTransacao" id="id-descricao" class="form-control" required>${transacao.descricaoTransacao}</textarea>
          </div>
          <input type="submit" value="Atualizar" class="btn btn-primary mt-3">
          <a href="transacoes?acao=listar-por-usuario" class="btn btn-secondary mt-3 ms-2">Voltar</a>
        </form>
      </div>
    </div>
  </div>
</div>
<%@include file="footer.jsp"%>
<script src="resources/js/bootstrap.js"></script>
</body>
</html>