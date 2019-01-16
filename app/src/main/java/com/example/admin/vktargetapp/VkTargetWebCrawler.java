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
    private final String clickShowFinishedTasksButtonCode =
            "document.getElementsByClassName('good')[%d].click()";

    private final int finishedTasksButtonIndex = 1;

    private static VkTargetWebCrawler instance = null;
    private WebView webView;

    private  VkTargetWebCrawler(){
        webView = MainActivity.WebCrawlerView;

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new VkTargetJSInterface
                (VkTargetApplication.getCurrentActivity()),"HtmlViewer");
    }

    public static VkTargetWebCrawler getInstance(){
        if(instance == null) {
            instance = new VkTargetWebCrawler();
        }
        return instance;
    }

    public void CheckIsLoginNeeded(){
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.loadUrl(
                                "javascript:HtmlViewer.setLoginNeeded" +
                                        "(document.getElementsByClassName('vkt-panel__user-email').length);");
                    }
                });
                webView.loadUrl(Constants.VkTargetUrl);
            }
        });
    }

    public void RetrieveApiKey(final String email, final String password) {
        webView.post(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    public void RetrieveTasks() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.loadUrl(
                                "javascript:setTimeout(" +
                                        "function(){" +
                                        "HtmlViewer.parseAvailableTasks" +
                                        "(''+document.body.innerHTML+'')}" +
                                        ",500)");
                    }
                });
                webView.loadUrl(Constants.VkTargetUrl + Constants.MyTasksUrl);
            }
        });

    }

    public void RetrieveFinishedTasks() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.loadUrl(
                                String.format("javascript: " + clickShowFinishedTasksButtonCode, finishedTasksButtonIndex)
                        );
                        webView.loadUrl(
                                "javascript:setTimeout(" +
                                        "function(){" +
                                        "HtmlViewer.parseDoneTasks" +
                                        "(''+document.body.innerHTML+'')}" +
                                        ",500)"
                        );
                    }
                });
                webView.loadUrl(Constants.VkTargetUrl + Constants.MyTasksUrl);
            }
        });
    }

    public void LogOut() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.loadUrl(
                                "javascript: document.getElementsByClassName('vkt-panel__user-logout left')[1].click()"
                        );
                    }
                });
                webView.loadUrl(Constants.VkTargetUrl);
            }
        });
    }
}
