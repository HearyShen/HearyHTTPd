package hhttpd.reactor;

import hhttpd.processor.GetProcessor;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * A SubReactor take accepted request sockets from working queue and handle them.
 */
public class SubReactor implements Runnable{

    private int id;
    private BlockingQueue<Socket> workingQueue;
    private String webRoot;

    /**
     * Construct a SubReactor instance.
     */
    public SubReactor(int id) {
        this.id = id;
        this.workingQueue = null;
        this.webRoot = ".";
    }

    /**
     * Construct a SubReactor instance with a working queue.
     * @param workingQueue SubReactor handles accepted request sockets in workingQueue
     */
    public SubReactor(int id, BlockingQueue<Socket> workingQueue) {
        this.id = id;
        this.workingQueue = workingQueue;
    }

    /**
     * Construct a SubReactor instance with a working queue.
     * @param workingQueue SubReactor handles accepted request sockets in workingQueue
     * @param webRoot root path of web directory
     */
    public SubReactor(int id, BlockingQueue<Socket> workingQueue, String webRoot) {
        this.id = id;
        this.workingQueue = workingQueue;
        this.webRoot = webRoot;
    }

    /**
     * Set the SubReactor's workingQueue.
     * @param workingQueue SubReactor handles accepted request sockets in workingQueue
     */
    public void setWorkingQueue(int id, BlockingQueue<Socket> workingQueue) {
        this.id = id;
        this.workingQueue = workingQueue;
    }

    /**
     * Override the method of Runnable interface.
     */
    @Override
    public void run() {
        System.out.println("Running SubReactor-" + this.id + " thread");
        while (true) {
            try {
                Socket socket = this.workingQueue.take();   // blocks when working queue is empty
                GetProcessor.handle(this.webRoot, socket.getInputStream(), socket.getOutputStream());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }
}
