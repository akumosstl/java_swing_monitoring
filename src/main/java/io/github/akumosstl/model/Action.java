package io.github.akumosstl.model;


public class Action {
    private String componentType;
    private String componentId;
    private String inputValue;
    private Long timeToNext;
    private String eventType;
    private int x;
    private int y;

    public Action() {
    }

    public Action(String componentType, String componentId, String inputValue, Long timeToNext, String eventType, int x, int y) {
        this.componentType = componentType;
        this.componentId = componentId;
        this.inputValue = inputValue;
        this.timeToNext = timeToNext;
        this.eventType = eventType;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public Long getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(Long timeToNext) {
        this.timeToNext = timeToNext;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
