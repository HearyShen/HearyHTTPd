package hhttpd.launcher;

import hhttpd.reactor.MainReactor;
import hhttpd.reactor.SubReactor;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Launcher {

    public static final int defaultWorkingQueueCapacity = 100;

    private final BlockingQueue<Socket> workingQueue;
    private final MainReactor mainReactor;
    private final SubReactor subReactor;

    public Launcher() {
        // create working queue
        this.workingQueue = new ArrayBlockingQueue<>(Launcher.defaultWorkingQueueCapacity);
        System.out.println("Constructed working queue of capacity " + Launcher.defaultWorkingQueueCapacity);

        // start MainReactor as a thread
        this.mainReactor = new MainReactor(this.workingQueue, 8080);
        System.out.println("MainReactor is configured to listen on port: " + this.mainReactor.getPort());

        // start SubReactor as a thread
        this.subReactor = new SubReactor(this.workingQueue);
    }

    public void launch() {
        Thread mainReactorThread = new Thread(this.mainReactor);
        mainReactorThread.start();
        System.out.println("MainReactor has been started as a thread.");

        Thread subReactorThread = new Thread(this.subReactor);
        subReactorThread.start();
        System.out.println("SubReactor has been started as as thread.");

        try {
            mainReactorThread.join();
            subReactorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher();

        System.out.println("Launching HearyHTTPD server!");
        launcher.launch();
        System.out.println("HearyHTTPD server is successfully launched!");
    }
}
