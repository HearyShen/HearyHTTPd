package hhttpd.reactor;

import hhttpd.resolver.DemoResolver;
import hhttpd.resolver.GetResolver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

/**
 * A SubReactor take accepted request sockets from working queue and handle them.
 */
public class SubReactor implements Runnable{

    private BlockingQueue<Socket> workingQueue;
    private String webRoot;

    /**
     * Construct a SubReactor instance.
     */
    public SubReactor() {
        this.workingQueue = null;
        this.webRoot = ".";
    }

    /**
     * Construct a SubReactor instance with a working queue.
     * @param workingQueue SubReactor handles accepted request sockets in workingQueue
     */
    public SubReactor(BlockingQueue<Socket> workingQueue) {
        this.workingQueue = workingQueue;
    }

    /**
     * Construct a SubReactor instance with a working queue.
     * @param workingQueue SubReactor handles accepted request sockets in workingQueue
     * @param webRoot root path of web directory
     */
    public SubReactor(BlockingQueue<Socket> workingQueue, String webRoot) {
        this.workingQueue = workingQueue;
        this.webRoot = webRoot;
    }

    /**
     * Set the SubReactor's workingQueue.
     * @param workingQueue SubReactor handles accepted request sockets in workingQueue
     */
    public void setWorkingQueue(BlockingQueue<Socket> workingQueue) {
        this.workingQueue = workingQueue;
    }

    /**
     * Override the method of Runnable interface.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.workingQueue.take();   // blocks when working queue is empty
                // TODO: handle the incomming socket
                GetResolver.resolve(this.webRoot, socket.getInputStream(), socket.getOutputStream());
//                DemoResolver.handle(socket.getInputStream(), socket.getOutputStream());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }


}
