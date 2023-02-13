package server;

import com.google.gson.Gson;

public class SimpleMessageGsonDeserializer {
    public static Message deserialize(String jsonMessage) {
        Message message = new Gson().fromJson(jsonMessage, Message.class);
        return message;
    }
}
