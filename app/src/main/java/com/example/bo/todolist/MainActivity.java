package com.example.bo.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  static final String TAG = "Todo";
    private List<MainCategory> mainCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Main created");
        initCategory();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MainCategoryAdapter adapter = new MainCategoryAdapter(mainCategories);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MainCategoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //todo:这里使用intent传递数据更好吗？
                if(mainCategories.get(postion).getName() == "Todo") {
                    Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(view.getContext(),"you clicked view " + mainCategories.get(postion).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity onStop");
    }


    private void initCategory() {
        MainCategory thingsTodo = new MainCategory("Todo");
        mainCategories.add(thingsTodo);
        MainCategory target = new MainCategory("Target");
        mainCategories.add(target);
        MainCategory habit = new MainCategory("Habit");
        mainCategories.add(habit);
    }
}
