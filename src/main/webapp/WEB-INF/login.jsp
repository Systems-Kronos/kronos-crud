
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <!-------------------- Fontes -------------------->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap" rel="stylesheet">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/crud/style/autenticacao.css">
    <title>Entrar - Kronos</title>
</head>
<body>
<main>
    <h1>KRONOS</h1>
    <h2>Entre em sua conta</h2>
    <form action="login-crud" method="post">
        <div>
            <label for="email">Email</label>
            <input type="email" name="email" id="email" placeholder="Digite seu email" required>
        </div>

        <div>
            <label for="senha">Senha</label>
            <input type="password" name="senha" id="senha" placeholder="Digite sua senha" required>
        </div>

        <button type="submit" class="botaoCriarConta">Entrar</button>

        <div class="linhaComTexto">
            <hr><span>OU</span><hr>
        </div>

        <button type="submit" class="botaoCriarComGoogle"><img src="../assets/crud/img/logo-g-google.png" alt="">Entrar com o Google</button>

        <small>Ainda não possui conta? <a href="cadastro.jsp">Cadastre-se</a></small>
    </form>
</main>
</body>
</html>
