Write-Host "Compilando Maze-Solver..." -ForegroundColor Green

# Compilar todos los archivos Java
$compileResult = javac -d build/classes -cp . src/MazeSolver.java src/controller/MazeController.java src/controller/MazeActionListener.java src/controller/MazeChangeListener.java src/controller/listeners/*.java src/model/*.java src/model/generators/*.java src/model/solvers/*.java src/view/*.java src/view/drawable/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilacion exitosa! Ejecutando Maze-Solver..." -ForegroundColor Green
    java -cp build/classes MazeSolver
} else {
    Write-Host "Error en la compilacion!" -ForegroundColor Red
    Read-Host "Presiona Enter para continuar"
} 