package hhttpd.responser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BytesResponser {

    public static int response(SocketChannel socketChannel, ByteBuffer byteBuffer, byte[] bytes) throws IOException {
        int offset = 0;
        while (offset < bytes.length) {
            byteBuffer.clear();
            int putLen = Math.min(byteBuffer.remaining(), bytes.length - offset);
            byteBuffer.put(bytes, offset, putLen);   // write to buffer
            offset += putLen;

            byteBuffer.flip();
            socketChannel.write(byteBuffer);    // read from buffer, write to socket channel
            byteBuffer.clear();
        }
        return offset;
    }
}
