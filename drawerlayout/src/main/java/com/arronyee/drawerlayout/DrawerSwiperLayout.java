package com.arronyee.drawerlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class DrawerSwiperLayout extends FrameLayout {

    public static final int DIRECTION_leftRight = 11;
    public static final int DIRECTION_rightLeft = 22;
    public static final int DIRECTION_topBottom = 33;
    public static final int DIRECTION_bottomTop = 44;
    private boolean leftRight,rightLeft,topBottom,bottomTop;
    private float lastX,lastY;
    private float moveX,moveY;
    private int distanceX,distanceY;
    private final float scrollPer = 0.85f;
    private int touchSolp ;
    private boolean isAnim = false;
    private boolean isBackGroudView =false;
    private OnChangeViewLevel onChangeViewLevel;
    private DrawerSwiperLayout dragView;

    private boolean isGetDown = false;

    private View itemView;

    private boolean closeDrag = false;

    public boolean isCloseDrag() {
        return closeDrag;
    }

    public void setCloseDrag(boolean closeDrag) {
        this.closeDrag = closeDrag;
    }

    public interface OnChangeViewLevel{
        void onChange(DrawerSwiperLayout drawerSwiperLayout, boolean isBackGroudView,int direction);
    }

    public DrawerSwiperLayout(Context context) {
        super(context);
        init();
    }

    public DrawerSwiperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerSwiperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(){
        touchSolp = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragView==null||isAnim || !isBackGroudView || closeDrag){
            return super.onTouchEvent(event);
        }
        Log.d("eventmo","this is onTouchEvent action is "+event.getAction()+" xy is ---"+event.getX()+"     --------"  +event.getY());
        float x = event.getX();
        float y = event.getY();

        if (event.getAction()==MotionEvent.ACTION_DOWN){
            isGetDown = true;
            release();

        }else if(event.getAction()==MotionEvent.ACTION_MOVE&&lastY!=0&&lastX!=0){
            if (x==lastX&&y==lastY){
                return true;
            }
            if (!isGetDown){
                return false;
            }
            int diffX = (int) (x-lastX);
            int diffy = (int) (y-lastY);
            diffX *= scrollPer;
            diffy *= scrollPer;
            distanceX = diffX;
            distanceY = diffy;
            if (leftRight||rightLeft||topBottom||bottomTop){
                drag();
            }else {
                if(Math.abs(diffX) > Math.abs(diffy)) {
                    if (distanceX > 0) {
                        leftRight = true;
                    } else {
                        rightLeft = true;
                    }
                }else{
                    if (distanceY > 0) {
                        topBottom = true;
                    } else {
                        bottomTop = true;
                    }
                }
                defaultChildrerDrawerOuter();
                drag();
                if (dragView.getVisibility()==View.GONE){
                    dragView.setVisibility(ViewGroup.VISIBLE);
                }
            }
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            if (dragView!=null&&dragView.getVisibility() == View.VISIBLE&&isGetDown){
                if (leftRight || rightLeft){
                    translationX(isAutoScroll());
                }else if(topBottom||bottomTop){
                    translationY(isAutoScroll());
                }
            }
            isGetDown = false;
            release();
        }

        if(event.getAction()!=MotionEvent.ACTION_UP) {
            lastX = x;
            lastY = y;
        }

        return true;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
        Log.d("eventmo","this is onInterceptTouchEvent action is "+ev.getAction()+" xy is ---"+ev.getX()+"     --------"  +ev.getY());
        if (dragView==null||isAnim ||!isBackGroudView || closeDrag){
            return super.onInterceptTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE){

            int x = (int) ev.getX();
            int y = (int) ev.getY();

            if(moveY!=0 && moveX!=0){
                int diffX = (int) (x-moveX);
                int diffy = (int) (y-moveY);

                if (Math.abs(diffX) > touchSolp || Math.abs(diffy) > touchSolp) {
                    isGetDown = true;
                }
            }
            moveX = x;
            moveY = y;
            return isGetDown;
        }

//        if (ev.getAction() == MotionEvent.ACTION_UP){
//            isGetDown = false;
//        }

        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public boolean isAutoScroll(){
        int autoDistance = (getRight()-getLeft())/4;
        if (leftRight){
            return dragView.getRight()-getLeft()>autoDistance;
        }else if(rightLeft){
            return getRight()-dragView.getLeft()>autoDistance;
        }else if(topBottom){
            return dragView.getBottom()-getTop()>autoDistance;
        }else if(bottomTop){
            return getBottom()-dragView.getTop()>autoDistance;
        }else{
            return false;
        }
    }

    public void translationX(final boolean center){
        int dist = 0;
        int direction = 0;
        if (center){
            dist = getRight()-dragView.getRight();
            direction = getDirection();
        }else{
            if (leftRight){
                dist = getLeft()-dragView.getRight();
            }else if(rightLeft){
                dist = getRight()-dragView.getLeft();
            }
        }
        Log.d("yeying","this is translationX center is "+center+"dist is -- "+dist+" --dragView left-- "+dragView.getLeft());
        final AnimatorSet translationAnimatorSet = new AnimatorSet();
        translationAnimatorSet.play(ObjectAnimator.ofFloat(dragView, "translationX", dist)
                .setDuration(500));
        final int finalDirection = direction;
        translationAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnim  = false;
                Log.d("yeying","this is  --onAnimationEnd-- left"+dragView.getLeft()+" ----top----" +dragView.getTop()+"----right----"+dragView.getRight()+"----bottom---"+dragView.getBottom());
                if (center) {
                    freezeCenter();
                    if (onChangeViewLevel!=null){
                        onChangeViewLevel.onChange(DrawerSwiperLayout.this,false, finalDirection);
                    }
                }
            }
        });
        translationAnimatorSet.start();
        isAnim = true ;
    }

    public void translationY(final boolean center){
        int dist = 0;
        int direction = 0;
        if (center){
            dist = getTop()-dragView.getTop();
            direction = getDirection();
        }else{
            if (bottomTop){
                dist = getBottom() -dragView.getTop();
            }else if(topBottom){
                dist = getTop() -dragView.getBottom() ;
            }
        }
        final AnimatorSet translationAnimatorSet = new AnimatorSet();
        translationAnimatorSet.play(ObjectAnimator.ofFloat(dragView, "translationY", dist)
                .setDuration(500));
        final int finalDirection = direction;
        translationAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnim = false;
                Log.d("yeying","this is  --onAnimationEnd-- left"+dragView.getLeft()+" ----top----" +dragView.getTop()+"----right----"+dragView.getRight()+"----bottom---"+dragView.getBottom());
                if (center) {
                    freezeCenter();
                    if (onChangeViewLevel!=null){
                        onChangeViewLevel.onChange(DrawerSwiperLayout.this,true, finalDirection);
                    }
                }
            }
        });
        translationAnimatorSet.start();
        isAnim = true;
    }

    public void drag(){
        Log.d("yeying","this is init dragView left -- "+dragView.getLeft()+" ----top----" +dragView.getTop()+"----right----"+dragView.getRight()+"----bottom---"+dragView.getBottom());
        Log.d("yeying","this is drag --begin --left leftRight="+leftRight+" ----rightLeft----" +rightLeft+"----topBottom----"+topBottom+"----bottomTop---"+bottomTop);
        if (dragView!=null){
            int orgLeft = dragView.getLeft();
            int orgRight = dragView.getRight();
            int orgTop = dragView.getTop();
            int orgBottom = dragView.getBottom();
            Log.d("yeying","distanceX --"+distanceX);

            if (leftRight){
                dragView.setLeftTopRightBottom((int) (orgLeft+distanceX),dragView.getTop(), (int) (orgRight+distanceX),dragView.getBottom());
            }else if(rightLeft){
                dragView.setLeftTopRightBottom((int) (orgLeft+distanceX),dragView.getTop(), (int) (orgRight+distanceX),dragView.getBottom());
            }else if(topBottom){
                dragView.setLeftTopRightBottom(dragView.getLeft(), (int) (orgTop+distanceY),dragView.getRight(), (int) (orgBottom+distanceY));
            }else if(bottomTop){
                dragView.setLeftTopRightBottom(dragView.getLeft(), (int) (orgTop+distanceY),dragView.getRight(), (int) (orgBottom+distanceY));
            }
            dragView.setTranslationX(0);
            dragView.setTranslationY(0);
            Log.d("yeying","this is drag --end-- left"+dragView.getLeft()+" ----top----" +dragView.getTop()+"----right----"+dragView.getRight()+"----bottom---"+dragView.getBottom());
        }
    }

    public void release(){
        leftRight = false;
        rightLeft = false;
        topBottom = false;
        bottomTop = false;
        distanceY = 0;
        distanceX = 0;
        lastY = 0;
        lastX = 0;
        moveY = 0;
        moveY = 0;
    }


    /**
     * 设置dragView对象并且根据滑动方向初始化dragView的方向位置
     * @param dragView
     */
    public void setUpDrag(DrawerSwiperLayout dragView){
        this.isBackGroudView  =true;
        this.dragView = dragView;
        dragView.setBackGroudView(false);
    }

    private void defaultChildrerDrawerOuter(){
        dragView.setTranslationX(0);
        dragView.setTranslationY(0);
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        defaultChildrerDrawerOuter(width,height);
    }

    public void freezeCenter(){
        if (dragView!=null){
            dragView.setTranslationY(0);
            dragView.setTranslationX(0);
            dragView.setLeftTopRightBottom(this.getLeft(),this.getTop(),this.getRight(),this.getBottom());
            dragView.setBackGroudView(true);
        }
    }

    public void defaultChildrerDrawerOuter(int width ,int height){
        dragView.setTranslationX(0);
        dragView.setTranslationY(0);
        if (leftRight){
            dragView.setLeftTopRightBottom(this.getLeft()-width, this.getTop(),this.getLeft(),this.getBottom());
        }else if(rightLeft){
            dragView.setLeftTopRightBottom(this.getRight(), this.getTop(),this.getRight()+width,this.getBottom());
        }else if(topBottom){
            dragView.setLeftTopRightBottom(this.getLeft(), this.getTop()-height ,this.getRight(),this.getTop());
        }else if(bottomTop){
            dragView.setLeftTopRightBottom(this.getLeft(), this.getBottom() ,this.getRight(),this.getBottom()+height);
        }else{
            dragView.setLeftTopRightBottom(this.getLeft()-width, this.getTop(),this.getLeft(),this.getBottom());
        }
    }


    public void setOnChangeViewLevel(OnChangeViewLevel onChangeViewLevel) {
        this.onChangeViewLevel = onChangeViewLevel;
    }

    public boolean isBackGroudView() {
        return isBackGroudView;
    }

    public void setBackGroudView(boolean backGroudView) {
        isBackGroudView = backGroudView;
    }

    public DrawerSwiperLayout getDragView() {
        return dragView;
    }

    public View getItemView() {
        return itemView;
    }

    public int getDirection(){
        if (leftRight){
            return DIRECTION_leftRight;
        }else if(rightLeft){
            return DIRECTION_rightLeft;
        }else if(topBottom){
            return DIRECTION_topBottom;
        }else if(bottomTop){
            return DIRECTION_bottomTop;
        }else{
            return 0;
        }
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
        this.addView(itemView);
    }

}
