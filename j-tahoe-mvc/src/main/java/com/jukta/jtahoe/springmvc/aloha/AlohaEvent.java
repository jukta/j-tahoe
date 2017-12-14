package com.jukta.jtahoe.springmvc.aloha;

/**
 * Created by Sergey on 12/14/2017.
 */
public class AlohaEvent {
    private String eventName;
    private Object data;

    public AlohaEvent() {
    }

    public AlohaEvent(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }
}
