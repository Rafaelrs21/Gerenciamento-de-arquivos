@echo off
echo Executando testes Selenium...

REM Verifica se o Chrome está instalado
where chrome >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Chrome não encontrado. Por favor, instale o Google Chrome.
    exit /b 1
)

REM Executa os testes
mvn test -Dtest=LoginTest

echo.
echo Testes concluídos!
pause 