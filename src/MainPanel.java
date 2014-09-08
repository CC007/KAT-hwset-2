import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;

// Functions involved in showing the simulation.

public class MainPanel extends JPanel implements MouseInputListener {
	private static final long	serialVersionUID	= 5678559858190916387L;

	Dimension	preferredSize	= new Dimension(650, 650);
	Simulation	scape;
	JLabel[][]	labels;
	int			xSize, ySize;
	int			xSelected		= 0, ySelected = 0;

	public MainPanel(Simulation controller) {
		this.scape = controller;
		xSize = scape.xSize;
		ySize = scape.ySize;
		setLayout(new GridLayout(xSize, ySize));
		addMouseListener(this);
		labels = new JLabel[xSize][ySize];
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				labels[x][y] = new JLabel("");
				labels[x][y].setOpaque(true);
				labels[x][y].setBorder(BorderFactory.createLineBorder(Color.black));
				labels[x][y].setHorizontalAlignment(SwingConstants.CENTER);
				labels[x][y].setVerticalAlignment(SwingConstants.CENTER);
				add(labels[x][y]);
			}
		}
		update();
	}

	public void update() {
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < ySize; x++) {
				Site site = scape.grid[x][y];
				JLabel label = labels[x][y];
				double energy = site.getFood();

				double div = (255 / scape.maxFood) * energy;
				int gradient = (int) (255 - div);
				Color background;

				background = (gradient > 235) ? new Color(255, 250, 205) : new Color(gradient, 255, gradient);

				label.setBackground(background);

				if (site.getAgent() != null) {
					label.setText("o");
					label.setForeground(Color.red);
				}
				else {
					label.setText("");
				}
			}
		}
	}

	public Dimension getPreferredSize() {
		return preferredSize;
	}

	// Methods required by the MouseInputListener interface.
	public void mouseClicked(MouseEvent e) {
		labels[xSelected][ySelected].setBorder(BorderFactory.createLineBorder(Color.black));
		xSelected = e.getX() / (650 / xSize);
		ySelected = e.getY() / (650 / ySize);
		labels[xSelected][ySelected].setBorder(BorderFactory.createLineBorder(Color.red));
		scape.buttonPanel.addInfo(scape.grid[xSelected][ySelected]);
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}
}
