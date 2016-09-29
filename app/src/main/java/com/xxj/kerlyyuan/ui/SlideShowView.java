package com.xxj.kerlyyuan.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xxj.kerlyyuan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;


/**
 * ViewPager实现的轮播图广告自定义视图,既支持自动轮播页面也支持手势滑动切换页面
 * Created by kerlyyuan on 2016/9/5.
 */
public class SlideShowView extends FrameLayout {

    private static final String TAG = SlideShowView.class.getSimpleName();

    //默认轮播图图片数量
    private final static int IMAGE_COUNT = 3;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源ID
    private int[] imagesResIds;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;
    //轮播图图片数量
    private int mImageCount = 0;

    private ViewPager viewPager;

    private Context context;

    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    public SlideShowView(Context context) {
        this(context,null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void init(int imageCount, int[] iamgeList){
        mImageCount = imageCount;
        loadData(iamgeList);
        initUI(context);
        if(isAutoPlay){
            startPlay();
        }
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        Log.d(TAG,"startPlay SlideShowView...");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }

    /*
    * 初始化相关Data
    * */
    public void loadData(int[] imageList){
        Log.d(TAG,"initData SlideShowView...");
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

        if(imageList == null || imageList.length == 0){
            imagesResIds = new int[IMAGE_COUNT];
            for(int i=0;i<IMAGE_COUNT;i++){
                if(i==0){
                    imagesResIds[i] = R.drawable.girl_1;
                }
                if(i==1){
                    imagesResIds[i] = R.drawable.girl_2;
                }
                if(i==2){
                    imagesResIds[i] = R.drawable.video_listab_global_off;
                }
                if(i==3){
                    imagesResIds[i] = R.drawable.video_listab_mylist_off;
                }
                if(i==4){
                    imagesResIds[i] = R.drawable.video_listab_attention_off;
                }
            }
        }else{
            imagesResIds = imageList;
        }
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context){
        Log.d(TAG,"initUI SlideShowView...");
        LayoutInflater.from(context).inflate(R.layout.slide_view, this, true);
        for(int imageID : imagesResIds){
            ImageView view =  new ImageView(context);
            view.setImageResource(imageID);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);
        }

        //通过动态添加view来控制图片的轮播数量
        LinearLayout ll_ViewList = (LinearLayout) findViewById(R.id.ll_ViewList);
        for(int i=0;i<mImageCount;i++){
            View dot = new View(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
            if(i != 0){
                lp.setMargins(10, 0, 0, 0);
                dot.setBackgroundResource(R.drawable.bga_banner_point_enabled);
            }else{
                dot.setBackgroundResource(R.drawable.bga_banner_point_disabled);
            }
            ll_ViewList.addView(dot, lp);
            dotViewsList.add(dot);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     *执行轮播图切换任务
     *@author caizhiming
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            //synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
           // }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     * @author caizhiming
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

    /**
     * 填充ViewPager的页面适配器
     * @author caizhiming
     */
    private class MyPagerAdapter  extends PagerAdapter{

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager)container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager)container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     * @author caizhiming
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            for(int i=0;i < dotViewsList.size();i++){
                View curView = (View) dotViewsList.get(i);
                if(i == pos){
                    curView.setBackgroundResource(R.drawable.bga_banner_point_disabled);
                }else {
                    curView.setBackgroundResource(R.drawable.bga_banner_point_enabled);
                }
            }
        }

    }
}
