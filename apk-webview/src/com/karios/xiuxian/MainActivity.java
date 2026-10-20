package com.karios.xiuxian;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowFileAccessFromFileURLs(true);
        s.setAllowUniversalAccessFromFileURLs(true);
        s.setDefaultTextEncodingName("utf-8");
        s.setTextZoom(100);
        s.setBuiltInZoomControls(false);

        web.setBackgroundColor(0xFF0B0E0C);
        web.setWebViewClient(new WebViewClient());
        web.addJavascriptInterface(new Bridge(this), "NativeStore");

        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }

    /** 供 JS 调用的本地存储桥，保证存档不丢 */
    public static class Bridge {
        private final SharedPreferences sp;

        Bridge(Context c) {
            sp = c.getSharedPreferences("xiuxian_store", Context.MODE_PRIVATE);
        }

        @JavascriptInterface
        public String get(String key) {
            try {
                return sp.getString(key, "");
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public void set(String key, String value) {
            try {
                sp.edit().putString(key, value).apply();
            } catch (Exception ignored) {
            }
        }
    }
}