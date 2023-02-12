package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class ServerStarter {
    private static final int PORT = 23456;

    private static final String SERVER_ADDRESS = "localhost";

    public static void startServer() {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            System.out.println("Server started!");
            while (true) {
                Session session = new Session(server.accept());
                session.start();
                // it does not block this thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Session extends Thread {
    private final Socket socket;
    private final Database database = new Database();

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            // reading and process the next client message
            String inputMsg = input.readUTF();
            System.out.printf("Received: %s%n", inputMsg);
            String[] request = parseRequest(inputMsg);
            // send answer to client
            String outputMsg = getResponse(request);
            output.writeUTF(outputMsg); // resend it to the client
            System.out.printf("Sent: %s%n", outputMsg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] parseRequest(String inputMsg) {
        return inputMsg.split("\\s+", 3);
    }

    private String getResponse(String[] request) throws IOException {
        String response = null;
        String method = null;
        int index = 0;
        String value = null;
        for (int i = 0; i < request.length; i++) {
            if (i == 0) {
                method = request[0];
            }
            if (i == 1) {
                index = Integer.parseInt(request[1]);
            }
            if (i == 2) {
                value = request[2];
            }
        }
        Requester requester = new Requester();

        switch (method) {
            case "get" : requester.setRequest(new GetRequest(database, index));
                        response = requester.executeRequest();
                         break;
            case "set" : requester.setRequest(new SetRequest(database, index, value));
                        response = requester.executeRequest();
                        break;
            case "delete" : requester.setRequest(new DeleteRequest(database, index));
                            response = requester.executeRequest();
                            break;
            case "exit" : System.exit(0);

            default: break;

        }
        return response;
    }
}
