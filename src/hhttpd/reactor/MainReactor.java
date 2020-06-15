package hhttpd.reactor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class MainReactor implements Runnable{

    public static final int defaultPort = 8080;
    public static final int defaultBacklog = 50;

    private ServerSocket serverSocket;
    private int port;
    private int backlog;
    private BlockingQueue<Socket> workingQueue;

    public MainReactor(BlockingQueue<Socket> workingQueue) {
        this.buildServerSocket(workingQueue, MainReactor.defaultPort, MainReactor.defaultBacklog);
    }

    public MainReactor(BlockingQueue<Socket> workingQueue, int port) {
        this.buildServerSocket(workingQueue, port, MainReactor.defaultBacklog);
    }

    public MainReactor(BlockingQueue<Socket> workingQueue, int port, int backlog) {
        this.buildServerSocket(workingQueue, port, backlog);
    }

    public void buildServerSocket(BlockingQueue<Socket> workingQueue, int port, int backlog) {
        try {
            this.serverSocket = new ServerSocket(port, backlog);
        } catch (IOException e) {
            System.out.println("Failed to bind port " + port + " !");
            e.printStackTrace();
        }
        this.workingQueue = workingQueue;
        this.port = port;
        this.backlog = backlog;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket =  this.serverSocket.accept();
                this.workingQueue.put(socket);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPort() {
        return port;
    }

    public int getBacklog() {
        return backlog;
    }
}
