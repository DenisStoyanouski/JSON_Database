package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CustomerMessageGsonSerializer implements JsonSerializer<Message> {

    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject messageJsonObj = new JsonObject();

        messageJsonObj.addProperty("response", src.getType());
        messageJsonObj.addProperty("reason", src.getKey());
        messageJsonObj.addProperty("value", src.getValue());

        return messageJsonObj;
    }
}
