package com.archeanx.lib.tv.vlc;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.TextureView;
import android.widget.FrameLayout;

public class VLCVideoView extends FrameLayout{
    public enum State {
        /**
         * 停止
         */
        STOPPED,
        /**
         * 预览中
         */
        PREPARING,
        /**
         * 打开视屏中
         */
        OPENING,
        /**
         * 播放中
         */
        PLAYING,
        /**
         * 暂停中
         */
        PAUSED,
        /**
         * 错误
         */
        ERROR,
    }

    private TextureView mTextureView;

    public VLCVideoView(@NonNull Context context) {
        this(context,null);
    }

    public VLCVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public VLCVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VLCVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView(){
        mTextureView = new TextureView(getContext());

        mTextureView.setSurfaceTextureListener(textureListener);

        addView(mTextureView);
    }


    TextureView.SurfaceTextureListener textureListener= new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };



    public interface OnStateChangedListener {
        void onStateChanged(VLCTextureVideoView view, VLCTextureVideoView.State state);
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(VLCTextureVideoView view, int position, int duration);
    }

}
