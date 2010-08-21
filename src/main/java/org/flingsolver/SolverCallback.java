package org.flingsolver;

import java.util.Set;

public interface SolverCallback {
	Grid moveAndTransform(Grid grid, Move move);
	Set<Move>getValidMoves(Grid grid);
}
