@echo off
setlocal

set "MAVEN_CMD=mvn"
where mvn >nul 2>nul
if errorlevel 1 (
    set "MAVEN_CMD=C:\Program Files\JetBrains\IntelliJ IDEA 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd"
)

"%MAVEN_CMD%" test
