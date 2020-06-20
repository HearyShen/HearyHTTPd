package hhttpd.launcher;

import hhttpd.reactor.MainReactor;
import hhttpd.reactor.SubReactor;

import java.io.IOException;

/**
 * Launcher of HearyHTTPd server.
 * @author Heary Shen
 */
public class Launcher {

    public static final int defaultBacklog = 100;

    private final MainReactor mainReactor;
    private final SubReactor subReactor;

    /**
     * Construct the launcher of HearyHTTPd server.
     * @throws IOException if MainReactor can not be constructed.
     */
    public Launcher(String webRoot) throws IOException {
        // construct MainReactor
        this.mainReactor = new MainReactor("localhost", 8080, defaultBacklog);
        System.out.println("MainReactor is configured to listen on: '" + this.mainReactor.getHost() + ":"+ this.mainReactor.getPort() + "'"
                + " with backlog: " + this.mainReactor.getBacklog());

        // construct SubReactor
        this.subReactor = new SubReactor(webRoot);

        this.mainReactor.setSubReactor(this.subReactor);
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
        System.out.println("SubReactor has been started as a thread.");

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
            Launcher launcher = new Launcher("D:\\myHexo\\public");
            launcher.launch();
            System.out.println("HearyHTTPD server is successfully launched!");
        } catch (IOException e) {
            System.out.println("Failed to launch HearyHTTPD server!");
            e.printStackTrace();
        }
    }
}
