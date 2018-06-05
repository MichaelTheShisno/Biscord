package com.mmunoz.util;

import com.mmunoz.BiscordClientUI;

import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private String userName;
    private String hostName;
    private int serverPort;
    private Socket socket;
    private BiscordClientUI ui;
    private ServerThread serverThread;

    /**
     * @param userName name as appears in chat
     * @param hostName name of server host machine
     * @param serverPort port of server host machine to connect to
     */
    public Client(BiscordClientUI ui, String userName, String hostName, int serverPort) {
        this.ui = ui;
        this.userName = userName;
        this.hostName = hostName;
        this.serverPort = serverPort;
    }// end: constructor Client

    /**
     * Connect to server, start thread to handle data exchange
     */
    @Override public void run() {
        try {
            socket = new Socket(hostName,serverPort);
            Thread.sleep(1000);
            System.out.println("Connection established to server " + hostName + " on port " + serverPort);

            serverThread = new ServerThread(ui,socket,userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
        } catch(IOException e) {
            System.err.println("Fatal connection error");
            e.printStackTrace();
        } catch(InterruptedException e) {
            System.err.println("Interrupted");
        }
    }// end: run

    /**
     * @return thread that handles data exchange
     */
    public ServerThread getServerThread() {
        return serverThread;
    }// end: getServerThread

}// end: class Client
