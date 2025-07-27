package model;

public enum SolverType {
    BFS("BFS"),
    DFS("DFS"),
    RECURSIVO("Recursivo (2 dir)"),
    RECURSIVO_COMPLETO("Recursivo (4 dir)"),
    RECURSIVO_BT("Recursivo (4 dir + BT)");

    private final String name;

    SolverType(String name) {
        this.name = name;
    }

    /**
     * Returns the enum value corresponding to the input string (if it exists).
     *
     * @param name A string
     * @return The SolverType with the name corresponding to the input string (if it exists).
     */
    public static SolverType fromString(String name) {
        for (SolverType solverType : SolverType.values()) {
            if (solverType.name.equalsIgnoreCase(name)) {
                return solverType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}