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
    private final SubReactor[] subReactors;

    /**
     * Construct the launcher of HearyHTTPd server.
     * @throws IOException if MainReactor can not be constructed.
     */
    public Launcher(int subReactorCount, String webRoot) throws IOException {
        // create working queue
        this.workingQueue = new ArrayBlockingQueue<>(Launcher.defaultWorkingQueueCapacity);
        System.out.println("Constructed working queue of capacity " + Launcher.defaultWorkingQueueCapacity);

        // construct MainReactor
        this.mainReactor = new MainReactor(this.workingQueue, 8080);
        System.out.println("MainReactor is configured to listen on port: " + this.mainReactor.getPort());

        // construct SubReactor
        this.subReactors = new SubReactor[subReactorCount];
        for (int i=0; i<subReactorCount; i++) {
            this.subReactors[i] = new SubReactor(i, this.workingQueue, webRoot);
        }
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
        Thread[] subReactorThreads = new Thread[this.subReactors.length];
        for (int i=0; i<this.subReactors.length; i++) {
            Thread subReactorThread = new Thread(this.subReactors[i]);
            subReactorThreads[i] = subReactorThread;
            subReactorThread.start();
        }
        System.out.println("SubReactor(s) have been started as thread(s).");

        // fork-join for reactor threads
        try {
            mainReactorThread.join();
            for (Thread subReactorThread : subReactorThreads) {
                subReactorThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching HearyHTTPD server!");
        try {
            Launcher launcher = new Launcher(8, "D:\\myHexo\\public");
            launcher.launch();
            System.out.println("HearyHTTPD server is successfully launched!");
        } catch (IOException e) {
            System.out.println("Failed to launch HearyHTTPD server!");
            e.printStackTrace();
        }
    }
}
