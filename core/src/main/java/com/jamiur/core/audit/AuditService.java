package com.jamiur.core.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final StreamBridge streamBridge;

    public void sendAuditEvent(String action, Map<String, Object> details) {
        AuditEvent event = new AuditEvent(action, details);
        streamBridge.send("audit-out-0", event);
    }
}