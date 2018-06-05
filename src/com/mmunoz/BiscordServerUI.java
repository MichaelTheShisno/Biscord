package com.mmunoz;

import com.mmunoz.util.PortNumber;
import com.mmunoz.util.Server;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BiscordServerUI {
    private static JFrame frame;
    private JPanel pnlServer;
    private JTextField fldPortNumber;
    private JLabel lblStatus;
    private JLabel label_logo;

    private int portNumber;
    private ArrayList<String> messageLog;

    private BiscordServerUI() {
        messageLog = new ArrayList<>();
        frame.setIconImage(createImageIcon("images/icon.png").getImage());
        label_logo.setIcon(createImageIcon("images/logo.png"));

        fldPortNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (fldPortNumber.getText() != null && PortNumber.isValid(fldPortNumber.getText())) {
                        portNumber = Integer.parseInt(fldPortNumber.getText());
                        establishServer();                      // Start up the server
                        lblStatus.setText("Server listening on port: " + portNumber);
                        fldPortNumber.setEnabled(false);        // Disable test field for port number
                    }
                }
            }
        });
    }// end: constructor BiscordServerUI

    public static void main(String[] args) {
        frame = new JFrame("BiscordServerUI");
        frame.setContentPane(new BiscordServerUI().pnlServer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }// end: main

    public ArrayList<String> getMessageLog() {
        return messageLog;
    }// end: getMessageLog

    private void establishServer() {
        Thread thread = new Thread(new Server(this, portNumber));
        thread.start();
    }// end: establishServer

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }// end: createIcon

}// end: class BiscordServerUI
