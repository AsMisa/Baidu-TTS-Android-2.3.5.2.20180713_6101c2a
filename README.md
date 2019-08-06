# 简介及运行环境

## 概述

本文档是百度语音开放平台Andriod SDK的用户指南，描述了**在线合成**等相关接口的使用说明。 合成的策略是边下载边播放。区别于Rest Api一次性下载整个录音文件。

## 兼容性

| 类别 | 兼容范围 |
| --- | --- |
| 系统 | 支持Android 2.3 以上版本 API LEVEL 9 |
| 机型 | 手机和平板均可 |
| 硬件要求 | 要求设备上有麦克风 |
| 网络 | 支持移动网络（包括2G等）、WIFI等网络环境 |
| 开发环境 | 建议使用最新版本Android Studio 进行开发 |

## SDK库文件

| 资源名称 | 资源大小 | 资源描述 |
| --- | --- | --- |
| com.baidu.tts_x.x.x.xxxxx_xxxxx.jar | 约380KB | jar 库 |

## NDK so库架构

共计5个架构目录：armeabi，armeabi-v7a，arm64-v8a，x86，x86_64，每个架构下均有以下5个so库文件。 如果为了节省安装包体积，可以只使用armeabi目录，性能损失微小。

| 资源名称 | 资源大小 |
| --- | --- |
| libbd_etts.so | 1.5M |
| libBDSpeechDecoder_V1.so | 400k |
| libbdtts.so | 18K |
| libgnustl_shared.so | 710k |

**如果仅需要在线功能，不使用压缩传输，可以不需要上述so文件。**

```
// 不使用压缩传输
mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_PCM);
mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_PCM);

```

如果仅需要在线功能，使用压缩传输，仅需要保留ibBDSpeechDecoder_V1.so

## DEMO压缩包说明

DEMO压缩包下载即可运行，其中DEMO内已经附带了SDK的库。

*   com.baidu.tts_x.x.x.xxxxx_xxxxx.jar 位于 app/libs 目录下。
*   armeabi，armeabi-v7a，arm64-v8a，x86，x86_64 5个架构目录位于app\src\main\jniLibs 目录下

## 版本更新

1.  历史bug修复，底层库完全升级
2.  Demo修改，去除原DEMO中无用功能。
3.  Demo中添加正式授权。

# 功能简介

语音合成分为 在线合成和离线合成。仅有中英文混合这一种语言。

## 语言

目前只有中英文混合这一种语言，优先中文发音。 示例： ” I bought 3 books” 发音 “three”; “ 3 books are bought” 发音 “三”; “我们买了 3 books” 发音“三”

## 标注发音

该功能适用于多音字或特殊名词的发音设置。 如： 重(chong2)报集团, “重”发音 chong第二声

## 在线和离线判别

以6s超时的MIX_MODE_DEFAULT和MIX_MODE_HIGH_SPEED_NETWORK为例 ![](https://ai.bdstatic.com/file/A873E4A0321D468C8CF8915CD7E41F69)用户可以选择纯在线模式或者离在线混合模式。 没有纯离线模式。

**纯在线模式** ： WIFI 4G 3G 2G 都会尝试连接百度服务器。如果百度服务器失败，那么合成失败。

**离在线混合模式**： WIFI下强制尝试在线优先。其它网络情况可以设置是否为在线优先（如果连接失败，那么切换成离线合成）或者直接离线合成。

*   _MIX_MODE_DEFAULT_： WIFI下在线优先（连接百度服务器失败或者超时6s，那么切换成离线合成）， 其它网络状况下离线合成。
*   _MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI_： WIFI下在线优先（连接百度服务器失败或者超时1.2s，那么切换成离线合成）， 其它网络状况下离线合成。
*   _MIX_MODE_HIGH_SPEED_NETWORK_： WIFI 4G 3G 2G下在线优先( 如果在线连接百度服务器失败或者超时6s，那么切换成离线合成)， 其它网络状况离线合成。
*   _MIX_MODE_HIGH_SPEED_SYNTHESIZE_： WIFI 4G 3G 2G下在线优先( 如果在线连接百度服务器失败或者超时1.2s，那么切换成离线合成)， 其它网络状况离线合成。

**建议使用场景**： 在小说阅读、导航播报等场景中，若存在网络信号不稳定(频繁断网）的情况，您可以使用百度提供的离在线融合模式。 我们推荐您使用 MIX_MODE_HIGH_SPEED_NETWORK模式；若您需要在移动网络下不想消耗流量，或是对响应速度有较强需求，请自行根据业务需求选择其他模式。

## 发音

**在线时支持5种发音** 普通女声 普通男声 特别男声 情感男声<度逍遥> 情感儿童声<度丫丫> 除特别男声外，其它4种发声具体效果可以在[http://ai.baidu.com/tech/speech/tts](http://ai.baidu.com/tech/speech/tts "http://ai.baidu.com/tech/speech/tts")上测试

**离线时只支持4种发音** 离线时无特别男声， 其它发音都有离线版本。

**注意：**

1.  在线合成的声音和离线合成的声音会有略微不同。在线合成的效果好。
2.  在极端网络的情况下，可能在线合成与离线合成频繁切换。

**合成效果** 通过对PARAM_SPEAKER（发音人）、PARAM_PITCH（音调）、PARAM_VOLUME（音量）和PARAM_SPEED（语速）参数的调整，可以获得不同的发声效果，更好满足您业务场景中的播报需求。 且音调越高，声音听起来会显得越年轻。

## 合成和播放

synthesize 方法直接合成。不播放。 开发者可以通过onSynthesizeDataArrived 获取音频数据，自行处理。 speak 方法先合成为音频，之后立即播放。等同调用 synthesize方法，再调用系统播放器。

在SDK内部中有队列，可以不断调用synthesize或者speak方法，将合成的文本添加到队列中。

## 其它事项

1.  每次合成的文本不超过1024 GBK字节，即512个汉字或者字母数字。
2.  合成的耗时同文本长度成正比。对合成速度敏感的话，请自行按照标点切分成短句。
3.  多音字可以通过标注自行定义发音。格式如：重(chong2)报集团。

# 接口及调用过程

## 简介

DEMO在SDK的基础上，封装了调用逻辑，您可以直接使用SDK，或者使用DEMO封装好SDK接口的类。也可以对比参考DEMO中对SDK的调用封装。 使用SDK方式的话，比较底层，开发者需要自行实现一部分逻辑。

*   SDK方式调用请参考_MiniActivity_
*   DEMO方式调用请参考_SynthActivity_

本文仅描述SDK的调用方式。

## 初始化

### 获取 SpeechSynthesizer 实例

```
SpeechSynthesizer mSpeechSynthesizer = SpeechSynthesizer.getInstance();

```

SpeechSynthesizer.getInstance(); 建议每次只使用一个实例。release方法调用后，可以使用第二个。

### 设置当前的Context

```
mSpeechSynthesizer.setContext(this); // this 是Context的之类，如Activity

```

注意 setContext只要在SpeechSynthesizer.getInstance();设置一次即可，不必切换Context时重复设置。

### 设置合成结果的回调

如合成成功后，SDK会调用用户设置的SpeechSynthesizerListener 里的回调方法

```
mSpeechSynthesizer.setSpeechSynthesizerListener(listener); //listener是SpeechSynthesizerListener 的实现类，需要实现您自己的业务逻辑。SDK合成后会对这个类的方法进行回调。

```

### 设置 App Id和 App Key 及 App Secret

用户在语音官网或者百度云网站上申请语音合成的应用后，会有appId appKey及appSecret

如：

```
String AppId = "8535996";
String AppKey = "MxPpf3nF5QX0pnd******cB";
String AppSecret = "7226e84664474aa09********b2aa434"

```

```
mSpeechSynthesizer.setAppId("8535996"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
mSpeechSynthesizer.setApiKey("MxPpf3nF5QX0pnd******cB",         "7226e84664474aa09********b2aa434"/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);

```

如果需要使用离线合成功能的话，请在申请的语音合成的应用填写您自己的包名： demo的包名是“com.baidu.tts.sample"， 定义在build.gradle中。

### 授权检验接口

测试您的AppId，AppKey AppSecret填写正确，语音合成服务是否开通。

*   离在线混合模式下，自动下载正式授权文件。每次调用时，可能会更新正式授权文件。
*   在线合成模式下，验证权限。首次验证时比较耗时，第一次调用成功，之后可以不必调用。

离在线混合模式下 ，检验应用里包名是否填写正确，如果正确，自动下载正式授权文件。如果不正确，请在应用管理页面检查合成服务是否开通，包名是否填写正确。

建议在app安装后，检查首次联网状态时调用该方法，成功之后就不必调用。 该方法也可以忽略，第一次在线合成时会自动调用。

```
mSpeechSynthesizer.auth(TtsMode.ONLINE);  // 纯在线
//或 mSpeechSynthesizer.auth(TtsMode.MIX); // 离在线混合

```

注意 demo的包名是com.baidu.tts.sample，定义在build.gradle文件中。

### 设置合成参数

具体参数请参见 “输入参数和输出回调”一节

可以在初始化设置，也可以在合成前设置。 示例：

```
mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0"); // 设置发声的人声音，在线生效

```

### 初始化合成引擎

设置合成的参数后，需要调用此方法初始化

```
mSpeechSynthesizer.initTts(TtsMode.MIX); // 初始化离在线混合模式，如果只需要在线合成功能，使用 TtsMode.ONLINE

```

## 控制接口

### 合成及播放接口

如果需要合成后立即播放的请调用speak方法，如果只需要合成请调用synthesize方法

该接口线程安全，可以重复调用。内部采用排队策略，调用后将自动加入队列，SDK会按照队列的顺序进行合成及播放。 注意需要合成的每个文本text不超过1024的GBK字节，即512个汉字或英文字母数字。超过请自行按照句号问号等标点切分，调用多次合成接口。

返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

speak方法示例：

```
 int speak(String text);
 int speak(String text, String utteranceId); // utteranceId在SpeechSynthesizerListener 相关事件方法中回调

speechSynthesizer.speak("百度一下");

```

synthesize方法示例：

```
int synthesize(String text);
int synthesize(String text, String utteranceId); // utteranceId在SpeechSynthesizerListener 相关事件方法中回调

speechSynthesizer.synthesize("百度一下");

```

调用这两个方法后，SDK会回调SpeechSynthesizerListener中的onSynthesizeDataArrived方法。 音频数据在byte[] audioData参数中，采样率16K 16bits编码 单声道。连续将audioData写入一个文件，即可作为一个可以播放的pcm文件（采样率16K 16bits编码 单声道）。

### 批量合成并播放接口

效果同连续调用speak 方法。 该接口可以批量传入多个文本并进行排队合成并播放（如果没有设置utteranceId，则使用list的索引值作为utteranceId）。 注意需要合成的每个文本text不超过1024的GBK字节，即512个汉字或英文字母数字。超过请自行按照句号问号等标点切分，放入多个SpeechSynthesizeBag

```
int batchSpeak(java.util.List<SpeechSynthesizeBag> speechSynthesizeBags)

```

以下为批量调用示例

```
List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
bags.add(getSpeechSynthesizeBag("123456", "0"));
bags.add(getSpeechSynthesizeBag("你好", "1"));
bags.add(getSpeechSynthesizeBag("使用百度语音合成SDK", "2"));
bags.add(getSpeechSynthesizeBag("hello", "3"));
bags.add(getSpeechSynthesizeBag("这是一个demo工程", "4"));
int result = mSpeechSynthesizer.batchSpeak(bags);

 private SpeechSynthesizeBag getSpeechSynthesizeBag(String text, String utteranceId) {
        SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
        //需要合成的文本text的长度不能超过1024个GBK字节。
        speechSynthesizeBag.setText(text);
        speechSynthesizeBag.setUtteranceId(utteranceId);
        return speechSynthesizeBag;
    }

```

返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

### 播放过程中的暂停及继续

仅speak方法调用后有效。可以使用pause暂停当前的播放。pause暂停后，可使用resume进行播放。

```
int result = mSpeechSynthesizer.pause();
int result = mSpeechSynthesizer.resume();

```

返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

### 停止合成并停止播放

取消当前的合成。并停止播放。

```
int result = mSpeechSynthesizer.stop();

```

返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

## 其它接口

### 释放资源

不再使用后，请释放资源，并将mSpeechSynthesizer设为null。如果需要再次使用，可以通过SpeechSynthesizer.getInstance() 获取，并重复上述流程。

```
int result = mSpeechSynthesizer.release();

```

返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

### 切换离线发音

切换离线发音人接口。 目前只有4种离线发声，用这个方法可以切换离线发音人。 离线合成时的参数，填入两个资源文件的路径。如果切换的话，也是使用这两个文件路径。

注意：必须在引擎空闲的时候调用这个方法，否则有不为0的错误码返回。空闲是指最后一个合成回调onSynthesizeFinish 之后。

```
int result = mSpeechSynthesizer.loadModel(speechModelPath,  textModelPath);

```

返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

### 设置音量

该接口用来设置播放器的音量，即使用speak 播放音量时生效。范围为[0.0f-1.0f]。

```
int result = mSpeechSynthesizer.setStereoVolume (leftVolume, rightVolume);

```

此接口与PARAM_VOLUME参数的设置不同，PARAM_VOLUME设置的是服务器合成音频时的音量，而该接口设置的是播放时Android系统的音量。 返回结果不为0，表示出错。错误码请参见“错误码及解决方法”一节

### 音频流类型

该接口用来设置播放器的音频流类型，默认值为AudioManager.STREAM_MUSIC, AudioManager.STREAM_MUSIC指的是用与音乐播放的音频流。 其它可选参数见Android 官方文档对AudioManager的描述

```
 int setAudioStreamType(int streamType)

```

### 打开调试日志

```
LoggerProxy.printable(true); // 日志打印在logcat中

```

## SpeechSynthesizerListener回调方法

请参见 “输入参数和输出回调”一节

# 输入参数和输出回调

## 离线及在线选择

```
int initTts(TtsMode ttsMode);

```

初始化tts引擎，可以指定纯在线，离在线混合引擎。

*   TtsMode.ONLINE 纯在线。断网即不可使用。
*   TtsMode.MIX 离在线混合。 断网或者网络请求超时使用离线。

## 网络状况及离在线合成引擎：

## 合成参数

在SpeechSynthesizer类中setParam 方法中使用的参数及值。 填入的值如果不在范围内，相当于没有填写使用默认值。

| 参数名 | 类型，值 | 在线/离线生效 | 常用程度 | 解释 |
| --- | --- | --- | --- | --- |
| PARAM_SPEAKER | 选项 | 在线 | 常用 | 仅在线生效，在线的发音 |
| ~ | "0"（默认） | ~ | ~ | 度小美（标准女声） |
| ~ | "1" | ~ | ~ | 度小宇（标准男声） |
| ~ | "3" | ~ | ~ | 度逍遥（情感男声） |
| ~ | "4" | ~ | ~ | 度丫丫（情感儿童声） |
| ~ | "106" | ~ | ~ | 度博文（情感男声） |
| ~ | "110" | ~ | ~ | 度小童（情感儿童声） |
| ~ | "111" | ~ | ~ | 度小萌（情感女声） |
| ~ | "103" | ~ | ~ | 度米朵（情感儿童声） |
| ~ | "5" | ~ | ~ | 度小娇（情感女声） |
| PARAM_VOLUME | String, 默认"5" | 全部 | 常用 | 在线及离线合成的音量 。范围["0" - "15"], 不支持小数。 "0" 最轻，"15" 最响。 |
| PARAM_SPEED | String, 默认"5" | 全部 | 常用 | 在线及离线合成的语速 。范围["0" - "15"], 不支持小数。 "0" 最慢，"15" 最快 |
| PARAM_PITCH | String, 默认"5" | 全部 | 常用 | 在线及离线合成的语调 。范围["0" - "15"], 不支持小数。 "0" 最低沉， "15" 最尖 |
| PARAM_MIX_MODE | 选项 | 全部 | 常用 | 控制何种网络状况切换到离线。 SDK没有纯离线功能，强制在线优先。设置initTts(SpeechSynthesizer.PARAM_MIX_MODE)后，该参数生效。 |
| ~ | MIX_MODE_
DEFAULT（默认） | ~ | ~ | WIFI 使用在线合成，非WIFI使用离线合成 ，6s超时 |
| ~ | MIX_MODE_HIGH
_SPEED_NETWORK | ~ | ~ | WIFI,4G,3G 使用在线合成，其他使用离线合成 ，6s超时 |
| ~ | MIX_MODE_HIGH
_SPEED_
SYNTHESIZE | ~ | ~ | 同MIX_MODE_HIGH_SPEED_NETWORK。但是连接百度服务器超时1.2s后，自动切换离线合成引擎 |
| ~ | MIX_MODE_HIGH_
SPEED_SYNTHESIZE
_WIFI | ~ | ~ | 同 MIX_MODE_DEFAULT。 但是连接百度服务器超时1.2s后，自动切换离线合成引擎 |
| PARAM_TTS_TEXT
_MODEL_FILE | String , 文件路径 | 离线 | 常用 | 文本模型文件路径，即bd_etts_text.dat所在的路径。 |
| PARAM_TTS_SPEECH
_MODEL_FILE | String , 文件路径 | 离线 | 常用 | 声学模型文件路径。即"bd_etts_speech_female.dat“所在的路径。需要男声，请使用bd_etts_speech_male.dat。 |
| PARAM_AUDIO
_ENCODE | 选项 | 在线 | 基本不用 | 不使用改参数即可。SDK与服务器音频传输格式，与 PARAM_AUDIO_RATE参数一起使用。可选值为SpeechSynthesizer.AUDIO_ENCODE_*， 其中SpeechSynthesizer.AUDIO_ENCODE_PCM为不压缩 |
| PARAM_AUDIO
_RATE | 选项 | 在线 | 基本不用 | 不使用改参数即可。SDK与服务器音频传输格式，与 PARAM_AUDIO_ENCODE参数一起使用。可选值为SpeechSynthesizer.AUDIO_BITRATE_*, 其中SpeechSynthesizer.AUDIO_BITRATE_PCM 为不压缩传输 |
| PARAM_VOCODER_
OPTIM_LEVEL | 选项, 默认"0" | 离线 | 基本不用 | 离线合成引擎速度优化等级。取值范围["0", "2"]，值越大速度越快，但是效果越差 |
| PARAM_TTS_
LICENCE_FILE | String , 文件路径 | 离线 | 基本不用 | 临时授权文件。目前SDK会自动下载正式授权文件。正式授权文件有效期3年，并在最后一个月SDK自动更新下载授权文件。 |

## 输出回调接口

SpeechSynthesizerListener 中，SDK会根据合成的状态及数据调用这个接口中的不同方法。

SDK使用的是边获取合成结果，边播放的方式。因此合成，播放的回调会交替进行。使用speak方法时，都会产生合成和播放的回调。 如果使用synthesize方法，则只会产生合成的回调，需要用户自行处理onSynthesizeDataArrived中的audioData的音频数据。

**其中回调方法中的utteranceId是调用speak或者synthesize合成方法时输入的，默认是"0"。**

### 合成开始

本次合成过程开始时，SDK的回调

```
void onSynthesizeStart (String utteranceId);

```

### 合成过程中的数据回调接口

合成数据过程中的回调接口，返回合成数据和进度，分多次回调。

```
 void onSynthesizeDataArrived(String utteranceId, byte[] audioData, int progress);

```

*   audioData: 合成的部分数据，可以就这部分数据自行播放或者顺序保存到文件。如果保存到文件的话，是一个pcm可以播放的音频文件。 音频数据是16K采样率，16bits编码，单声道。

*   progress 大致进度。从0 到 “合成文本的字符数”。

### 合成结束

本次合成正常结束状态时，SDK的回调

```
 void onSynthesizeFinish (String utteranceId);

```

### 播放开始

SDK开始控制播放器播放合成的声音。如果使用speak方法会有此回调，使用synthesize没有。

```
void onSpeechStart (String utteranceId);

```

### 播放过程中的回调

播放数据过程中的回调接口，分多次回调。 如果使用speak方法会有此回调，使用synthesize没有。

```
void onSpeechProgressChanged(String utteranceId, int progress);

```

*   progress 大致进度。从0 到 “合成文本的字符数”。

### 播放结束

播放正常结束状态时的回调方法，如果过程中出错，则回调onError，不再回调此接口。

```
 void onSpeechFinish (String utteranceId);

```

### 合成和播放过程中出错时的回调

合成和播放过程中出错时回调此接口

```
onError(String utteranceId，SpeechError error);

```

SpeechError 类有2个值：

*   code：int，错误码。 具体错误码见“错误码及解决方法”一节
*   description： String, 具体的错误信息。

# 集成指南

*   DEMO 中已经集成了 SDK。您可以参考DEMO，集成SDK。
*   集成前，请先测通DEMO，了解调用原理。
*   如果您自己代码过于复杂，可以使用一个helloworld项目了解集成过程。 本文以Android Studio 2.3.3作为示例

## AndroidManifest.xml 文件

设置权限：

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />

```

## 设置AppId, AppKey, SecretKey

```
mSpeechSynthesizer.setAppId(appId);
mSpeechSynthesizer.setApiKey(appKey, secretKey);

```

## android 6.0 以上版本权限申请

以下代码可以在demo中查找

```
/**
 * android 6.0 以上需要动态申请权限
  */
 private void initPermission() {
     String permissions[] = {
             Manifest.permission.INTERNET,
             Manifest.permission.ACCESS_NETWORK_STATE,
             Manifest.permission.MODIFY_AUDIO_SETTINGS,
             Manifest.permission.WRITE_EXTERNAL_STORAGE,
             Manifest.permission.WRITE_SETTINGS,
             Manifest.permission.READ_PHONE_STATE,
             Manifest.permission.ACCESS_WIFI_STATE,
             Manifest.permission.CHANGE_WIFI_STATE
     };

     ArrayList<String> toApplyList = new ArrayList<String>();

     for (String perm : permissions) {
         if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
             toApplyList.add(perm);
             //进入到这里代表没有权限.
         }
     }
     String tmpList[] = new String[toApplyList.size()];
     if (!toApplyList.isEmpty()) {
         ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
     }

 }

 @Override
 public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
     // 此处为android 6.0以上动态授权的回调，用户自行实现。
 }

```

## 引入jar包

**com.baidu.tts_2.3.1.*.jar 库** 将app/libs/com.baidu.tts_2.3.1.20170808_e39ea89.jar复制到您的项目的同名目录中。确认在build.gradle文件中引入。

## 复制NDK 架构目录

1.  将 app/src/main/jniLibs 下armeabi等5个目录，复制到您的项目的同名目录中。
2.  如与第三方库集成，至少要保留armeabi目录。如第三方库有7个架构目录，比语音识别SDK多出2个目录 mips和mips64，请将mips和mips64目录删除，剩下5个同名目录合并。
3.  如第三方库仅有armeabi这一个目录，请将语音识别SDK的额外4个目录如armeabi-v7a删除,合并armeabi目录下的so。 即目录取交集，so文件不可随意更改所属目录。
4.  打包成apk文件，按照zip格式解压出libs目录可以验证。

## build.gradle 文件及包名确认

1.  根目录下build.gradle确认下gradle的版本。
2.  app/build.gradle 确认下 applicationId 包名是否与官网申请应用时相一致（离线功能需要）。 demo的包名是"com.baidu.tts.sample"。
3.  确认 compileSdkVersion buildToolsVersion 及 targetSdkVersion

## proguard文件

```
    -keep class com.baidu.tts.**{*;}
    -keep class com.baidu.speechsynthesizer.**{*;}

```

## BEST PRACTICE

*   请先测通DEMO，了解DEMO的功能，**代码的运行原理**后再集成。DEMO有bug，请立即反馈。
*   对应任何第三方库，从一开始集成，边开发边测试，不要等所有功能都开发完再集成。否则一旦有问题，难以隔离排查。
*   有问题先与DEMO做对比。DEMO有bug，请查看错误码文档，如无法解决请立即反馈；DEMO无bug，自身代码有问题，请设置同样的输入参数后，对比两边代码及日志，自行排查问题。

# 错误码

生成错误码共2处位置：

*   调用接口的方法时的返回，如initTTs方法的返回
*   ​onError(String utteranceId，SpeechError error); SpeechError 中的code

## 错误码

| 错误码值 | 错误码描述 |
| --- | --- |
| -1 | 在线引擎授权失败 |
| -2 | 在线合成请求失败 |
| -3 | 在线合成停止失败 |
| -4 | 在线授权中断异常 |
| -5 | 在线授权执行时异常 |
| -6 | 在线授权时间超时 |
| -7 | 在线合成返回错误信息 |
| -8 | 在线授权token为空 |
| -9 | 在线引擎没有初始化 |
| -10 | 在线引擎合成时异常 |
| -100 | 离线引擎授权失败 |
| -101 | 离线合成停止失败 |
| -102 | 离线授权下载License失败 |
| -103 | 离线授权信息为空 |
| -104 | 离线授权类型未知 |
| -105 | 离线授权中断异常 |
| -106 | 离线授权执行时异常 |
| -107 | 离线授权执行时间超时 |
| -108 | 离线合成引擎初始化失败 |
| -109 | 离线引擎未初始化 |
| -110 | 离线合成时异常 |
| -111 | 离线合成返回值非0 |
| -112 | 离线授权已过期 |
| -113 | 离线授权包名不匹配 |
| -114 | 离线授权签名不匹配 |
| -115 | 离线授权设备信息不匹配 |
| -116 | 离线授权平台不匹配 |
| -117 | 离线授权的license文件不存在 |
| -200 | 混合引擎离线在线都授权失败 |
| -201 | 混合引擎授权中断异常 |
| -202 | 混合引擎授权执行时异常 |
| -203 | 混合引擎授权执行时间超时 |
| -204 | 在线合成初始化成功，离线合成初始化失败。 可能是离线资源dat文件未加载或包名错误 |
| -300 | 合成文本为空 |
| -301 | 合成文本长度过长（不要超过GBK1024个字节） |
| -302 | 合成文本无法获取GBK字节 |
| -400 | TTS未初始化 |
| -401 | TTS模式无效 |
| -402 | TTS合成队列已满（最大限度为1000） |
| -403 | TTS批量合成文本过多（最多为100） |
| -404 | TTS停止失败 |
| -405 | TTS APP ID无效 |
| -406 | TTS被调用方法参数无效 |
| -500 | Context被释放或为空 |
| -600 | 播放器为空 |
| -9999 | 未知错误 |

## 错误反馈