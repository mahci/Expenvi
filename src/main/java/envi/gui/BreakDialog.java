package envi.gui;

import envi.experiment.Experimenter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BreakDialog extends JDialog implements KeyListener {

    public BreakDialog() {
        setTitle("Break!");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 300));
//        setMaximumSize(new Dimension(600, 500));
        setUndecorated(true);

        //-----------------------------------------
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel textLabel = new JLabel("Take a break!");
        textLabel.setAlignmentX(CENTER_ALIGNMENT);
        textLabel.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel instLabel = new JLabel(
                "<html>When ready, press <B>SHIFT + \\</B> to start the next block</html>",
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

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        // Close on Ctrl + DEL
        if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
            setVisible(false);
            Experimenter.get().nextBlock();
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
}
