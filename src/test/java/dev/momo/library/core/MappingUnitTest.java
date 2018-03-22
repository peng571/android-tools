package dev.momo.library.core;

import android.test.suitebuilder.annotation.SmallTest;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.json.JSONObject;
import org.junit.Test;

import dev.momo.library.core.tool.json.SimpleMapper;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertEquals;


/**
 * Test if simpleMapper do as same as org.JSON
 * Created by Peng on 2016/11/29.
 */
@SmallTest
public class MappingUnitTest {

    private static final String TAG = MappingUnitTest.class.getSimpleName();

    @Test
    public void valueTest() throws Exception {

        String key1 = "keyValue";
        String value1 = "xxxxxxx";

        String key2 = "isBoolean";
        Boolean value2 = false;

        String key3 = "int";
        Integer value3 = 12345;

        String key4 = "double";
        Double value4 = 45.0098;

        ObjectNode node = SimpleMapper.newJson();
        node.put(key1, value1).put(key2, value2);
        assertEquals(node.get(key1).asText(), value1);
        assertEquals(node.get(key2).asBoolean(), value2.booleanValue());
        assertEquals(node.get(key3).asInt(), value3.intValue());
        assertEquals(node.get(key4).asDouble(), value4.doubleValue());

    }

    @Test
    public void vsJsonTest() throws Exception {

        String key1 = "keyValue";
        String value1 = "xxxxxxx";

        String key2 = "isBoolean";
        boolean value2 = false;

        ObjectNode node = SimpleMapper.newJson();
        node.put(key1, value1).put(key2, value2);
        assertEquals(node.get(key1).asText(), value1);
        assertEquals(node.get(key2).asBoolean(), value2);

        JSONObject json = new JSONObject();
        json.put(key1, value1).put(key2, value2);
        assertEquals(node.get(key1).asText(), json.getString(key1));
        assertEquals(node.get(key2).asBoolean(), json.getBoolean(key2));
    }

}

