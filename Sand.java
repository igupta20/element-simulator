

import java.awt.Color;


public class Sand extends Element {
	/**
	 * return yellow color for sand.
	 */
	@Override
	public Color getColor() {
		return Color.YELLOW;
	}
	
	/**
	 *  get weight of sand.
	 *  weights more than empty but less than metal 
	 */
	@Override
	public int getWeight() {
		return 10;
	}
	
	/**
	 *  method for fall of sand.
	 *  check if bottom is less weight and fall to it if so
	 *  check weight and see if can move down or not
	 */
	@Override
	public void fall(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		int l = 0;
		if(row+1 >= 120){
			l = 1;
		}
		else{
			//find out which element is below
			Element below = grid.get(row+1).get(col);
			if(this.compareTo(below) >= 0 && below.push(grid, row+1, col)) {
				grid.get(row).set(col, grid.get(row+1).get(col));
				grid.get(row+1).set(col, this);
			}
			
		}
	}
	
	
	/**
	 *  push method.
	 *  sand may push left or right
	 *  check weight, check everything left and right and see if can push
	 *  @return boolean
	 */
	@Override
	public boolean push(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		if(col == 0 || col >= 80){
			return false;
		}	
		
		Element left = grid.get(row).get(col-1);
		if(this.compareTo(left) >= 0 && left.push(grid, row, col-1)) {
			grid.get(row).set(col, grid.get(row).get(col-1));
			grid.get(row).set(col-1, this);
			return true;
		}
		return false;
	}
	
	/**
	 *  Main method that shows an example of testing an element.
	 *  
	 *  @param args not used
	 */
	public static void main(String args[]){
		
		
		//create a grid and a piece of Metal
		DynamicArray<DynamicArray<Element>> grid = new DynamicArray<>();
		//todo: initialize the grid to a 3x3 area
		
		Sand m = new Sand();
		
		//put the element in the middle
		grid.get(1).set(1, m);
		
		
		//(2) call some method(s) that will change (or not change) the scenario
		m.fall(grid, 1, 1);
		
		
		//(3) check that what should happen did happen
		

		if(grid.get(2).get(1) == m) {
			//^ carful about == here, that's checking for the same memory location...
			System.out.println("Yay");
		}
		
	
	}
}