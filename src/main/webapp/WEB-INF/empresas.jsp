<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <!-------------------- Fontes -------------------->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
        href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap"
        rel="stylesheet">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/crud/style/dados.css">
    <title>CRUD - Kronos</title>
</head>

<body>
    <div class="meuPlaceholder"></div>
    <header>
        <h1>KRONOS</h1>
        <nav>
            <ul>
                <li><a href="#"><img src="../assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
                <li><a href="#" class="ativo"><img src="../assets/crud/img/img-crud-empresas.png" alt=""> Empresas</a></li>
                <li><a href="#"><img src="../assets/crud/img/img-crud-planos.png" alt=""> Planos</a></li>
                <li><a href="#"><img src="../assets/crud/img/img-crud-habilidades.png" alt=""> Habilidades</a></li>
                <li><a href="#"><img src="../assets/crud/img/img-crud-setores.png" alt=""> Setores</a></li>
                <li><a href="#"><img src="../assets/crud/img/img-crud-usuario.png" alt=""> Usuário</a></li>
            </ul>
        </nav>
    </header>

    <div class="conteudoPrincipal">
        <div class="procurarCadastrar">
            <form class="pesquisa">
                <input type="search" placeholder="Pesquisar" class="buscar">

                <details class="filtros">
                    <summary>Filtros</summary>
                    <div class="conteudoFiltros">
                        <label class="opcaoFiltro">
                            <input type="radio" name="ordem" value="crescente">Crescente
                        </label>
                        <label class="opcaoFiltro">
                            <input type="radio" name="ordem" value="decrescente">Decrescente
                        </label>
                        <button type="reset">Limpar filtros</button>
                    </div>
                </details>
            </form>

            <section>
                <button type="button" onclick="" class="cadastrar">Cadastrar</button>
            </section>
        </div>

        <main>
            <table class="tabelaEmpresas">
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>CEP</th>
                        <th>E-mail</th>
                        <th>Mais Detalhes</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Empresa 1</td>
                        <td>30.567.991/0001-50</td>
                        <td>02545-140</td>
                        <td>empresa1@dominio.com</td>
                        <td><a href="#" target="_blank" class="detalhes"><img src="../assets/crud/img/mais-detalhes.png" alt=""></a></td>
                    </tr>
                    <tr>
                        <td>Empresa 1</td>
                        <td>30.567.991/0001-50</td>
                        <td>02545-140</td>
                        <td>empresa1@dominio.com</td>
                        <td><a href="#" target="_blank" class="detalhes"><img src="../assets/crud/img/mais-detalhes.png" alt=""></a></td>
                    </tr>
                    <tr>
                        <td>Empresa 1</td>
                        <td>30.567.991/0001-50</td>
                        <td>02545-140</td>
                        <td>empresa1@dominio.com</td>
                        <td><a href="#" target="_blank" class="detalhes"><img src="../assets/crud/img/mais-detalhes.png" alt=""></a></td>
                    </tr>
                    <tr>
                        <td>Empresa 1</td>
                        <td>30.567.991/0001-50</td>
                        <td>02545-140</td>
                        <td>empresa1@dominio.com</td>
                        <td><a href="#" target="_blank" class="detalhes"><img src="../assets/crud/img/mais-detalhes.png" alt=""></a></td>
                    </tr>
                    <tr>
                        <td>Empresa 1</td>
                        <td>30.567.991/0001-50</td>
                        <td>02545-140</td>
                        <td>empresa1@dominio.com</td>
                        <td><a href="#" target="_blank" class="detalhes"><img src="../assets/crud/img/mais-detalhes.png" alt=""></a></td>
                    </tr>
                    <tr>
                        <td>Empresa 1</td>
                        <td>30.567.991/0001-50</td>
                        <td>02545-140</td>
                        <td>empresa1@dominio.com</td>
                        <td><a href="#" target="_blank" class="detalhes"><img src="../assets/crud/img/mais-detalhes.png" alt=""></a></td>
                    </tr>
                </tbody>
            </table>
        </main>
    </div>
</body>
</html>