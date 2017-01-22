/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dsmis Class handling the GUI of the application.
 */
public class BattleShipGUI extends JFrame {

    public Boolean END_TURN = false;
    public Boolean STAGE_SHOOT = false;
    public Boolean STAGE_PLACE = false;

    private ArrayList<JButton> Buttons = new ArrayList<JButton>();
    private final JButton actionButton;
    private final PrintWriter out;

    private String PLAYER_NAME;
    private String commandSt = "";

    private JLabel positionLabel;
    private Timer timer;
    private int counter;

    public final int gridSize;

    public BattleShipGUI(String name, PrintWriter out) {
        this.PLAYER_NAME = name;
        this.out = out;

        this.gridSize = 10;

        this.setTitle(this.PLAYER_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 80));

        JPanel labelPanel = new JPanel();
        positionLabel = new JLabel("Waiting for connection...", JLabel.CENTER);
        JPanel buttonLeftPanel = new JPanel();
        actionButton = new JButton("SEND");
//        actionButton.setEnabled(false);
        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                send(commandSt);
                commandSt = "";
            }
        });
        labelPanel.add(positionLabel);
        buttonLeftPanel.add(actionButton);
        leftPanel.add(labelPanel);
        leftPanel.add(buttonLeftPanel);

        contentPane.add(leftPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(gridSize, gridSize, 10, 10));
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JButton button = new JButton("[" + i + "," + j + "]");
                Buttons.add(button);
                button.setBackground(Color.lightGray);
//                button.setEnabled(false);
                button.setActionCommand("[" + i + "," + j + "]");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        JButton but = (JButton) ae.getSource();
                        commandSt = but.getActionCommand();
                        positionLabel.setText(commandSt + " chosen.");
                    }
                });
                buttonPanel.add(button);
            }
        }
        contentPane.add(buttonPanel);
        setContentPane(contentPane);
        pack();

        setLocationByPlatform(true);
        setVisible(true);
    }

    private void send(String s) {
        String[] a = s.split(":");
        if (a.length > 1 && a[1].equals("END")) {
            this.out.println("END");
            System.out.println(this.PLAYER_NAME + " was taken down...");
            System.out.println("Congratulations..." + s.split(":")[0]);
            System.out.println("closing...");
            System.exit(0);
        } else {
            this.out.println(this.PLAYER_NAME + ":" + s);
        }
    }

    private String getRandomPosition() {
        return "[" + ((int) (Math.random() * (gridSize))) + "," + ((int) (Math.random() * (gridSize))) + "]";
    }

    // Methid startign the timer for the application.
    private void createTimer(Boolean state) {
        if (state) {
            counter = 5;
            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    counter--;
                    actionButton.setText(counter + "");
                    if (counter == 0) {
                        END_TURN = true;
                        send(getRandomPosition());
                        timer.stop();
                    }
                }
            });
            timer.start();
        } else if (timer != null) {
            actionButton.setText("WAIT");
            timer.stop();
        }
    }

    // Method changing the buttons for the app.
    public void changeButtonsState(Boolean state, String placed) {
        END_TURN = false;
        createTimer(state);
        Color selectedColor = null;
        if (STAGE_PLACE) {
            selectedColor = Color.green;
        } else if (STAGE_SHOOT) {
            selectedColor = Color.red;
        }
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            if (placed != null && placed.equals(b.getText())) {
                b.setBackground(selectedColor);
                b.setEnabled(false);
            } else {
                b.setBackground(Color.lightGray);
                b.setEnabled(state);
            };

        }
        actionButton.setEnabled(state);
    }

    public void updateLabels(String newLabel) {
        setTitle(PLAYER_NAME + ": " + newLabel);
        positionLabel.setText(newLabel);
    }
}
