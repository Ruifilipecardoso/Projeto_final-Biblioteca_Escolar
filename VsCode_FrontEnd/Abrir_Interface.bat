@echo off
title Servidor Frontend - SIBE
echo A iniciar o servidor web para o Frontend...

:: Sobe duas pastas para encontrar o Java portátil e inicia um mini-servidor na porta 3000
"..\SistemaInternoBibliotecaEscolar\java-runtime\bin\java" -m jdk.httpserver --port 3000 --directory .

pause