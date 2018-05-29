package com.example.danieltruong.voice_memos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity {

    public JSONObject jsonObject = null;
    public JSONArray jsonArray = null;
    private static final String TAG = "JSON_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume(){
        super.onResume();
        ListView list = findViewById(R.id.memo_list_view);
        TextView text = findViewById(R.id.no_memos);
        text.setVisibility(View.INVISIBLE);

        jsonObject = null;

        try{
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream inputStream = new FileInputStream(f);
            ObjectInputStream objectStream = new ObjectInputStream(inputStream);
            String j = null;
        }catch(IOException e){
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_memo:
                startActivity(new Intent(MainActivity.this, AddMemo.class));
                break;
                default:
                    super.onOptionsItemSelected(item);
                    break;
        }
        return true;
    }
}
