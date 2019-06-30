package com.example.bo.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TodoDetailActivity extends AppCompatActivity {
    private  static final String TAG = "Todo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        Intent intent = getIntent();
        String data = intent.getStringExtra("extra_data");
        Log.d(TAG, "TodoDetailActivity onCreate getText : "+ data);
        TextView textView = (TextView)findViewById(R.id.item_name);
        textView.setText(data);
    }
}
