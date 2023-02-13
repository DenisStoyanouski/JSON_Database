package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;



public class Client {
    @Parameter(names="-t", description = "the type of the request")
    String request = null;

    @Parameter(names="-k", description = "the index of the cell")
    String index = null;

    @Parameter(names="-v",variableArity = true, description = "the value to save in the database")
    String value = null;



    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) {
        Client main = new Client();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);


        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String requestJson = new Gson().toJson(new Request(main.request, main.index, main.value));
            System.out.println(requestJson);
            output.writeUTF(requestJson);
            if (!"exit".equals(main.request)) {
                String receivedMsg = input.readUTF();
                System.out.printf("Received: %s%n",receivedMsg);
            }
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
