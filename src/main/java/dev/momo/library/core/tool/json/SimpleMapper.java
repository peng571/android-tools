package dev.momo.library.core.tool.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Create simple method to use ObjectMapper of Jackson
 * Created by Peng on 2017/1/5.
 */
public class SimpleMapper {

    private static final String TAG = SimpleMapper.class.getSimpleName();

    private static Gson gson;

    private static final String EMPTY_JSON_ARRAY = "[]";
    private static final String EMPTY_JSON = "{}";

    public static Gson getMapper() {
        if (gson == null) {
            gson = new GsonBuilder()
                    //                    .excludeFieldsWithoutExposeAnnotation()
                    .excludeFieldsWithModifiers(Modifier.STATIC | Modifier.FINAL)
                    .setPrettyPrinting()
                    .create();
        }
        return gson;
    }


    // Object Wrapper
    public static String toString(Object object) {
        if (object == null) return EMPTY_JSON;
        return getMapper().toJson(object);
    }


    public static <T> T toObject(String jsonString, Class<T> type) {
        return getMapper().fromJson(jsonString, type);
    }

    public static <T> List<T> toList(String jsonString, Class<T> type) {
        Type listType = new TypeToken<List<T>>(){}.getType();
        return getMapper().fromJson(jsonString, listType);
    }


    public static JsonObject newJson() {
        return new JsonObject();
    }

    public static JsonObject newJson(String jsonString) {
        JsonParser parser = new JsonParser();
        return parser.parse(jsonString).getAsJsonObject();
    }

    public static JsonArray newArray() {
        return new JsonArray();
    }

    // TODO upda
    public static JsonArray newArray(String[] jsons) {
        JsonArray array = newArray();
        for (String s : jsons) {
            array.add(s);
        }
        return array;
    }

}