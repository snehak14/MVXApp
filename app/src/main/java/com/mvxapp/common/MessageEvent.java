package com.mvxapp.common;

public class MessageEvent {

    private Object syncPojos;
    private Object syncPojos1;
    private byte eventTrack;
    private int position;

    public MessageEvent(byte eventTrack, Object syncPojos) {
        this.syncPojos = syncPojos;
        this.eventTrack = eventTrack;
    }

    public MessageEvent(byte eventTrack, Object syncPojos, int position) {
        this.syncPojos = syncPojos;
        this.eventTrack = eventTrack;
        this.position = position;
    }

    public MessageEvent(byte eventTrack, Object syncPojos, Object syncPojos1) {
        this.syncPojos = syncPojos;
        this.eventTrack = eventTrack;
        this.syncPojos1 = syncPojos1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getSyncPojos() {
        return syncPojos;
    }

    public void setSyncPojos(Object syncPojos) {
        this.syncPojos = syncPojos;
    }

    public Object getSyncPojos1() {
        return syncPojos1;
    }

    public void setSyncPojos1(Object syncPojos1) {
        this.syncPojos1 = syncPojos1;
    }

    public byte getEventTrack() {
        return eventTrack;
    }

    public void setEventTrack(byte eventTrack) {
        this.eventTrack = eventTrack;
    }
}
