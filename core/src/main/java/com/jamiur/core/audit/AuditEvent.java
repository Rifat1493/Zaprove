package com.jamiur.core.audit;

import java.util.Map;

public record AuditEvent(String action, Map<String, Object> details) {
}