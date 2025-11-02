<%@ page import="java.util.List" %>
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
    <link rel="icon" href="${pageContext.request.contextPath}/assets/error/img/favikronos.ico" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/error/style/error.css">
    <title>Erro 403 - Kronos</title>
</head>

<body>
    <header>
        <div class="kronos">
            <img src="${pageContext.request.contextPath}/assets/crud/img/favikronos.ico" alt="LOGO KRONOS" class="logoKronosMobile">
            <h1 class="logoKronosDesktop">KRONOS</h1>
        </div>
        <a href="${pageContext.request.contextPath}/landingpage/index.html" class="sairCrud">Voltar para Landing Page</a>
    </header>

    <main>
        <h1>
            ERRO 403 <br> 
            Você não pode acessar essa página!
        </h1>
        <img src="${pageContext.request.contextPath}/assets/error/img/engrenagem-quebrada-erro.png" class="engrenagemQuebrada" alt="">
        <h2>
            Oops! <br>
            O relógio despertou rápido demais para você, segura um pouco!
        </h2>
    </main>
</body>