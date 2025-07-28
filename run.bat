@echo off
echo Compilando Maze-Solver...
javac -d build/classes -cp . src/MazeSolver.java src/controller/MazeController.java src/controller/MazeActionListener.java src/controller/MazeChangeListener.java src/controller/listeners/*.java src/model/*.java src/model/generators/*.java src/model/solvers/*.java src/view/*.java src/view/drawable/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilacion exitosa! Ejecutando Maze-Solver...
    java -cp build/classes MazeSolver
) else (
    echo Error en la compilacion!
    pause
) 