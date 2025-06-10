package io.github.akumosstl.automation.record;

public class RecorderQueue implements Runnable {

    @Override
    public void run() {
        Recorder.startRecording();
    }

}
