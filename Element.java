

import java.awt.Color;


public abstract class Element implements Comparable<Element> {


	public abstract Color getColor();
	
	
	public abstract int getWeight();
	
	
	@Override
	public int compareTo(Element e) {
		
		return this.getWeight() - e.getWeight();
	}
	
	
	public abstract void fall(DynamicArray<DynamicArray<Element>> grid, int row, int col);
	

	public abstract boolean push(DynamicArray<DynamicArray<Element>> grid, int row, int col);
	

	public boolean pushLeft(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		if(col == 0) return false; //definitely can't push left
		
		//find out which element is to the left
		Element left = grid.get(row).get(col-1);
		
	
		if(this.compareTo(left) >= 0 && left.push(grid, row, col-1)) {
			grid.get(row).set(col, grid.get(row).get(col-1));
			grid.get(row).set(col-1, this);
			return true;
		}
		
		return false;
	}
	

	public boolean pushUp(DynamicArray<DynamicArray<Element>> grid, int row, int col) {

		if(row == 0) return false; //definitely can't push up
		
		//find out which element is above
		Element up = grid.get(row-1).get(col);
		
	
		if(this.compareTo(up) >= 0 && up.push(grid, row-1, col)) {
			grid.get(row).set(col, grid.get(row-1).get(col));
			grid.get(row-1).set(col, this);
			return true;
		}
		
		return false;


		
	}
}