package com.baidu.tts.sample;

import android.app.Application;

import com.baidu.tts.sample.tts.SpeakVoiceUtil;

/**
 * @Description: java类作用描述
 * @Author: wjq
 * @CreateDate: 2019-08-05 11:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-08-05 11:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeakVoiceUtil.getInstance().init(this);
    }
}
