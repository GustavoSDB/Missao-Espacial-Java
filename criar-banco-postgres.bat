@echo off
setlocal

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0criar-banco-postgres.ps1"
set "EXIT_CODE=%ERRORLEVEL%"

echo.
if not "%EXIT_CODE%"=="0" (
    echo O script terminou com erro. Veja a mensagem acima.
)
pause
exit /b %EXIT_CODE%
