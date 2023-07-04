package org.colin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DigitalLogicSimulator extends JFrame {
    private boolean switch1State;
    private boolean switch2State;
    private boolean andGateState;
    private boolean orGateState;

    private JButton switch1Button;
    private JButton switch2Button;
    private JButton andGateButton;
    private JButton orGateButton;

    public DigitalLogicSimulator() {
        switch1State = false;
        switch2State = false;
        andGateState = false;
        orGateState = false;

        setTitle("Digital Logic Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        switch1Button = new JButton("Switch 1 (OFF)");
        switch2Button = new JButton("Switch 2 (OFF)");
        andGateButton = new JButton("AND Gate (OFF)");
        orGateButton = new JButton("OR Gate (OFF)");

        switch1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch1State = !switch1State;
                switch1Button.setText("Switch 1 " + (switch1State ? "(ON)" : "(OFF)"));
                updateLogic();
            }
        });

        switch2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch2State = !switch2State;
                switch2Button.setText("Switch 2 " + (switch2State ? "(ON)" : "(OFF)"));
                updateLogic();
            }
        });

        andGateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                andGateState = !andGateState;
                andGateButton.setText("AND Gate " + (andGateState ? "(ON)" : "(OFF)"));
                updateLogic();
            }
        });

        orGateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orGateState = !orGateState;
                orGateButton.setText("OR Gate " + (orGateState ? "(ON)" : "(OFF)"));
                updateLogic();
            }
        });

        add(switch1Button);
        add(switch2Button);
        add(andGateButton);
        add(orGateButton);

        pack();
        setVisible(true);
    }

    private void updateLogic() {
        boolean output = (switch1State && switch2State && andGateState) || (switch1State || switch2State || orGateState);
        System.out.println("Output: " + output);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DigitalLogicSimulator();
            }
        });
    }
}
