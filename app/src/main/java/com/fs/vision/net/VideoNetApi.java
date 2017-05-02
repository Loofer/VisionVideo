package com.fs.vision.net;

import android.text.TextUtils;

import com.fs.vision.entity.PageDetailInfo;
import com.fs.vision.entity.PageInfo;
import com.fs.vision.ui.MyApplication;
import com.fs.vision.utils.ACache;
import com.fs.vision.utils.LogUitl;
import com.fs.vision.utils.NetWorkUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Devil on 2016/11/25.
 */

public class VideoNetApi {
    /**
     * url编码
     */
    public static final String urlencodde = "gb2312";
    /**
     * useragent
     */
    public static String userAgent = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 4 Build/LMY48T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.89 Mobile Safari/537.36";

    /**
     * 链接超时时间
     */
    public static final int timeout = 5000;

    /**
     * 缓存时间10分
     */
    public static final int cache_time = 10 * ACache.TIME_MINUTE;
    /**
     * 主链接
     */
    private static String BaseUrl = "http://m.kankanwu.com";

    /**
     * 搜索链接
     */
    private static String BaseUrlSearch = "http://m.kankanwu.com/index.php?s=vod-search";
    public static final String typeSearch = "search";


    /**
     * 最新更新链接
     */
    private static final String BaseUrlUpdate = "http://m.kankanwu.com/";
    public static final String typeUpdate = "new.html";

    /**
     * 最新电影链接http://m.kankanwu.com/top/toplist_15.html
     */
    private static String BaseUrlFilm = "http://m.kankanwu.com/dy/index";
    //    private static String BaseUrlFilm = "http://m.kankanwu.com/top/toplist_15";
    public static final String typeFilm = "/movie/";

    /**
     * 最新电视剧链接http://m.kankanwu.com/top/toplist_16.html
     */
    private static String BaseUrlTv = "http://m.kankanwu.com/dsj/index";
    //    private static String BaseUrlTv = "http://m.kankanwu.com/top/toplist_16";
    public static final String typeTv = "/tv/";

    /**
     * 最新动漫链接http://m.kankanwu.com/top/toplist_7.html
     */
    private static String BaseUrlCartoon = "http://m.kankanwu.com/Animation/index";
    //    private static String BaseUrlCartoon = "http://m.kankanwu.com/top/toplist_7";
    public static final String typeCartoon = "/Animation/";

    /**
     * 最新综艺链接http://m.kankanwu.com/top/toplist_8.html
     */
    private static String BaseUrlVariety = "http://m.kankanwu.com/Arts/index";
    //    private static String BaseUrlVariety = "http://m.kankanwu.com/top/toplist_8";
    public static final String typeVariety = "/Arts/";

    /**
     * wifi状态
     */
    private static final int StatueWifi = 111;

    /**
     * wifi状态
     */
    private static final int StatueAll = 222;

    private File mFile;

    private String cookie = "";

    /**
     * 获得对象
     *
     * @return jsoup对象
     */
    public static VideoNetApi NewInstans(File context) {
        return new VideoNetApi(context);
    }

    /**
     * 构造
     *
     * @param context
     */
    private VideoNetApi(File context) {
        this.mFile = context;
    }

    /**
     * 获取doc对象
     *
     * @param URL    链接
     * @param method 访问的方法MethodGet MethodPost
     * @return doc对象
     */
    private Document GetDoc(String URL, Connection.Method method, String referer, Map<String, String> jsoupData) {
        Document mdoc = null;
        ACache aCache = ACache.get(mFile);
        //判断是否有缓存
        try {
            String doc;
            if ((doc = aCache.getAsString(URL)) != null && referer == null) {//如果有数据，则返回
                mdoc = Jsoup.parse(doc);
                return mdoc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //异常或者没有数据，则正常访问并存储
        //需要wifi，wifi未连接，则拦截
        if (statueNeed() == StatueWifi && !NetWorkUtil.isWifiConnected()) {
            return null;
        }
        userAgent = MyApplication.userAgent;
        try {
            if (referer != null && referer != "") {
                if (method == Connection.Method.POST && jsoupData != null) {
                    mdoc = Jsoup.connect(URL)
                            .timeout(timeout)
                            .method(method)
                            .data(jsoupData)
                            .userAgent(userAgent)
                            .header("Referer", referer)
                            .execute()
                            .parse();
                } else if (method == Connection.Method.GET) {
                    mdoc = Jsoup.connect(URL)
                            .timeout(timeout)
                            .method(method)
                            .userAgent(userAgent)
                            .header("Referer", referer)
                            .execute()
                            .parse();
                }
            } else {
                mdoc = Jsoup.connect(URL)
                        .timeout(timeout)
                        .method(method)
                        .userAgent(userAgent)
                        .execute()
                        .parse();
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUitl.LogIMsg("VideoNetApi---Jsoup:" + "  出现异常");
            return null;
        }
        //保存缓存，设置过期时间
        aCache.put(URL, mdoc.html(), cache_time);
        return mdoc;

    }

    /**
     * 设置需要的链接wifi，移动连接
     *
     * @return 所需要的状态
     */
    private int statueNeed() {
        return StatueAll;
    }

    /**
     * 判断需要的网络状态，是否链接失败
     *
     * @param doc
     * @return 如果需要的状态不可用，则返回null
     */
    private boolean judgeConState(Document doc) {
        if (doc == null) {//判断网络链接
            switch (statueNeed()) {
                case StatueWifi: {//wifi状态
                    if (!NetWorkUtil.isWifiConnected()) {
                        return true;
                    }
                }
                case StatueAll: {//所有网络
                    if (!NetWorkUtil.isNetworkConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取详情数据
     *
     * @param URL 页面链接
     * @return doc为null则网络链接有问题
     */
    public PageDetailInfo GetPageDetail(String URL) {
        Document doc = null;
        do {
            doc = GetDoc(URL, Connection.Method.GET, null, null);
            if (judgeConState(doc)) return null;
        } while (doc == null);

        String title = "";
        String cover = "";
        String smallTxt = "";
        String allTxt = "";
        String videoInfo = "";
        List<List> downList = null;
        try {
            //标题
            title = doc.select("div.vod-n-l h1").first().html();
            //封面链接
            cover = doc.select("div.vod-n-img").first().getElementsByTag("img").first().select("img").attr("src").trim();
            //剧情介绍
            smallTxt = doc.select("div.v-js.clear.yc").first().html();
            allTxt = doc.select("div.vod_content").first().html();
            //主演,更新时间
            Elements videoInfoElements = doc.select("div.vod-n-l p");
            for (Element videoInfoElement : videoInfoElements) {
                videoInfo = videoInfo + "<br>" + videoInfoElement.html().replaceAll("a", "span").replaceAll("font", "span") + "</br>";
            }
            //下载地址
            Elements downListElements = doc.select("div.play-title span a");
            //下载地址列表
            downList = new ArrayList<List>();
            for (Element downListEt : downListElements) {
                List<String> urlList = new ArrayList<>();
                if (downListEt.parent().attr("id") != "" && downListEt.parent().attr("id") != null && downListEt.parent().attr("id") != "undefind" && !downListEt.parent().attr("id").contains("xigua") && !downListEt.parent().attr("id").contains("pan")) {
                    String titleName = "";
                    titleName = MatchName(downListEt.parent().attr("id"));
                    if (titleName == "") {
                        titleName = downListEt.text();
                    }
                    urlList.add(titleName + "*");
                    //下载地址
                    Elements downUrlElements = doc.select("div.play-box#" + downListEt.parent().attr("id") + " ul.plau-ul-list li");
                    for (Element downUrlEt : downUrlElements) {
                        urlList.add(downUrlEt.select("a").attr("title") + "*" + BaseUrl + downUrlEt.select("a").attr("href"));
                    }
                    downList.add(urlList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new PageDetailInfo(URL, title, cover, smallTxt, allTxt, videoInfo, downList);
    }

    private String MatchName(String id) {
        String name = "";
        if (id.contains("tudou")) {//在线12
            name = "土豆";
        } else if (id.contains("qiyi")) {//在线9
            name = "爱奇艺";
        } else if (id.contains("mg") || id.contains("hunan")) {//在线10
            name = "芒果";
        } else if (id.contains("you")) {//在线3、在线13
            name = "优酷";
        } else if (id.contains("qq") || id.contains("vip")) {
            name = "腾讯";
        } else if (id.contains("acfun")) {//在线1
            name = "Acfun";
        } else if (id.contains("bilibili")) {//在线2
            name = "Bilibili";
        } else if (id.contains("le")) {//在线4
            name = "乐视";
        } else if (id.contains("flv")) {//在线4
            name = "搜狐";
        } else if (id.contains("pptv")) {
            name = "皮皮";
        }
        return name;
    }

    /**
     * 获取播放链接
     * iframe链接src是https://yundied.duapp.com/player2.php?vid=bADmATwCMxjwII4qNhjcdoyYN9H6UJO0O0OU~892b9753.qq;
     * 播放url获取链接"https://yundied.duapp.com/parse2.php?h5url=bADmATwCMxjwII4qNhjcdoyYN9H6UJO0O0OU.qq&tm=1480425777&sign=9caf4b3a093571cef5b016d3226db6fb&ajax=1&userlink=http%3A%2F%2Fm.kankanwu.com%2FDomestic%2Fqingyunzhidianshiju%2Fplayer-5-53.html";
     * 防盗链 refer为https://yundied.duapp.com/player2.php?vid=bADmATwCMxjwII4qNhjcdoyYN9H6UJO0O0OU~892b9753.qq
     *
     * @param Url 页面链接
     * @return 返回null则网络链接有问题
     */
    public String GetIframeUrl(String Url) {
        Document doc = null;
        do {
            doc = GetDoc(Url, Connection.Method.GET, null, null);
            if (judgeConState(doc)) return null;
        }
        while (doc == null);
        String playUrl = "";
        String iframeUrl = doc.select("div.playerbox iframe").attr("src").trim();
        if (iframeUrl.contains("yundied.duapp.com")) {
            playUrl = GetVideoPlayUrlByGet(iframeUrl, Url);
        } else if (iframeUrl.contains("lsmmr.com")) {
            playUrl = GetVideoPlayUrlByPost(iframeUrl, Url);
        }
        if (iframeUrl.contains("lsmmr.com") && TextUtils.isEmpty(playUrl)) {
            playUrl = GetVideoPlayUrlByGet(iframeUrl, Url);
        }
        return playUrl;
    }

    public String GetVideoPlayUrlByGet(String iframeUrl, String referer) {
        Document doc = null;
        do {
            doc = GetDoc(iframeUrl, Connection.Method.GET, referer, null);
            if (judgeConState(doc)) return null;
        }
        while (doc == null);
        String playUrl = "";
        Elements scriptElements = doc.select("script");
        for (Element scriptElement : scriptElements) {
            if (scriptElement != null && scriptElement.html() != null && scriptElement.html().indexOf("xhr.open(\"GET\"") >= 0) {
                String[] scripts = scriptElement.html().split(";");
                String baseUrl = "";
                String urlplay = "";
                String tm = "";
                String sign = "";
                String refer = "";
                if (scripts.length > 0) {
                    for (int i = 0; i < scripts.length; i++) {
                        String temp = scripts[i];
                       if(temp.indexOf("urlplay") > 0 && urlplay == ""){
                           urlplay = temp.split("'")[1].trim();
                       }else if(temp.indexOf("tm") > 0 && tm == ""){
                           tm = temp.split("'")[1].trim();
                       }else if(temp.indexOf("sign") > 0 && sign == ""){
                           sign = temp.split("'")[1].trim();
                        }else if(temp.indexOf("refer") > 0 && refer == ""){
                           refer = temp.split("'")[1].trim();
                        }else if(temp.indexOf("\'&tm=\'+tm+\'&sign=\'+sign+\'&ajax=1&userlink=\'+refer") > 0 && baseUrl == ""){
                           temp = temp.split(",")[1];
                           baseUrl = iframeUrl.substring(0,iframeUrl.lastIndexOf("/")+1)+temp.split("'")[1];
                       }
                    }
                    String videoPlayhtml = baseUrl+urlplay + "&tm=" + tm + "&sign=" + sign + "&ajax=1&userlink=" + refer;
                    do {
                        doc = GetDoc(videoPlayhtml, Connection.Method.GET, iframeUrl, null);
                        if (judgeConState(doc)) return null;
                    }
                    while (doc == null);
                }
                playUrl = doc.select("body").html();
            }
        }
        return playUrl;
    }

    public String GetVideoPlayUrlByPost(String iframeUrl, String referer) {
        Document doc = null;
        do {
            doc = GetDoc(iframeUrl, Connection.Method.GET, referer, null);
            if (judgeConState(doc)) return null;
        } while (doc == null);
        String playUrl = "";
        Elements scriptElemnts = doc.select("script");
        for (Element scriptElemnt : scriptElemnts) {
            if (scriptElemnt != null && scriptElemnt.html() != null && scriptElemnt.html().indexOf("{\"") >= 0) {
                System.out.println("scriptElemnt---" + scriptElemnt.html());
                int start = scriptElemnt.html().indexOf("{\"");
                int end = scriptElemnt.html().indexOf("\"}");
                String dataStr = scriptElemnt.html().substring(start, end + 2);//组装成新的Json数据
                try {
                    JSONObject dataJson = new JSONObject(dataStr);
                    Map<String, String> dataMap = new HashMap<>();
                    dataMap.put("id", dataJson.getString("id"));
                    dataMap.put("type", dataJson.getString("type"));
                    dataMap.put("siteuser", dataJson.getString("siteuser"));
                    dataMap.put("md5", dataJson.getString("md5"));
                    String videoPlayhtml = iframeUrl.substring(0, iframeUrl.indexOf("?")).replace(dataJson.getString("type"), "url");
                    System.out.println("videoPlayhtml---" + videoPlayhtml);
                    do {
                        doc = GetDoc(videoPlayhtml, Connection.Method.POST, iframeUrl, dataMap);
                        if (judgeConState(doc)) return null;
                    }
                    while (doc == null);
                    playUrl = new JSONObject(doc.select("body").html()).getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return playUrl;
    }

    /**
     * 获取信息封面链接
     *
     * @param URL 页面链接
     * @return 返回null则网络链接有问题
     */
    public String GetPageDetailCover(String URL) {
        Document doc = null;
        do {
            doc = GetDoc(URL, Connection.Method.GET, null, null);
            if (judgeConState(doc)) return null;
        }
        while (doc == null);
        //封面链接
        String cover = doc.select("div.pic").first().getElementsByTag("img").first().select("img").attr("src").trim();
        return cover;
    }

    /**
     * 获取搜索结果数据
     * http://www.dytt.com/search.asp?page=4&searchword=%C0%CF&searchtype=-1
     *
     * @param kw 关键字
     * @return 返回null则网络链接有问题
     */
    public List<PageInfo> GetPageSearch(int page, String kw) {
        Document doc = null;
        do {
            doc = GetDoc(DealUrlSearch(page, kw), Connection.Method.POST, null, null);
            if (judgeConState(doc)) return null;
        }
        while (doc == null);
        List<PageInfo> pageInfos = dealPage(doc, true);
        return pageInfos;
    }

    /**
     * 获取展示数据
     *
     * @param Page 页码
     * @param type 类型
     * @return 返回null则网络链接有问题
     */
    public List<PageInfo> GetPage(int Page, String type) {
        Document doc = null;
        do {
            doc = GetDoc(dealUrl(Page, type), Connection.Method.GET, null, null);
            if (judgeConState(doc)) return null;
        }
        while (doc == null);
        List<PageInfo> pageInfos = dealPage(doc, false);
        return pageInfos;
    }

    /**
     * 处理展示,搜索数据
     *
     * @param doc doc对象
     * @return 解析失败list长度0
     */
    private List<PageInfo> dealPage(Document doc, boolean needPage) {
        ArrayList<PageInfo> pageInfos = new ArrayList<>();
        String page = "";
        try {
            Elements movielist = doc.select("ul.list_tab_img li");
            for (Element et : movielist) {
                String score = et.select(".score").first().html();
                String actor = et.select("p").first().html();
                String ahref = BaseUrl + et.select("h2 a").first().select("a").attr("href");
                String title = et.select("h2 a").first().html();
                String type = et.select(".title").first().html();
                String imgUrl = et.select("img.loading").attr("src");

                pageInfos.add(new PageInfo(score, type, actor, "", ahref, title, "", "", page, imgUrl));
                //Log.i("msg", "  页码：" + page + "  评分：" + score + "  网址:" + ahref + "  标题:" + title + "  类型:" + type + "  主演:" + actor + "  更新时间:" + updatetime + "  年代:" + year + "  地区:" + addr);
            }
            //防止npe
            if (needPage && pageInfos.size() > 0) {
                page = doc.select("span.fbbnt input").first().nextElementSibling().attr("onclick").substring(13, 14);
                pageInfos.get(0).setPage(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pageInfos;
    }

    /**
     * 处理链接格式,展示页面
     *
     * @param mPage 页面
     * @return 处理后的结果
     */
    private String dealUrl(int mPage, String type) {
        //处理页码
        String html = "_______1.html";
        //处理链接
        switch (type) {
            case typeFilm:
                return BaseUrlFilm + "_" + mPage + html;
            case typeTv:
                return BaseUrlTv + "_" + mPage + html;
            case typeCartoon:
                return BaseUrlCartoon + "_" + mPage + html;
            case typeVariety:
                return BaseUrlVariety + "_" + mPage + html;
        }
        return BaseUrl;
    }

    /**
     * 获取搜索数据
     *
     * @param page       页码
     * @param searchWord 搜索关键字
     * @return 返回null则网络链接有问题
     */
    public List<PageInfo> SearchPage(int page, String searchWord) {
        Document doc = null;
        do {
            doc = GetDoc(DealUrlSearch(page, searchWord), Connection.Method.GET, null, null);
            if (judgeConState(doc)) return null;
        }
        while (doc == null);
        List<PageInfo> pageInfos = dealSearchPage(doc, false);
        return pageInfos;
    }

    /**
     * 处理展示,搜索数据
     *
     * @param doc doc对象
     * @return 解析失败list长度0
     */
    private List<PageInfo> dealSearchPage(Document doc, boolean needPage) {
        ArrayList<PageInfo> pageInfos = new ArrayList<>();
        String page = "";
        try {
            Elements movielist = doc.select("ul.new_tab_img li");
            for (Element et : movielist) {
                String score = "";
                String actor = "";
                String ahref = BaseUrl + et.select("div.list_info h2").first().select("a").attr("href");
                String title = et.select("div.list_info h2").first().select("a").first().html();
                String type = "";
                String imgUrl = et.select("img.loading").attr("src");

                pageInfos.add(new PageInfo(score, type, actor, "", ahref, title, "", "", page, imgUrl));
                //Log.i("msg", "  页码：" + page + "  评分：" + score + "  网址:" + ahref + "  标题:" + title + "  类型:" + type + "  主演:" + actor + "  更新时间:" + updatetime + "  年代:" + year + "  地区:" + addr);
            }
            //防止npe
            if (needPage && pageInfos.size() > 0) {
                page = doc.select("span.fbbnt input").first().nextElementSibling().attr("onclick").substring(13, 14);
                pageInfos.get(0).setPage(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pageInfos;
    }

    /**
     * 处理链接格式，搜索页面
     * http://www.dytt.com/search.asp?page=4&searchword=%C0%CF&searchtype=-1
     *
     * @param kw 关键词（不需要编码转换）
     * @return 处理后的结果
     */
    private String DealUrlSearch(int page, String kw) {
        String kdKey = "-wd-";
        String pageKey = "-p-";
        String html = ".html";
        return BaseUrlSearch + kdKey + kw + pageKey + page + html;
    }
}
