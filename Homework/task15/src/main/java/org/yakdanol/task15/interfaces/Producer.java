package org.yakdanol.task15.interfaces;

public interface Producer {

    long produceMessageAndMeasureLatency(String topic, String message);

    void produceMessage(String topic, String message);

    void stopProducer();
}

