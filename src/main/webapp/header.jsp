<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<nav class="navbar navbar-dark navbar-expand-lg bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="home.jsp">FreeTech</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" href="sobre.jsp">Sobre</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="contato.jsp">Contato</a>
                </li>
            </ul>
            <c:if test="${empty user}">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" href="usuario?acao=login-usuario">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="usuario?acao=cadastro-usuario">Cadastro</a>
                    </li>
                </ul>
            </c:if>
            <c:if test="${not empty user}">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            Menu
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="transacoes?acao=listar-por-usuario">Minhas transações</a></li>
                            <li><a class="dropdown-item" href="investimentos?acao=listar">Meus investimentos</a></li>
                            <li><a class="dropdown-item" href="conta-bancaria?acao=listar">Minhas contas bancárias</a></li>
                            <li><a class="dropdown-item" href="perfil?acao=abrir-perfil">Meu perfil</a></li>
                        </ul>
                    </li>
                    <form action="usuario" method="post" class="d-inline">
                        <input type="hidden" name="acao" value="logout">
                        <button type="submit" class="btn btn-danger">Sair</button>
                    </form>
                </ul>
            </c:if>
        </div>
    </div>
</nav>