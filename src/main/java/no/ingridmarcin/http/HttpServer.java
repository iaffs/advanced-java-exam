package no.ingridmarcin.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static no.ingridmarcin.http.HttpMessage.readHeaders;

public class HttpServer {

    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final ServerSocket serverSocket;
    private String fileLocation;
    private final HttpController defaultController;
    private final Map<String, HttpController> controllers = new HashMap<>();


    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        defaultController = new FileHttpController(this);
        controllers.put("/echo", new EchoHttpController());
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.startServer();
    }

    public void startServer() {
        new Thread(() -> run()).start();
        logger.info("Started on http://localhost:{}", getPort());
    }



    private void run() {
        while (true) {
            // this is a request line made by the client
            try (Socket socket = serverSocket.accept()) {

                String requestLine = HttpMessage.readLine(socket.getInputStream());
                if(requestLine.isBlank()) continue;

                logger.debug("Handling request: {}", requestLine);
                Map<String, String> headers = readHeaders(socket.getInputStream());
                String body = HttpMessage.readBody(headers, socket.getInputStream());

                String requestAction = requestLine.split(" ")[0];
                String requestTarget = requestLine.split(" ")[1];
                int questionPos = requestTarget.indexOf("?");
                String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);
                Map<String, String> queryParameters = parseQueryParameters(requestTarget);

                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestAction, requestPath, queryParameters, body, socket.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> parseQueryParameters(String requestTarget) {
        int questionPos = requestTarget.indexOf("?");
        if(questionPos > 0) {
            String query = requestTarget.substring(questionPos+1);
            return parseQueryString(query);
        }
        return new HashMap<>();
    }

    public static Map<String, String> parseQueryString(String query) {
        Map<String,String> parameters = new HashMap<>();
        for(String parameter : query.split("&")) {
            int equalsPos = parameter.indexOf("=");
            String paramName = parameter.substring(0, equalsPos);
            String paramValue = parameter.substring(equalsPos+1);
            parameters.put(paramName, paramValue);
        }
        return parameters;
    }

    public int getPort(){
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void addController(String requestPath, HttpController controller) {
        controllers.put(requestPath, controller);
    }
}
