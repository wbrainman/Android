package com.example.bo.todolist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> implements View.OnClickListener {
    private  static final String TAG = "MainActivity";
    private List<MainCategory> mCategoryList;
    private onItemClickListener mOnItemClickListener = null;

    public static interface onItemClickListener {
        void onItemClick(View view, int postion);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView name;
        public ViewHolder(View view) {
            super(view);
            itemView = view;
            name = (TextView) view.findViewById(R.id.item_name);
            //Log.d(TAG, "text size is " + name.getTextSize());
        }
    }
    public MainCategoryAdapter(List<MainCategory> mainCategoryList) {
        mCategoryList = mainCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = holder.getAdapterPosition();
                MainCategory mainCategory = mCategoryList.get(postion);
                Toast.makeText(v.getContext(),"you clicked view " + mainCategory.getName(), Toast.LENGTH_SHORT).show();

            }
        }); */
        Log.d(TAG, "set this into itemView click listener");
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MainCategory mainCategory = mCategoryList.get(i);
        viewHolder.name.setText(mainCategory.getName());
        Log.d(TAG, "set tag into itemView");
        viewHolder.itemView.setTag(i);
    }

    @Override
    public void onClick(View v) {
       if(mOnItemClickListener != null) {
           Log.d(TAG, "onClick called");
          mOnItemClickListener.onItemClick(v, (int)v.getTag());
       }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
