package com.example.danieltruong.voice_memos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddMemo extends AppCompatActivity {

    static final public String MYPREFS = "myprefs";
    static final public String PREF_URL = "restore_url";
    static final public String WEBPAGE_NOTHING = "about:blank";
    static final public String MY_WEBPAGE = "https://users.soe.ucsc.edu/~dustinadams/CMPS121/assignment3/www/index.html";
    static final public String LOG_TAG = "addMemo_webview";
    public static final int RequestPermissionCode = 1;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;

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

                    // open file.ser
                    try{
                        File f = new File(getFilesDir(), "file.ser");
                        FileInputStream inputStream = new FileInputStream(f);
                        ObjectInputStream objectStream = new ObjectInputStream(inputStream);
                        String j = null;

                        try{
                            j = (String) objectStream.readObject();

                            // read in the String to an int
                            // add one to that int
                            int num_memos = Integer.valueOf(j) + 1;

                            // convert back to a String (this is filename for your audio file)
                            j = String.valueOf(num_memos);
                            Log.d("ser_file_number: ", j);

                            try{
                                FileOutputStream outputStream = new FileOutputStream(f);
                                ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
                                objectOutput.writeObject(j);
                                objectOutput.close();
                                outputStream.close();
                            }catch(IOException exception){

                            }
                        }catch(ClassNotFoundException c){
                            c.printStackTrace();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                        Toast.makeText(AddMemo.this, "No Memos, creating first...", Toast.LENGTH_LONG).show();
                        Log.d("add_status: ", "no memos, creating first");
                        try{
                            File f = new File(getFilesDir(), "file.ser");
                            FileOutputStream outputStream = new FileOutputStream(f);
                            ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
                            String j = "1";
                            objectOutput.writeObject(j);
                            objectOutput.close();
                            outputStream.close();
                        }catch(IOException io){
                            io.printStackTrace();
                        }
                    }
                    // audio file's extension is .3gp
                    // Start recording and save recording as "Audio Recording 'j'"
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
                    // Must use startActivity() instead of finish() for listView to update in MainActivity onResume()
                    Intent i = new Intent(AddMemo.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        public void MediaRecorderRead(){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(AudioSavePathInDevice);
        }

        // TODO: REQUEST PERMISSIONS FROM USER
        private void requestPermission(){
            ActivityCompat.requestPermissions(AddMemo.this, new
                    String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
        }

        // TODO: CALLBACK METHOD
        public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
            switch(requestCode){
                case RequestPermissionCode:
                    if (grantResults.length> 0) {
                        boolean StoragePermission = grantResults[0] ==
                                PackageManager.PERMISSION_GRANTED;
                        boolean RecordPermission = grantResults[1] ==
                                PackageManager.PERMISSION_GRANTED;

                        if (StoragePermission && RecordPermission) {
                            Toast.makeText(AddMemo.this, "Permission Granted",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddMemo.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }

        public boolean checkPermission(){
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }
}
