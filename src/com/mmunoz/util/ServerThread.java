package com.mmunoz.util;

import com.mmunoz.BiscordClientUI;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private Queue<String> messagesToSend;
    private boolean hasMessages = false;
    private BiscordClientUI ui;

    /**
     * @param socket connection to server
     * @param userName name as appears in chat room
     */
    public ServerThread(BiscordClientUI ui, Socket socket, String userName) {
        this.ui = ui;
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<>();
    }// end: constructor ServerThread

    /**
     * Add a message to a queue of messages to send
     *
     * @param message input from client to be sent to server
     */
    public void addMessage(String message) {
        synchronized(messagesToSend) {
            hasMessages = true;
            messagesToSend.add(message);
        }
    }// end: addMessage

    /**
     * Run exchange of data to and from server from this client
     */
    @Override public void run() {
        System.out.println("Welcome " + userName);
        System.out.println("Local Port: " + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try {
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while(!socket.isClosed()) {
                if(serverInStream.available() > 0) {
                    if(serverIn.hasNextLine()) {
                        String line = serverIn.nextLine();
                        System.out.println(line);
                        ui.displayNewMessage(line);
                    }
                }
                if(hasMessages) {
                    String nextSend = "";
                    synchronized(messagesToSend) {
                        nextSend = messagesToSend.remove();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(userName + " > " + nextSend);
                    serverOut.flush();
                }
            }
            System.out.println("Goodbye");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }// end: run

}// end: class ServerThread
