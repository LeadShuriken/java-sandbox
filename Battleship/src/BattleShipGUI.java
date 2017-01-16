/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author dsmis
 */
public class BattleShipGUI extends JFrame {

    private ArrayList<JButton> Buttons = new ArrayList<JButton>();

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    private final int gridSize;
    private final int[][] board;

    private String PLAYER_NAME;

    private boolean boatBool;

    private static final String INITIAL_TEXT = "Waiting for connection...";
    private static final String WAIT_FOR_OTHER = "Waiting for the other player...";
    private static final String PLACE_YOUR_SHIP = "Choose where to place your ship!";
    private static final String MAKE_YOUR_CHOICE = "Choose where to strike";
    private static final String YOU_LOST = "YOU LOST!!!";
    private static final String YOU_WON = "YOU WON!!!";
    private static final String ADDED_TEXT = " chosen.";
    private String commandSt = "";

    private JLabel positionLabel;
    private JButton actionButton;

    public BattleShipGUI(String name, Socket socket, BufferedReader in, PrintWriter out) {
        this.PLAYER_NAME = name;
        this.socket = socket;
        this.out = out;
        this.in = in;

        this.gridSize = 3;
        this.board = new int[gridSize][gridSize];

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
        positionLabel = new JLabel(INITIAL_TEXT, JLabel.CENTER);
        JPanel buttonLeftPanel = new JPanel();
        actionButton = new JButton("Execute");
        Buttons.add(actionButton);
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
                JButton button = new JButton("[" + i + ", " + j + "]");
                Buttons.add(button);
                button.setActionCommand("[" + i + ", " + j + "]");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        JButton but = (JButton) ae.getSource();
                        commandSt = but.getActionCommand();
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

    public void enableAll() {
        for (int i = 0; i < Buttons.size(); i++) {
            Buttons.get(i).setEnabled(true);
        }
    }
    
    public void disableAll() {
        for (int i = 0; i < Buttons.size(); i++) {
            Buttons.get(i).setEnabled(false);
        }
    }

    public void setStage() {
        positionLabel.setText(WAIT_FOR_OTHER);
        this.disableAll();
    }

    private void send(String s) {
        if (s.length() == 0) {
            int quit = JOptionPane.showConfirmDialog(null, "Exit chat");
            if (quit == 0) {
                out.println("END");
                System.out.println("closing...");

                try {
                    socket.close();
                } catch (Exception expt) {
                    System.out.println(expt);
                }
                System.exit(0);
            }
        } else {
            out.println(this.PLAYER_NAME + ":" + s);
        }
    }
}
