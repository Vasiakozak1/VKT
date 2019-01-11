package com.example.admin.vktargetapp;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;

public class VkTargetWebCrawler {
    private final String clickSignInBtnCode =
            "document.getElementsByClassName('login')[0].click();";
    private final String enterEmailCode =
            "document.getElementsByName('username')[0].value='%s';";
    private final String enterPasswordCode =
            "document.getElementsByName('password')[0].value='%s';";
    private final String clickLoginBtnCode =
            "document.getElementsByClassName('login')[2].click();";



    private static VkTargetWebCrawler instance = null;
    private WebView webView;

    private  VkTargetWebCrawler(){
        webView = MainActivity.WebCrawlerView;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new VkTargetJSInterface
                (VkTargetApplication.getCurrentActivity()),"HtmlViewer");
    }

    public static VkTargetWebCrawler getInstance(){
        if(instance == null) {
            instance = new VkTargetWebCrawler();
        }
        return instance;
    }

    public void RetrieveApiKey(final String email, final String password) {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl(
                        "javascript:(function(){" +
                                clickSignInBtnCode  +
                                String.format(enterEmailCode, email) +
                                String.format(enterPasswordCode, password) +
                                clickLoginBtnCode +
                                " }())");
                webView.loadUrl(
                        "javascript:HtmlViewer.setApiKey" +
                                "(''+document.getElementsByClassName('key__value')[0].value+'');");
            }
        });
        webView.loadUrl(Constants.VkTargetUrl + Constants.ApiPageUrl);
    }

    public void RetrieveTasks() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl(
                        "javascript:HtmlViewer.parseAvailableTasks" +
                                "(''+document.body.innerHTML+'');");
            }
        });
        webView.loadUrl(Constants.VkTargetUrl + Constants.MyTasksUrl);
    }
}
