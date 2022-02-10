# DrawerLayout

[![](https://jitpack.io/v/ArronYee/DrawerLayout.svg)](https://jitpack.io/#ArronYee/DrawerLayout)

![img](https://github.com/ArronYee/DrawerLayout/blob/main/ujbj4-e71nh.gif)

# What's the DrawerLayout

When you want get a flip drawer layout which both have horizon and vertical ,that it is.


In this folder repository contains:

1. [/app](https://github.com/ArronYee/DrawerLayout/tree/main/app) a sample example for DrawerLayout.
2. [/drawerLayout](https://github.com/ArronYee/DrawerLayout/tree/main/drawerlayout) the libs code.
3. [demo](https://github.com/ArronYee/DrawerLayout/blob/main/app-debug.apk)

## Table of Contents

- [Partial-code-description](#Partial-code-description)
- [Install](#install)
- [Related-Mentality](#Related-Mentality)
- [To-be-improved](#To-be-improved)

## Partial-code-description

```sh
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">

	    <com.arronyee.drawerlayout.DrawerWrapperLayout
		android:id="@+id/drawer_layout"
		android:layout_width="match_parent"
		android:layout_height="match_parent">

	    </com.arronyee.drawerlayout.DrawerWrapperLayout>

	</LinearLayout>
```

Put DrawerWrapperLayout into your XML.

```sh
  drawerWrapperLayout = findViewById(R.id.drawer_layout);
  drawerWrapperLayout.addDrawerSwiperLayout();

```
Your need to invoke addDrawerSwiperLayout() to get first start.

```sh
  @Override
    public void onChange(DrawerSwiperLayout drawerSwiperLayout, boolean isBackGroudView, int direction) {
        DrawerSwiperLayout view = addDrawerSwiperLayout();
        if (onChangeViewLevel!=null){
            onChangeViewLevel.onChange(view,drawerSwiperLayout,direction);
        }
    }
    
    //-----//
    
    public static final int DIRECTION_leftRight = 11;
    public static final int DIRECTION_rightLeft = 22;
    public static final int DIRECTION_topBottom = 33;
    public static final int DIRECTION_bottomTop = 44;

```

You can set onChange listener to get the direction when flip view.



```sh
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

```

It use ObjectAnimator to achieve property animation。

## Install

In your project build.gradle:

```sh
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

In your needed module
```sh
	dependencies {
		  implementation 'com.github.ArronYee:Form:0.0.3'
	}
```

## Related-Mentality

This Sample use mvp frame,but in the "form", It's more likely mvvm,

i use
```sh
 tv_last.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                memoString.setValue(s.toString());
            }
        });
```


```sh
 DataParser dataParser = serverDataProvider.updateItemValue(name, value);
 
 //--//
 
 localRender.updateItemView(name, showText, value, dataParser.getView() == 1);
 
 
```

Ensure immediate response between them。

## To-be-improved
In my own business project,it also support "Cordition","ExtraFile",but it is inconvenient to package into Library,it has map or upload file need to be done,so let me think and it will be improved in the future。

