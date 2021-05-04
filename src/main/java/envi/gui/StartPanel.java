package envi.gui;

import envi.experiment.Experimenter;
import envi.experiment.Practicer;
import envi.tools.Config;
import envi.tools.Utils;
import org.checkerframework.checker.units.qual.C;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class StartPanel extends JPanel {

    private final String showCaseHint = "Welcome to the Moose! Press SPACE to start";
    private final String showCaseBtnText = "Show Me How It Works";

    private final String warmUpHint = "Let's warm-up a bit. Press SPACE to go";
    private final String warmUpBtnText = "Let's Go!";

    private final String experimentHint = "Now the real experiment! Press SPACE to start";
    private final String experimentBtnText = "Start Experiment";

    private final Action startShowCase = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.get().showPanel(new PracticePanel());
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

    JButton startButton = new JButton();
    JLabel hintLabel = new JLabel();

    /**
     * Constructor
     */
    public StartPanel(Config.PROCESS_STATE state) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        setPreferredSize(new Dimension(600, 500));
//        setMaximumSize(new Dimension(600, 500));

        // Create the generic button
        startButton.setFont(new Font("Sans", Font.PLAIN, 14));
        startButton.setMaximumSize(new Dimension(350, 50));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);

        // Check state for different texts
        String labelText = "";
        String btnText = "";
        switch(state) {
        case SHOW_CASE:
            labelText = showCaseHint;
            btnText = showCaseBtnText;
//            startButton.setText(btnText);

            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startExperiment);
            break;
        case WARM_UP:
            labelText = warmUpHint;
            btnText = warmUpBtnText;
//            startButton.setText(btnText);
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startWarmUp);
            break;
        case EXPERIMENT:
            labelText = experimentHint;
            btnText = experimentBtnText;
//            startButton.setText(btnText);
            startButton.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),
                    "SPACE");
            startButton.getActionMap().put("SPACE", startExperiment);
            break;
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
