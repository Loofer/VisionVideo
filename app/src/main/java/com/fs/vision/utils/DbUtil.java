package com.fs.vision.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Devil on 2016/11/25.
 */
public class DbUtil {

    /**
     * * RealmResults转换成List
     *
     * @param results   RealmResults对象
     * @param isReverse true倒序，false不改动
     * @param <T>       class对象
     * @return 返回list
     */
    public static <T extends RealmObject> List converToList(RealmResults<T> results, boolean isReverse) {
        ArrayList<T> lists = new ArrayList<>();
        for (T obj : results) {
            lists.add(obj);
        }
        if (isReverse) {
            Collections.reverse(results);
        }
        return lists;
    }
}
