package io.github.akumosstl.automation;

import io.github.akumosstl.app.SwingApp;
import io.github.akumosstl.automation.execute.Executor;
import io.github.akumosstl.automation.record.Recorder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RecorderExecutorITTest {

    @Test
    public void testRecordingActivation() throws InterruptedException {
        SwingApp.main(null);
        Thread.sleep(2000);
        Recorder.startRecording();

        Assert.assertTrue(Recorder.recording.get());

        Recorder.stopRecording();

        Assert.assertFalse(Recorder.recording.get());

    }

    @Test
    public void testJsonRecording() throws InterruptedException, IOException {
        SwingApp.main(null);
        Recorder.startRecording();

        Thread.sleep(2000);

        Assert.assertTrue(Recorder.recording.get());

        Thread.sleep(10000);
        Recorder.stopRecording();
        SwingApp.stop();

    }

    @Test
    public void testJsonExecuting() throws InterruptedException, IOException {
        SwingApp.main(null);

        Thread.sleep(2000);

        String filePath = "queue.json";

        List<String> strings = Files.readAllLines(Paths.get(filePath));
        new Executor().execute(String.join("", strings));

        Thread.sleep(5000);

    }

}