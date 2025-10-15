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
    <link rel="stylesheet" href="../assets/crud/style/autenticacao.css">
    <title>Cadastre-se - Kronos</title>
</head>
<body>
    <main>    
        <h1>KRONOS</h1>    
        <h2>Crie sua conta</h2>
        <form action="" method="post">
            <div>
                <label for="nome">Nome</label>
                <input type="text" name="nome" id="nome" placeholder="Digite seu nome" required>
              </div>
            
              <div>
                <label for="cpf">CPF</label>
                <input type="text" name="cpf" id="cpf" placeholder="Digite seu CPF" required>
              </div>
            
              <div>
                <label for="genero">Gênero</label>
                <select name="genero" id="genero" required>
                  <option value="masculino">Masculino</option>
                  <option value="feminino">Feminino</option>
                  <option value="outro" selected>Outro</option>
                </select>
              </div>
            
              <div>
                <label for="telefone">Telefone</label>
                <input type="tel" name="telefone" id="telefone" placeholder="Digite seu telefone" required>
              </div>
            
              <div>
                <label for="email">Email</label>
                <input type="email" name="email" id="email" placeholder="Digite seu email" required>
              </div>
            
              <div>
                <label for="senha">Senha</label>
                <input type="password" name="senha" id="senha" placeholder="Digite sua senha" required>
              </div>
              
              <div class="checkboxTermos">
                <input type="checkbox" id="termos" name="termos" required>
                <label for="termos">Eu aceito todos os <a href="">Termos de Uso</a></label>
              </div>

              <button type="submit" class="botaoCriarConta">Criar</button>
              
              <div class="linhaComTexto">
                <hr><span>OU</span><hr>
              </div>
              
              <button type="submit" class="botaoCriarComGoogle"><img src="../assets/crud/img/logo-g-google.png" alt="">Criar com o Google</button>              
              
              <small>Já possui uma conta? <a href="login.jsp">Entrar</a></small>

        </form>
      </main>
</body>
</html>