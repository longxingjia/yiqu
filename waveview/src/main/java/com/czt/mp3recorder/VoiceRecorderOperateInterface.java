package com.czt.mp3recorder;

public interface VoiceRecorderOperateInterface {
    public void recordVoiceBegin();

    public void recordVoiceStateChanged(int volume, long recordDuration);

    public void prepareGiveUpRecordVoice();

    public void recoverRecordVoice();

    public void giveUpRecordVoice();

    public void recordVoiceFail();

    public void recordVoiceFinish();
}
