package com.example.danieltruong.voice_memos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AddMemo extends AppCompatActivity {

    static final public String MYPREFS = "myprefs";
    static final public String PREF_URL = "restore_url";
    static final public String WEBPAGE_NOTHING = "about:blank";
    static final public String MY_WEBPAGE = "https://users.soe.ucsc.edu/~dustinadams/CMPS121/assignment3/www/index.html";
    static final public String LOG_TAG = "addMemo_webview";

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        myWebView = (WebView) findViewById(R.id.add_memo_webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Bind the js interface
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        myWebView.loadUrl(MY_WEBPAGE);
    }

    public class JavaScriptInterface{
        Context mContext;

        JavaScriptInterface(Context c){
            mContext = c;
        }

        @JavascriptInterface
        public void record(){
            Log.i(LOG_TAG, "I am in the js (record) call");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: METHOD CODE HERE
                    Toast.makeText(AddMemo.this, "Recording", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void stop(){
            Log.i(LOG_TAG, "I am in the js (stop) call");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: METHOD CODE HERE
                    Toast.makeText(AddMemo.this, "Stopping", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void play(){
            Log.i(LOG_TAG, "I am in the js (play) call");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: METHOD CODE HERE
                    Toast.makeText(AddMemo.this, "Playing", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void stoprec(){
            Log.i(LOG_TAG, "I am in the js (stoprec) call");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: METHOD CODE HERE
                    Toast.makeText(AddMemo.this, "Stopping recording", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void exit(){
            Log.i(LOG_TAG, "I am in the js (exit) call");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: METHOD CODE HERE
                    Toast.makeText(AddMemo.this, "Exiting activity", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }
}
