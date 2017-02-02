/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;

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

    public Boolean STAGE_SHOOT = false;
    public Boolean STAGE_PLACE = false;
    public static int PLAYER_COUNT;
    public final int gridSize;

    private ArrayList<JButton> Buttons = new ArrayList<JButton>();
    private final JButton actionButton;
    private final PrintWriter out;

    private String PLAYER_NAME;
    private String ENEMY_NAME;

    private String SHOW_NAME;
    private String commandSt = "";

    private JLabel positionLabel;
    private Timer timer;
    private int counter;

    public BattleShipGUI(String SHOW_NAME, PrintWriter out) {
        this.SHOW_NAME = SHOW_NAME;
        this.PLAYER_NAME = "pl_" + PLAYER_COUNT++;
        this.ENEMY_NAME = PLAYER_COUNT == 1 ? "pl_1" : "pl_0";
        this.out = out;

        this.gridSize = 10;

        this.setTitle(SHOW_NAME + ": Wait for the other player...");
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
        positionLabel = new JLabel("Wait for the other player...", JLabel.CENTER);
        JPanel buttonLeftPanel = new JPanel();
        actionButton = new JButton("SEND");
        actionButton.setEnabled(false);
        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!commandSt.equals("")) {
                    changeButtonsState(commandSt);
                    send(commandSt);
                    commandSt = "";
                }
            }
        });
        labelPanel.add(positionLabel);
        buttonLeftPanel.add(actionButton);
        leftPanel.add(labelPanel);
        leftPanel.add(buttonLeftPanel);

        contentPane.add(leftPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(gridSize, gridSize, 5, 5));
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JButton button = new JButton("[" + i + "," + j + "]");
                Buttons.add(button);
                button.setBackground(Color.lightGray);
                button.setEnabled(false);
                button.setPreferredSize(new Dimension(30, 30));
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setFont(new Font("Arial", Font.PLAIN, 10));
                button.setActionCommand("[" + i + "," + j + "]");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        JButton but = (JButton) ae.getSource();
                        if (but.getBackground() == Color.lightGray) {
                            commandSt = but.getActionCommand();
                            positionLabel.setText(commandSt + " chosen.");
                        }
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
            out.println("END");
            System.out.println(PLAYER_NAME + " was taken down...");
            System.out.println("Congratulations..." + ENEMY_NAME);
            System.out.println("closing...");
            System.exit(0);
        } else {
            if (STAGE_SHOOT) {
                out.println(PLAYER_NAME + ":" + s + ":" + ENEMY_NAME);
            } else if (STAGE_PLACE) {
                out.println(PLAYER_NAME + ":" + s);
            }
        }
    }

    private String getRandomPosition() {
        return "[" + ((int) (Math.random() * (gridSize))) + "," + ((int) (Math.random() * (gridSize))) + "]";
    }

    private void createTimer(Boolean state) {
        if (state) {
            counter = 10;
            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    counter--;
                    actionButton.setText(counter + "");
                    if (counter == 0) {
                        String pos;
                        if (STAGE_PLACE) {
                            pos = getRandomPosition();
                            changeButtonsState(pos);
                            send(pos);
                        } else {
                            pos = "[]";
                            changeButtonsState(pos);
                            send(pos);
                        }
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

    public void changeButtonsState(String placed) {
        createTimer(false);
        Color selectedColor = null;
        if (STAGE_PLACE) {
            selectedColor = Color.green;
        } else if (STAGE_SHOOT) {
            selectedColor = Color.yellow;
        }
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            if (placed.equals(b.getText())) {
                b.setBackground(selectedColor);
            }
            b.setEnabled(false);
        }
        actionButton.setEnabled(false);
    }

    public void resetButtons() {
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            b.setBackground(Color.lightGray);
        }
    }

    public void placeShot(String s) {
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            if (s.equals(b.getText())) {
                Color a = b.getBackground();
                if (a == Color.green) {
                    send(PLAYER_NAME + ":END");
                } else {
                    b.setBackground(Color.red);
                }
            }
        }
    }

    public void switchButtonsLock(Boolean state) {
        createTimer(state);
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            Color a = b.getBackground();
            if (a == Color.lightGray) {
                b.setEnabled(state);
            } else {
                b.setEnabled(false);
            }
        }
        actionButton.setEnabled(state);
    }

    public void updateLabels(String newLabel) {
        setTitle(SHOW_NAME + ": " + newLabel);
        positionLabel.setText(newLabel);
    }
}
