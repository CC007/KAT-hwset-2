/**
 * @author elske
 * @author alleveenstra
 *
 */

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;

public class Simulation {

	// Global scape variables
	Site[][] grid;
	int xSize = 50;
	int ySize = 50;
	double minFood = 0;
	double maxFood = 6;
	int epochs = 0;
	int numAgents = 10;
	Vector<Agent> agents;
	Random gen = new Random();
	double totalFoodCapacity;

	// Layout variables
	int center1 = 12, center2 = 37;
	int spread = 20;

	// Visualization variables
	JFrame frame;
	MainPanel mainPanel;
	ButtonPanel buttonPanel;

	public static void main(String args[]) {
		Simulation sim = new Simulation();
		sim.run();
	}

	public Simulation() {
		epochs = 0;
		initGrid();
		initAgents();
	}

	public void run() {
		createAndShowGUI();
	}

	// Function involved in showing the simulation.
    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Scape");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Set up the content pane.
        buildUI(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        buttonPanel.forwardEpochs.grabFocus();
    }

    // Function involved in showing the simulation.
    private void buildUI(Container pane) {
        pane.setLayout(new FlowLayout());
        mainPanel = new MainPanel(this);
        pane.add(mainPanel);
        buttonPanel = new ButtonPanel(this);
        pane.add(buttonPanel);
    }

    // Initializing the Scape with Sites, each with its own energyMax according to its graphical location.
	private void initGrid() {
		grid = new Site[xSize][ySize];
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				double distance = Math.sqrt(Math.pow((center1 - x), 2) + Math.pow((center1 - y), 2));
				double distance2 = Math.sqrt(Math.pow((center2 - x), 2) + Math.pow((center2 - y), 2));
				double cap = minFood;
				if(distance <= distance2 && distance >= 0) {
					cap = maxFood * (1 - distance / spread);
				}
				if(distance > distance2 && distance2 >= 0) {
					cap = maxFood * (1 - distance2 / spread);
				}
				if(cap < minFood) {
					cap = minFood;
				}

				grid[x][y] = new Site(cap, x, y);
				totalFoodCapacity = totalFoodCapacity + cap;
			}
		}
	}


	// Iniliatizing the Scape with Agents - generating the Hashtable the Agents are stored in,
	// then assigning each agent to a Site by its x and y co-ordinates.
    private void initAgents() {
    		agents = new Vector<Agent>();

    		for(int a = 0; a < numAgents; a++) {
    			agents.add(new Agent(this));
    		}

    		for(int a = 0; a < agents.size(); a++) {
    			int x = 0;
    			int y = 0;
    			boolean free = false;
    			while (!free) {
    				x = gen.nextInt(xSize);
    				y = gen.nextInt(ySize);
    				free = (grid[x][y].getAgent() == null);
        		}
        		Agent agent = agents.elementAt(a);
        		agent.setPosition(x,y);
        		grid[x][y].setAgent(agent);
    		}
    }

    // The function step () is called every turn, determining Scape and Agent behavior.
    public void step() {
    	for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				grid[x][y].grow();
				// YOU WILL NEED TO IMPLEMENT GROW() YOURSELF.
			}
		}

    	Collections.shuffle(agents);

    	for(int a = agents.size()-1; a >= 0; a--) {
			Agent agent = agents.elementAt(a);
			agent.act();
			// YOU WILL NEED TO COMPLETE ACT() YOURSELF.
	    }
    }

}

