@echo off
title Sistema Interno de Biblioteca Escolar - Servidor
echo A iniciar o servidor da Biblioteca...
".\java-runtime\bin\java" -jar target/SistemaInternoBibliotecaEscolar-0.0.1-SNAPSHOT.jar --spring.config.additional-location=file:./application.properties
pause

===================================================================
  SISTEMA INTERNO DE BIBLIOTECA ESCOLAR (SIBE) - GUIA DE INSTALAÇÃO
===================================================================

REQUISITOS DO SISTEMA:
1. Ter o Java 17 ou superior instalado no computador.
2. Ter o PostgreSQL instalado.

PASSO A PASSO PARA CONFIGURAÇÃO:

Passo 1: Criar a Base de Dados
- Abra o pgAdmin.
- Crie uma nova base de dados chamada: Sistema_Interno_de_Biblioteca_Escolar
- Abra a ferramenta de Query (Query Tool) e execute o ficheiro "sibe_base_dados.sql" para criar as tabelas e dados iniciais.

Passo 2: Configurar as Credenciais
- Abra o ficheiro "application.properties" com o Bloco de Notas.
- Altere as linhas "username" e "password" com os seus dados de acesso ao PostgreSQL, caso sejam diferentes dos padrões.

Passo 3: Iniciar o Sistema
- Dê um duplo clique no ficheiro "Ligar_Servidor.bat".
- Mantenha a janela preta aberta.
- Abra o ficheiro "login.html" no seu navegador de internet para começar a usar o sistema!