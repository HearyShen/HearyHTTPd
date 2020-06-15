package hhttpd.reactor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;


/**
 * A MainReactor accepts incoming request sockets and put them into a working queue.
 * @author Heary Shen
 */
public class MainReactor implements Runnable{

    private final ServerSocket serverSocket;
    private final int port;
    private final int backlog;
    private BlockingQueue<Socket> workingQueue;

    /**
     * Construct a MainReactor instance with a BlockingQueue as workingQueue.
     */
    public MainReactor() throws IOException {
        this.workingQueue = null;
        this.serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
        this.backlog = serverSocket.getReceiveBufferSize();
    }

    /**
     * Construct a MainReactor instance with a BlockingQueue as workingQueue.
     * @param workingQueue MainReactor accepts request sockets and put them into workingQueue.
     */
    public MainReactor(BlockingQueue<Socket> workingQueue) throws IOException {
        this.workingQueue = workingQueue;
        this.serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
        this.backlog = serverSocket.getReceiveBufferSize();
    }

    /**
     * Construct a MainReactor instance with a BlockingQueue as workingQueue.
     * @param workingQueue MainReactor accepts request sockets and put them into workingQueue.
     * @param port MainReactor is bounded to listening on this port.
     */
    public MainReactor(BlockingQueue<Socket> workingQueue, int port) throws IOException {
        this.workingQueue = workingQueue;
        this.serverSocket = new ServerSocket(port);
        this.port = port;
        this.backlog = serverSocket.getReceiveBufferSize();
    }

    /**
     * Construct a MainReactor instance with a BlockingQueue as workingQueue.
     * @param workingQueue MainReactor accepts request sockets and put them into workingQueue.
     * @param port MainReactor is binded to listening on this port.
     * @param backlog requested maximum length of the queue of incoming connections.
     */
    public MainReactor(BlockingQueue<Socket> workingQueue, int port, int backlog) throws IOException {
        this.workingQueue = workingQueue;
        this.serverSocket = new ServerSocket(port, backlog);
        this.port = port;
        this.backlog = serverSocket.getReceiveBufferSize();
    }

    /**
     * Set working queue of MainReactor to workingQueue
     * @param workingQueue MainReactor accepts request sockets and put them into workingQueue.
     */
    public void setWorkingQueue(BlockingQueue<Socket> workingQueue) {
        this.workingQueue = workingQueue;
    }

    /**
     * Override method of Runnable interface
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket =  this.serverSocket.accept();
                this.workingQueue.put(socket);
//                System.out.println("MainReactor: received a request.");
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
