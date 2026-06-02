@echo off
title Inicializador Geral - SIBE
chcp 65001 > nul

:: Guarda o caminho absoluto da pasta principal do projeto
set "RAIZ_PROJETO=%~dp0"

echo =======================================================
echo     SISTEMA INTERNO DE BIBLIOTECA ESCOLAR (SIBE)
echo =======================================================
echo.

:PERGUNTA
set /p PRIMEIRA_VEZ="É a primeira vez que está a correr o sistema? [S/N]: "
if /i "%PRIMEIRA_VEZ%"=="S" (
    echo.
    echo [PASSO INICIAL] A abrir a configuração da Base de Dados...
    cd /d "%RAIZ_PROJETO%Intelij_BackEnd\SistemaInternoBibliotecaEscolar"
    call Configurar_Base_Dados.bat
    echo.
    echo Base de dados processada. A avançar para a inicialização...
    echo.
    goto INICIAR
)
if /i "%PRIMEIRA_VEZ%"=="N" (
    goto INICIAR
)
echo Opção inválida. Por favor, digite S para Sim ou N para Não.
echo.
goto PERGUNTA

:INICIAR
echo =======================================================
echo     A INICIAR OS SERVIDORES DA APLICAÇÃO
echo =======================================================
echo.

:: 1. Iniciar o Servidor Backend (Spring Boot)
echo [1/3] A ligar o servidor Spring Boot...
cd /d "%RAIZ_PROJETO%Intelij_BackEnd\SistemaInternoBibliotecaEscolar"
start /min Ligar_Servidor.bat

:: Aguarda 8 segundos para dar tempo ao Spring Boot de iniciar o Tomcat
timeout /t 8 /nobreak > nul

:: 2. Iniciar o Servidor Frontend (Java HTTP Server)
echo [2/3] A iniciar o servidor da interface web...
cd /d "%RAIZ_PROJETO%VsCode_FrontEnd"
start /min "" "%RAIZ_PROJETO%Intelij_BackEnd\SistemaInternoBibliotecaEscolar\java-runtime\bin\java" -m jdk.httpserver --port 3000 --directory .

:: 3. Abrir o Navegador automaticamente
echo [3/3] A abrir a aplicação no seu navegador...
start http://localhost:3000/login.html

echo.
echo =======================================================
echo     SISTEMA EM EXECUÇÃO COM SUCESSO!
echo =======================================================
echo As janelas de plano de fundo foram minimizadas.
echo Pode começar a utilizar a aplicação no seu navegador.
echo.
pause