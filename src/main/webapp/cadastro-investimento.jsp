<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Cadastrar Investimento</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
    <link rel="stylesheet" href="./resources/css/bootstrap.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <div class="mt-5 ms-5 me-5">
        <div class="card mb-3">
            <div class="card-header">
                CADASTRO DE INVESTIMENTO
            </div>
            <c:if test="${not empty mensagem}">
                <div class="alert alert-success ms-2 me-2 m-auto">${mensagem}</div>
            </c:if>
            <c:if test="${not empty erro}">
                <div class="alert alert-danger ms-2 me-2 m-auto"> ${erro}</div>
            </c:if>
            <div class="card-body">
                <form action="investimentos?acao=cadastrar" method="post">
                    <div class="form-group">
                        <label for="tipoInvestimento">Tipo do Investimento</label>
                        <textarea name="tipoInvestimento" id="tipo-investimento" class="form-control" rows="1"
                                  placeholder="Informe o tipo de investimento (Exemplo: Tesouro Direto)" required></textarea>
                    </div>
                    <div class="form-group">
                        <label for="valorInvestimento">Valor do investimento</label>
                        <input type="number" step="0.01" name="valorInvestimento" id="valor-investimento"
                               class="form-control" placeholder="Informe o valor investido" required/>
                    </div>
                    <div class="form-group">
                        <label for="taxaRendimento">Taxa de Rendimento</label>
                        <div class="input-group">
                            <input type="number" step="0.01" name="taxaRendimento" id="taxa-rendimento"
                                   class="form-control"
                                   placeholder="Informe a taxa de rendimento"
                                   required/>
                            <span class="input-group-text">%</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataInvestimento">Data de Investimento</label>
                        <input type="date" name="dataInvestimento" id="data-investimento" class="form-control"
                               placeholder="Selecione a data de investimento" required/>
                    </div>
                    <div class="form-group">
                        <label for="dataResgate">Data de Resgate (Opcional)</label>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" id="data-resgate-checkbox"
                                   onchange="toggleDataResgate(this)">
                            <label class="form-check-label" for="data-resgate-checkbox">Sem data de resgate</label>
                        </div>
                        <div class="input-group">
                            <input type="date" name="dataResgate" id="data-resgate" class="form-control"
                                   placeholder="Selecione a data de resgate">
                        </div>
                    </div>
                    <input type="submit" value="Salvar" class="btn btn-primary mt-3">
                    <a href="investimentos?acao=listar" class="btn btn-secondary mt-3">Voltar</a>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
<script>
    function toggleDataResgate(checkbox) {
        const dataResgateInput = document.getElementById("data-resgate");
        if (checkbox.checked) {
            dataResgateInput.style.display = "none";
        } else {
            dataResgateInput.style.display = "block";
        }
    }
    window.onload = function () {
        const checkbox = document.getElementById("data-resgate-checkbox");
        toggleDataResgate(checkbox);
    };
</script>
<script src="resources/js/bootstrap.bundle.js"></script>
</body>
</html>