package com.fs.vision.db.realm;

import android.content.Context;

import com.fs.vision.entity.SearchHistroy;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * Created by WANG on 2016/7/28.
 */
public class DbSearchHis {

    /**
     * 主字段
     */
    public static final String FIELD = "field";

    /**
     * reaml对象
     */
    private static Realm myRealm;

    /**
     * 创建对象
     *
     * @param context
     * @param name
     */
    public static DbSearchHis builder(Context context, String name) {
        myRealm = Realm.getInstance(
                new RealmConfiguration.Builder(context)
                        .deleteRealmIfMigrationNeeded()
                        .name(name + ".realm")
                        .build()
        );
        return new DbSearchHis();
    }

    /**
     * 插入数据
     *
     * @param obj
     */
    public Boolean insert(SearchHistroy obj) {
        myRealm.beginTransaction();
        myRealm.copyToRealm(obj);
        myRealm.commitTransaction();
        if (findIsItemExit(obj.getField())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查找所有数据
     *
     * @return
     */
    public RealmResults<SearchHistroy> findAll() {
        return myRealm
                .where(SearchHistroy.class)
                .findAll();
    }

    /**
     * 删除所有数据
     *
     * @return true成功
     */
    public Boolean deleteAll() {
        RealmResults<SearchHistroy> result = findAll();
        if (result.size() <= 0) {
            return true;
        }
        myRealm.beginTransaction();
        boolean isResult = result.deleteAllFromRealm();
        myRealm.commitTransaction();
        return isResult;
    }

    /**
     * 查询是否存在
     *
     * @param url uri链接
     * @return
     */
    public Boolean findIsItemExit(String url) {
        RealmResults<SearchHistroy> result = findItem(url);
        if (result.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询数据是否存在
     *
     * @return
     */
    public Boolean findIsAllExit() {
        RealmResults<SearchHistroy> result = findAll();
        if (result.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询单条结果
     *
     * @param url
     * @return
     */
    public RealmResults<SearchHistroy> findItem(String url) {
        return myRealm.where(SearchHistroy.class)
                .equalTo(FIELD, url)
                .findAll();
    }

    /**
     * 删除单条结果
     *
     * @param url
     * @return true表示不存在
     */
    public Boolean deleteItem(String url) {
        RealmResults<SearchHistroy> result = findItem(url);
        if (result.size() <= 0) {
            return true;
        }
        myRealm.beginTransaction();
        boolean isResult = result.deleteAllFromRealm();
        myRealm.commitTransaction();
        return isResult;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (myRealm != null && !myRealm.isClosed()) {
            myRealm.close();
        }
    }
}
