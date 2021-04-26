

import java.awt.Color;

public class Metal extends Element {
	
	@Override
	public Color getColor() {
		return Color.RED;
	}
	

	@Override
	public int getWeight() {
		return Integer.MAX_VALUE;
	}
	
	
	@Override
	public void fall(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		//metal doesn't fall down
	}
	
	
	@Override
	public boolean push(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		//metal can't be pushed
		return false;
	}
	
	/**
	 *  Main method that shows an example of testing an element.
	 *  
	 *  @param args not used
	 */
	public static void main(String args[]){
		
		DynamicArray<DynamicArray<Element>> grid = new DynamicArray<>();
		//todo: initialize the grid to a 3x3 area
		grid.add(new DynamicArray<Element>(3));
		grid.add(new DynamicArray<Element>(3));
		grid.add(new DynamicArray<Element>(3));
		
		Metal m = new Metal();
		
		//put the element in the middle
		grid.get(1).set(1, m);
		
		
		//(2) call some method(s) that will change (or not change) the scenario
		m.fall(grid, 1, 1);
		
		
		
		
		if(grid.get(1).get(1) == m) {
			//^ carful about == here, that's checking for the same memory location...
			System.out.println("Yay");
		}
		
	}
}