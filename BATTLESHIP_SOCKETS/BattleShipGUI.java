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
 * @author dsmis
 * @member default
 * @name BattleShipGUI
 * @description Class handling the GUI of the application, acting as the GUI of
 * the client and inheriting from JFrame
 */
public class BattleShipGUI extends JFrame {

    // Game stage handlers
    public Boolean STAGE_SHOOT = false;
    public Boolean STAGE_PLACE = false;

    // Game grid number
    public final int gridSize;

    // Player handlers
    public static int PLAYER_COUNT;
    private final String PLAYER_NAME;
    private final String ENEMY_NAME;
    private final String SHOW_NAME;

    // UI handlers
    private final ArrayList<JButton> Buttons;
    private final JButton actionButton;
    private final PrintWriter out;
    private JLabel positionLabel;
    private Timer timer;

    // Logic helpers
    private String commandSt = ""; //Player command holder
    private int counter; // Timer timeout helper

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name BattleShipGUI
     * @description Constructor
     * @param SHOW_NAME {String} The name the clients wants to be referred as
     * @param out {PrintWriter} - The socket output to the server
     */
    public BattleShipGUI(String SHOW_NAME, PrintWriter out) {
        this.SHOW_NAME = SHOW_NAME;
        this.PLAYER_NAME = "pl_" + PLAYER_COUNT++;
        this.ENEMY_NAME = PLAYER_COUNT == 1 ? "pl_1" : "pl_0";
        this.Buttons = new ArrayList<JButton>();
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

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name send
     * @description Output handler (for sending to the server)
     * @param send {String} Sent command
     */
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

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name getRandomPosition
     * @description Getter for a random position within the board (game grid)
     */
    private String getRandomPosition() {
        return "[" + ((int) (Math.random() * (gridSize))) + "," + ((int) (Math.random() * (gridSize))) + "]";
    }

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name createTimer
     * @description method creating a timer for the duration of the player turn
     * @param state {Boolean} create or destroy
     */
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

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name changeButtonsState
     * @description method changing properties of one slot according to stage of
     * the game on the side of the sender
     * @param placed {String} position of the selected slot
     */
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

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name placeShot
     * @description method placing a shot on the side of the receiver
     * @param s {String} position of the shot slot
     */
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

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name switchButtonsLock
     * @description method toggling access to all selectable slots
     * @param state {Boolean} switch selectable/un-selectable handler
     */
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

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name resetButtons
     * @description a clearer for all the buttons
     */
    public void resetButtons() {
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            b.setBackground(Color.lightGray);
        }
    }

    /**
     * @author dsmis
     * @member BattleShipGUI
     * @name updateLabels
     * @description an updater for the labels of the GUI
     */
    public void updateLabels(String newLabel) {
        setTitle(SHOW_NAME + ": " + newLabel);
        positionLabel.setText(newLabel);
    }
}
