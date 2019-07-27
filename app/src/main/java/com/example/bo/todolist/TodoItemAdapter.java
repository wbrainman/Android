package com.example.bo.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {
    private  static final String TAG = "Todo";
    private List<TodoItem> mTodoList;

    /*** interface ***/
    private onItemClickListener mOnItemClickListener = null;

    public static interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private onItemRemoveListener mOnItemRemoveListener = null;

    public static interface onItemRemoveListener {
        void onItemRemove(int position);
    }

    public void setItemRemoveListener(onItemRemoveListener listener) {
        mOnItemRemoveListener = listener;
    }

    /*** view holder ***/
    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView itemName;
        ImageView imgAlarm;
        ImageView imgAdd;
        public ViewHolder(View view) {
            super(view);
            itemView = view;
            itemName = (TextView) view.findViewById(R.id.item_name);
            imgAlarm = (ImageView) view.findViewById(R.id.image_alarm);
            imgAdd = (ImageView) view.findViewById(R.id.image_add);
        }
    }

    public TodoItemAdapter(List<TodoItem> todoList) {
       mTodoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder i: "+ i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        //holder.itemView.setOnClickListener(this);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "item view Click v.getTag : "+ v.getTag());
            }
        });
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "add image Click  getAdapterPosition: "+ holder.getAdapterPosition());
                if(mOnItemClickListener != null) {
                    //mOnItemClickListener.onItemClick(v, (int)v.getTag());
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder pos: "+ position);
        TodoItem todoItem = mTodoList.get(position);
        holder.itemName.setText(todoItem.getName());
        holder.imgAlarm.setImageResource(R.drawable.alarm);
        holder.imgAdd.setImageResource(R.drawable.add);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    public List<TodoItem> getDataList() {
        return mTodoList;
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        mTodoList.remove(position);

        Log.d(TAG, " Adapter removeItem pos: "+ position);
        if(mOnItemRemoveListener != null) {
            mOnItemRemoveListener.onItemRemove(position);
        }
    }

    /*** end ***/
}
