package com.mmunoz.util;

import com.mmunoz.BiscordServerUI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private Server server;
    private BiscordServerUI ui;

    /**
     * @param server that handles this thread
     * @param socket connection from server to client
     */
    public ClientThread(BiscordServerUI ui, Server server, Socket socket) {
        this.ui = ui;
        this.server = server;
        this.socket = socket;
    }// end: ClientThread

    /**
     * @return print writer for this client
     */
    private PrintWriter getWriter(){
        return clientOut;
    }// end: getWriter

    /**
     * Handle back and forth between this client and server
     */
    @Override public void run() {
        try {
            this.clientOut = new PrintWriter(socket.getOutputStream(),false);
            Scanner in = new Scanner(socket.getInputStream());

            while(!socket.isClosed()) {
                if(in.hasNextLine()) {
                    String input = in.nextLine();
                    System.out.println(input);
                    ui.getMessageLog().add(input);
                    for(ClientThread client : server.getClients()) {
                        PrintWriter clientOut = client.getWriter();
                        if(clientOut != null) {
                            clientOut.write(input + "\r\n");
                            clientOut.flush();
                        }
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }// end: run

}// end: class ClientThread
