package model;

/**
 * Enum que representa los diferentes estados que puede tener una celda en el laberinto
 */
public enum CellState {
    EMPTY("Vacía"),
    WALL("Muro"),
    START("Inicio"),
    END("Fin"),
    VISITED("Visitada"),
    SOLUTION("Solución"),
    CURRENT("Actual");

    private final String displayName;

    CellState(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Obtiene el nombre para mostrar
     * @return Nombre legible del estado
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Convierte un string a CellState
     * @param name Nombre del estado
     * @return CellState correspondiente o null si no se encuentra
     */
    public static CellState fromString(String name) {
        for (CellState state : CellState.values()) {
            if (state.name().equalsIgnoreCase(name) ||
                    state.displayName.equalsIgnoreCase(name)) {
                return state;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}