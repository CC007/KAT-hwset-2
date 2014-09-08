import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class ButtonPanel extends JPanel implements ActionListener {
	private static final long	serialVersionUID	= 4429598756080187232L;

	Dimension					preferredSize		= new Dimension(250, 650);
	Simulation					scape;
	JTextPane					info;
	JPanel						buttons1, buttons2, buttons3, body;
	JLabel						epochsLabel, forwardLabel;
	JTextField					forwardEpochs;
	JButton						next, forward, restart, exit;
	private String				newline				= "\n";
	String						content[]			= { "Scape", "Agents: ", newline + "Site", "Coordinates: ", "Site food: ", newline + "Agent on Site", "Agent ID: ", "Age: ", "Agent Energy: ", };

	String						style[]				= { "bold", "regular", "bold", "regular", "regular", "bold", "regular", "regular", "regular", "regular", "regular", };

	public ButtonPanel(Simulation controller) {
		scape = controller;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		info = new JTextPane();
		info.setPreferredSize(new Dimension(270, 300));
		info.setMaximumSize(new Dimension(270, 300));
		info.setEditable(false);
		info.setOpaque(false);
		info.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(0, 0, 20, 0),
						BorderFactory.createCompoundBorder(
								BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
								BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		StyledDocument doc = info.getStyledDocument();
		addStylesToDocument(doc);
		updateInfo();

		epochsLabel = new JLabel("", SwingConstants.CENTER);
		String ep = "Epochs: " + scape.epochs;
		epochsLabel.setText(ep);

		buttons1 = new JPanel();
		buttons1.setLayout(new FlowLayout());
		buttons1.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		next = new JButton("Next");
		next.setActionCommand("next");
		next.addActionListener(this);

		buttons1.add(next);

		forwardLabel = new JLabel("Enter the number of epochs to forward.", SwingConstants.LEFT);
		forwardLabel.setVerticalAlignment(SwingConstants.BOTTOM);

		buttons2 = new JPanel();
		buttons2.setLayout(new BoxLayout(buttons2, BoxLayout.LINE_AXIS));

		forwardEpochs = new JTextField("100");
		forwardEpochs.setMaximumSize(new Dimension(100, 25));

		forward = new JButton("Forward");
		forward.setActionCommand("forward");
		forward.addActionListener(this);

		buttons2.add(forwardEpochs);
		buttons2.add(Box.createRigidArea(new Dimension(5, 0)));
		buttons2.add(forward);

		buttons3 = new JPanel();
		buttons3.setLayout(new BoxLayout(buttons3, BoxLayout.LINE_AXIS));
		buttons3.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		restart = new JButton("Restart");
		restart.setActionCommand("restart");
		restart.addActionListener(this);

		exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);

		buttons3.add(Box.createHorizontalGlue());
		buttons3.add(restart);
		buttons3.add(Box.createRigidArea(new Dimension(5, 0)));
		buttons3.add(exit);

		body = new JPanel();
		body.setLayout(new GridLayout(0, 1));

		body.add(epochsLabel);
		body.add(buttons1);
		body.add(forwardLabel);
		body.add(buttons2);

		this.add(info, BorderLayout.NORTH);
		this.add(body, BorderLayout.CENTER);
		this.add(buttons3, BorderLayout.SOUTH);
	}

	public Dimension getPreferredSize() {
		return preferredSize;
	}

	public void actionPerformed(ActionEvent e) {
		if ("next".equals(e.getActionCommand())) {
			update(1);
		}

		if ("forward".equals(e.getActionCommand())) {
			Integer num = new Integer(forwardEpochs.getText());
			update(num.intValue());
		}

		if ("restart".equals(e.getActionCommand())) {
			scape.frame.dispose();
			scape.run();
		}

		if ("exit".equals(e.getActionCommand())) {
			scape.frame.dispose();
		}
	}

	private void update(int cycles) {
		if (cycles < 0) {
			while (true) {
				scape.step();
				addInfo(scape.grid[scape.mainPanel.xSelected][scape.mainPanel.ySelected]);
				scape.epochs++;
				String ep = "Epochs:  " + scape.epochs;
				epochsLabel.setText(ep);
			}
		}
		else {
			for (int c = 0; c < cycles; c++) {
				scape.step();
				addInfo(scape.grid[scape.mainPanel.xSelected][scape.mainPanel.ySelected]);
				scape.epochs++;
				String ep = "Epochs:  " + scape.epochs;
				epochsLabel.setText(ep);
				scape.mainPanel.update();
			}
		}
	}

	private void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		StyleConstants.setFontSize(regular, 14);

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);
	}

	public void addInfo(Site s) {
		content[0] = "Scape";
		content[1] = "Agents: " + scape.agents.size();
		content[2] = newline + "Site";
		content[3] = "Coordinates: (" + s.getXPosition() + ", " + s.getYPosition() + ")";
		content[4] = "Site food: " + round(s.getFood());
		content[5] = newline + "Agent on Site";

		Agent a = s.getAgent();
		if (a != null) {
			content[6] = "Agent ID: " + a;
			content[7] = "Age: " + a.getAge();
			content[8] = "Agent Energy: " + round(a.getEnergy());
		}

		else {
			content[6] = "ID: ";
			content[7] = "Age: ";
			content[8] = "Agent Energy: ";
		}

		updateInfo();
	}

	private void updateInfo() {
		StyledDocument doc = info.getStyledDocument();
		try {
			doc.remove(0, doc.getLength());
			for (int i = 0; i < content.length; i++) {
				doc.insertString(doc.getLength(), content[i] + newline, doc.getStyle(style[i]));
			}
		}
		catch (BadLocationException ble) {
			System.err.println("Couldn't insert text into text pane.");
		}
	}

	public String round(double value) {
		DecimalFormat df = new DecimalFormat("0.00");
		String stringValue = df.format(value);
		return stringValue;
	}
}
