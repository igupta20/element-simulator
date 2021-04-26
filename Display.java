

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;	
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import java.awt.image.BufferedImage;

import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
	

public class Display extends JComponent implements MouseListener,
	MouseMotionListener, ActionListener, ChangeListener, ComponentListener {
	
	
	public static final int MIN_SIZE = 30;
	
	/**
	 *  The element id for empty space.
	 */
	public static final int EMPTY = 0;
	
	/**
	 *  The element id for metal.
	 */
	public static final int METAL = 1;
	
	/**
	 *  The element id for sand.
	 */
	public static final int SAND = 2;
	
	/**
	 *  The element id for water.
	 */
	public static final int WATER = 3;
	
	//[GUI:Advanced] Add more elements here...
	
	/**
	 *  The actual image/graphic being displayed.
	 */
	private Image image;
	
	/**
	 *  The number of pixels to one cell in the image.
	 */
	private int cellSize;
	
	/**
	 *  The frame that holds everything.
	 */
	private JFrame frame;
	
	/**
	 *  The currently selected tool.
	 */
	private int tool;
	
	/**
	 *  The number of rows of cells.
	 */
	private int numRows;
	
	/**
	 *  The number of columns of cells.
	 */
	private int numCols;
	
	/**
	 *  The location of the mouse.
	 */
	private int[] mouseLoc;
	
	/**
	 *  The buttons on the right side of the GUI.
	 */
	private JButton[] buttons;
	
	/**
	 *  The slider bar that speeds up and slows
	 *  down the animation.
	 */
	private JSlider slider;
	
	/**
	 *  The current speed of the animation.
	 */
	private int speed;
	
	/**
	 *  Whether the simulation should pause after it
	 *  handles some event from the display (drawing
	 *  or resizing only, not falling/pushing).
	 */
	private boolean pauseMode = false;
	
	/**
	 *  When resizeing, this was the previous dimension of
	 *  the component.
	 */
	private Dimension currentDim;
	
	/**
	 *  When resizing, this is the new dimension of the
	 *  component.
	 */
	private Dimension newDim;

	/**
	 *  Initializes the display.
	 *  
	 *  @param title the window title
	 *  @param numRows the desired number of rows for the image
	 *  @param numCols the desired number of columns for the image
	 */
	public Display(String title, int numRows, int numCols) {
		
		
		//initialize the rows/columns for the image
		this.numRows = Math.max(MIN_SIZE, numRows);
		this.numCols = Math.max(MIN_SIZE, numCols);
		
		//some defaults
		tool = EMPTY;
		mouseLoc = null;
		speed = computeSpeed(50);

		//determine cell size and setup the image
		cellSize = Math.max(1, 600 / Math.max(numRows, numCols));
		image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);

		//makes the main GUI frame
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//makes the top panel (the one with the image)
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		frame.getContentPane().add(topPanel);

		setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(this);
		topPanel.add(this);

		//makes the panel with the buttons (including the buttons)
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		topPanel.add(buttonPanel);

		//setup the buttons
		String[] buttonNames = {"Empty", "Metal", "Sand", "Water"};
		buttons = new JButton[buttonNames.length+1];
		//^ one more for the "debug" button at the end...

		for (int i = 0; i < buttons.length-1; i++) {
			buttons[i] = new JButton(buttonNames[i]);
			buttons[i].setActionCommand("" + i);
			buttons[i].addActionListener(this);
			buttonPanel.add(buttons[i]);
		}
		buttons[tool].setSelected(true);
		
		buttons[buttons.length-1] = new JButton("Debug");
		buttons[buttons.length-1].setActionCommand("pauseMode");
		buttons[buttons.length-1].addActionListener(this);
		buttonPanel.add(buttons[buttons.length-1]);

		//setup the slider
		slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("Slow"));
		labelTable.put(100, new JLabel("Fast"));
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);

		frame.getContentPane().add(slider);

		//pack and display everything!
		frame.pack();
		frame.setVisible(true);
		
		//init the current dimensions
		currentDim = getSize();
	}

	/**
	 *  Given a tool, return a new instance of that element.
	 *  
	 *  @return the element created with the tool
	 */
	private Element getElementFromTool() {
	
		
		switch(tool) {
			case METAL:
				return new Metal();
			case SAND:
				return new Sand();
			case WATER:
				return new Water();
			default:
			case EMPTY:
				return new Empty();
		}
	}
	
	/**
	 *  Sets the color of the cell at (row,col).
	 *  
	 *  @param row the row where the color should be drawn
	 *  @param col the column where the color should be drawn
	 *  @param color the color to draw with
	 */
	public void setColor(int row, int col, Color color) {
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
	}
	

	public boolean handle(Simulation sim) {
		//if the mouse was clicked (a tool was used)
		if (mouseLoc != null) {
			//tell the simulator to draw the element there
			sim.locationClicked(mouseLoc[0], mouseLoc[1], getElementFromTool());
			return true;
		}
		//if the window was resized
		else if (newDim != null && !newDim.equals(currentDim)) {
			//determine the new size
			this.numRows = Math.max(MIN_SIZE, newDim.height / cellSize);
			this.numCols = Math.max(MIN_SIZE, newDim.width / cellSize);
			
			//reset things
			currentDim = newDim;
			newDim = null;
			
			//return whether or not anything _actually_ changed
			if(sim.resize(numRows, numCols)) {
				//make an appropriate image for that size
				this.image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 *  Pause for some milliseconds (1000ms = 1sec).
	 *  
	 *  @param milliseconds number of milliseconds
	 */
	public void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}
		catch(InterruptedException e) {
			
		}
	}

	/**
	 *  for the image.
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	/**
	 *  location of mouse.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		mouseLoc = toLocation(e);
	}

	/**
	 *  mouse released.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseLoc = null;
	}

	/**
	 * when the mouse is dragged.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseLoc = toLocation(e);
	}
	
	/**
	 *  resizing.
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		newDim = e.getComponent().getSize();
	}

	/**
	 *  for the actions.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if(action.equals("pauseMode")) {
			pauseMode = !pauseMode;
			((JButton)e.getSource()).setSelected(pauseMode);
		}
		else {
			tool = Integer.parseInt(action);
			for (JButton button : buttons)
				button.setSelected(false);
			((JButton)e.getSource()).setSelected(true);
		}
	}

	/**
	 *  speed of state changed.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		speed = computeSpeed(slider.getValue());
	}
	
	/**
	 *  Return whether or not the GUI is in pause mode.
	 *  
	 *  @return true if pause mode is enabled
	 */
	public boolean pauseMode() {
		return pauseMode;
	}
	
	/**
	 *  Gets the location of a mouse event.
	 *  
	 *  @param e the mouse event
	 *  @return the location of the event (cell x and y)
	 */
	private int[] toLocation(MouseEvent e) {
		int row = e.getY() / cellSize;
		int col = e.getX() / cellSize;
		
		if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
			return null;
		}
		
		int[] loc = new int[2];
		loc[0] = row;
		loc[1] = col;
		return loc;
	}

	/**
	 *  Returns number of times to step between repainting
	 *  and processing mouse input.
	 *  
	 *  @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 *  Returns speed based on sliderValue. Speed of 0 returns 10^3
	 *  and speed of 100 returns 10^6.
	 *  
	 *  @param sliderValue the slider value chosen
	 *  @return the speed determined
	 */
	private int computeSpeed(int sliderValue) {
		return (int)Math.pow(10, 0.03 * sliderValue + 3);
	}
	
	//Methods below here are required by various interfaces, but not used.
	
	/**
	 *  mouse clicked.
	 */
	@Override public void mouseClicked(MouseEvent e) { }

	/**
	 *  moust entered.
	 */
	@Override public void mouseEntered(MouseEvent e) { }

	/**
	 *  exited mouse.
	 */
	@Override public void mouseExited(MouseEvent e) { }

	/**
	 *  moved mouse.
	 */
	@Override public void mouseMoved(MouseEvent e) { }
	
	/**
	 *  componenet hidden.
	 */
	@Override public void componentHidden(ComponentEvent e) { }
	
	/**
	 *  When componenet moved.
	 */
	@Override public void componentMoved(ComponentEvent e) { }
	
	/**
	 *  when the componenet is shown.
	 */
	@Override public void componentShown(ComponentEvent e) { }
}