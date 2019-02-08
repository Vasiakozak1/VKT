package com.example.admin.vktargetapp.task_executors;


import android.app.AlertDialog;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.admin.vktargetapp.R;
import com.example.admin.vktargetapp.TaskDataStorage;
import com.example.admin.vktargetapp.TasksFragment;
import com.example.admin.vktargetapp.VkTargetApplication;


public final class YoutubeTaskExecutor extends BaseTaskExecutor {
    private int taskType;
    private final String clickShowMenuCode = "javascript: document.getElementsByTagName('ytm-top-menu')[0].firstElementChild.firstElementChild.click()";
    private final String clickLogInButtonCode = "javascript: setTimeout(function(){" +
            "document.getElementsByClassName('menu-content')[0].firstElementChild.click()},100)";
    private final String enterEmailCode = "javascript: setTimeout(function(){" +
            "document.getElementsByName('identifier')[0].value='qweqwe'";
    private final String enterNextButtonCode = "javascript: document.getElementById('identifierNext').click()";
    private final String enterPasswordCode = "javascript: document.getElementsByName('password')[0].value = '%s'";
    private final String clickNextButtonAfterPasswordCode = "javascript: setTimeout(function(){ " +
            "document.getElementById('passwordNext').click()},2100)";

    public YoutubeTaskExecutor(WebView webView) {
        super(webView);
        this.siteOfTaskIcon = R.drawable.youtube;
    }

    @Override
    public void ExecuteTask(final int taskType, final String url) {

        this.taskType = taskType;
        this.currentTasktToExecuteUrl = url;
        startExecuteTask();
    }

     protected void login(final String email, final String password) {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl(clickShowMenuCode);
                webView.loadUrl(clickLogInButtonCode);
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if(url.contains("identifier")) {
                            webView.loadUrl(String.format("javascript: setTimeout(function(){ " +
                                    "document.getElementsByName('identifier')[0].value='%s'},300)", email));
                            webView.loadUrl("javascript: setTimeout(function(){ " +
                                    "document.getElementById('identifierNext').click()},350)");
                        }
                        if(url.contains("pwd") || url.contains("identifier")) {
                                webView.loadUrl(String.format("javascript: setTimeout(function(){ " +
                                        "document.getElementsByName('password')[0].value='%s'},2000)", password));
                                webView.loadUrl("javascript: setTimeout(function(){ " +
                                        "document.getElementById('passwordNext').click()},2100)");
                                VkTargetApplication.saveIsLoggedInService("youtube", email);
                                determineWhatToDoInTask(); // complete task
                        }
                        }});
                }}
                );
    }

    protected void determineWhatToDoInTask() {
        final int taskType = this.taskType;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                switch (taskType) {
                    case 23:
                        subscribeChannel();
                        break;
                    case 24:
                        likeVideo();
                        break;
                    case 25:
                        dislikeVideo();
                        break;
                }
                webView.setWebViewClient(new WebViewClient());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Allow checking task
                        Button button = TaskDataStorage
                                .getInstance()
                                .getCheckTaskButton(currentTasktToExecuteUrl, siteOfTaskIcon);
                        button.setEnabled(true);
                    }
                }, 700);
            }
        });
    }

    protected void logout() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                relogin(tempLogin, tempPassword);
                            }
                        });
                        webView.loadUrl("javascript: setTimeout(function() { document.getElementsByClassName('compact-link-endpoint')[5].click(); }, 500)");
                    }
                });
                webView.loadUrl("javascript: document.getElementsByClassName('active-account-name image-overlay-text cbox')[0].click()");
            }
        });
        webView.loadUrl("https://m.youtube.com/feed/account");
    }

    private void relogin(final String email,final String password) {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.contains("identifier")) {
                    webView.loadUrl(String.format("javascript: setTimeout(function(){ " +
                            "document.getElementsByName('identifier')[0].value='%s'},300)", email));
                    webView.loadUrl("javascript: setTimeout(function(){ " +
                            "document.getElementById('identifierNext').click()},350)");
                }
                if(url.contains("pwd") || url.contains("identifier")) {
                    webView.loadUrl(String.format("javascript: setTimeout(function(){ " +
                            "document.getElementsByName('password')[0].value='%s'},2000)", password));
                    webView.loadUrl("javascript: setTimeout(function(){ " +
                            "document.getElementById('passwordNext').click()},2100)");
                    VkTargetApplication.saveIsLoggedInService("youtube", email);
                    webView.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            webView.loadUrl(currentTasktToExecuteUrl);
                            determineWhatToDoInTask(); // complete task
                        }
                    });

                }
            }});
    }

    private void startExecuteTask() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                currentLoggedInUserEmail = VkTargetApplication.getLoggedInUserNameInService("youtube");
                setDialogForLogin();

            }
        });
    }
    private void likeVideo() {
        webView.loadUrl("javascript: setTimeout(function(){ " +
                "document.getElementsByClassName('slim-video-metadata-actions')[0].firstChild.firstChild.click()}, 500)");
    }
    private void dislikeVideo() {
        webView.loadUrl("javascript: setTimeout(function(){ " +
                "document.getElementsByClassName('slim-video-metadata-actions')[0].children[1].firstChild.click()}, 500)");
    }
    private void subscribeChannel() {
        webView.loadUrl("javascript: setTimeout(function(){ " +
                "document.getElementsByClassName('c3-material-button-button')[0].click()}, 500)");
    }
}
