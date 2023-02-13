package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Message.class, new CustomerMessageGsonSerializer())
            .disableHtmlEscaping()
            .create();

    private String jsonOutputMessage;

    public Session(Socket socketForClient, Database database) {
        this.socket = socketForClient;
        this.database = database;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            // reading the next client message;
            String inputMsg = input.readUTF();
            //deserialize String inputMsg to instance of Message class;
            Message request = SimpleMessageGsonDeserializer.deserialize(inputMsg);
            if ("exit".equals(request.getType())) {
                Message exitMsg = new Message();
                exitMsg.setType("OK");
                jsonOutputMessage = gson.toJson(exitMsg);
                output.writeUTF(jsonOutputMessage);
                System.out.println(jsonOutputMessage);
                ServerStarter.shotDownServer();
            } else {
                //make request to DB and get response on this request;
                Message outputMsg = getResponse(request);
                //serialize Message to JsonObject
                output.writeUTF(jsonOutputMessage);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message getResponse(Message message) throws IOException {
        //create request to DB and receive response
        Requester requester = new Requester();
        Message response = null;

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
