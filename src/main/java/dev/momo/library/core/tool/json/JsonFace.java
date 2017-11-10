package dev.momo.library.core.tool.json;

/**
 * (TBD)
 *
 * Created by Peng on 2017/11/10.
 */
public interface JsonFace {

    <T> String toString(T t);

    <T> T toObject(String s);

    <T> T toObjectList(String s);

}
