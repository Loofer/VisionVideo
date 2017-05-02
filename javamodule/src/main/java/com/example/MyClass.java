package com.example;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static String userAgent = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 4 Build/LMY48T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.89 Mobile Safari/537.36";
    private static Document mdoc;
    private  String ss = "https://yundied.duapp.com/parse3.php?h5url=QA0m9TECSxXwlIOqVhEcEo1YT9W6cJ9UPQO0O0OO0O0O.acku";
    private static String allUrl = "https://yundied.duapp.com/parse3.php?h5url=MAjmQT0CNxjwQIyqMhgcO0O0OoO0O0OY.acfun&tm=1480517067&sign=e5acc15200c0c67f1df8ef9109788f75&ajax=1&userlink=http%3A%2F%2Fm.kankanwu.com%2FHorror%2Fmengguiaiqinggushi%2Fplayer-0-1.html";
    private static String dd = "https://yundied.duapp.com/player3.php?vid=WAEm9TECUxXwhIOqVhFclo5YT9U6RJVUMgO0O0OO0O0O~88fa1dc1.youk";
    private static String cc = "http://m.kankanwu.com/Action/guhuozi1zhirenyuiao/player-2-0.html";

    public static  void main(String[] args) {
        long tm = System.currentTimeMillis();

//        testContainsAll();
        GetIframeUrl(cc);
//        TestJsoup();
        //a233350d72c4eb632aeaf5167a451ab4
//        System.out.println("VideoNetApi---Jsoup:" + mdoc.html());
    }

    private static void testContainsAll() {
        User user = new User();
        user.setId("1");
        user.setName("小明");
        user.setPassword("111111");
        user.setSex("1");

        User user1 = new User();
        user1.setId("2");
        user1.setName("小明");
        user1.setPassword("111111");
        user1.setSex("1");

        List<User> userA = new ArrayList<>();
        userA.add(user);
//        userA.add(user1);

        List<User> userB= new ArrayList<>();
       userB.add(user1);

        System.out.println("-----"+userA.containsAll(userB)+"-----"+userB.containsAll(userA));
    }

    private static void TestJsoup() {
//                try {
//            BrowserVersion browserVersion = new BrowserVersion("Mozilla","0",userAgent,0);
//            WebClient wc = new WebClient(browserVersion);
//            URL link = new URL(dd);
//            WebRequest request = new WebRequest(link);
//            request.setCharset("UTF-8");
//            request.setAdditionalHeader("Referer", cc);//设置请求报文头里的refer字段
//            request.setAdditionalHeader("User-Agent", userAgent);
//            wc.setJavaScriptTimeout(10000);//设置JS执行的超时时间
//            wc.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常
//            wc.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常
//            wc.getOptions().setTimeout(30000);//设置“浏览器”的请求超时时间
//            wc.getOptions().setCssEnabled(false);//是否启用CSS
//            wc.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
//            wc.waitForBackgroundJavaScript(30000);//设置JS后台等待执行时间
//            wc.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
//            HtmlPage page = wc.getPage(request);
//            System.out.println("---------------page--------------\n"+page.asXml()+"\n---------------page--------------");
//            DomNodeList<HtmlElement> htmlElement = page.getElementById("player").getElementsByTagName("source");
//            System.out.println("---------------playUrl--------------\n"+htmlElement.get(0).getAttribute("src").replaceAll("[`]","")+"\n---------------playUrl--------------");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
//            String refer = URLEncoder.encode(cc,"utf-8");
//            String sign = "";
//            String getVideUrl = ss+"&tm="+tm+"&sign="+sign+"&ajax=1&userlink="+refer;
            mdoc = Jsoup.connect(allUrl)
                    .timeout(10*1000)
                    .method(Connection.Method.GET)
                    .userAgent(userAgent)
                    .header("Referer",dd)
                    .header("Host","https://yundied.duapp.com")
                    .execute()
                    .parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetIframeUrl(String Url) {
        System.out.println("----GetVideoPlayUrl = " + Url);
        Document doc = null;
        do {
            doc = GetDoc(Url, Connection.Method.GET, null);
        }
        while (doc == null);
        String playUrl = "";
        String iframeUrl = doc.select("div.playerbox iframe").attr("src").trim();
        playUrl = GetVideoPlayUrl(iframeUrl, Url);
        return playUrl;
    }

    public static String GetVideoPlayUrl(String iframeUrl, String referer) {
        Document doc = null;
        do {
            doc = GetDoc(iframeUrl, Connection.Method.GET, referer);
        }
        while (doc == null);
        String playUrl = "";
        Element scriptElemnt = doc.select("script:eq(1)").first();
        String[] scripts = scriptElemnt.html().split(";");
        System.out.println("--GetVideoPlayUrl--length = " + scripts.length);
        String baseUrl = iframeUrl.split("~")[0].replace("player", "parse").replace("vid", "h5url") + "." + iframeUrl.split("~")[1].split("[.]")[1];
        if (scripts.length > 3) {
            String tm = scripts[1].split("'")[1].trim();
            String sign = scripts[2].split("'")[1].trim();
            String refer = scripts[3].split("'")[1].trim();
            System.out.println("--GetVideoPlayUrl--tm = " + tm + "--sign = " + sign + "----refer = " + refer);
            String videoPlayhtml = baseUrl + "&tm=" + tm + "&sign=" + sign + "&ajax=1&userlink=" + refer;
            System.out.println("--videoPlayhtml--" + videoPlayhtml);
            do {
                doc = GetDoc(videoPlayhtml, Connection.Method.GET, iframeUrl);
            }
            while (doc == null);
        }
        playUrl = doc.html();
        System.out.println("--playUrl--" + playUrl);
        return playUrl;
    }

    /**
     * 获取doc对象
     *
     * @param URL    链接
     * @param method 访问的方法MethodGet MethodPost
     * @return doc对象
     */
    private static Document GetDoc(String URL, Connection.Method method, String referer) {
        Document mdoc;
        try {
            if (referer != null && referer != "") {
                System.out.println("VideoNetApi---referer=" + referer);
                Map<String,String> headers = new HashMap<>();
                headers.put("Referer",referer);
                headers.put("Host","https://yundied.duapp.com");
                headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                headers.put("Accept-Encoding","gzip, deflate, br");
                headers.put("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                headers.put("Cookie","BAEID=8E3281D16ADF084082BAC7A0083FD457");
                mdoc = Jsoup.connect(URL)
                        .timeout(10*1000)
                        .method(method)
                        .userAgent(userAgent)
                        .header("Referer", referer)
//                        .header("Host", "https://yundied.duapp.com")
                        .execute()
                        .parse();
            } else {
                mdoc = Jsoup.connect(URL)
                        .timeout(10*1000)
                        .method(method)
                        .userAgent(userAgent)
                        .execute()
                        .parse();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("VideoNetApi---Jsoup:" + "  出现异常");
            return null;
        }
        return mdoc;
    }
}
