package io.github.akumosstl.automation.execute;

public class ExecutorQueue implements Runnable {

    private final String json;

    public ExecutorQueue(String json) {
        this.json = json;
    }

    @Override
    public void run() {
        new Executor().execute(json);
    }
}
