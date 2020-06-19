package hhttpd.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;


/**
 * A MainReactor accepts incoming request sockets and put them into a working queue.
 * @author Heary Shen
 */
public class MainReactor implements Runnable{

    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;
    private final String host;
    private final int port;
    private final int backlog;

    private SubReactor subReactor;

    public MainReactor(String host, int port, int backlog) throws IOException {
        this.host = host;
        this.port = port;
        this.backlog = backlog;

        this.selector = Selector.open();

        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.configureBlocking(false);  // non-blocking
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

//        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(this.host, this.port);
        this.serverSocketChannel.bind(inetSocketAddress, backlog);
//        serverSocket.bind(inetSocketAddress, backlog);
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getBacklog() {
        return backlog;
    }

    public SubReactor getSubReactor() {
        return subReactor;
    }

    public void setSubReactor(SubReactor subReactor) {
        this.subReactor = subReactor;
    }

    /**
     * Override method of Runnable interface
     */
    @Override
    public void run() {
        while (true) {
            try {
                this.selector.select();
                System.out.println("MainReactor: selected at " + System.currentTimeMillis());

                Set<SelectionKey> selectionKeySet = this.selector.selectedKeys();
                Iterator<SelectionKey> selectionKeys = selectionKeySet.iterator();

                while (selectionKeys.hasNext()) {
                    SelectionKey selectionKey = selectionKeys.next();

                    if (selectionKey.isAcceptable()) {
                        long tic = System.currentTimeMillis();
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);     // non-blocking
//                        System.out.println("MainReactor: selected acceptable socketChannel from " + socketChannel.getRemoteAddress());
                        this.subReactor.putRequest(socketChannel);  // put request socketChannel to subReactor
//                        System.out.println("MainReactor: accepted socketChannel has been put to SubReactor");
                        long toc = System.currentTimeMillis();
                        System.out.println("MainReactor: request accepted in " + (toc-tic) + " ms, " + toc);
                    }

                    selectionKeys.remove();
                }
//                subReactor.wakeUp();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
