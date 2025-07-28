package model;

/**
 * Directions available for maze generation and solving.
 */
public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    /**
     * The delta x and y coordinates (in the Cartesian coordinate system) for each direction
     */
    public final int dx, dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Calculates and returns the opposite direction of a direction (i.e. if UP, returns DOWN)
     *
     * @return The opposite direction of a direction
     */
    public Direction oppositeDirection() {
        Direction oppositeDirection = null;
        int opposite_dx = this.dx * -1;
        int opposite_dy = this.dy * -1;

        for (Direction dir : Direction.values()) {
            if (dir.dx == opposite_dx && dir.dy == opposite_dy) {
                oppositeDirection = dir;
                break;
            }
        }
        return oppositeDirection;
    }
}