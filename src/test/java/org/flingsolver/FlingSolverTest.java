package org.flingsolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class FlingSolverTest {

	private static FlingSolver flingSolver;
	
	
	@Before
	public void setup(){
		flingSolver = new FlingSolver();
	}
	
	
	@Test
	public void testGridSize(){
		assertEquals(7, flingSolver.getGridWidth());
		assertEquals(8, flingSolver.getGridHeight());
	}
	
	@Test
	public void testAddFurry(){
		flingSolver.addFurry(3,2);
		int[][] grid=flingSolver.getStartingGrid().getGridArr();
		assertEquals(1,grid[1][2]);
		grid[1][2]=0;
		assertEquals(1,flingSolver.getStartingGrid().getGridArr()[1][2]);
		
	}
	
	@Test
	public void testMoveFurryInvalidIfNotPresent(){
		flingSolver.addFurry(1,1);
		assertEquals(MoveStatus.INVALID_NON_EXISTANT_FURRY,flingSolver.calcMove(flingSolver.getStartingGrid(),2,2,Direction.LEFT).getMoveStatus());
	}
	
    @Test
	public void testMoveFurryValid(){
    	flingSolver.addFurry(4,4);
    	flingSolver.addFurry(2,4);
    	flingSolver.addFurry(7,4);
    	flingSolver.addFurry(4,6);
    	flingSolver.addFurry(4,2);
    	assertEquals(MoveStatus.VALID,flingSolver.calcMove(flingSolver.getStartingGrid(),4,4,Direction.LEFT).getMoveStatus());
    	assertEquals(MoveStatus.VALID,flingSolver.calcMove(flingSolver.getStartingGrid(), 4,4,Direction.RIGHT).getMoveStatus());
    	assertEquals(MoveStatus.VALID,flingSolver.calcMove(flingSolver.getStartingGrid(),4,4,Direction.UP).getMoveStatus());
    	assertEquals(MoveStatus.VALID,flingSolver.calcMove(flingSolver.getStartingGrid(),4,4,Direction.DOWN).getMoveStatus());
    }
    
	
    @Test
    public void testMoveFurryInvalidOutsideGrid(){
    	flingSolver.addFurry(1,1);
    	flingSolver.addFurry(1,8);
    	flingSolver.addFurry(7,8);
    	assertEquals(MoveStatus.INVALID_OUTSIDE_GRID,flingSolver.calcMove(flingSolver.getStartingGrid(),1, 1, Direction.LEFT).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_OUTSIDE_GRID,flingSolver.calcMove(flingSolver.getStartingGrid(),1, 1, Direction.DOWN).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_OUTSIDE_GRID,flingSolver.calcMove(flingSolver.getStartingGrid(),1, 8, Direction.UP).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_OUTSIDE_GRID,flingSolver.calcMove(flingSolver.getStartingGrid(),7, 8, Direction.RIGHT).getMoveStatus());
    	
    }
    
	
    @Test
    public void testMoveFurryInvalidNoHit(){
    	flingSolver.addFurry(2,2);
    	assertEquals(MoveStatus.INVALID_NOTHING_TO_HIT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.LEFT).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_NOTHING_TO_HIT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.DOWN).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_NOTHING_TO_HIT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.UP).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_NOTHING_TO_HIT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.RIGHT).getMoveStatus());
    }
    
    @Test
    public void testMoveFurryInvalidIfAdjacent(){
    	flingSolver.addFurry(2,2);
    	flingSolver.addFurry(1,2);
    	flingSolver.addFurry(3,2);
    	flingSolver.addFurry(2,3);
    	flingSolver.addFurry(2,1);
    	assertEquals(MoveStatus.INVALID_FURRY_ADJACENT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.LEFT).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_FURRY_ADJACENT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.DOWN).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_FURRY_ADJACENT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.UP).getMoveStatus());
    	assertEquals(MoveStatus.INVALID_FURRY_ADJACENT,flingSolver.calcMove(flingSolver.getStartingGrid(),2, 2, Direction.RIGHT).getMoveStatus());    	
    }
	
    @Test
	public void testGetValidMoves(){
    	flingSolver.addFurry(4,4);
    	flingSolver.addFurry(2,4);
    	flingSolver.addFurry(7,4);
    	flingSolver.addFurry(4,6);
    	flingSolver.addFurry(4,2);
    	
    	List<Move>expectedValidMoves=new ArrayList<Move>();
    	expectedValidMoves.add(new Move(2,4, Direction.RIGHT, MoveStatus.VALID, new GridPosition(4, 4)));
      	expectedValidMoves.add(new Move(4,4, Direction.LEFT, MoveStatus.VALID, new GridPosition(2, 4)));
    	expectedValidMoves.add(new Move(4,4, Direction.RIGHT, MoveStatus.VALID, new GridPosition(7, 4)));
    	expectedValidMoves.add(new Move(4,4, Direction.DOWN, MoveStatus.VALID, new GridPosition(4, 2)));
    	expectedValidMoves.add(new Move(4,4, Direction.UP, MoveStatus.VALID, new GridPosition(4, 6)));
      	expectedValidMoves.add(new Move(7,4, Direction.LEFT, MoveStatus.VALID, new GridPosition(4, 4)));
    	expectedValidMoves.add(new Move(4,6, Direction.DOWN, MoveStatus.VALID, new GridPosition(4, 4)));
    	
    	Set<Move>actualMoves=flingSolver.getValidMoves(flingSolver.getStartingGrid());
    	assertTrue(actualMoves.contains(new Move(4,2, Direction.UP, MoveStatus.VALID, new GridPosition(4, 4))));
    	assertTrue(actualMoves.contains(new Move(4,4, Direction.LEFT, MoveStatus.VALID, new GridPosition(2, 4))));
    	assertTrue(actualMoves.contains(new Move(4,4, Direction.RIGHT, MoveStatus.VALID, new GridPosition(7, 4))));
    	assertTrue(actualMoves.contains(new Move(4,4, Direction.DOWN, MoveStatus.VALID, new GridPosition(4, 2))));
    	assertTrue(actualMoves.contains(new Move(4,4, Direction.UP, MoveStatus.VALID, new GridPosition(4, 6))));
    	assertTrue(actualMoves.contains(new Move(7,4, Direction.LEFT, MoveStatus.VALID, new GridPosition(4, 4))));
    	assertTrue(actualMoves.contains(new Move(4,2, Direction.UP, MoveStatus.VALID, new GridPosition(4, 4))));
    	assertTrue(actualMoves.contains(new Move(4,6, Direction.DOWN, MoveStatus.VALID, new GridPosition(4, 4))));
    	
    	
    }
    
    @Test
    public void testMoveAndTransformGridSingleCollision(){
    	Grid grid = new Grid(7,8);
    	grid.addFurries(new GridPosition[] { new GridPosition(4,4), new GridPosition(2,4), new GridPosition(7,4), new GridPosition(4,6), new GridPosition(4,2) });
    	
    	Move leftMove = new Move(4,4, Direction.LEFT, MoveStatus.VALID, new GridPosition(2, 4));
    	Move rightMove = new Move(4,4, Direction.RIGHT, MoveStatus.VALID, new GridPosition(7, 4));
    	Move upMove = new Move(4,4, Direction.UP, MoveStatus.VALID, new GridPosition(4, 6));
    	Move downMove = new Move(4,4, Direction.DOWN, MoveStatus.VALID, new GridPosition(4, 2));
    	Grid gridAfterLeft=flingSolver.moveAndTransform(grid, leftMove);
    	Grid gridAfterRight=flingSolver.moveAndTransform(grid, rightMove);
    	Grid gridAfterUp=flingSolver.moveAndTransform(grid, upMove);
    	Grid gridAfterDown=flingSolver.moveAndTransform(grid, downMove);
    	
    	Grid expectedGridAfterLeft = new Grid(7,8);
    	expectedGridAfterLeft.addFurries(new GridPosition[] { new GridPosition(3,4), new GridPosition(7,4), new GridPosition(4,6), new GridPosition(4,2) });
    	Grid expectedGridAfterRight = new Grid(7,8);
    	expectedGridAfterRight.addFurries(new GridPosition[] { new GridPosition(6,4),new GridPosition(2,4),new GridPosition(4,6), new GridPosition(4,2) });
    	Grid expectedGridAfterUp = new Grid(7,8);
    	expectedGridAfterUp.addFurries(new GridPosition[] { new GridPosition(4,5), new GridPosition(7,4),new GridPosition(2,4), new GridPosition(4,2) });
    	Grid expectedGridAfterDown = new Grid(7,8);
    	expectedGridAfterDown.addFurries(new GridPosition[] { new GridPosition(4,3), new GridPosition(7,4),new GridPosition(2,4),new GridPosition(4,6) });
        
    	assertEquals(expectedGridAfterLeft,gridAfterLeft);
    	assertEquals(expectedGridAfterRight,gridAfterRight);
    	assertEquals(expectedGridAfterUp,gridAfterUp);
    	assertEquals(expectedGridAfterDown,gridAfterDown);
    	
    }
    
    @Test
    public void testMoveAndTransformGridMultipleCollisions(){
    	Grid grid = new Grid(7,8);
    	grid.addFurries(new GridPosition[] { new GridPosition(4,4), new GridPosition(1,4), new GridPosition(2,4), new GridPosition(6,4),new GridPosition(7,4), 
    			new GridPosition(4,8), new GridPosition(4,6), new GridPosition(4,2), new GridPosition(4,1) });
    	
    	
    	Move leftMove = new Move(4,4, Direction.LEFT, MoveStatus.VALID, new GridPosition(2, 4));
    	Move rightMove = new Move(4,4, Direction.RIGHT, MoveStatus.VALID, new GridPosition(6, 4));
    	Move upMove = new Move(4,4, Direction.UP, MoveStatus.VALID, new GridPosition(4, 6));
    	Move downMove = new Move(4,4, Direction.DOWN, MoveStatus.VALID, new GridPosition(4, 2));
    	Grid gridAfterLeft=flingSolver.moveAndTransform(grid, leftMove);
    	Grid gridAfterRight=flingSolver.moveAndTransform(grid, rightMove);
    	Grid gridAfterUp=flingSolver.moveAndTransform(grid, upMove);
    	Grid gridAfterDown=flingSolver.moveAndTransform(grid, downMove);
    	
    	
    	Grid expectedGridAfterLeft = new Grid(7,8);
    	expectedGridAfterLeft.addFurries(new GridPosition[] { new GridPosition(3,4), new GridPosition(2,4), new GridPosition(6,4),new GridPosition(7,4), 
    			new GridPosition(4,8), new GridPosition(4,6), new GridPosition(4,2), new GridPosition(4,1) });
    	
    	Grid expectedGridAfterRight = new Grid(7,8);
    	expectedGridAfterRight.addFurries(new GridPosition[] { new GridPosition(5,4), new GridPosition(1,4), new GridPosition(2,4), new GridPosition(6,4), 
    			new GridPosition(4,8), new GridPosition(4,6), new GridPosition(4,2), new GridPosition(4,1) });
    	
    	Grid expectedGridAfterUp = new Grid(7,8);
    	expectedGridAfterUp.addFurries(new GridPosition[] { new GridPosition(4,5), new GridPosition(1,4), new GridPosition(2,4), new GridPosition(6,4),new GridPosition(7,4), 
    			new GridPosition(4,7), new GridPosition(4,2), new GridPosition(4,1) });
    	
    	Grid expectedGridAfterDown = new Grid(7,8);
    	expectedGridAfterDown.addFurries(new GridPosition[] { new GridPosition(4,3), new GridPosition(1,4), new GridPosition(2,4), new GridPosition(6,4),new GridPosition(7,4), 
    			new GridPosition(4,8), new GridPosition(4,6), new GridPosition(4,2)});
    	
    	assertEquals(expectedGridAfterLeft,gridAfterLeft);
    	assertEquals(expectedGridAfterRight,gridAfterRight);
    	assertEquals(expectedGridAfterUp,gridAfterUp);
    	assertEquals(expectedGridAfterDown,gridAfterDown);  
    	
    }
    
    @Test
    public void testGetSimpleSolutionPuzzle1_1(){
    	Grid grid = new Grid(7,8);
    	grid.addFurries(new GridPosition[] { new GridPosition(3,6), new GridPosition(5,3), new GridPosition(6,6) });
    	
    	flingSolver=new FlingSolver(grid);
    	List<List<MoveNode>> solutions=flingSolver.findSolutions();
    	assertEquals(2, solutions.size());
    }

    @Test
    public void testLoadGrid(){
    	String testFileName="/testGridOne.txt";
    	boolean success=flingSolver.loadGrid(testFileName);
    	assertTrue(success);
    	Grid actualGrid = flingSolver.getStartingGrid();
    	assertTrue(actualGrid.isFurryPresent(3, 2));
    	assertTrue(actualGrid.isFurryPresent(6, 2));
    	assertTrue(actualGrid.isFurryPresent(2, 4));
    	assertTrue(actualGrid.isFurryPresent(7, 4));
    	assertTrue(actualGrid.isFurryPresent(1, 5));
    	assertTrue(actualGrid.isFurryPresent(2, 6));
    	assertTrue(actualGrid.isFurryPresent(7, 6));
    	assertTrue(actualGrid.isFurryPresent(2, 7));
    	assertEquals(8, actualGrid.getFurryCount());
    	
    }
    
    @Test
    public void testGetSimpleSolutionPuzzle1_1Solve(){
    	Grid grid = new Grid(7,8);
    	grid.addFurries(new GridPosition[] { new GridPosition(3,6), new GridPosition(5,3), new GridPosition(6,6) });
    	
    	flingSolver=new FlingSolver(grid);
    	flingSolver.solve();
    }    

    @Test
    public void testGetSimpleSolutionPuzzle16_1Solve(){
    	Grid grid = new Grid(7,8);
    	grid.addFurries(new GridPosition[] { new GridPosition(3,1), new GridPosition(6,1), 
    			        new GridPosition(2,3),
    			        new GridPosition(1,4), new GridPosition(2,4),
    			        new GridPosition(2,5), new GridPosition(6,5), new GridPosition(7,5),
    			        new GridPosition(5,6), new GridPosition(6,6),
    			        new GridPosition(4,7)
    			});
    	
    	flingSolver=new FlingSolver(grid);
    	flingSolver.solve();
    }    
}
