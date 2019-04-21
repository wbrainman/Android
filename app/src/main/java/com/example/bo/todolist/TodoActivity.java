package com.example.bo.todolist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {
    private  static final String TAG = "Todo";
    private static List<TodoItem> mTodoItemList = new ArrayList<>();
    private TodoItemAdapter mAdapter = null;
    private String mInput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Log.d(TAG, "Todo created" + this);
        initTodoList();
        initRecyView();
    }

    public void initRecyView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(null == mAdapter) {
            Log.d(TAG, "onCreate adapter is null");
            mAdapter = new TodoItemAdapter(mTodoItemList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        }
        else {
            Log.d(TAG, "onCreate adapter is not null");
        }
        mInput = new String();
        setListClickListener();

        RecyItemTouchHelperCallback recyItemTouchHelperCallback = new RecyItemTouchHelperCallback(mAdapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



    }

    private void initTodoList() {
        Log.d(TAG, "initTodoList: size = " + mTodoItemList.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                /*
                Toast.makeText(this,"You clicked add",Toast.LENGTH_SHORT).show();
                int i = mTodoItemList.size();
                TodoItem todoItem = new TodoItem("todo" + i);
                mTodoItemList.add(todoItem);
                mAdapter.notifyItemInserted(mTodoItemList.size() - 1);
                */
                showInputDiag();
                break;
            case R.id.just_do_not_know:
                //Toast.makeText(this,"You clicked don't know",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void showInputDiag() {
        final EditText editText = new EditText(TodoActivity.this);
        AlertDialog.Builder inputDiag = new AlertDialog.Builder(TodoActivity.this);
        inputDiag.setTitle("What fuck you want to do ?");
        inputDiag.setView(editText);
        inputDiag.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(TodoActivity.this,editText.getText().toString(),Toast.LENGTH_SHORT).show();
                mInput = editText.getText().toString();
                Log.d(TAG, "onOptionsItemSelected mInput: "+ mInput);
                TodoItem todoItem = new TodoItem(mInput);
                mTodoItemList.add(todoItem);
                mAdapter.notifyItemInserted(mTodoItemList.size() - 1);
            }
        });
        inputDiag.show();
    }

    private void setListClickListener() {
        mAdapter.setItemClickListener(new TodoItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "setListClickListener click item : "+ position);
            }
        });
    }
}
