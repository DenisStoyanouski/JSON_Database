package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Client {
    @Parameter(names="-t", description = "the type of the request")
    String type = null;

    @Parameter(names="-k", description = "the index of the cell")
    String key = null;

    @Parameter(names="-v",variableArity = true, description = "the value to save in the database")
    String value = null;

    @Parameter(names="-in",variableArity = true, description = "the value to save in the database")
    String fileName = null;

    /*private static final Path path = Paths.get("." + File.separator + "JSON Database" + File.separator + "task" +
            File.separator + "src" + File.separator + "client" + File.separator + "data");*/

    private static final Path path = Paths.get("." + File.separator + "src" + File.separator + "client" + File.separator + "data");
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) {
        Client arguments  = new Client();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String requestJson;
            if (arguments.fileName != null) {
                requestJson = deserializeRequestFromFile(arguments.fileName);
            } else {
                Message message = new Message(arguments.type, arguments.key, arguments.value);
                requestJson = SimpleMessageGsonSerializer.serialize(message);
            }
            System.out.printf("Sent: %s%n", requestJson);
            output.writeUTF(requestJson);
            String receivedMsg = input.readUTF();
            System.out.printf("Received: %s%n",receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String deserializeRequestFromFile(String fileName) {
        StringBuilder request = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(path.toString() + File.separator + fileName));
            while (scanner.hasNextLine()) {
                request.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return request.toString();
    }
}
