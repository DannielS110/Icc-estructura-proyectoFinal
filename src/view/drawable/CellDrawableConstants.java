package view.drawable;

/**
 * Constants for cell drawing
 */
class CellDrawableConstants {
	static final int CELL_SIZE = 20;
	static final int MARGIN = 10;
	static final int WALL_STROKE_SIZE = 2;
	static final int SOLUTION_PATH_STROKE_SIZE = 1;
	static final int SOLUTION_ROUTE_POINT_SCALING_FACTOR = 4;
	static final double SOLUTION_ROUTE_POINT_SIZE = ((double) CELL_SIZE - ((double) WALL_STROKE_SIZE / 2)) /
			(double) SOLUTION_ROUTE_POINT_SCALING_FACTOR;
	static final double SOLUTION_ROUTE_POINT_OFFSET = ((double) WALL_STROKE_SIZE / 2) + ((double) CELL_SIZE / 2) -
			((double) CELL_SIZE / (2 * (double) SOLUTION_ROUTE_POINT_SCALING_FACTOR));
	static final double SOLUTION_ROUTE_PATH_OFFSET = (double) WALL_STROKE_SIZE / 2 + (double) CELL_SIZE / 2;
	static final double CELL_OFFSET = (double) WALL_STROKE_SIZE / 2;
}
