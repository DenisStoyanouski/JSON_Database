package client;

import com.google.gson.*;

public class SimpleRequestGsonSerializer{
    public static String serialize(Request request) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                //.setPrettyPrinting()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(request);
    }
}
