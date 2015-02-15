package com.daemon.netty.server.raw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by daemon on 15-2-13.
 */
public class BlockingEchoServer {
    private final int port;

    public BlockingEchoServer(final int port) {
        this.port = port;
    }

    public void run() throws Exception {
        final ServerSocket serverSocket = new ServerSocket(port);
        try {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from : " + clientSocket);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            final PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                            while (true) {
                                writer.println(reader.readLine());
                                writer.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                clientSocket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println();

        try {
            new BlockingEchoServer(7894).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
