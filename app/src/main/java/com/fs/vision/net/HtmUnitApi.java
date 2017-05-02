package com.fs.vision.net;

/**
 * Created by Devil on 2016/11/30.
 */

public class HtmUnitApi {

    public static String getVideoPlayUrl(String playHtml, String referer){
        String playUrl = "";
//        try {
//            BrowserVersion browserVersion = new BrowserVersion("Mozilla","0", MyApplication.userAgent,0);
//            WebClient wc = new WebClient(browserVersion);
//            URL link = new URL(playHtml);
//            WebRequest request = new WebRequest(link);
//            request.setCharset("UTF-8");
//            request.setAdditionalHeader("Referer", referer);//设置请求报文头里的refer字段
//            request.setAdditionalHeader("User-Agent", MyApplication.userAgent);
//            wc.setJavaScriptTimeout(10000);//设置JS执行的超时时间
//            wc.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常
//            wc.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常
//            wc.getOptions().setTimeout(30000);//设置“浏览器”的请求超时时间
//            wc.getOptions().setCssEnabled(false);//是否启用CSS
//            wc.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
//            wc.waitForBackgroundJavaScript(30000);//设置JS后台等待执行时间
//            wc.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
//            HtmlPage page = wc.getPage(request);
////            System.out.println("---------------page--------------\n"+page.asXml()+"\n---------------page--------------");
//            DomNodeList<HtmlElement> htmlElement = page.getElementById("player").getElementsByTagName("source");
//            playUrl = htmlElement.get(0).getAttribute("src").replaceAll("[`]","");
////            System.out.println("---------------playUrl--------------\n"+htmlElement.get(0).getAttribute("src").replaceAll("[`]","")+"\n---------------playUrl--------------");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return playUrl;
    }
}
