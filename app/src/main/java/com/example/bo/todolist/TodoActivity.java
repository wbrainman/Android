package com.example.bo.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {
    private  static final String TAG = "Todo";
    private static List<TodoItem> mTodoItemList = new ArrayList<>();
    private TodoItemAdapter mAdapter = null;
    //private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Log.d(TAG, "TodoActivity onCreate");

        initTodoList();
        initRecyView();
        dbInit();

        setListClickListener();
        setListRemoveListener();
        setListMoveListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TodoActivity onDestroy");
        mTodoItemList.clear();

        //debug
        print_mTodoItemList();
        print_TodoDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "TodoActivity onStart");

        //debug
        print_mTodoItemList();
        print_TodoDatabase();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "TodoActivity onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "TodoActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "TodoActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "TodoActivity onStop");
        //dbDeleteAll();
    }

    public void initRecyView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(null == mAdapter) {
            mAdapter = new TodoItemAdapter(mTodoItemList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        }
        else {
            Log.d(TAG, "onCreate adapter is not null");
        }

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

    /***********************************************************************************************/
    /***                                Dialog                                                 ***/
    /***********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                showInputDiag();
                break;
            case R.id.just_do_not_know:
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
                String mInput = editText.getText().toString();
                int id = 0;
                TodoItem todoItem = new TodoItem(mInput);

                mTodoItemList.add(todoItem);
                id = mTodoItemList.size() - 1;
                mAdapter.notifyItemInserted(id);

                /* db insert here */
                Log.d(TAG, "onOptionsItemSelected mInput: "+ mInput + " size:" + mTodoItemList.size());
                dbAdd(id, mInput);
                /*db update*/
                dbUpdate();

                //debug
                print_mTodoItemList();
                print_TodoDatabase();
            }
        });
        inputDiag.show();
    }

    /***********************************************************************************************/
    /***                                set listener                                           ***/
    /***********************************************************************************************/
    private void setListClickListener() {
        mAdapter.setItemClickListener(new TodoItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "setListClickListener click item : "+ position + "text:"+mTodoItemList.get(position).getName() );
                String data = mTodoItemList.get(position).getName();
                Intent intent = new Intent(TodoActivity.this, TodoDetailActivity.class);
                intent.putExtra("extra_data",data);
                startActivity(intent);
                }
        });
    }

    private void setListRemoveListener() {
        mAdapter.setItemRemoveListener(new TodoItemAdapter.onItemRemoveListener() {
            @Override
            public void onItemRemove(int position) {
                Log.d(TAG, "setListRemoveListener remove item : "+ position);
                int index = position;
                dbDelete(index);
                dbUpdate();

                //debug
                print_mTodoItemList();
                print_TodoDatabase();
            }
        });
    }

    private void setListMoveListener() {
        mAdapter.setItemMoveListener(new TodoItemAdapter.onItemMoveListener() {
            @Override
            public void onItemMove(int fromPos, int toPos) {
                Log.d(TAG, "setListMoveListener move from "+ fromPos + " to " + toPos);
                //debug
                print_mTodoItemList();
                print_TodoDatabase();
            }
        });
    }

    /***********************************************************************************************/
    /***                                save date by write/read file                            ***/
    /***********************************************************************************************
    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     **********************************************************************************************/

/******************************************************************************************************/
/***                                SQLite                                                          ***/
/******************************************************************************************************
    public void dbInit() {
        dbHelper = new MyDatabaseHelper(this, "TodoList.db", null, 2);
        dbHelper.getWritableDatabase();
    }

    public void dbInsert(String item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("item", mInput);
        db.insert("Todo", null, values);
        values.clear();
    }

    public void dbUpdate() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("item", "ttt");
        db.update("Todo", values, "item = ?", new String[] {"wwwer"});
        values.clear();
    }

    public void dbDelete() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.delete("Todo","item = ?", new String[] {"ttt"});
        values.clear();
    }
 ***************************************************************************************************/

/***************************************************************************************************/
/***                                LitePal                                                     ***/
/***************************************************************************************************/
public void dbInit() {
    LitePal.getDatabase();
    int count = LitePal.count(TodoDatabase.class);
    Log.d(TAG, "dbInit, count is " + count);

    List<TodoDatabase> todoDatabases = dbQuery();
    for(TodoDatabase todoDatabase:todoDatabases) {
        TodoItem todoItem = new TodoItem(todoDatabase.getItem());
        mTodoItemList.add(todoItem);
        mAdapter.notifyItemInserted(mTodoItemList.size() - 1);
    }
}

public void dbAdd(int index, String item) {
    TodoDatabase todoDatabase = new TodoDatabase();
    todoDatabase.setItem(item);
    todoDatabase.setIndex(index);
    todoDatabase.save();
}

public void dbAddAll() {
    for(TodoItem todoItem : mTodoItemList) {
        Log.d(TAG, "dbAddAll, item is " + todoItem.getName());
        TodoDatabase todoDatabase = new TodoDatabase();
        todoDatabase.setItem(todoItem.getName());
        todoDatabase.save();
    }
}
public void dbUpdate() {

    List<TodoDatabase> todoDatabases = dbQuery();
    int index = 0;
    for(TodoDatabase todoDatabase : todoDatabases) {
        todoDatabase.setIndex(index);
        todoDatabase.save();
        index ++;
    }
}

public void dbSetToDefault() {
    TodoDatabase todoDatabase = new TodoDatabase();
    todoDatabase.setToDefault("item");
    todoDatabase.updateAll();
}

public void dbDelete(int index) {
    Log.d(TAG, "dbDelete");
    TodoDatabase todoDatabase = new TodoDatabase();
    String condition = String.valueOf(index);
    LitePal.deleteAll(TodoDatabase.class, "index = ?", condition);
}

public void dbDeleteAll() {
    Log.d(TAG, "dbDelete all");
    LitePal.deleteAll(TodoDatabase.class);
}

public List<TodoDatabase> dbQuery() {
    Log.d(TAG, "dbQuery");
    List<TodoDatabase> todoDatabases = LitePal.findAll(TodoDatabase.class);
    for(TodoDatabase todoDatabase:todoDatabases) {
        //Log.d(TAG,"dbQuery tododatabase id is " + todoDatabase.getIndex());
        //Log.d(TAG,"dbQuery tododatabase item is " + todoDatabase.getItem());
    }
    return todoDatabases;
}


/***********************************************************************************************/
/***                                debug info                                              ***/
/***********************************************************************************************/
private void print_mTodoItemList() {
    for(TodoItem todoItem : mTodoItemList) {
        Log.d(TAG, "print_mTodoItemList item is " + todoItem.getName());
    }
}

private void print_TodoDatabase() {
    List<TodoDatabase> todoDatabases = dbQuery();
    for(TodoDatabase todoDatabase:todoDatabases) {
        TodoItem todoItem = new TodoItem(todoDatabase.getItem());
        Log.d(TAG, "print_TodoDatabase todoItem " + todoItem.getName());
    }
}

//end
}
