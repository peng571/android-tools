package org.pengyr.tool.core.util;

import java.util.Random;
import java.util.UUID;

/**
 * Created by momo peng on 2018/3/21.
 */

public class RandomStringUtil {

    public static String getUUIDFileName(String fileExtension) {
        return getUUID() + "." + fileExtension;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomString(int length) {
        return getRandomString(length, "0123456789" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    public static String getRandomNumberString(int length) {
        return getRandomString(length, "0123456789");
    }

    public static String getRandomString(int length, String cell) {
        String randomString = "";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int randNum = rand.nextInt(cell.length() - 1);
            randomString += cell.charAt(randNum);
        }
        return randomString;
    }

}
