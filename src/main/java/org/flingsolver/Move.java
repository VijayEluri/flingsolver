package org.flingsolver;

public class Move {

	private int x;
	private int y;
	private Direction dir;
	private GridPosition gridPositionOfHittableFurry;
	private MoveStatus moveStatus;
	
	public Move(int x, int y, Direction dir, MoveStatus moveStatus, GridPosition gridPositionOfHittableFurry){
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.moveStatus=moveStatus;
		this.gridPositionOfHittableFurry=gridPositionOfHittableFurry;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDir() {
		return dir;
	}

	public GridPosition getGridPositionOfHittableFurry() {
		return gridPositionOfHittableFurry;
	}

	public MoveStatus getMoveStatus() {
		return moveStatus;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime
				* result
				+ ((gridPositionOfHittableFurry == null) ? 0
						: gridPositionOfHittableFurry.hashCode());
		result = prime * result
				+ ((moveStatus == null) ? 0 : moveStatus.hashCode());
		result = prime * result + x;
		result = prime * result + y;
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
		Move other = (Move) obj;
		if (dir == null) {
			if (other.dir != null)
				return false;
		} else if (!dir.equals(other.dir))
			return false;
		if (gridPositionOfHittableFurry == null) {
			if (other.gridPositionOfHittableFurry != null)
				return false;
		} else if (!gridPositionOfHittableFurry
				.equals(other.gridPositionOfHittableFurry))
			return false;
		if (moveStatus == null) {
			if (other.moveStatus != null)
				return false;
		} else if (!moveStatus.equals(other.moveStatus))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Move [dir=" + dir + ", gridPositionOfHittableFurry="
				+ gridPositionOfHittableFurry + ", moveStatus=" + moveStatus
				+ ", x=" + x + ", y=" + y + "]";
	}
	
}
