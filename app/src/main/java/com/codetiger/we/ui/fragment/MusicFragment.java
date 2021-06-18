package com.codetiger.we.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.codetiger.we.R;
import com.codetiger.we.data.dto.Music;
import com.codetiger.we.net.APIService;
import com.codetiger.we.utils.LogUtil;
import com.codetiger.we.utils.RxSchedulers;
import com.codetiger.we.utils.ThreadPoolManager;
import com.codetiger.we.utils.ToastUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.HEAD;


/**
 * 描述：看资讯的Fragment
 */

public class MusicFragment extends Fragment {
    private String TAG = "liuhu";
    private static final int UPDATEPROGRESS = 1;
    private static final int PLAYMUSIC = 2;
    protected boolean mIsAudioFocusGained = false;
    private AudioAttributes mAttributes = null;
    private AudioFocusRequest mFocusRequest = null;
    private AudioManager mAudioManager = null;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView mTextView2;
    private Button startBt, stopBt, nextBt;
    private SeekBar mSeekBar;
    private Boolean FLAG = true;
    private CompositeDisposable mSubscriptions;
    private String music_category = "热歌榜";

    private Boolean FLAG_UPDATE = true;

    private MediaPlayer mMediaPlayer;
    private Timer timer;
    private TimerTask task;
    private Spinner mSpinner;
    private ThreadPoolManager mThreadPoolManager;
    private UpdateProgress updateProgress;
    private RequestMusicTask requestMusicTask;


    public static MusicFragment newInstance() {
        MusicFragment musicFragment = new MusicFragment();
        return musicFragment;
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case UPDATEPROGRESS:
                    if (null != mMediaPlayer){
                        try {
                            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case PLAYMUSIC :
                    doHandlerResult((Music) message.obj);
                default:
                    break;
            }
            return true;
        }
    });


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        mImageView = view.findViewById(R.id.music_pic);
        mTextView = view.findViewById(R.id.music_name);
        mTextView2 = view.findViewById(R.id.music_artist);
        mSeekBar = view.findViewById(R.id.music_prog);
        mSpinner = view.findViewById(R.id.spinner_music);
        startBt = view.findViewById(R.id.bt_start);
        stopBt = view.findViewById(R.id.bt_stop);
        nextBt = view.findViewById(R.id.bt_next);
        mSubscriptions = new CompositeDisposable();
        mThreadPoolManager = ThreadPoolManager.getInstance();
        mAudioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMusic(music_category);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (null != mMediaPlayer) {
                    //获取当前进度条位置
                    int currentPosition = seekBar.getProgress();
                    //跳转到某个位置播放
                    mMediaPlayer.seekTo(currentPosition);
                }
            }
        });

        stopBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                LogUtil.d(TAG, "after stopBt.setOnClickListener : " + mMediaPlayer.isPlaying());
            }
        });

        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mMediaPlayer && !mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                music_category = (String) mSpinner.getSelectedItem();
                getMusic(music_category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (!FLAG) {
            getMusic(music_category);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(TAG,"123");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(TAG,"123456");
        mThreadPoolManager.remove(updateProgress);
        mThreadPoolManager.remove(requestMusicTask);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"123456789");
        if (null != mMediaPlayer) {
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        mSubscriptions.clear();
        FLAG_UPDATE= false;
    }

    private void getMusic(String category) {
        requestMusicTask = new RequestMusicTask();
        mThreadPoolManager.execute(requestMusicTask);
    }

    private void doHandlerResult(Music music) {
        Glide.with(this).load(music.getPicurl()).into(mImageView);
        mTextView.setText(music.getName());
        mTextView2.setText(music.getArtistsname());
        startMusicPlay(music.getUrl());
    }

    private void startMusicPlay(String url) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(new MyPrepareListener());
            mMediaPlayer.setOnCompletionListener(new MyCompletionListener());
            mMediaPlayer.setOnErrorListener(new MyErrorListener());
        } else {
            mMediaPlayer.reset();
        }

        boolean requestAudioFocus = false;

        requestAudioFocus = requestAudioFocus();
        Log.d(TAG, "requestAudioFocus: " + requestAudioFocus);
        if (requestAudioFocus) {
            try {
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
//            exit();
        }
    }


    public boolean requestAudioFocus() {
        boolean resultRequestAudioFocus = mIsAudioFocusGained;

        LogUtil.d(TAG, "mAudioManager: " + mAudioManager.getMode());
        if (!mIsAudioFocusGained) {
            if (mAudioManager != null) {
                if (mAttributes == null) {
                    mAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
                }
                if (mFocusRequest == null) {
                    mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(mAttributes)
                            .setOnAudioFocusChangeListener(mAudioFocusListener)
                            .setAcceptsDelayedFocusGain(true)
                            .build();
                }

                int result = mAudioManager.requestAudioFocus(mFocusRequest);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    LogUtil.d(TAG, "requestAudioFocus: AudioManager.AUDIOFOCUS_REQUEST_GRANTED");
                    mIsAudioFocusGained = true;
                    resultRequestAudioFocus = true;
                } else if (result == AudioManager.AUDIOFOCUS_REQUEST_DELAYED) {
                    LogUtil.d(TAG, "requestAudioFocus: AudioManager.AUDIOFOCUS_REQUEST_DELAYED");
                    resultRequestAudioFocus = true;
                }
                LogUtil.d(TAG, "mAttributes.getUsage(): " + mAttributes.getUsage());
            }
        }

        return resultRequestAudioFocus;
    }


    /**
     * 音频焦点
     */
    public final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int audioFocus) {
            switch (audioFocus) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    LogUtil.d(TAG, "onAudioFocusChange: AudioManager.AUDIOFOCUS_GAIN");
                    mIsAudioFocusGained = true;
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    LogUtil.d(TAG, "onAudioFocusChange: AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    LogUtil.d(TAG, "onAudioFocusChange: AudioManager.AUDIOFOCUS_LOSS");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mAudioManager.abandonAudioFocusRequest(mFocusRequest);
                    }
                    mIsAudioFocusGained = false;
                    if (mMediaPlayer != null || mMediaPlayer.isPlaying()) {
//                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }
                    break;

                default:
                    LogUtil.d(TAG, "onAudioFocusChange: default");
                    break;
            }
        }
    };

    private class MyPrepareListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            LogUtil.d(TAG, "onPrepared");
            mMediaPlayer.start();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            updateProgress = new UpdateProgress();
            mThreadPoolManager.execute(updateProgress);
        }

    }

    private class MyCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            LogUtil.d(TAG, "onCompletion");
            mSeekBar.setProgress(0);
            getMusic(music_category);
        }
    }

    private class MyErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            LogUtil.e(TAG, "onError");

            return false;
        }
    }


    class RequestMusicTask implements Runnable {

/*        private String category;
        public RequestMusicTask(String category) {
            super();
            this.category = category;
            LogUtil.d(TAG,"类别为："+category+"的歌曲请求中...");
        }*/
        @Override
        public void run() {
            LogUtil.d(TAG, "类别为："+music_category+"的歌曲请求中...");
            Disposable subscribe = null;
            subscribe = APIService.getInstance().apis.getMusic("json",music_category)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        Message message = Message.obtain();
                        message.what = PLAYMUSIC;
                        message.obj =data.getData();
                        handler.sendMessage(message);
                    }, RxSchedulers::processRequestException);
            mSubscriptions.add(subscribe);
        }
    }


    class UpdateProgress implements Runnable {
        @Override
        public void run() {
            LogUtil.d(TAG,"更新进度条");
            try {
                while (FLAG_UPDATE) {
                    Message message = Message.obtain();
                    message.what = UPDATEPROGRESS;
                    handler.sendMessage(message);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
