package org.flingsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FlingSolver implements SolverCallback {

	private static final Logger logger = LogManager
			.getLogger(FlingSolver.class);

	private int gridWidth = 7;
	private int gridHeight = 8;
	private Grid startingGrid;

	public FlingSolver() {
		startingGrid = new Grid(gridWidth, gridHeight);
	}

	public FlingSolver(Grid startingGrid) {
		this.startingGrid = startingGrid;
	}

	public Object getGridWidth() {
		return gridWidth;
	}

	public Object getGridHeight() {
		return gridHeight;
	}

	public Grid getStartingGrid() {
		return startingGrid;
	}

	public void addFurry(int x, int y) {
		startingGrid.addFurry(x, y);
	}

	List<List<MoveNode>> findSolutions() {
		MoveNode rootNode = new MoveNode(null, null, startingGrid, this);
		List<List<MoveNode>> solutionsHolder = new ArrayList<List<MoveNode>>();
		populateNodes(rootNode, solutionsHolder);
		return solutionsHolder;
	}

	public void solve() {
		List<List<MoveNode>> solutionsHolder = findSolutions();
		if (solutionsHolder.size() == 0) {
			logger.info("solve:: NO SOLUTIONS FOUND FOR grid "
					+ startingGrid.toString());
		} else {
			logger.info("solve:: " + solutionsHolder.size()
					+ " solutions found.");
			int solutionCounter = 0;
			for (List<MoveNode> solution : solutionsHolder) {
				logger.info("");
				logger.info("SOLUTION " + (++solutionCounter) + " solved in "
						+ (solution.size() - 1) + " moves.");
				logger
						.info("=================================================");
				for (int moveCounter = 0; moveCounter < solution.size(); moveCounter++) {
					Move nextMove = null;
					if (moveCounter < solution.size() - 1) {
						nextMove = solution.get(moveCounter + 1).getMove();
					}
					logger.info("Step " + (moveCounter + 1) + ". ");
					logger.info(solution.get(moveCounter)
							.printGridWithMoveArrows(nextMove));
				}
			}
		}
	}

	void populateNodes(MoveNode nodeToPopulate, List<List<MoveNode>> solutions) {
		boolean populatedOk = nodeToPopulate.populateChildNodes();
		if (populatedOk) {
			int invalidNodeCount=0;
			for (MoveNode childNode : nodeToPopulate.getChildNodes()) {
			populateNodes(childNode, solutions);
			if (childNode.isNodeLeafInvalidSolution()){
				invalidNodeCount++;
			}
			
		  }
	      if (invalidNodeCount == nodeToPopulate.getChildNodes().size()){
	    	  nodeToPopulate.prune();
	      }
		} else {
			if (nodeToPopulate.isNodeLeafSolution()) {
				logger.info("populateNodes::solution found.");
				solutions.add(nodeToPopulate
						.getMoveNodesInOrderThatEndInThisNode());
			} 

		}
	}

	public Set<Move> getValidMoves(Grid grid) {
		Set<Move> validMoves = new HashSet<Move>();
		for (int y = 1; y <= grid.getGridHeight(); y++) {
			for (int x = 1; x <= grid.getGridWidth(); x++) {
				for (Direction dir : Direction.values()) {
					if (grid.isFurryPresent(x, y)) {
						Move move = calcMove(grid, x, y, dir);
						if (move.getMoveStatus() == MoveStatus.VALID) {
							validMoves.add(move);
						}
					}
				}
			}
		}
		return validMoves;
	}

	public Grid moveAndTransform(Grid grid, Move move) {
		Grid gridToTransform = grid.clone();
		GridPosition hitFurry = move.getGridPositionOfHittableFurry();
		int newX = move.getX(), newY = move.getY();
		if (hitFurry != null) {
			switch (move.getDir()) {
			case LEFT:
				newX = hitFurry.getX() + 1;
				break;
			case RIGHT:
				newX = hitFurry.getX() - 1;
				break;
			case UP:
				newY = hitFurry.getY() - 1;
				break;
			case DOWN:
				newY = hitFurry.getY() + 1;
				break;
			}
			gridToTransform.moveFurry(move.getX(), move.getY(), newX, newY);
			GridPosition nextHittableFurryPosition = grid
					.getGridPositionOfHittableFurry(hitFurry.getX(), hitFurry
							.getY(), move.getDir(), true);
			return moveAndTransform(gridToTransform, new Move(hitFurry.getX(),
					hitFurry.getY(), move.getDir(), MoveStatus.VALID_BEEN_HIT,
					nextHittableFurryPosition));
		} else {
			gridToTransform.rollFurryOffGrid(move.getX(), move.getY());
		}
		return gridToTransform;
	}

	public Move calcMove(Grid grid, int x, int y, Direction dir) {
		MoveStatus moveStatus = MoveStatus.INVALID_NOT_DEFINED;
		GridPosition hittableFurryPosition = null;
		if (!grid.isFurryPresent(x, y)) {
			moveStatus = MoveStatus.INVALID_NON_EXISTANT_FURRY;
		} else if (grid.isFurryMovingOutsideGrid(x, y, dir)) {
			moveStatus = MoveStatus.INVALID_OUTSIDE_GRID;
		} else if (grid.isFurryAdjacent(x, y, dir)) {
			moveStatus = MoveStatus.INVALID_FURRY_ADJACENT;
		} else {
			hittableFurryPosition = grid.getGridPositionOfHittableFurry(x, y,
					dir);
			if (hittableFurryPosition == null) {
				moveStatus = MoveStatus.INVALID_NOTHING_TO_HIT;
			} else {
				moveStatus = MoveStatus.VALID;
			}
		}
		logger.debug("calcMoveStatus:: " + moveStatus
				+ " when moving furry from position " + x + "," + y
				+ " in direction " + dir + " ");
		return new Move(x, y, dir, moveStatus, hittableFurryPosition);
	}

	public boolean loadGrid(String testFileName) {
		InputStream in = null;
		// first try and load file using filename directly
		File gridFile = new File(testFileName);
		if (!gridFile.exists()) {
			in = this.getClass().getResourceAsStream(testFileName);
		} else {
			try {
				in = new FileInputStream(gridFile);
			} catch (FileNotFoundException e) {
				logger.error("File not found exception for file " + gridFile);
			}
		}
		if (in == null) {
			logger.error("Unable to read from file " + testFileName
					+ " exiting.");
			return false;
		} else {
			Grid grid = new Grid(7, 8);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String gridLine = null;
			int rowCount = 8;

			try {
				while ((gridLine = reader.readLine()) != null) {
					if (rowCount < 1) {
						logger.error("File must only contain 8 rows, exiting");
						return false;
					}
					if (gridLine.length() != 7) {
						logger
								.error("Each row must contain 7 characters, exiting");
						return false;
					}
					for (int i = 0; i < 7; i++) {
						if (gridLine.charAt(i) != '0') {
							grid.addFurry(i + 1, rowCount);
						}
					}
					rowCount--;

				}
				if (rowCount != 0) {
					logger.error("File contains insufficient rows, exiting");
					return false;
				}
			} catch (IOException e) {
				logger.error("Error reading row" + e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			startingGrid = grid;
			return true;
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			logger.info("Must provide starting Grid File name as only parameter");
			logger.info("Grid File must contain a 7x8 grid of the form\n");
			logger.info("0000000");
			logger.info("0100000");
			logger.info("0100001");
			logger.info("1000000");
			logger.info("0100001");
			logger.info("0000000");
			logger.info("0010010");
			logger.info("0000000");
			logger.info("");
            logger.info("with the 1's indicating the position of the furries.");
            System.exit(1);
		} else {
			String fileName = args[0];
			logger.info("Loading grid " + fileName);
			FlingSolver solver = new FlingSolver();
			if (solver.loadGrid(fileName)){
				solver.solve();
			}
		}
	}

}
