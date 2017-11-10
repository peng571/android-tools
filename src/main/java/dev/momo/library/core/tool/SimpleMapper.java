package dev.momo.library.core.tool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create simple method to use ObjectMapper of Jackson
 * Created by Peng on 2017/1/5.
 */
public class SimpleMapper {

    private static final String TAG = SimpleMapper.class.getSimpleName();

    private static SimpleMapper mapper;
    private static Gson gson;

    private SimpleMapper() {
        gson = new Gson();
    }

    public static SimpleMapper getMapper() {
        if (mapper == null) {
            mapper = new SimpleMapper();
        }
        return mapper;
    }


    // Object Wrapper
    public static String toString(Object object) {
        return gson.toJson(object);
    }


    public static <T> T toObject(String jsonString) {
        Type type = new TypeToken<T>() {}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static <T> List<T> toObjectList(String jsonString) {
        Type type = new TypeToken<List<T>>() {}.getType();
        return gson.fromJson(jsonString, type);
    }


}
