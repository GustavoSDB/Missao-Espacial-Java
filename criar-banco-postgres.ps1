$ErrorActionPreference = "Stop"

$envPath = Join-Path $PSScriptRoot ".env"
if (Test-Path $envPath) {
    Get-Content $envPath | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#") -and $line.Contains("=")) {
            $parts = $line.Split("=", 2)
            $name = $parts[0].Trim()
            $value = $parts[1].Trim()

            if ($name -and -not [Environment]::GetEnvironmentVariable($name, "Process")) {
                [Environment]::SetEnvironmentVariable($name, $value, "Process")
            }
        }
    }
}

$candidates = @()
$command = Get-Command psql -ErrorAction SilentlyContinue
if ($command) {
    $candidates += $command.Source
}

$candidates += @(
    "C:\Program Files\PostgreSQL\18\bin\psql.exe",
    "C:\Program Files\PostgreSQL\17\bin\psql.exe",
    "C:\Program Files\PostgreSQL\16\bin\psql.exe",
    "C:\Program Files\PostgreSQL\15\bin\psql.exe",
    "C:\Program Files\PostgreSQL\14\bin\psql.exe",
    "C:\Program Files\PostgreSQL\13\bin\psql.exe",
    "C:\Program Files\PostgreSQL\12\bin\psql.exe"
)

$psql = $candidates | Where-Object { $_ -and (Test-Path $_) } | Select-Object -First 1
if (-not $psql) {
    Write-Host "psql nao encontrado. Instale o PostgreSQL ou adicione o psql ao PATH."
    exit 1
}

$usuarioPadrao = if ($env:DB_USERNAME) { $env:DB_USERNAME } else { "postgres" }
$usuario = Read-Host "Usuario do PostgreSQL [$usuarioPadrao]"
if ([string]::IsNullOrWhiteSpace($usuario)) {
    $usuario = $usuarioPadrao
}

if ($env:DB_PASSWORD) {
    $ptr = [Runtime.InteropServices.Marshal]::StringToBSTR($env:DB_PASSWORD)
} else {
    $senhaSegura = Read-Host "Senha do PostgreSQL" -AsSecureString
    $ptr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($senhaSegura)
}

try {
    $env:PGPASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr)

    $existe = & $psql -h localhost -p 5432 -U $usuario -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = 'missao_espacial';"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Nao foi possivel conectar ao PostgreSQL. Verifique usuario e senha."
        Write-Host "Use a senha definida na instalacao do PostgreSQL, nao a senha do Windows."
        exit $LASTEXITCODE
    }

    if ($existe.Trim() -eq "1") {
        Write-Host "Banco missao_espacial ja existe."
        exit 0
    }

    & $psql -h localhost -p 5432 -U $usuario -d postgres -c "CREATE DATABASE missao_espacial;"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Nao foi possivel criar o banco missao_espacial."
        exit $LASTEXITCODE
    }

    Write-Host "Banco missao_espacial criado com sucesso."
} finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)
    Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
}
