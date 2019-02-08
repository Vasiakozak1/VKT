package com.example.admin.vktargetapp;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskData;

import java.io.IOException;

public class VkTargetWebCrawler {
    private final String enterEmailCode =
            "document.getElementsByName('email')[0].value='%s';";
    private final String enterPasswordCode =
            "document.getElementsByName('password')[0].value='%s';";
    private final String clickLoginBtnCode =
            "document.getElementsByClassName('default__small__btn')[0].click();";
    private final String clickShowFinishedTasksButtonCode =
            "document.getElementsByClassName('good')[%d].click()";
    private final String setApiKeyCode = "javascript:setTimeout(" +
            "function(){ HtmlViewer.setApiKey(document.getElementsByClassName('vkt-panel__user-data').length, ''+document.getElementsByClassName('key__value')[0].parentNode.parentElement.outerHTML+'', ''+document.getElementsByClassName('key__value')[0].value+'') },800)";

    private final String pushCheckButtonCode = "var taskToCheck;\n" +
            "var tasks = document.getElementsByClassName('row tb__row ')\n" +
            "for(var taskIndex = 0; taskIndex < tasks.length; taskIndex++) {\n" +
            "\tfor(var taskChildrenIndex = 0; taskChildrenIndex < tasks[taskIndex].childElementCount; taskChildrenIndex++)\n" +
            "    {\n" +
            "        var taskChild = tasks[taskIndex].children[taskChildrenIndex];\n" +
            "        if(taskChild.className == 'col-12 col-md link__col flex-middle')\n" +
            "        {\n" +
            "            var link = taskChild.firstElementChild.children[1];\n" +
            "            if(link.getAttribute('href') === '%s' && taskIndex == %d) {\n" +
            "                taskToCheck = tasks[taskIndex];\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n" +
            "for(var taskChildrenIndex = 0; taskChildrenIndex < taskToCheck.childElementCount; taskChildrenIndex++){\n" +
            "    var taskChild = taskToCheck.children[taskChildrenIndex];\n" +
            "    if(taskChild.className === 'col-12 col-sm check__col flex-middle ') {\n" +
            "        var checkButton = taskChild.getElementsByTagName('button')[0];\n" +
            "        checkButton.click();\n" +
            "    }\n" +
            "}";

    private final String chooseAccountCode = "var chooseAcc = function() {\n" +
            "    var accountDivs = document.getElementsByTagName('ul')[0].children;\n" +
            "    for(var accountsIndex = 0; accountsIndex < accountDivs.length; accountsIndex++) {\n" +
            "        var accountNameElement = accountDivs[accountsIndex].firstChild.firstChild.firstChild.children[1].children[1];\n" +
            "        var accountName = accountNameElement.innerText;\n" +
            "        if(accountName === '%s') {\n" +
            "            accountDivs[accountsIndex].firstChild.click();\n" +
            "            break;\n" +
            "        }\n" +
            "    }\n" +
            "}\n" +
            "chooseAcc();";
    private final String checkIsTaskDoneCode = "javascript: setTimeout(function(){ " +
            "HtmlViewer.checkIsTaskDone(document.getElementsByClassName('default__small__btn white check__btn success').length);}, %d)";

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
                                        "(document.getElementsByClassName('user__data col').length);");
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
                        if(url.equals("https://vktarget.ru/"))
                        {
                            webView.loadUrl(
                                    "javascript:(function(){" +
                                            String.format(enterEmailCode, email) +
                                            String.format(enterPasswordCode, password) +
                                            clickLoginBtnCode +
                                            " }())");
                        }
                        else if(url.equals("https://vktarget.ru/list/")) {
                            webView.loadUrl("javascript: window.location.replace('https://vktarget.ru/api2')");
                            webView.setWebViewClient(new WebViewClient(){
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);
                                    webView.loadUrl(setApiKeyCode);
                                    webView.setWebViewClient(new WebViewClient());
                                }
                            });
                        }
                    }
                });
                webView.loadUrl(Constants.VkTargetUrl);
            }
        });
    }

    public void GenerateApiKey() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(
                        "javascript: document.getElementById('new_key_btn').click()"
                );
                webView.reload();
                webView.loadUrl(setApiKeyCode);
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
                                        ",600)");
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
                                String.format("javascript: " + "document.getElementsByClassName('tabs__ul')[0].children[1].click()")
                        );
                        webView.loadUrl(
                                "javascript:setTimeout(" +
                                        "function(){" +
                                        "HtmlViewer.parseDoneTasks" +
                                        "(''+document.body.innerHTML+'')}" +
                                        ",2500)"
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
                                "javascript: document.getElementsByClassName('setting__menu')[0].children[9].click()"
                        );
                    }
                });
                webView.loadUrl(Constants.VkTargetUrl);
            }
        });
    }

    public void CheckTask(final String taskHref, final int taskIndex, final TaskData webViewForCheckingTask) {


        final String formattedPushButtonCode = "var taskToCheck;" +
                "var tasks = document.getElementsByClassName('row tb__row ');" +
                "for(var taskIndex = 0; taskIndex < tasks.length; taskIndex++) { " +
                "for(var taskChildrenIndex = 0; taskChildrenIndex < tasks[taskIndex].childElementCount; taskChildrenIndex++)" +
                "    {" +
                "        var taskChild = tasks[taskIndex].children[taskChildrenIndex];" +
                "        if(taskChild.className == 'col-12 col-md link__col flex-middle')" +
                "        {" +
                "            var link = taskChild.firstElementChild.children[1];" +
                "            if(link.getAttribute('href') === '" + taskHref + "' && taskIndex == " + taskIndex + ") {" +
                "                taskToCheck = tasks[taskIndex];" +
                "            }" +
                "        }" +
                "    }" +
                "}" +
                "for(var taskChildrenIndex = 0; taskChildrenIndex < taskToCheck.childElementCount; taskChildrenIndex++){" +
                "    var taskChild = taskToCheck.children[taskChildrenIndex];" +
                "    if(taskChild.className === 'col-12 col-sm check__col flex-middle ') {" +
                "        var checkButton = taskChild.getElementsByTagName('button')[0];" +
                "        checkButton.click();" +
                "    }" +
                "}";

        webViewForCheckingTask.webView.post(new Runnable() {
            @Override
            public void run() {

                webViewForCheckingTask.webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                webViewForCheckingTask.webView.setWebViewClient(new WebViewClient(){
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        webViewForCheckingTask.webView.setWebViewClient(new WebViewClient());


                                    }
                                });
                                String formattedChooseAccountCode = String.format(chooseAccountCode, webViewForCheckingTask.email);
                                webViewForCheckingTask.webView.loadUrl(
                                        "javascript: " + formattedChooseAccountCode
                                );
                                checkTaskSuccessful(webViewForCheckingTask.webView);
                            }

                        });
                        webViewForCheckingTask.webView.loadUrl(
                                "javascript: var pushBtn = function(){ " + formattedPushButtonCode + " }; pushBtn();"
                        );
                        checkTaskSuccessful(webViewForCheckingTask.webView);

                    }
                });
                webViewForCheckingTask.webView.loadUrl(Constants.VkTargetUrl + Constants.MyTasksUrl);
            }
        });
    }

    private void checkTaskSuccessful(WebView webView) {
        int firstWaitTimeUntilCheckTask = 2500;
        int secondWaitUntilCheckTask = 4000;
        String formattedIsTaskDoneCodeFirstTry = String.format(checkIsTaskDoneCode, firstWaitTimeUntilCheckTask);
        String formattedIsTaskDoneCodeSecondTry = String.format(checkIsTaskDoneCode, secondWaitUntilCheckTask);
        webView.loadUrl(formattedIsTaskDoneCodeFirstTry);
        webView.loadUrl(formattedIsTaskDoneCodeSecondTry);
    }
}
