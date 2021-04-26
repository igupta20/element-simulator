

import java.awt.Color;

public class Empty extends Element {

	private final static int TIME = 2000;
	
	/**
	 *  How many times color has returned the
	 *  same value so far.
	 */
	private int counter;
	
	/**
	 *  The current color of this part of the void.
	 */
	private Color currentColor;
	
	/**
	 *  Initializes this element of empty void.
	 */
	public Empty() {
		counter = (int)(Math.random()*TIME);
		currentColor = getRandomColor();
	}
	
	/**
	 *  method to get color.
	 */
	@Override
	public Color getColor() {
		if(counter == 0) currentColor = getRandomColor();
		counter = (counter+1) % TIME;
		return currentColor;
	}
	
	/**
	 *  Returns a random shade of dark grey.
	 *  
	 *  @return a shade of grey
	 */
	private Color getRandomColor() {
		int val = (int)(Math.random()*20);
		return new Color(val, val, val);
	}
	
	/**
	 *  gets weight which is 0 for empty.
	 */
	@Override
	public int getWeight() {
		return 0;
	}
	
	/**
	 *  fall method however, empty doesn't fall.
	 */
	@Override
	public void fall(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		//void doesn't fall
	}
	
	/**
	 *  push method, but empty void is compressed to nothing.
	 */
	@Override
	public boolean push(DynamicArray<DynamicArray<Element>> grid, int row, int col) {
		//empty void is automatically compressed into nothingness
		return true;
	}
}