package com.example.admin.vktargetapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CustomWebViewFragment extends WebViewFragment {
    private final String clickSignInBtnCode =
            "document.getElementsByClassName('login')[0].click();";
    private final String enterEmailCode =
            "document.getElementsByName('username')[0].value='junglehunter2707@gmail.com';";
    private final String enterPasswordCode =
            "document.getElementsByName('password')[0].value='Wryw339v!';";
    private final String clickLoginBtnCode =
            "document.getElementsByClassName('login')[2].click();";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @JavascriptInterface
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View result= super.onCreateView(inflater, container, savedInstanceState);
            final WebView webView = getWebView();
            webView.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new VkTargetJSInterface
                    (CustomWebViewFragment.this.getActivity()),"HtmlViewer");
            // настройка масштабирования
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    try {
                        SignIn(webView);
                    }
                    catch (IOException e){
                        Toast.makeText(getActivity()
                                , "An error occured when connection to the internet" + e.getMessage()
                                , Toast.LENGTH_SHORT);
                    }
                }
            });
            webView.loadUrl("https://vktarget.ru/api2/");
            return result;
    }
    public void SignIn(WebView webView) throws IOException {
        webView.loadUrl(
                "javascript:(function(){" +
                        clickSignInBtnCode  +
                        enterEmailCode +
                        enterPasswordCode +
                        clickLoginBtnCode +
                        " }())");
        webView.loadUrl(
                "javascript:HtmlViewer.getApiKey" +
                        "(''+document.getElementsByClassName('key__value')[0].value+'');");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
