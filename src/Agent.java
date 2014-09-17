
import java.util.*;

class Agent {

    // Agent attributes
    private int xPosition;
    private int yPosition;
    private int age;
    private double energy;

    // Scape properties
    public Simulation sim;

    // Agent variables
    // These values should be used for answering the questions (unless they ask
    // to vary the values). Feel free to experiment though.
    double moveCost = 0.40;
    int vision = 4;
    int metabolism = 4;
    int procreateReq = 16;
    int procreateCost = 10;

    // Agent constructor
    public Agent(Simulation controller) {
        sim = controller;
        age = 0;
        energy = 6;
    }

    public void act() {
    	// This function handles all of an Agents' behavior in
        // any given step.
        // First, check if the Agent has enough energy to survive
        // the step, otherwise remove it.
        if (energy < metabolism) {
            remove();
        } else {
            // Let the Agent know it has survived another step.
            age++;

			// 'Cost of living': Subtracting the metabolism from the
            // Agents' energy level.
            energy -= metabolism;

			// Generating a vector with the Sites that can be moved to,
            // and a vector with Sites suitable for offspring..
            Vector<Site> freeSites = findFreeSites();

			// Evaluating each of the possible Sites to move to.
            // YOU WILL NEED TO IMPLEMENT FINDBESTSITE YOURSELF.
            Site bestSite = findBestSite(freeSites);

			// Moving to the best possible Site, and reaping the energy
            // from it.
            // YOU WILL NEED TO IMPLEMENT MOVE AND REAP YOURSELF.
            move(bestSite);
            reap(bestSite);

			// Checking if the Agent is fertile and has a free neighboring
            // Site, and if so, producing offspring.
            if (energy > procreateReq) {
                Vector<Site> babySites = findBabySites();
                if (babySites.size() > 0) {
                    procreate(babySites);
                }
            }
        }

    }

    // Below is a suggestion of functions you need to finish this code.
    // Returns the best Site to reap energy from.
    public Site findBestSite(Vector<Site> freeSites) {
    	// Your own code determining what the best Site is of all
        // possible freeSites for the agent to move to;
        Iterator<Site> i = freeSites.iterator();
        Site bestSite = new Site();
        if (i.hasNext()) {
            bestSite = i.next();
        }
        while (i.hasNext()) {
            Site freeSite = i.next();
            if (freeSite.getFood() > bestSite.getFood()) {
                bestSite = freeSite;
            }
        }
        // Then return the best Site.
        return bestSite;
    }

    // Move to the new site. (Tell the old Site the agent has left,
    // the new Site the agent has arrived, and the agent itself its
    // new location...). Make use of the moveCost variable as well.
    public void move(Site newSite) {
        sim.grid[this.xPosition][this.yPosition].setAgent(null);
        this.xPosition = newSite.getXPosition();
        this.yPosition = newSite.getYPosition();
        energy -= moveCost;
        newSite.setAgent(this);
    }

    // Gather food from the site.
    public void reap(Site s) {
        this.energy += s.getFood();
        s.setFood(0.0);
    }

    // Below are functions we already created for you. You can change
    // them if you must, as long as you turn in a working version.
    // Allows an agent to procreate.
    public void procreate(Vector<Site> babySites) {
        energy -= procreateCost;
        Agent baby = new Agent(sim);
        sim.agents.add(baby);

        Site babySite = (Site) babySites.elementAt(0);
        baby.setPosition(babySite.getXPosition(), babySite.getYPosition());
        babySite.setAgent(baby);
    }

    // When an Agent dies, remove it from the Agent Vector, and remove
    // it from the Site it was on.
    public void remove() {
        sim.agents.remove(this);
        sim.grid[xPosition][yPosition].setAgent(null);
    }

    // Generating a Vector with all available free Sites within vision.
    public Vector<Site> findFreeSites() {
        Vector<Site> freeSites = new Vector<Site>();

        for (int m = -vision; m <= vision; m++) {
            for (int n = -vision; n <= vision; n++) {
                Site site;
                int x = xPosition + m;
                int y = yPosition + n;
                if (x >= 0 && x < sim.xSize && y >= 0 && y < sim.ySize) {
                    site = sim.grid[x][y];
                    Agent occ = site.getAgent();
                    if (occ == null || this.equals(occ)) {
                        freeSites.addElement(site);
                    }
                }
            }
        }
        Collections.shuffle(freeSites);
        return freeSites;
    }

    // Generating a Vector with all Sites available for offspring.
    public Vector<Site> findBabySites() {
        Vector<Site> babySites = new Vector<Site>();

        for (int m = -1; m <= 1; m++) {
            for (int n = -1; n <= 1; n++) {
                Site site;
                int x = xPosition + m;
                int y = yPosition + n;
                if (x >= 0 && x < sim.xSize && y >= 0 && y < sim.ySize) {
                    site = sim.grid[x][y];
                    Agent occ = site.getAgent();
                    if (occ == null) {
                        babySites.addElement(site);
                    }
                }
            }
        }
        Collections.shuffle(babySites);
        return babySites;
    }

    // A utility function: Calculating the distance from the agents'
    // current position to a specified (x, y) location.
    public double calculateDistance(int x, int y) {
        return Math.sqrt(Math.pow((x - xPosition), 2) + Math.pow((y - yPosition), 2));
    }

    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public double getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }
}
