package envi.gui;

import envi.experiment.Experimenter;
import envi.experiment.ShowCaser;
import envi.tools.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StartPanel extends JPanel {

    // Text labels for each phase
    private final String showCaseHint = "Welcome to the Moose! Press SPACE to start";
    private final String warmUpHint = "Let's warm-up a bit. Press SPACE to go";
    private final String experimentHint = "Now the real experiment! Press SPACE to start";

    // Components
    JButton startButton = new JButton();
    JLabel hintLabel = new JLabel();

    // -------------------------------------------------------------------------------
    // Actions to perform on click of the button

    private final Action startShowCase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ShowCaser.get().startShowCase();
        }
    };

    private final Action startWarmUp = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().startExperiment(false);
        }
    };

    private final Action startExperiment = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Experimenter.get().startExperiment(true);
        }
    };

    // ===============================================================================
    /**
     * Constructor
     */
    public StartPanel(Config.PROCESS_STATE state) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create the generic button
        startButton.setFont(new Font("Sans", Font.PLAIN, 14));
        startButton.setMaximumSize(new Dimension(350, 50));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);

        // Check state for different texts
        String labelText = "";
        switch (state) {
            case SHOW_CASE -> {
                labelText = showCaseHint;
                startButton.getInputMap().put(
                        KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                        "SPACE");
                startButton.getActionMap().put("SPACE", startShowCase);
            }
            case WARM_UP -> {
                labelText = warmUpHint;
                startButton.getInputMap().put(
                        KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                        "SPACE");
                startButton.getActionMap().put("SPACE", startWarmUp);
            }
            case EXPERIMENT -> {
                labelText = experimentHint;
                startButton.getInputMap().put(
                        KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                        "SPACE");
                startButton.getActionMap().put("SPACE", startExperiment);
            }
        }

        // Hint label
        hintLabel.setText(labelText);
        hintLabel.setAlignmentX(CENTER_ALIGNMENT);
        hintLabel.setFont(new Font("Sans", Font.PLAIN, 14));

        // Adding the components
        this.add(Box.createVerticalStrut(350)); // Top space
        this.add(hintLabel);
        this.add(Box.createVerticalStrut(20)); // Space
        this.add(startButton);
        this.add(Box.createVerticalStrut(100)); // Space

        startButton.requestFocusInWindow();
    }

    /**
     * Main painting method
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        startButton.requestFocus();
    }

}
