package model;

/**
 * The potential states for the maze to be in.
 */
public enum MazeState {
    INIT(MazeInstructionConstants.INIT), GENERATING(MazeInstructionConstants.GENERATING),
    GENERATED(MazeInstructionConstants.GENERATED), SOLVING(MazeInstructionConstants.SOLVING),
    SOLVED(MazeInstructionConstants.SOLVED), WALL_EDIT(MazeInstructionConstants.WALL_EDIT);

    public final String instruction;

    MazeState(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}