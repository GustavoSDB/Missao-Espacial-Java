@echo off
setlocal

if exist "%~dp0.env" (
    for /f "usebackq eol=# tokens=1,* delims==" %%A in ("%~dp0.env") do (
        if not "%%A"=="" set "%%A=%%B"
    )
)

set "MAVEN_CMD=mvn"
where mvn >nul 2>nul
if errorlevel 1 (
    set "MAVEN_CMD=C:\Program Files\JetBrains\IntelliJ IDEA 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd"
)

"%MAVEN_CMD%" test
