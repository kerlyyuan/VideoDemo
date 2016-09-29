package com.xxj.kerlyyuan.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xxj.kerlyyuan.PlayerActivity;
import com.xxj.kerlyyuan.R;
import com.xxj.kerlyyuan.ui.SlideShowView;
import com.xxj.kerlyyuan.ui.ThumbnailsImageView;

/**
 * Created by admin on 13-11-23.
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AttentionFragment.class.getSimpleName();

    private SlideShowView slideShowView;

    private ThumbnailsImageView iv_content_1;
    private ThumbnailsImageView iv_content_2;
    private ThumbnailsImageView iv_content_3;
    private ThumbnailsImageView iv_content_4;

    private static final String VIDEO_PATH_1 = "/sdcard/Android/data/com.tencent.qqlive" +
            "/files/videos/j0021e3re0p.sd/j0021e3re0p.10203.1.mp4";
    private static final String VIDEO_PATH_2 = "/sdcard/Android/data/com.tencent.qqlive" +
            "/files/videos/j0021z6py8s.sd/j0021z6py8s.10203.1.mp4";
    private static final String VIDEO_PATH_3 = "/sdcard/Android/data/com.tencent.qqlive" +
            "/files/videos/k0021cjmdlu.sd/k0021cjmdlu.10203.1.mp4";
    private static final String VIDEO_PATH_4 = "/sdcard/Android/data/com.tencent.qqlive" +
            "/files/videos/m0021sf3im1.sd/m0021sf3im1.10203.1.mp4";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_fragment, null);

        //mAccordionBanner = (BGABanner) view.findViewById(R.id.banner_main_accordion);
        slideShowView = (SlideShowView) view.findViewById(R.id.slideshowView);
        slideShowView.init(3, null);

        Bitmap image1 = getVideoThumbnail(VIDEO_PATH_1 ,150, 180, MediaStore.Images.Thumbnails.MINI_KIND);
        iv_content_1 = (ThumbnailsImageView) view.findViewById(R.id.iv_content_1);
        iv_content_1.setBackground(new BitmapDrawable(image1));
        iv_content_1.setPath(VIDEO_PATH_1);
        iv_content_1.setOnClickListener(this);

        Bitmap image2 = getVideoThumbnail(VIDEO_PATH_2, 150, 180, MediaStore.Images.Thumbnails.MINI_KIND);
        iv_content_2 = (ThumbnailsImageView) view.findViewById(R.id.iv_content_2);
        iv_content_2.setBackground(new BitmapDrawable(image2));
        iv_content_2.setPath(VIDEO_PATH_2);
        iv_content_2.setOnClickListener(this);

        Bitmap image3 = getVideoThumbnail(VIDEO_PATH_3, 150, 180, MediaStore.Images.Thumbnails.MINI_KIND);
        iv_content_3 = (ThumbnailsImageView) view.findViewById(R.id.iv_content_3);
        iv_content_3.setBackground(new BitmapDrawable(image2));
        iv_content_3.setPath(VIDEO_PATH_3);
        iv_content_3.setOnClickListener(this);

        Bitmap image4 = getVideoThumbnail(VIDEO_PATH_4, 150, 180, MediaStore.Images.Thumbnails.MINI_KIND);
        iv_content_4 = (ThumbnailsImageView) view.findViewById(R.id.iv_content_4);
        iv_content_4.setBackground(new BitmapDrawable(image2));
        iv_content_4.setPath(VIDEO_PATH_4);
        iv_content_4.setOnClickListener(this);

        return view;
    }


    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        Log.d(TAG, "w"+bitmap.getWidth());
        Log.d(TAG, "h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    public String getContent() {
        return "这是精选界面";
    }

    /*
    * ThumbnailsImageView 点击回调的处理事件方法，启动Player播放器播放源视频
    * */
    @Override
    public void onClick(View view) {
        ThumbnailsImageView clickView = (ThumbnailsImageView) view;
        Log.d(TAG, "start play video, path:"+clickView.getPath());
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("uri", clickView.getPath());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
