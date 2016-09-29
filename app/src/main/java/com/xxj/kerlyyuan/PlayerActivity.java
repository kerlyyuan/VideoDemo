package com.xxj.kerlyyuan;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by kerlyyuan on 2016/9/7.
 */
public class PlayerActivity extends Activity {

    private VideoView mVideoView;
    private MediaController mMediaController;
    private TextView percentTv;
    private TextView netSpeedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Vitamio.initialize(this)){
            setContentView(R.layout.player);

            mVideoView = (VideoView) findViewById(R.id.surface_view);

            percentTv = (TextView) findViewById(R.id.buffer_percent);
            netSpeedTv = (TextView) findViewById(R.id.net_speed);

            String path = getIntent().getExtras().getString("uri");
            Uri uri = Uri.fromFile(new File(path));
            mVideoView.setVideoURI(uri);

            //设置播放地址
            //mVideoView.setVideoURI(Uri.parse("http://112.253.22.157/17/z/z/y/u/zzyuasj"
            //        + "wufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv"));

            mMediaController = new MediaController(this);  //实例化控制器
            mMediaController.show(5000);  //控制器显示5s后自动隐藏
            mVideoView.setMediaController(mMediaController);  //绑定控制器
            mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH); //设置播放画质 高画质
            mVideoView.requestFocus();

            mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener(){
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    percentTv.setText("已缓冲：" + percent + "%");
                }
            });

            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener(){
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        // 开始缓冲
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            percentTv.setVisibility(View.VISIBLE);
                            netSpeedTv.setVisibility(View.VISIBLE);
                            mp.pause();
                            break;
                        // 缓冲结束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            percentTv.setVisibility(View.GONE);
                            netSpeedTv.setVisibility(View.GONE);
                            mp.start();
                            break;
                        // 正在缓冲
                        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                            netSpeedTv.setText("当前网速:" + extra + "kb/s");
                            break;
                    }
                    return true;
                }
            });
        }

    }
}
