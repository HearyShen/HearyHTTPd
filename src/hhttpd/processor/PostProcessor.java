package hhttpd.processor;

import hhttpd.resolver.RequestLine;
import hhttpd.responser.BytesResponser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class PostProcessor {
    public static void handle(String commands,
                              String cgiPath,
                              SocketChannel socketChannel,
                              ByteBuffer byteBuffer,
                              String message,
                              RequestLine requestLine) throws IOException, InterruptedException {
        // DEMO: use specified command and variables for demo
        String[] command = {
                "C:\\Users\\heary\\miniconda3\\envs\\python3\\python.exe",
                "echo_envs.py"
        };
        String[] envs = {
                "PATH_INFO=" + requestLine.getUri().getPath(),
                "QUERY_STRING=" + message,
                "REQUEST_METHOD=" + requestLine.getMethod(),
                "REMOTE_ADDR=" + socketChannel.getRemoteAddress(),
                "SERVER_NAME=" + socketChannel.getLocalAddress(),
                "SERVER_SOFTWARE=HearyHTTPd(hhttpd)/experimental(noarch)"
        };
        File file = new File("./src/cgi");
        Process process = Runtime.getRuntime().exec(command, envs, file);
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
//            System.out.println(line);
            stringBuilder.append(line).append("\r\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        process.waitFor();
        String responseFromCGI = stringBuilder.toString();
        BytesResponser.response(socketChannel, byteBuffer, responseFromCGI.getBytes(StandardCharsets.UTF_8));
    }
}
