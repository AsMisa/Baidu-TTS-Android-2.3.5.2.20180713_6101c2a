package com.baidu.tts.sample.tts;

import android.content.Context;

import com.baidu.tts.client.SpeechError;

/**
 * @Description: 百度语音合成的接口
 * @Author: wjq
 * @CreateDate: 2019-08-01 09:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-08-01 09:23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface SpeechListener {
    void onInitFinish();
    void onStart(Context context, String resultValue);
    void onProgress(Context context, String resultValue, int current);
    void onFinish(Context context, String resultValue);
    void onError(Context context, String resultValue, SpeechError speechError);

}
