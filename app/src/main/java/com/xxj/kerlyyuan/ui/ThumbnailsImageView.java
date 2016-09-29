package com.xxj.kerlyyuan.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.xxj.kerlyyuan.PlayerActivity;

/**
 * Created by kerlyyuan on 2016/9/6.
 */
public class ThumbnailsImageView extends ImageView {

    private static final String TAG = ThumbnailsImageView.class.getSimpleName();

    private String path = "";

    public ThumbnailsImageView(Context context){
        super(context);
    }

    public ThumbnailsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbnailsImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //设置Path路径，给后面播放视频使用
    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }

}
