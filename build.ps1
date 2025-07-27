# Script para compilar y crear el JAR ejecutable
Write-Host "Compilando proyecto..." -ForegroundColor Green

# Crear directorio build si no existe
if (!(Test-Path "build")) {
    New-Item -ItemType Directory -Name "build"
}

# Compilar todos los archivos Java
$compileResult = javac -d build -cp "src;lib/*" src/*.java src/controller/*.java src/controller/listeners/*.java src/model/*.java src/model/generators/*.java src/model/solvers/*.java src/view/*.java src/view/drawable/*.java src/dao/*.java src/dao/daoImpl/*.java src/utils/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilación exitosa!" -ForegroundColor Green
    
    # Copiar archivos de recursos (imágenes)
    Write-Host "Copiando recursos..." -ForegroundColor Yellow
    if (Test-Path "src/images") {
        Copy-Item -Path "src/images" -Destination "build/images" -Recurse -Force
    }
    
    # Extraer bibliotecas JFreeChart al directorio build
    Write-Host "Extrayendo bibliotecas..." -ForegroundColor Yellow
    if (Test-Path "lib") {
        Get-ChildItem "lib/*.jar" | ForEach-Object {
            Write-Host "Extrayendo: $($_.Name)" -ForegroundColor Cyan
            jar xf $_.FullName
            # Mover el contenido extraído al directorio build
            if (Test-Path "org") {
                Move-Item "org" "build/" -Force
            }
            if (Test-Path "META-INF") {
                Move-Item "META-INF" "build/" -Force
            }
            if (Test-Path "jfree") {
                Move-Item "jfree" "build/" -Force
            }
        }
    }
    
    # Crear JAR ejecutable con dependencias incluidas
    Write-Host "Creando JAR ejecutable..." -ForegroundColor Yellow
    
    # Crear JAR directamente desde el directorio build
    jar cfm MazeSolver.jar MANIFEST.MF -C build .
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "¡JAR ejecutable creado exitosamente!" -ForegroundColor Green
        Write-Host "Archivo: MazeSolver.jar" -ForegroundColor Cyan
        Write-Host "Para ejecutar: java -jar MazeSolver.jar" -ForegroundColor Cyan
    } else {
        Write-Host "Error al crear el JAR" -ForegroundColor Red
    }
} else {
    Write-Host "Error en la compilación" -ForegroundColor Red
} 