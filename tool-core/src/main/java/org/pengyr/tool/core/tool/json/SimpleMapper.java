package org.pengyr.tool.core.tool.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

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


    public static <T> T toObject(Class<T> type, String jsonString) {
        return getMapper().fromJson(jsonString, type);
    }

    //    public static <T> List<T> toObjectList(Class<List<T>> type, String jsonString) {
    //        return getMapper().fromJson(jsonString, type);
    //    }
}
