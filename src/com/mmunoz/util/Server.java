package com.mmunoz.util;

import com.mmunoz.BiscordServerUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private int serverPort;
    private BiscordServerUI ui;
    private ArrayList<ClientThread> clients;
    private ServerSocket serverSocket = null;

    /**
     * @param portNumber port on this machine to accept clients on
     */
    public Server(BiscordServerUI ui, int portNumber) {
        this.ui = ui;
        this.serverPort = portNumber;
    }// end: constructor Server

    /**
     * @return list of client threads on this server machine
     */
    public ArrayList<ClientThread> getClients() {
        return clients;
    }// end: getClients

    /**
     * Open server socket, listen for incoming clients
     */
    @Override public void run() {
        clients = new ArrayList<ClientThread>();
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server listening on port: " + serverPort);
            acceptClients();
        } catch(IOException e) {
            System.err.println("Could not listen on port: " + serverPort);
        }
    }// end: run

    /**
     * Continously accept clients, start new threads to handle incoming clients
     */
    private void acceptClients() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Accept new client: " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(ui,this,socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch(IOException e) {
                System.out.println("Accept failed on: " + serverPort);
            }
        }
    }// end: acceptClients

}// end: class Server
