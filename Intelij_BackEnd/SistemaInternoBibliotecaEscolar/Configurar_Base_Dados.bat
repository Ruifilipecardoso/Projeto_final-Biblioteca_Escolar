@echo off
title Configurar Base de Dados - SIBE
chcp 65001 > nul

echo =======================================================
echo     CONFIGURAÇÃO DA BASE DE DADOS - PROJETO SIBE
echo =======================================================
echo.

:: 1. Pedir os dados de acesso ao utilizador
set /p PG_USER="Introduza o utilizador do PostgreSQL [Predefinição: postgres]: "
if "%PG_USER%"=="" set PG_USER=postgres

set /p PG_PASS="Introduza a palavra-passe do PostgreSQL: "

set /p PG_VERSION="Introduza a versão do PostgreSQL instalada (ex: 15, 16, 17): "

:: 2. Definir caminhos e variáveis de ambiente locais
set PGPASSWORD=%PG_PASS%
set PG_PATH=C:\Program Files\PostgreSQL\%PG_VERSION%\bin

:: 3. Verificar se o PostgreSQL existe no caminho indicado
if not exist "%PG_PATH%\createdb.exe" (
    echo.
    echo [ERRO] Não foi possível encontrar o PostgreSQL na pasta especificada.
    echo Caminho testado: %PG_PATH%
    echo Por favor, verifique se a versão introduzida está correta.
    goto FIM
)

echo.
echo A criar a base de dados "Sistema_Interno_de_Biblioteca_Escolar"...
"%PG_PATH%\createdb.exe" -U %PG_USER% Sistema_Interno_de_Biblioteca_Escolar
if %errorlevel% neq 0 (
    echo [AVISO] Se a base de dados já existir, o processo vai continuar para a importação.
)

echo.
echo A importar as tabelas e dados iniciais (sibe_base_dados.sql)...
"%PG_PATH%\psql.exe" -U %PG_USER% -d Sistema_Interno_de_Biblioteca_Escolar -f sibe_base_dados.sql

echo.
echo =======================================================
echo     PROCESSO CONCLUÍDO COM SUCESSO!
echo =======================================================

:FIM
set PGPASSWORD=
pause