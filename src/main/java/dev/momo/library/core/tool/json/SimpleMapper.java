package dev.momo.library.core.tool.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.momo.library.core.log.Logger;

/**
 * Create simple method to use ObjectMapper of Jackson
 * Created by Peng on 2017/1/5.
 */
public class SimpleMapper {

    private static final String TAG = SimpleMapper.class.getSimpleName();

    private static ObjectMapper mapper;

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }


    // Object Wrapper
    public static String toString(Object object) {

        try {
            return getMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            Logger.E(TAG, e);
            return "";
        }
    }


    public static <T> T toObject(String jsonString, Class<T> c) {
        try {
            return getMapper().readValue(jsonString, c);
        } catch (JsonParseException | JsonMappingException e1) {
            Logger.E(TAG, e1);
            return null;
        } catch (IOException e2) {
            Logger.E(TAG, e2);
            return null;
        }
    }

    public static <T> List<T> toObjectList(InputStream jsonInput, Class<T> c) {
        try {
            return getMapper().readValue(jsonInput, getMapper().getTypeFactory().constructCollectionType(List.class, c));
        } catch (JsonParseException | JsonMappingException e1) {
            Logger.E(TAG, e1);
            return null;
        } catch (IOException e) {
            Logger.E(TAG, e);
            return null;
        }
    }

    public static <T> List<T> toObjectList(String jsonString, Class<T> c) {
        try {
            return getMapper().readValue(jsonString, getMapper().getTypeFactory().constructCollectionType(List.class, c));
        } catch (JsonParseException | JsonMappingException e1) {
            Logger.E(TAG, e1);
            return null;
        } catch (IOException e) {
            Logger.E(TAG, e);
            return null;
        }
    }


    public static <T> ArrayNode toArrayNode(T[] array) {
        return mapper.valueToTree(array);
    }


    /**
     * Simple node method to cover Org.JSON method
     */
    public static ObjectNode newNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    /**
     * Simple node method to cover Org.JSONArray method
     */
    public static ArrayNode newArrayNode() {
        return JsonNodeFactory.instance.arrayNode();
    }
}