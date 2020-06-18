package sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
[Client] starts writing
[Client] finished writing
[Client] starts reading
Server: 'Have a nice day!'
[Client] fininshed reading
[Client] closed socket
*/

public class NIOClient {

    public static void main(String[] args) throws IOException {
        // open socket
        Socket socket = new Socket("127.0.0.1", 8888);

        // write request to server
        OutputStream out = socket.getOutputStream();
        String s = "Client: 'Hello!'";
        System.out.println("[Client] starts writing");
        out.write(s.getBytes());
        socket.shutdownOutput();
        System.out.println("[Client] finished writing");

        // read response from server
        InputStream in = socket.getInputStream();
        byte[] readBuffer = new byte[1024];
        System.out.println("[Client] starts reading");
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            int ret = in.read(readBuffer);
            if (ret == -1) {
                break;
            }
            stringBuilder.append(new String(readBuffer, StandardCharsets.UTF_8));
        }
        System.out.println(stringBuilder.toString());
        System.out.println("[Client] fininshed reading");

        // close socket
        socket.close();
        System.out.println("[Client] closed socket");
    }
}
