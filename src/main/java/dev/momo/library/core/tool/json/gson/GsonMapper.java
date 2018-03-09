//package dev.momo.library.core.tool.json;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
///**
// * Create simple method to use ObjectMapper of Jackson
// * Created by Peng on 2017/1/5.
// */
//public class GsonMapper {
//
//    private static final String TAG = GsonMapper.class.getSimpleName();
//
//    private static GsonMapper mapper;
//    private static Gson gson;
//
//    private GsonMapper() {
//        gson = new Gson();
//    }
//
//    public static GsonMapper getMapper() {
//        if (mapper == null) {
//            mapper = new GsonMapper();
//        }
//        return mapper;
//    }
//
//
//    // Object Wrapper
//    public static String toString(Object object) {
//        return gson.toJson(object);
//    }
//
//
//    public static <T> T toObject(String jsonString) {
//        Type type = new TypeToken<T>() {}.getType();
//        return gson.fromJson(jsonString, type);
//    }
//
//    public static <T> List<T> toObjectList(String jsonString) {
//        Type type = new TypeToken<List<T>>() {}.getType();
//        return gson.fromJson(jsonString, type);
//    }
//
//
//}
