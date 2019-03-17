package com.example.bo.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> implements View.OnClickListener {
    private  static final String TAG = "Todo";
    private List<TodoItem> mTodoList;
    private onItemClickListener mOnItemClickListener = null;

    public static interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        public ViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_name);
        }
    }

    public TodoItemAdapter(List<TodoItem> todoList) {
       mTodoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoItem todoItem = mTodoList.get(position);
        holder.itemName.setText(todoItem.getName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    @Override
    public void onClick(View v) {
        //Log.d(TAG, "setListClickListener click item : "+ v.getTag());
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }
}
