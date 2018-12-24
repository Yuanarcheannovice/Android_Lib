package com.archeanx.lib.tv.vlc;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wurenhai
 * @since 2017/4/14
 */
public class VLCTextureVideoView extends TextureView {

    private static final String TAG = "VLCTextureVideoView";

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

    public interface OnStateChangedListener {
        void onStateChanged(VLCTextureVideoView view, State state);
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(VLCTextureVideoView view, int position, int duration);
    }

    private static final ExecutorService worker = Executors.newSingleThreadExecutor();

    private MediaPlayer mediaPlayer;
    private long position = 0;

    private boolean isPreparing = false;
    private boolean isCreated = false;

    private int videoWidth;
    private int videoHeight;

    private volatile Uri current = null;

    private State state = State.STOPPED;

    private OnStateChangedListener onStateChangedListener;
    private OnTimeChangedListener onTimeChangedListener;

    /**
     * Activity是否是暂停状态
     */
    private boolean isActivityPause = false;

    /**
     * 是否全屏
     */
    private boolean isFullScreen;

    public VLCTextureVideoView(Context context) {
        this(context, null);
    }

    public VLCTextureVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLCTextureVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.videoWidth = 0;
        this.videoHeight = 0;

        setSurfaceTextureListener(surfaceTextureListener);
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.onStateChangedListener = listener;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener listener) {
        this.onTimeChangedListener = listener;
    }

    /**
     * 进度改变
     */
    private void setTimeChanged() {
        if (mediaPlayer == null) {
            return;
        }
        int time = (int) (mediaPlayer.getTime() / 1000);
        int length = (int) (mediaPlayer.getLength() / 1000);
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(this, time, length);
        }
    }

    /**
     * 播放完毕
     */
    private void setEndReached() {
        if (mediaPlayer == null) {
            return;
        }
        int length = (int) (mediaPlayer.getLength() / 1000);
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(this, length, length);
        }
    }

    /**
     * 得到状态
     *
     * @return
     */
    public State getState() {
        return this.state;
    }

    /**
     * 设置状态
     */
    private void setState(State state) {
        State old = this.state;
        this.state = state;
        Log.d(TAG, "state changed from " + old + " to " + state);

        if (state == State.STOPPED) {
//            prepare(current);
        } else if (state == State.PLAYING) {
            if (position > 0) {
                seek(position);
            }
            position = 0;
        }

        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChanged(this, state);
        }
    }

    public void onActivityPause() {
        isActivityPause = true;
    }

    public void onActivityResume() {
        isActivityPause = false;
    }

    /**
     * 重新播放
     */
    public void onVideoPrepare() {
        if (current != null) {
            prepare(current);
        }
    }

    /**
     * 暂停
     */
    @MainThread
    public void onVideoPause() {
        if (mediaPlayer == null) {
            Log.w(TAG, "mediaPlayer is null when stop");
            return;
        }
        if (getState() == State.PLAYING) {
            mediaPlayer.pause();
        }
    }

    /**
     * 继续
     */
    @MainThread
    public void onVideoResume() {
        if (mediaPlayer == null) {
            Log.w(TAG, "mediaPlayer is null when stop");
            return;
        }
        if (getState() == State.PAUSED) {
            mediaPlayer.play();
        }
    }


    /**
     * 停止并释放
     */
    @MainThread
    public void stop() {
        if (mediaPlayer == null) {
            Log.w(TAG, "mediaPlayer is null when stop");
            return;
        }

        final MediaPlayer mp = mediaPlayer;
        this.mediaPlayer = null;

        if (mp != null) {
            mp.pause();
        }

        final Media media = mp.getMedia();
        if (media != null) {
            media.setEventListener(null);
            media.release();
        }

        mp.getVLCVout().detachViews();
        mp.getVLCVout().removeCallback(iVLCVoutCallback);
        mp.setEventListener(null);

        Log.i(TAG, "stopped then try release");

        worker.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Before release mediaPlayer");
                mp.setMedia(null);
                mp.release();
                Log.d(TAG, "After release mediaPlayer");
            }
        });
    }

    /**
     * 开始播放
     *
     * @param uri
     */
    @MainThread
    public void prepare(String uri) {
        uri = uri.trim();
        String name = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
        String path = uri.substring(0, uri.lastIndexOf("/") + 1);
        String encodeName = Uri.encode(name);
        prepare(Uri.parse(path + encodeName));
    }

    @MainThread
    private void prepare(Uri uri) {
        if (uri == null) {
            Log.e(TAG, "prepare: the uri is null");
            return;
        }
        isPreparing = true;
        current = uri;
        position = 0;
        doPrepare();
    }

    @MainThread
    public void play() {
        if (mediaPlayer == null) {
            Log.e(TAG, "play: mediaPlayer is null");
            return;
        }
        toggle();
    }

    @MainThread
    public void toggle() {
        if (mediaPlayer == null) {
            Log.e(TAG, "toggle: mediaPlayer is null");
            return;
        }

        if (state == State.STOPPED && !isPreparing) {
            prepare(current);
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            position = mediaPlayer.getTime();
            Log.v(TAG, "toggle: pause at " + position);
        } else {
            mediaPlayer.play();
            Log.v(TAG, "toggle: resume at " + position);
        }
    }

    @MainThread
    public void seek(long position) {
        if (mediaPlayer == null) {
            Log.e(TAG, "mediaPlayer is null when seek to " + position);
            return;
        }
        if (mediaPlayer.isSeekable()) {
            mediaPlayer.setTime(position);
            Log.v(TAG, "seek to " + position);
        }
    }

    /**
     * 设置音量
     */
    public void setVolume(int v1) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(v1);
        }
    }

    @MainThread
    private void doPrepare() {
        Log.d(TAG, "doPrepare: isPreparing=" + isPreparing + ", isCreated=" + isCreated);
        if (isPreparing && isCreated && !isActivityPause) {
            isPreparing = false;

            //stop first
            stop();

            //recreate media player
            mediaPlayer = new MediaPlayer(VLC.get(getContext()));
            mediaPlayer.setEventListener(mediaPlayerListener);
            mediaPlayer.getVLCVout().addCallback(iVLCVoutCallback);
            mediaPlayer.getVLCVout().setVideoSurface(getSurfaceTexture());
            mediaPlayer.getVLCVout().attachViews();

            //prepare media
            final Media media = new Media(VLC.get(getContext()), current);
            media.setEventListener(mediaListener);
            mediaPlayer.setMedia(media);
            media.release();

            setState(State.PREPARING);
        }
    }

    @MainThread
    private void resize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (mediaPlayer == null) {
            return;
        }
        if (!mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.getVLCVout().setWindowSize(width, height);
    }

    //endregion

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VLCTextureVideoView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VLCTextureVideoView.class.getName());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);

        if (videoWidth > 0 && videoHeight > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (videoWidth * height < width * videoHeight) {
                    width = height * videoWidth / videoHeight;
                } else if (videoWidth * height > width * videoHeight) {
                    height = width * videoHeight / videoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * videoHeight / videoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * videoWidth / videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = videoWidth;
                height = videoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            isCreated = true;
            Log.d(TAG, "onSurfaceTextureAvailable");
            doPrepare();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            resize(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            isCreated = false;
            Log.d(TAG, "onSurfaceTextureDestroyed");
            stop();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };


    IVLCVout.Callback iVLCVoutCallback = new IVLCVout.Callback() {
        @Override
        public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
            Log.v(TAG, "onNewLayout " + width + "x" + height);
            videoWidth = width;
            videoHeight = height;
            requestLayout();
        }

        @Override
        public void onSurfacesCreated(IVLCVout vlcVout) {
            Log.d(TAG, "IVLCVout.onSurfacesCreated");
        }

        @Override
        public void onSurfacesDestroyed(IVLCVout vlcVout) {
            Log.d(TAG, "IVLCVout.onSurfacesDestroyed");
        }

        @Override
        public void onHardwareAccelerationError(IVLCVout vlcVout) {

        }
    };

    //endregion

    private MediaPlayer.EventListener mediaPlayerListener = new MediaPlayer.EventListener() {
        @Override
        public void onEvent(MediaPlayer.Event event) {
            switch (event.type) {
                case MediaPlayer.Event.MediaChanged:
                    Log.d(TAG, "MediaPlayer.Event.MediaChanged");
                    break;
                case MediaPlayer.Event.Opening:
                    Log.d(TAG, "MediaPlayer.Event.Opening");
                    setState(State.OPENING);
                    break;
                case MediaPlayer.Event.Buffering:
                    Log.v(TAG, "MediaPlayer.Event.Buffering " + event.getBuffering());
                    break;
                case MediaPlayer.Event.Playing:
                    Log.d(TAG, "MediaPlayer.Event.Playing");
                    setState(State.PLAYING);
                    break;
                case MediaPlayer.Event.Paused:
                    Log.d(TAG, "MediaPlayer.Event.Paused");
                    setState(State.PAUSED);
                    break;
                case MediaPlayer.Event.Stopped:
                    setState(State.STOPPED);
                    Log.d(TAG, "MediaPlayer.Event.Stopped");
                    break;
                case MediaPlayer.Event.EndReached:
                    Log.d(TAG, "MediaPlayer.Event.EndReached");
                    setEndReached();
                    break;
                case MediaPlayer.Event.EncounteredError:
                    Log.d(TAG, "MediaPlayer.Event.EncounteredError");
                    setState(State.ERROR);
                    break;
                case MediaPlayer.Event.TimeChanged:
                    Log.v(TAG, "MediaPlayer.Event.TimeChanged " + event.getTimeChanged());
                    setTimeChanged();
                    break;
                case MediaPlayer.Event.PositionChanged:
                    Log.v(TAG, "MediaPlayer.Event.PositionChanged " + event.getPositionChanged());
                    break;
                case MediaPlayer.Event.SeekableChanged:
                    Log.v(TAG, "MediaPlayer.Event.SeekableChanged " + event.getSeekable());
                    break;
                case MediaPlayer.Event.PausableChanged:
                    Log.v(TAG, "MediaPlayer.Event.PausableChanged " + event.getPausable());
                    break;
                case MediaPlayer.Event.Vout:
                    Log.v(TAG, "MediaPlayer.Event.Vout");
                    break;
                case MediaPlayer.Event.ESAdded:
                    Log.d(TAG, "MediaPlayer.Event.ESAdded");
                    break;
                case MediaPlayer.Event.ESDeleted:
                    Log.d(TAG, "MediaPlayer.Event.ESDeleted");
                    break;
                default:
                    Log.v(TAG, "mMediaPlayer.onEvent " + event.type);
                    break;
            }
        }
    };

    private Media.EventListener mediaListener = new Media.EventListener() {
        @Override
        public void onEvent(Media.Event event) {
            switch (event.type) {
                case Media.Event.MetaChanged:
                    Log.d(TAG, "Media.Event.MetaChanged " + event.getMetaId());
                    break;
                case Media.Event.SubItemAdded:
                    Log.v(TAG, "Media.Event.SubItemAdded");
                    break;
                case Media.Event.DurationChanged:
                    Log.v(TAG, "Media.Event.DurationChanged");
                    break;
                case Media.Event.ParsedChanged:
                    Log.v(TAG, "Media.Event.ParsedChanged " + event.getParsedStatus());
                    break;
                case Media.Event.StateChanged:
                    Log.v(TAG, "Media.Event.StateChanged");
                    break;
                case Media.Event.SubItemTreeAdded:
                    Log.v(TAG, "Media.Event.SubItemTreeAdded");
                    break;
                default:
                    Log.v(TAG, "media.onEvent " + event.type);
                    break;
            }
        }
    };

}
