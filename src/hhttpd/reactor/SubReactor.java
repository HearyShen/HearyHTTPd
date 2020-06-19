package hhttpd.reactor;

import hhttpd.worker.HttpWorker;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A SubReactor take accepted request sockets from working queue and handle them.
 */
public class SubReactor implements Runnable{

    private final Selector selector;
    private final ExecutorService executorService;
    private final String webRoot;

    public SubReactor(String webRoot) throws IOException {
        this.selector = Selector.open();
        this.executorService = Executors.newFixedThreadPool(8);
        this.webRoot = webRoot;
    }

    public void putRequest(SocketChannel socketChannel) {
        try {
            socketChannel.register(this.selector, SelectionKey.OP_READ);    // socketChannel is always Writable
            // socketChannel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            this.selector.wakeup();
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    public void wakeUp() {
        this.selector.wakeup();
    }

    /**
     * Override the method of Runnable interface.
     */
    @Override
    public void run() {
        while (true) {
            try {
//                System.out.println("SubReactor: selecting socketChannels");
                this.selector.select();

                Set<SelectionKey> selectionKeySet = this.selector.selectedKeys();
                Iterator<SelectionKey> selectionKeys = selectionKeySet.iterator();

                while (selectionKeys.hasNext()) {
                    SelectionKey selectionKey = selectionKeys.next();

                    if (selectionKey.isReadable()) {
                        long tic = System.currentTimeMillis();
                        selectionKey.cancel();      // avoid repeating selecting the same channel
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//                        System.out.println("SubReactor: selected readable socketChannel from " + socketChannel.getRemoteAddress());

                        HttpWorker httpWorker = new HttpWorker(webRoot, socketChannel);
                        this.executorService.submit(httpWorker);
//                        System.out.println("SubReactor: submitted HttpWorker for " + socketChannel.getRemoteAddress());
                        long toc = System.currentTimeMillis();
                        System.out.println("SubReactor: request submitted in " + (toc-tic) + " ms, " + toc);
                    }

                    selectionKeys.remove();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
