package com.mmunoz;

import com.mmunoz.util.Client;
import com.mmunoz.util.PortNumber;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BiscordClientUI {
    private static JFrame frame;
    private JPanel panel_telem;
    private JButton button_connect;
    private JPanel panel_client;
    private JList listpane_msg;
    private DefaultListModel<String> list_msg;
    private JScrollPane scrollpane_msg;
    private JTextField field_address;
    private JTextField field_port;
    private JLabel label_address;
    private JLabel label_port;
    private JLabel label_title;
    private JTextField field_message;
    private JLabel label_biscord;

    private String hostName;
    private int portNumber;
    private Client client;
    private String userName;
    private boolean isConnected;

    private BiscordClientUI() {
        frame.setIconImage(createImageIcon("images/icon.png").getImage());
        label_biscord.setIcon(createImageIcon("images/logo.png"));

        list_msg = new DefaultListModel<>();
        listpane_msg.setModel(list_msg);
        button_connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hostName = "";
                portNumber = -1;
                boolean isValidAddress = hasValidAddress();
                boolean isValidPort = hasValidPort();

                if (isValidAddress)
                    hostName = field_address.getText();
                else
                    field_address.setText("Invalid IP Address");
                if (isValidPort)
                    portNumber = Integer.parseInt(field_port.getText());
                else
                    field_port.setText("Invalid Port Number");

                if (isValidAddress && isValidPort) {
                    obtainUserName();
                    establishClient();
                    isConnected = true;
                    button_connect.setEnabled(false);
                }
            }
        });
        field_message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (isConnected && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (field_message.getText() != null && !field_message.getText().equals("")) {
                        if (client.getServerThread() != null) {
                            client.getServerThread().addMessage(field_message.getText());
                            field_message.setText("");
                        }
                    }
                }
            }
        });
    }// end: constructor BiscordClientUI

    public static void main(String[] args) {
        frame = new JFrame("BiscordClientUI");
        frame.setContentPane(new BiscordClientUI().panel_client);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }// end: main

    private boolean hasValidAddress() {
        if (field_address.getText().equals(""))
            return false;
        if (field_address.getText() == null)
            return false;
        return true;
    }// end: hasValidAddress

    private boolean hasValidPort() {
        if (field_port.getText().equals(""))
            return false;
        if (field_port.getText() == null)
            return false;
        return PortNumber.isValid(field_port.getText());
    }// end: hasValidPort

    private void establishClient() {
        client = new Client(this, userName, hostName, portNumber);
        Thread thread = new Thread(client);
        thread.start();
    }// end: establishServer

    private void obtainUserName() {
        userName = JOptionPane.showInputDialog(new JFrame("Chatroom Username"), "What is your username?");
        if(userName.equals(""))
            userName = "no_name";
    }// end: obtainUserName

    public void displayNewMessage(String message) {
        list_msg.addElement(message);
    }// end: displayNewMessage

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }// end: createIcon

}// end: class BiscordClientUI
