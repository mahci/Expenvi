package envi.gui;

import envi.experiment.Experimenter;
import envi.tools.Strs;
import envi.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BreakDialog extends JDialog implements KeyListener {

    public BreakDialog() {
        setTitle("Break!");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 300));
        setUndecorated(true);

        //-----------------------------------------
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel textLabel = new JLabel(Strs.DLG_BREAK_TITLE);
        textLabel.setAlignmentX(CENTER_ALIGNMENT);
        textLabel.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel instLabel = new JLabel(
                Strs.DLG_BREAK_TEXT,
                JLabel.CENTER);
        instLabel.setAlignmentX(CENTER_ALIGNMENT);
        instLabel.setFont(new Font("Sans", Font.PLAIN, 14));

        panel.add(Box.createVerticalStrut(100)); // Top space
        panel.add(textLabel);
        panel.add(Box.createVerticalStrut(20)); // space
        panel.add(instLabel);

        add(panel);
        addKeyListener(this);
    }

    //region [Overrides]
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        // Close on Ctrl + DEL
        if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
            setVisible(false);
//            Experimenter.get().nextBlock();
            // Set the start of homing
//            Experimenter.get().setHomingStart(Utils.now());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
    //endregion
}
