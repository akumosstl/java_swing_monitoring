package io.github.akumosstl.model;

import java.awt.*;

public class ComponentJson {

    private String id;
    private Object component;

    public ComponentJson(Object c) {
        setId(ID.of((Component) c));
        setComponent((Component) c);
    }

    public ComponentJson() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
