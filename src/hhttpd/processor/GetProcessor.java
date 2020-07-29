package hhttpd.processor;

import hhttpd.resolver.RequestLine;
import hhttpd.responser.BinaryResponser;
import hhttpd.responser.BytesResponser;
import hhttpd.responser.NotFoundResponser;
import hhttpd.responser.TextResponser;
import hhttpd.utils.ContentTypeDict;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class GetProcessor {
    /**
     * Handle GET requests with static resources
     *
     * @param webRoot root path of static resources
     * @param socketChannel socketChannel with client
     * @param byteBuffer byteBuffer for NIO
     * @param message HTTP request message
     * @param requestLine HTTP request line
     * @throws IOException response may fail
     */
    public static void handle(String webRoot,
                              SocketChannel socketChannel,
                              ByteBuffer byteBuffer,
                              String message,
                              RequestLine requestLine) throws IOException {
        File localFile = Path.of(webRoot, requestLine.getUri().getPath()).toFile();

        if (localFile.isDirectory()) {
            // request index.html as default
            localFile = Path.of(webRoot, requestLine.getUri().getPath(), "index.html").toFile();
        }

        if (localFile.isFile()) {
            String contentType = ContentTypeDict.getContentType(localFile.getName());
            // if file exists
            if (contentType.startsWith("text/")) {
                // text file
                TextResponser.response(socketChannel, byteBuffer, localFile);
            } else {
                // binary data file
                BinaryResponser.response(socketChannel, byteBuffer, localFile);
            }
        } else {
            // if file not exists
            NotFoundResponser.response(socketChannel, byteBuffer);
        }
    }

    /**
     * Handle request with CGI
     * @param commands commands to execute
     * @param cgiPath path of cgi file
     * @param socketChannel socketChannel with client
     * @param byteBuffer byteBuffer for NIO
     * @param message HTTP request message
     * @param requestLine HTTP request line
     * @throws IOException stream exception
     * @throws InterruptedException cgi execution may be interrupted
     */
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
                "QUERY_STRING=" + requestLine.getUri().getQuery(),
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
