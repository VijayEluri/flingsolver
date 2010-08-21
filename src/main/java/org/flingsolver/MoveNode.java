package org.flingsolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Used to create a tree of valid fling moves
 * 
 * @author kierone
 * 
 */
public class MoveNode {

	private Move move;
	private Set<MoveNode> childNodes;
	private MoveNode parentNode;
	private Grid currentGrid;
	private SolverCallback solverCallback;
	private static final Logger logger = LogManager
	.getLogger(MoveNode.class);


	public MoveNode(Move move, MoveNode parentNode, Grid currentGrid,
			SolverCallback solverCallback) {
		super();
		this.move = move;
		this.parentNode = parentNode;
		this.currentGrid = currentGrid;
		this.solverCallback = solverCallback;
	}

	public Move getMove() {
		return move;
	}

	public Set<MoveNode> getChildNodes() {
		return childNodes;
	}

	public MoveNode getParentNode() {
		return parentNode;
	}

	public Grid getCurrentGrid() {
		return currentGrid;
	}

	public boolean populateChildNodes() {
		if (isNodeLeafSolution()) {
			return false;
		}
		childNodes = new HashSet<MoveNode>();
		Set<Move> validMoves = solverCallback.getValidMoves(currentGrid);
		if (validMoves == null || validMoves.size() == 0) {
			return false;
		}
		for (Move move : validMoves) {
			Grid nextGrid = solverCallback.moveAndTransform(currentGrid, move);
			childNodes.add(new MoveNode(move, this, nextGrid, solverCallback));
		}
		return true;
	}

	public boolean isNodeLeafSolution() {
		return currentGrid.getFurryCount() == 1;
	}

	public boolean isNodeLeafInvalidSolution() {
		return currentGrid.getFurryCount() > 1
				&& (childNodes == null || childNodes.size() == 0);
	}

	public List<MoveNode> getMoveNodesInOrderThatEndInThisNode() {
		List<MoveNode> moveNodes = new ArrayList<MoveNode>();
		moveNodes.add(this);
		MoveNode currentNode = getParentNode();
		while (currentNode != null) {
			moveNodes.add(currentNode);
			currentNode = currentNode.getParentNode();
		}
		Collections.reverse(moveNodes);
		return moveNodes;

	}

	@Override
	public String toString() {
		return "MoveNode [childNodes=" + childNodes + ", currentGrid="
				+ currentGrid + ", move=" + move + ", parentNode=" + parentNode
				+ "]";
	}
	
	public String printGridWithMoveArrows(Move nextMove) {
			StringBuffer outputBuffer = new StringBuffer("\n\n");
			int[][]gridArr = currentGrid.getGridArr();
			for (int i=gridArr.length-1; i >= 0; i--  ){
				int[] rowArr = gridArr[i];
				for (int j=0; j < rowArr.length; j++){
					if (nextMove != null && i+1 == nextMove.getY() && j+1 == nextMove.getX()){
						switch (nextMove.getDir()){
						case LEFT : outputBuffer.append("L ");
						            break;
						case RIGHT : outputBuffer.append("R ");
			                        break;
						case UP : outputBuffer.append("U ");
			                      break;
						case DOWN : outputBuffer.append("D ");
			                      break;
						}
					}
					else if (rowArr[j]==1){
						outputBuffer.append("X ");
					} else {
					outputBuffer.append(rowArr[j] + " ");
					}
				}
				outputBuffer.append("\n");
			}
			return outputBuffer.toString();
		}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentGrid == null) ? 0 : currentGrid.hashCode());
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoveNode other = (MoveNode) obj;
		if (currentGrid == null) {
			if (other.currentGrid != null)
				return false;
		} else if (!currentGrid.equals(other.currentGrid))
			return false;
		if (move == null) {
			if (other.move != null)
				return false;
		} else if (!move.equals(other.move))
			return false;

		return true;
	}

	public void prune() {
		this.childNodes.clear();
	}

}

