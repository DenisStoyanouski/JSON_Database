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
            // reading the next client message
            String inputMsg = input.readUTF();
            //deserialize String inputMsg to instance of Message class
            Message request = SimpleMessageGsonDeserializer.deserialize(inputMsg);
            System.out.println(request.toString());

            if ("exit".equals(request.getType())) {
                ServerStarter.shotDownServer();
            } else {
                String outputMsg = getResponse(request);
                output.writeUTF(outputMsg);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getResponse(Message message) throws IOException {

        Requester requester = new Requester();
        String response = null;

        switch (message.getType()) {
            case "get" : requester.setRequest(new GetRequest(database, Integer.parseInt(message.getKey())));
                         response = requester.executeRequest();
                         break;
            case "set" : requester.setRequest(new SetRequest(database, Integer.parseInt(message.getKey()), message.getValue()));
                        response = requester.executeRequest();
                        break;
            case "delete" : requester.setRequest(new DeleteRequest(database, Integer.parseInt(message.getKey())));
                            response = requester.executeRequest();
                            break;
            default: break;

        }
        return response;
    }
}
