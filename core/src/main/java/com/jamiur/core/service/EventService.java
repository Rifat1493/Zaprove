package com.jamiur.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final StreamBridge streamBridge;

    public <T> void sendEvent(String bindingName, T eventPayload) {
        log.info("Sending event to binding [{}]: {}", bindingName, eventPayload);
        streamBridge.send(bindingName, eventPayload);
    }
}