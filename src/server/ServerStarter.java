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
    private static Session session;

    private static boolean isServerOn = true;

    public static void startServer() {

        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            System.out.println("Server started!");
            Database database = new Database();
            while (isServerOn) {
                session = new Session(server.accept(), database);
                session.start();
                session.join(300);
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void shotDownServer() {
        isServerOn = false;

    }
}

class Session extends Thread {
    private final Socket socket;

    private final Database database;

    public Session(Socket socketForClient, Database database) {
        this.socket = socketForClient;
        this.database = database;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            // reading and process the next client message
            String inputMsg = input.readUTF();
            if (inputMsg.contains("exit")) {
                ServerStarter.shotDownServer();
            } else {
                String[] request = parseRequest(inputMsg);
                String outputMsg = getResponse(request);
                output.writeUTF(outputMsg);
            }
            //System.out.printf("Received: %s%n", inputMsg);
            //System.out.println(Arrays.toString(request));
            // send answer to client
             // resend it to the client
            //System.out.printf("Sent: %s%n", outputMsg);
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
            default: break;

        }
        return response;
    }
}
