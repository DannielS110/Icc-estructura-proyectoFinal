@echo off
echo ========================================
echo    Resolvedor de Laberintos
echo    Proyecto Final
echo ========================================
echo.
echo Iniciando aplicacion...
echo.

java -jar MazeSolver.jar

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error: No se pudo ejecutar la aplicacion.
    echo Verifique que Java esté instalado correctamente.
    echo.
    pause
) 