package com.fs.vision.ui;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.fs.vision.R;
import com.fs.vision.utils.ThemeHelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by Devil on 2016/11/25.
 */

public class MyApplication extends Application implements ThemeUtils.switchColor {

    public static Context mAppContext;
    public static String userAgent;


    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        ThemeUtils.setSwitchColor(this);
        //在自己的Application中添加如下代码
//        LeakCanary.install(this);
        getUserAgent();
        //检查更新配置
//        checkUpadte();
    }

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return context.getResources().getColor(colorId);
        }
        String theme = getTheme(context);
        if (theme != null) {
            colorId = getThemeColorId(context, colorId, theme);
        }
        return context.getResources().getColor(colorId);
    }

    @Override
    public int replaceColor(Context context, @ColorInt int originColor) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return originColor;
        }
        String theme = getTheme(context);
        int colorId = -1;

        if (theme != null) {
            colorId = getThemeColor(context, originColor, theme);
        }
        return colorId != -1 ? getResources().getColor(colorId) : originColor;
    }

    private String getTheme(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return "red";
        }
        return null;
    }

    private
    @ColorRes
    int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case R.color.colorPrimary:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case R.color.colorPrimaryDark:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case R.color.colorAccent:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return colorId;
    }

    private
    @ColorRes
    int getThemeColor(Context context, int color, String theme) {
        switch (color) {
            case 0xfffb7299:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case 0xffb85671:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case 0x99f0486c:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return -1;
    }

    private void getUserAgent() {
        WebView webview = new WebView(this);
        webview.layout(0, 0, 0, 0);
        WebSettings settings = webview.getSettings();
        userAgent = settings.getUserAgentString();
    }

    /**
     * 配置检查更新
     */
    private void checkUpadte() {
        // UpdateConfig为全局配置。当在其他页面中。使用UpdateBuilder进行检查更新时。
        // 对于没传的参数，会默认使用UpdateConfig中的全局配置
//        UpdateConfig.getConfig()
//                // 必填：数据更新接口
//                .url(UpdateConstant.updateUrl)
//                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
//                .jsonParser(new UpdateParser() {
//                    @Override
//                    public Update parse(String response) {
//                        Update update;
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            // 此处模拟一个Update对象
//                            update = new Update(response);
//                            // 此apk包的更新时间
//                            update.setUpdateTime(object.getLong("updated_at"));
//                            // 此apk包的下载地址
//                            update.setUpdateUrl(object.getString("installUrl"));
//                            // 此apk包的版本号
//                            update.setVersionCode(object.getInt("build"));
//                            // 此apk包的版本名称
//                            update.setVersionName(object.getString("versionShort"));
//                            // 此apk包的更新内容
//                            update.setUpdateContent(object.getString("changelog"));
//                            // 此apk包是否为强制更新
//                            update.setForced(false);
//                            // 是否忽略此次版本更新
//                            update.setIgnore(false);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            return null;
//                        }
//                        return update;
//                    }
//                });
    }

    public static Context getContext() {
        return mAppContext;
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取deviceinfo
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
