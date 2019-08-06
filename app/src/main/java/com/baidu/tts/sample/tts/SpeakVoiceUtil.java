package com.baidu.tts.sample.tts;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.sample.FileUtil;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: wjq
 * @CreateDate: 2019-08-01 09:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-08-01 09:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SpeakVoiceUtil {
    private static final String TAG = "SpeakVoiceUtil";
    private SpeechSynthesizer mSpeechSynthesizer;
    private Context mContext;
    private static SpeakVoiceUtil mSpeakVoiceUtil;
    private SpeechListener mSpeechListener;
    private SpeechSynthesizerListener mSpeechSynthesizerListener;

    private SpeakVoiceUtil() {
    }

    public static SpeakVoiceUtil getInstance() {
        if (mSpeakVoiceUtil == null) {
            mSpeakVoiceUtil = new SpeakVoiceUtil();
        }
        return mSpeakVoiceUtil;
    }

    /**
     * 初始化
     *
     * @param context 外部上下文
     */
    public void init(Context context) {
        this.mContext = context;
        if (mSpeakVoiceUtil == null) {
            Log.e(TAG, "Error SpeakVoiceUtil is null");
            return;
        }
        initFile();
        initTTs();
        checkAuth();
        if (mSpeechListener != null) {
            mSpeechListener.onInitFinish();
        }
    }

    /**
     * 合成语音并且播放
     *
     * @param text 要合成的text文本
     */
    public void speak(String text) {
        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "Error speak:mSpeechSynthesizer is null.");
            return;
        }
        int result = mSpeechSynthesizer.speak(text);
        checkResult(result, "speak");
        Log.i(TAG, "播放语音：" + text);
    }

    /**
     * 停止语音合成和播放,清空列队
     */
    public void stop() {
        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "Error stop:mSpeechSynthesizer is null.");
            return;
        }
        int result = mSpeechSynthesizer.stop();
        checkResult(result, "stop");
        Log.i(TAG, "停止语音");
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    public void pause() {
        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "Error pause:mSpeechSynthesizer is null.");
            return;
        }
        int result = mSpeechSynthesizer.pause();
        checkResult(result, "pause");
        Log.i(TAG, "暂停语音");
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    public void resume() {
        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "Error resume:mSpeechSynthesizer is null.");
            return;
        }
        int result = mSpeechSynthesizer.resume();
        checkResult(result, "resume");
        Log.i(TAG, "恢复语音");
    }

    /**
     * 批量播放
     * List<SpeechSynthesizeBag> list = new ArrayList<>();
     * SpeechSynthesizeBag s1 = new SpeechSynthesizeBag();
     * s1.setText("开始批量播放");
     * s1.setUtteranceId("1");
     * list.add(s1);
     * SpeechSynthesizeBag s2 = new SpeechSynthesizeBag();
     * s2.setText("批量播放成功");
     * s2.setUtteranceId("2");
     * list.add(s2);
     * SpeakVoiceUtil.getI().batchSpeak(list);
     */
    public void batchSpeak(List<SpeechSynthesizeBag> list) {
        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "Error batchSpeak:mSpeechSynthesizer is null.");
            return;
        }
        int result = mSpeechSynthesizer.batchSpeak(list);
        checkResult(result, "batchSpeak");
    }

    /**
     * 释放资源
     */
    public void Destroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            mSpeechSynthesizer.release();
            mSpeechSynthesizer = null;
        }
    }


    public int setVoice(float leftVoice, float rightVoice) {
        if (mSpeechSynthesizer == null) {
            Log.e(TAG, "Error setVoice: mSpeechSynthesizer is null");
            return -1;
        }
        int result = mSpeechSynthesizer.setStereoVolume(leftVoice, rightVoice);
        return result;

    }

    /**
     * 语音播放监听接口回调
     *
     * @param SpeechListener 接口类
     */
    public void onSpeechListener(SpeechListener SpeechListener) {
        this.mSpeechListener = SpeechListener;

    }


    /**
     * 初始化语音参数
     */
    private void initTTs() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(mContext);
        getSpeechSynthesizerListener();
        mSpeechSynthesizer.setSpeechSynthesizerListener(mSpeechSynthesizerListener);
        int result = mSpeechSynthesizer.setAppId("11005757");
        checkResult(result, "setAppId");//检查结果  TODO
        result = mSpeechSynthesizer.setApiKey("Ovcz19MGzIKoDDb3IsFFncG1", "e72ebb6d43387fc7f85205ca7e6706e2");
        checkResult(result, "setApiKey");

        mSpeechSynthesizer.auth(VoiceConfigData.TTS_MODE);
        // 文本模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE,
                VoiceConfigData.TEMP_DIR  + VoiceConfigData.TEXT_FILENAME);
        // 声学模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                VoiceConfigData.TEMP_DIR  + VoiceConfigData.OFFLINE_FILE_TWO);
        Log.d(TAG, VoiceConfigData.TEMP_DIR  + VoiceConfigData.OFFLINE_FILE_TWO);
        // 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        // 设置参数的组合模式
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE);

        mSpeechSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);//设置音频流出口
        mSpeechSynthesizer.setStereoVolume(1f, 1f);//设置音量


        result = mSpeechSynthesizer.initTts(VoiceConfigData.TTS_MODE);//初始化在线模式:TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
        checkResult(result, "initTts");

    }

    /**
     * 初始化文件,将assets目录的离线语音包复制到sd卡中
     */
    private void initFile() {
//        Log.i(TAG, "开始初始化离线文件");
//        String[] files = {VoiceConfigData.OFFLINE_FILE_ONE,
//                VoiceConfigData.OFFLINE_FILE_TWO,
//                VoiceConfigData.OFFLINE_FILE_THREE,
//                VoiceConfigData.OFFLINE_FILE_FOUR,
//                VoiceConfigData.TEXT_FILENAME};
//        for (String file : files) {
//            String filePath = VoiceConfigData.TEMP_DIR  + file;
//            if (!FileUtil.fileIsExists(filePath)) {
//                Log.i(TAG, "initFile 准备复制文件file:" + file + " 到指定目录:" + filePath);
//                FileUtil.putAssetsToSDCard(mContext, file, VoiceConfigData.TEMP_DIR);
                FileUtil.getInstance(mContext).copyAssetsToSD("baiduTTS","/baiduTTS").setFileOperateCallback(new FileUtil.FileOperateCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(String error) {
                        Log.d(TAG, error);
                    }
                });
//            } else {
//                Log.i(TAG, "initFile " + file + "文件存在不需要复制");
//            }
//        }
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.e(TAG, "error code :" + result + "   method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122");
        }
    }

    /**
     * 检查appId ak sk 是否填写正确，另外检查官网应用内设置的包名是否与运行时的包名一致。本demo的包名定义在build.gradle文件中
     *
     * @return
     */
    private boolean checkAuth() {
        AuthInfo authInfo = mSpeechSynthesizer.auth(VoiceConfigData.TTS_MODE);
        if (!authInfo.isSuccess()) {
            // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            Log.e(TAG, "error 鉴权失败 errorMsg=" + errorMsg);
            return false;
        } else {
            Log.e(TAG, "验证通过，离线正式授权文件存在");
            return true;
        }
    }

    /**
     * 语音合成监听接口回调方法
     */
    private void getSpeechSynthesizerListener() {
        if (mSpeechSynthesizerListener == null) {
            Log.e(TAG, "初始化SpeechSynthesizerListener");
            mSpeechSynthesizerListener = new SpeechSynthesizerListener() {
                @Override
                public void onSynthesizeStart(String s) {
                    Log.i(TAG, "onSynthesizeStart合成启动:返回码=" + s);

                }

                @Override
                public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
                    Log.i(TAG, "onSynthesizeDataArrived合成数据到达:" + "返回码=" + s + "; 字节=" + bytes + "; current=" + i);

                }

                @Override
                public void onSynthesizeFinish(String s) {
                    Log.i(TAG, "onSynthesizeFinish合成完成:返回码=" + s);

                }

                @Override
                public void onSpeechStart(String s) {
                    Log.i(TAG, "onSpeechStart语音开始:返回码=" + s);
                    if (mSpeechListener != null) {
                        mSpeechListener.onStart(mContext, s);
                    }

                }

                @Override
                public void onSpeechProgressChanged(String s, int i) {
                    Log.i(TAG, "onSpeechProgressChanged语音播放中:返回码=" + s + "; current=" + i);
                    if (mSpeechListener != null) {
                        mSpeechListener.onProgress(mContext, s, i);
                    }

                }

                @Override
                public void onSpeechFinish(String s) {
                    Log.i(TAG, "onSpeechFinish语音播放结束:返回码=" + s);
                    if (mSpeechListener != null) {
                        mSpeechListener.onFinish(mContext, s);
                    }

                }

                @Override
                public void onError(String s, SpeechError speechError) {
                    Log.e(TAG, "onError异常:返回码=" + s + "; SpeechError=" + speechError);
                    if (mSpeechListener != null) {
                        mSpeechListener.onError(mContext, s, speechError);
                    }

                }
            };
        }
    }

}
