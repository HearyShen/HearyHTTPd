package hhttpd.launcher;

import hhttpd.reactor.MainReactor;
import hhttpd.reactor.SubReactor;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Launcher of HearyHTTPd server.
 * @author Heary Shen
 */
public class Launcher {

    public static final int defaultWorkingQueueCapacity = 100;

    private final BlockingQueue<Socket> workingQueue;
    private final MainReactor mainReactor;
    private final SubReactor subReactor;

    /**
     * Construct the launcher of HearyHTTPd server.
     * @throws IOException if MainReactor can not be constructed.
     */
    public Launcher() throws IOException {
        // create working queue
        this.workingQueue = new ArrayBlockingQueue<>(Launcher.defaultWorkingQueueCapacity);
        System.out.println("Constructed working queue of capacity " + Launcher.defaultWorkingQueueCapacity);

        // construct MainReactor
        this.mainReactor = new MainReactor(this.workingQueue, 8080);
        System.out.println("MainReactor is configured to listen on port: " + this.mainReactor.getPort());

        // construct SubReactor
        this.subReactor = new SubReactor(this.workingQueue, "D:\\myHexo\\public");
    }

    /**
     * Launch the HearyHTTPd server's threads, including MainReactor and SubReactor.
     */
    public void launch() {
        // start MainReactor as a thread
        Thread mainReactorThread = new Thread(this.mainReactor);
        mainReactorThread.start();
        System.out.println("MainReactor has been started as a thread.");

        // start SubReactor as a thread
        Thread subReactorThread = new Thread(this.subReactor);
        subReactorThread.start();
        System.out.println("SubReactor has been started as as thread.");

        // fork-join for reactor threads
        try {
            mainReactorThread.join();
            subReactorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching HearyHTTPD server!");
        try {
            Launcher launcher = new Launcher();
            launcher.launch();
            System.out.println("HearyHTTPD server is successfully launched!");
        } catch (IOException e) {
            System.out.println("Failed to launch HearyHTTPD server!");
            e.printStackTrace();
        }
    }
}
