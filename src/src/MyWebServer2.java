package src;

import exceptions.PortOutOfBoundException;
import exceptions.UsedPortException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class MyWebServer2 extends Thread{

    protected Socket clientSocket;

    static Scanner myObj = new Scanner(System.in);  // Create a Scanner object

    public static void main(String[] args) throws IOException {
        ConfigManager configManager = new ConfigManager(new Configuration());

        try {
            ServerSocket serverConnect = new ServerSocket(configManager.getPort());
            System.out.println("Server started.\nListening for connections on port : " + configManager.getPort());

            // we listen until user halts server execution
            while (true) {
                WebServer myServer = new WebServer(serverConnect.accept(), configManager);
                configManager.setState("running");

                if (Configuration.verbose) {
                    System.out.println("Connecton opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

}
