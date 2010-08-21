package org.flingsolver;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * An x,y coordinated grid with bottom left square at 1,1.
 *  
 * @author kierone
 *
 */
public class Grid {

	private int gridWidth;
	private int gridHeight;
	private int[][] gridArr;
	private int furryCount=0;
	
	private static final Logger logger = LogManager
	.getLogger(Grid.class);
	
	public Grid(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		gridArr = new int[gridHeight][gridWidth];
	}
	

	
	
	
	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}


	public int[][] getGridArr() {
		return clone2DArray(gridArr);
	}

	public void moveFurry(int x, int y, int newx, int newy){
		if (x != newx || y != newy){
		gridArr[y-1][x-1]=0;
		gridArr[newy-1][newx-1]=1;
		logger.debug("moveFurry:: furry at " + x + "," + y + " moved to new position " + newx + "," + newy);
		}
	}
	
	public void rollFurryOffGrid(int x, int y){
		gridArr[y-1][x-1]=0;
		furryCount--;
		logger.debug("rollFurryOffGrid:: furry at " + x + "," + y + " rolled off grid");
	}

	public void addFurries(GridPosition[] positions){
		for (GridPosition position : positions){
			addFurry(position.getX(), position.getY());
		}
	}
	
	public void addFurry(int x, int y) {
		logger.debug("addFurry:: fury added to position " + x + "," + y);
		gridArr[y - 1][x - 1] = 1;
		furryCount++;
	}

	boolean isFurryPresent(int x, int y) {
		return (gridArr[y - 1][x - 1] == 1);
	}

	boolean isFurryMovingOutsideGrid(int x, int y, Direction direction) {
		switch (direction) {
		case LEFT:
			return (x == 1);
		case RIGHT:
			return (x == gridWidth);
		case UP:
			return (y == gridHeight);
		case DOWN:
			return (y == 1);
		default:
			return false;
		}
	}

	boolean isFurryAdjacent(int x, int y, Direction direction) {
		switch (direction) {
		case LEFT:
			return isFurryPresent(x - 1, y);
		case RIGHT:
			return isFurryPresent(x + 1, y);
		case UP:
			return isFurryPresent(x, y + 1);
		case DOWN:
			return isFurryPresent(x, y - 1);
		default:
			return false;
		}
	}

	/**
	 * Gets grid position of nearest hittable fury assuming adjacent hits arent allowed
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @return
	 */
	GridPosition getGridPositionOfHittableFurry(int x, int y, Direction direction){
		return getGridPositionOfHittableFurry(x,y,direction,false);
	}
	

	
	/**
	 * Gets grid position of nearest hittable fury
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @param allowAdjacentHits
	 * @return
	 */
	GridPosition getGridPositionOfHittableFurry(int x, int y, Direction direction, boolean allowAdjacentHits){
		GridPosition furryPosition=null;
		int gap=2;
		if (allowAdjacentHits){
			gap=1;
		}
		switch (direction){
		case LEFT :
			for (int i = 1; i <= x - gap; i++) {
				if (isFurryPresent(i, y)) {
					furryPosition=new GridPosition(i,y);
				}
			}
			return furryPosition;
		case RIGHT :
			for (int i = x + gap; i <= gridWidth; i++) {
				if (isFurryPresent(i, y)) {
					furryPosition=new GridPosition(i,y);
					break;
				}
			}
			return furryPosition;
		case UP :
			for (int j = y + gap; j <= gridHeight; j++) {
				if (isFurryPresent(x, j)) {
					furryPosition=new GridPosition(x,j);
				    break;
				}
			}
			return furryPosition;
		case DOWN :
			for (int j = 1; j <= y - gap; j++) {
				if (isFurryPresent(x, j)) {
					furryPosition=new GridPosition(x,j);
				}
			}
			return furryPosition;
		default :
			return null;

		}
	}
	
	public Grid clone() {
		Grid cloneGrid = new Grid(gridWidth, gridHeight);
		cloneGrid.furryCount=furryCount;
		cloneGrid.gridArr=clone2DArray(gridArr);
		return cloneGrid;
	}


	public static int[][] clone2DArray(int[][] array) {
		int rows = array.length;
		// clone the 'shallow' structure of array
		int[][] newArray = (int[][]) array.clone();
		// clone the 'deep' structure of array
		for (int row = 0; row < rows; row++) {
			newArray[row] = (int[]) array[row].clone();
		}

		return newArray;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(gridArr);
		result = prime * result + gridHeight;
		result = prime * result + gridWidth;
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
		Grid other = (Grid) obj;
		if (gridHeight != other.gridHeight)
			return false;
		if (gridWidth != other.gridWidth)
			return false;
		// check arrays are equal
		if (gridArr.length != other.gridArr.length){
			logger.debug ("This and others grid arrays contain different number of rows");
			return false;
		}
		for (int i = 0; i < this.gridArr.length; i++) {
			int[] row = gridArr[i];
			for (int j=0; j < row.length; j++){
				if (gridArr[i][j] != other.gridArr[i][j]){
					logger.debug("Grid arrays different at grid position " + (i+1) + "," + (j+1) + " value in this = " + gridArr[i][j] + ", value in other = " + other.gridArr[i][j]);
				    return false;
				}
			}
		}
		return true;
	}


	public int getFurryCount() {
		return furryCount;
	}





	@Override
	public String toString() {
		StringBuffer outputBuffer = new StringBuffer("\n\n");
		for (int i=gridArr.length-1; i >= 0; i--  ){
			int[] rowArr = gridArr[i];
			for (int j=0; j < rowArr.length; j++){
				if (rowArr[j]==1){
					outputBuffer.append("X ");
				} else {
				outputBuffer.append(rowArr[j] + " ");
				}
			}
			outputBuffer.append("\n");
		}
		return outputBuffer.toString();
	}
	
	
	
}
