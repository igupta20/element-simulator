
public class Simulation {
	

	public static final int INIT_ROWS = 120;
	
	/**
	 *  The default number of columns the grid should have.
	 */
	public static final int INIT_COLS = 80;

	/**
	 *  The grid that represents the location of each element.
	 */
	private final DynamicArray<DynamicArray<Element>> grid;
	

	private final Display display;
	

	public Simulation(boolean withDisplay) {
		// Initialize the grid (see above) to the default size (see above).
		grid = new DynamicArray<DynamicArray<Element>>(INIT_ROWS);

		for (int i = 0; i < INIT_ROWS; i++){
			grid.set(i, new DynamicArray<Element>(INIT_COLS));
		}
		// Fill the grid with empty void.
		for (int i = 0; i < INIT_ROWS; i++){
			DynamicArray<Element> t = grid.get(i);
			for (int j = 0; j < INIT_COLS; j++){
				
				t.set(j, new Empty());
			}
		}
	
		if(withDisplay){
			display = new Display("Project 1 Simulation",INIT_ROWS, INIT_COLS);
		}else{
			display = null;
		}
	}



	public void locationClicked(int row, int col, Element newElem) {
		// Put the new element in the grid at the row and column indicated.
		grid.get(row).set(col, newElem);


	}

	/**
	 *  Copies each element of grid's color into the display.
	 */
	public void updateDisplay() {
	
		int i = 0;
		//for (int i = 0; i < INIT_ROWS; i++){
		for (DynamicArray<Element> row: grid){
			//DynamicArray<Element> t = grid.get(i);
			//for (int j = 0; i < INIT_COLS; j++){
			//display.setColor(i, j, t.get(j).getColor());
			//}
			int j = 0;
			for (Element e: row){
				display.setColor(i, j, e.getColor());
				j++;
			}
			i++;
		}
		
	

	}
	
	/**
	 *  Resizes the grid (if needed) to a bigger or smaller size.
	 *  
	 *  @param numRows the new number of rows the grid should have
	 *  @param numCols the new number of columns the grid should have
	 *  @return whether or not anything was changed
	 */
	public boolean resize(int numRows, int numCols) {
	
		int currentRows = grid.size();
		int currentCols = grid.get(0).size();
		if (numRows < 1 || numCols < 1 || (numRows == currentRows && numCols == currentCols)){
			return false;
		}


		if (numRows < currentRows){
			for (int x = 0; x < currentRows-numRows; x++){
				grid.remove(0);
			}
		}

		if (numCols < currentCols){
			for (int row = 0; row < currentRows; row++){
				for (int col = 0; col < currentCols-numCols; col++){
					grid.get(row).remove(0);
				}
			}

		}

		if (numRows > currentRows){
			for (int x = 0; x < currentRows-numRows; x++){
				grid.add(x, new DynamicArray<Element>(INIT_COLS));
				DynamicArray<Element> t = grid.get(0);
				for (int j = 0; j < INIT_COLS; j++){
					t.set(j, new Empty());
				}
			}
		}


		if (numCols > currentCols){
			for (int row = 0; row < currentRows; row++){
				for (int col = 0; col < currentCols-numCols; col++){
					grid.get(row).add(0, new Empty());
				}
			}

		}

		
		
	
		return true;
	}
	
	
	public static String newElementPost() {
	
		return null;
	}
	
	
	public void step() {
		int row = (int)(Math.random()*grid.size());
		int col = (int)(Math.random()*grid.get(row).size()) ;
		
		grid.get(row).get(col).fall(grid, row, col);
	}
	
	/**
	 *  Game loop of the program (step, redraw, handlers, etc.).
	 */
	public void run() {
		while (true) {
			//step
			for (int i = 0; i < display.getSpeed(); i++) step();
			
		
			updateDisplay();
			display.repaint();
			
		
			display.pause(1);
		
			if(display.handle(this) && display.pauseMode()) {
				
				updateDisplay();
				display.repaint();
				display.pause(5000);
			}
		}
	}
	
	
	public DynamicArray<DynamicArray<Element>> getGrid() {
		return grid;
	}
	
	/**
	 *  Main method that kicks off the simulator.
	 *  
	 *  @param args not used
	 */
	public static void main(String[] args) {
		(new Simulation(true)).run();
	}
}
