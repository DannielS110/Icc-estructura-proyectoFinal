@echo off
echo Compilando proyecto...

REM Crear directorio build si no existe
if not exist "build" mkdir build

REM Compilar todos los archivos Java
javac -d build -cp "src;lib/*" src/*.java src/controller/*.java src/controller/listeners/*.java src/model/*.java src/model/generators/*.java src/model/solvers/*.java src/view/*.java src/view/drawable/*.java src/dao/*.java src/dao/daoImpl/*.java src/utils/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacion exitosa!
    
    REM Copiar archivos de recursos (imágenes)
    echo Copiando recursos...
    if exist "src\images" (
        xcopy "src\images" "build\images" /E /I /Y
    )
    
    REM Extraer bibliotecas JFreeChart al directorio build
    echo Extrayendo bibliotecas...
    if exist "lib" (
        for %%f in (lib\*.jar) do (
            echo Extrayendo: %%f
            jar xf "%%f"
            REM Mover el contenido extraído al directorio build
            if exist "org" (
                move "org" "build\" /Y
            )
            if exist "META-INF" (
                move "META-INF" "build\" /Y
            )
            if exist "jfree" (
                move "jfree" "build\" /Y
            )
        )
    )
    
    REM Crear JAR ejecutable con dependencias incluidas
    echo Creando JAR ejecutable...
    
    REM Crear JAR directamente desde el directorio build
    jar cfm MazeSolver.jar MANIFEST.MF -C build .
    
    if %ERRORLEVEL% EQU 0 (
        echo ¡JAR ejecutable creado exitosamente!
        echo Archivo: MazeSolver.jar
        echo Para ejecutar: java -jar MazeSolver.jar
    ) else (
        echo Error al crear el JAR
    )
) else (
    echo Error en la compilacion
)

pause 