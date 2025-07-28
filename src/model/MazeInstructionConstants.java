package model;

/**
 * Constants for maze instructions
 */
public class MazeInstructionConstants {
    static final String INIT = "Configura el número deseado de filas y columnas para el laberinto, y luego haz clic " +
            "en \"Generar\" para crear un laberinto.";
    static final String GENERATING = "¡Generando un nuevo laberinto! Ajusta el deslizador de animación para acelerar o ralentizar " +
            "la animación. Para interrumpir la generación del laberinto, haz clic en el botón \"Reiniciar\".";
    static final String GENERATED = "¡Laberinto generado exitosamente! Establece los puntos de inicio y fin del laberinto " +
            "haciendo clic en una celda del laberinto (los puntos de inicio y fin se establecerán por defecto si no lo haces), " +
            "elige el algoritmo de solución del laberinto, y luego haz clic en el botón \"Resolver\" para resolver el laberinto.";
    static final String SOLVING = "¡Resolviendo el laberinto! Ajusta el deslizador de animación para acelerar o ralentizar " +
            "la animación. Para interrumpir la resolución del laberinto, haz clic en el botón \"Reiniciar\".";
    static final String SOLVED = "¡Laberinto resuelto! Haz clic en el botón \"Reiniciar\" para comenzar de nuevo.";
    static final String WALL_EDIT = "Modo de edición de paredes activado. Haz clic en las paredes para agregarlas o quitarlas. Haz clic en \"Salir Edición\" para volver al modo normal.";
}