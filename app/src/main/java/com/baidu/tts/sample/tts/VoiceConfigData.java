package com.baidu.tts.sample.tts;

import android.os.Environment;

import com.baidu.tts.client.TtsMode;

/**
 * @Description: java类作用描述
 * @Author: wjq
 * @CreateDate: 2019-08-01 09:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-08-01 09:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class VoiceConfigData {
    /**
     * 模式   TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
     */
    public static final TtsMode TTS_MODE = TtsMode.MIX;

    /**
     *离线资源文件名称与离线资源转存路径
     */
    public static final String TEMP_DIR = Environment.getExternalStorageDirectory() +"/"+"baiduTTS";//转存路径
    //离线度丫丫
    public static final String OFFLINE_FILE_ONE = "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat";
    //离线女声
    public static final String OFFLINE_FILE_TWO = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";
    //离线男声
    public static final String OFFLINE_FILE_THREE = "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
    //yyjw 度逍遥
    public static final String OFFLINE_FILE_FOUR = "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat";
    //离线文字识别文件
    public static final String TEXT_FILENAME = "bd_etts_text.dat";

}
