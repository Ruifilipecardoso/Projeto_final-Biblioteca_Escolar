@echo off
title Painel de Controlo - SIBE
chcp 65001 > nul

cls
echo =======================================================
echo     SISTEMA INTERNO DE BIBLIOTECA ESCOLAR (SIBE)
echo =======================================================
echo.

:: Guarda o caminho absoluto da pasta principal do projeto
set "RAIZ_PROJETO=%~dp0"
set "JAVA_BIN=%RAIZ_PROJETO%Intelij_BackEnd\SistemaInternoBibliotecaEscolar\java-runtime\bin"
set "JAR_PATH=%RAIZ_PROJETO%Intelij_BackEnd\SistemaInternoBibliotecaEscolar\target\SistemaInternoBibliotecaEscolar-0.0.1-SNAPSHOT.jar"
set "PROP_PATH=%RAIZ_PROJETO%Intelij_BackEnd\SistemaInternoBibliotecaEscolar"

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
echo Opção inválida. Digite S para Sim ou N para Não.
echo.
goto PERGUNTA

:INICIAR
cls
echo =======================================================
echo     A INICIAR OS SERVIDORES DA APLICAÇÃO
echo =======================================================
echo.

:: 1. Iniciar o Servidor Backend (Spring Boot direto)
echo [1/3] A ligar o servidor Spring Boot...
cd /d "%PROP_PATH%"
start "SIBE_BACKEND" /min "%JAVA_BIN%\java" -jar "%JAR_PATH%" --spring.config.additional-location=file:./application.properties

:: Aguarda 8 segundos para dar tempo ao Spring Boot de iniciar o Tomcat
timeout /t 8 /nobreak > nul

:: 2. Iniciar o Servidor Frontend (Java HTTP Server)
echo [2/3] A iniciar o servidor da interface web...
cd /d "%RAIZ_PROJETO%VsCode_FrontEnd"
start "SIBE_FRONTEND" /min "" "%JAVA_BIN%\java" -m jdk.httpserver --port 3000 --directory .

:: 3. Abrir o Navegador automaticamente
echo [3/3] A abrir a aplicação no seu navegador...
start http://localhost:3000/login.html

:AGUARDAR
cls
echo =======================================================
echo     SISTEMA EM EXECUÇÃO (PORTAS 8080 E 3000)
echo =======================================================
echo.
echo  A aplicação foi aberta com sucesso no seu navegador.
echo  Mantenha esta janela aberta enquanto utilizar o sistema.
echo.
echo =======================================================

:PERGUNTA_DESLIGAR
set /p DESLIGAR_CONFIRM="Deseja desligar o sistema? [S]: "
if /i "%DESLIGAR_CONFIRM%"=="S" (
    goto SHUTDOWN
)
echo.
echo Operação cancelada. O sistema continua ativo em fundo.
echo.
goto PERGUNTA_DESLIGAR

:SHUTDOWN
cls
echo =======================================================
echo     A ENCERRAR OS SERVIDORES DO SISTEMA
echo =======================================================
echo.

echo A fechar as janelas do terminal (Backend e Frontend)...
taskkill /f /fi "WINDOWTITLE eq SIBE_BACKEND" 2>nul
taskkill /f /fi "WINDOWTITLE eq SIBE_FRONTEND" 2>nul

echo A libertar as portas locais e processos Java...
taskkill /f /im java.exe 2>nul

echo.
echo =======================================================
echo     SISTEMA DESLIGADO COM SUCESSO!
echo =======================================================
echo Todas as janelas em fundo e servidores foram encerrados.
echo A fechar este painel...
timeout /t 3 > nul
exit