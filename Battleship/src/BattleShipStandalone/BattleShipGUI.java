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
import java.net.Socket;
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

    private ArrayList<JButton> Buttons = new ArrayList<JButton>();
    private final JButton actionButton;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    private String PLAYER_NAME;
    private String commandSt = "";
    private JLabel positionLabel;
    private Timer timer;
    private int counter;

    public final int gridSize;

    public BattleShipGUI(String name, Socket socket, PrintWriter out, BufferedReader in) {
        this.PLAYER_NAME = name;
        this.socket = socket;
        this.out = out;
        this.in = in;

        this.gridSize = 3;

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

    // Methid startign the timer for the application.
    private void createTimer(Boolean state, BattleShipGameLogic g) {
        if (state) {
            counter = 11;
            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    counter--;
                    actionButton.setText(counter + "");
                    if (counter == 0) {
                        g.turnTimedOut(PLAYER_NAME);
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
    public void changeButtonsState(Boolean state, ArrayList<String> shot, String placed, BattleShipGameLogic g) {
        createTimer(state, g);
        for (int i = 0; i < Buttons.size(); i++) {
            JButton b = Buttons.get(i);
            if ((shot != null && shot.contains(b.getText()))) {
                b.setBackground(Color.red);
            } else if (placed != null && placed.equals(b.getText())) {
                b.setBackground(Color.green);
                b.setEnabled(false);
            } else {
                b.setBackground(Color.lightGray);
                b.setEnabled(state);
            };
        }
        actionButton.setEnabled(state);
    }

    // Method changing the labels for the user panel.
    public void updateLabels(String newLabel) {
        setTitle(PLAYER_NAME + ": " + newLabel);
        positionLabel.setText(newLabel);
    }

    // Method handlign the resend of the Clien messeges.
    public void send(String s) {
        String[] a = s.split(":");
        if (a.length > 1 && a[1].equals("END")) {
            out.println("END");
            System.out.println(this.PLAYER_NAME + " was taken down...");
            System.out.println("Congratulations..." + s.split(":")[0]);
            System.out.println("closing...");

            try {
                socket.close();
            } catch (Exception expt) {
                System.out.println(expt);
            }
            System.exit(0);
        } else {
            out.println(this.PLAYER_NAME + ":" + s);
        }
    }
}
