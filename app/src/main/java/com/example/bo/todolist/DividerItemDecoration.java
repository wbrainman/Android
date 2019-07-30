package com.example.bo.todolist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private  static final String TAG = "Todo";
    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    public static final int[] ATTRS = new int[]{
            android.R.attr.listDivider//啥意思？
    };

    public DividerItemDecoration(Context context, int orientation) {
        this.mContext = context;
        //啥意思？
        final TypedArray ta = context.obtainStyledAttributes(ATTRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if(orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(mOrientation == HORIZONTAL) {
            //Log.d(TAG, "onDraw ");
            drawHorizontalLine(c, parent, state);
        }
        else {
            drawVerticalLine(c, parent, state);
        }
    }

    public void drawHorizontalLine(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int chlidCount = parent.getChildCount();

        for (int i = 0; i < chlidCount; i ++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            //Log.d(TAG, "drawHorizontalLine: top " + top + " bottom " + bottom + "left " + left + "right" + right);
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    public void drawVerticalLine(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(mOrientation == HORIZONTAL) {
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
            //outRect.set(0,0,0,1);
            //Log.d(TAG, "getItemOffsets: getIntrinsicHeight = " + mDivider.getIntrinsicHeight());
        }
        else {
            outRect.set(0,0,mDivider.getIntrinsicHeight(),0);
        }
    }
}
