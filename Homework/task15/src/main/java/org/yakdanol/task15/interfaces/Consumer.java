package org.yakdanol.task15.interfaces;

public interface Consumer {

    long consumeMessageAndMeasureLatency();

    void consumeMessage();

    void stopConsumer();
}

