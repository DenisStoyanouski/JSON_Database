package client;

import com.google.gson.*;

public class SimpleMessageGsonSerializer{
    public static String serialize(Message message) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                //.setPrettyPrinting()
                //.serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(message);
    }
}
