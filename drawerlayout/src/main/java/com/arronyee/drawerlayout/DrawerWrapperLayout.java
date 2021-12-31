package com.arronyee.drawerlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DrawerWrapperLayout extends FrameLayout implements DrawerSwiperLayout.OnChangeViewLevel {

    public int[] color = new int[]{android.R.color.holo_red_dark, android.R.color.holo_blue_light};

    private List<DrawerSwiperLayout> drawerSwiperLayouts = new ArrayList<>();
    private int pos = 0;

    private OnChangeViewLevel onChangeViewLevel;


    public void setOnChangeViewLevel(OnChangeViewLevel onChangeViewLevel) {
        this.onChangeViewLevel = onChangeViewLevel;
    }

    public interface OnChangeViewLevel{
        void onChange(DrawerSwiperLayout newView, DrawerSwiperLayout oldView, int direction);
    }

    public DrawerWrapperLayout(Context context) {
        super(context);
        init();
    }

    public DrawerWrapperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount()>-0){
            int width = this.getRight()-this.getLeft();
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                if (i==0){
                    child.layout(this.getLeft(),this.getTop(),this.getRight(),this.getBottom());
                }else{
                    child.layout(this.getLeft()-width, this.getTop(),this.getLeft(),this.getBottom());
                }

            }
        }
    }

    private void init(){
        addDrawerSwiperLayout();
    }

    public DrawerSwiperLayout addDrawerSwiperLayout(){
        DrawerSwiperLayout drawerSwiperLayout = new DrawerSwiperLayout(getContext());
        addChild(drawerSwiperLayout);
        bindAndRelease();
        return drawerSwiperLayout;
    }

    public void bindAndRelease(){
        if (drawerSwiperLayouts.size()>2){
            List<DrawerSwiperLayout> removeList = drawerSwiperLayouts.subList(0,drawerSwiperLayouts.size()-2);
            List<DrawerSwiperLayout> bindList = drawerSwiperLayouts.subList(drawerSwiperLayouts.size()-2,drawerSwiperLayouts.size());
            if (removeList!=null&&removeList.size()>0) {
                for (DrawerSwiperLayout drawerSwiperLayout : removeList) {
                    this.removeView(drawerSwiperLayout);
                }
            }
            drawerSwiperLayouts = bindList;
        }
        if (drawerSwiperLayouts.size()==2) {
            drawerSwiperLayouts.get(0).setUpDrag(drawerSwiperLayouts.get(1));
        }
    }

    public void getCustomTextView(DrawerSwiperLayout drawerSwiperLayout){
        Button tv = new Button(getContext());
        LayoutParams layoutParams = new LayoutParams(250, 250);
        layoutParams.gravity = Gravity.CENTER;
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setText(pos+"");
        pos++;
        drawerSwiperLayout.addView(tv);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"this is test click event",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addChild(DrawerSwiperLayout dragView) {
        dragView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dragView.setVisibility(View.INVISIBLE);
        dragView.bringToFront();
        addView(dragView);
        dragView.bringToFront();

        getCustomTextView(dragView);
        dragView.setBackgroundColor(getResources().getColor(color[pos%2]));

        setDragLocationFromParent(dragView);
        dragView.setVisibility(View.VISIBLE);
        dragView.setOnChangeViewLevel(this);
        drawerSwiperLayouts.add(dragView);
        if (drawerSwiperLayouts.size()==1){
            dragView.setBackGroudView(true);
        }
    }

    public void setDragLocationFromParent(DrawerSwiperLayout dragView){
        if (dragView!=null) {
//            int width = this.getMeasuredWidth();
//            int height = this.getMeasuredHeight();
            dragView.setLeftTopRightBottom(this.getLeft(), this.getTop(),this.getRight(),this.getBottom());
            dragView.setTranslationX(0);
            dragView.setTranslationY(0);
        }
    }

    @Override
    public void onChange(DrawerSwiperLayout drawerSwiperLayout, boolean isBackGroudView, int direction) {
        DrawerSwiperLayout view = addDrawerSwiperLayout();
        if (onChangeViewLevel!=null){
            onChangeViewLevel.onChange(view,drawerSwiperLayout,direction);
        }
    }

    public DrawerSwiperLayout getLastView(){
        if (drawerSwiperLayouts.size()>0) {
            return drawerSwiperLayouts.get(drawerSwiperLayouts.size()-1);
        }
        return null;
    }

    public DrawerSwiperLayout getBackgroundView(){
        if (drawerSwiperLayouts.size()>1) {
            return drawerSwiperLayouts.get(drawerSwiperLayouts.size() - 2);
        }else if(drawerSwiperLayouts.size()==1){
            return drawerSwiperLayouts.get(0);
        }else{
            return null;
        }
    }
}
