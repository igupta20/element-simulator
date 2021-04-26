import java.awt.Color;


public class Water extends Element {
	/**
	 *  method to return color of water.
	 */
	@Override
	public Color getColor() {
		return Color.BLUE;
	}
	
	
	@Override
	public int getWeight() {
		return 100;
	}
	
	
	@Override
	public void fall(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		int l = 0;
		if(row+1 >= 120){
			l = 1;
		}
		else{
			//find out which element is below and check waight to fall or not
			Element below = grid.get(row+1).get(col);
			if(this.compareTo(below) >= 0 && below.push(grid, row+1, col)) {
				grid.get(row).set(col, grid.get(row+1).get(col));
				grid.get(row+1).set(col, this);
			}
			
		}
		
	}
	
	
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
	
	
	
	public static void main(String args[]){
		
		//test
	
		DynamicArray<DynamicArray<Element>> grid = new DynamicArray<>();
	
		
		Water m = new Water();
		
		
		grid.get(1).set(1, m);
		
		
		
		m.fall(grid, 1, 1);
		
		
		
		
		if(grid.get(1).get(2) == m || grid.get(1).get(0) == m) {
		
			System.out.println("Yay");
		}
		
		
	}
}