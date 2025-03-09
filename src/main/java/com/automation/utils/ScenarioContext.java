package com.automation.utils;

import java.util.HashMap;
import java.util.Map;

import com.automation.Enums.Context;

public class ScenarioContext {

    private final Map<String, Object> scenarioContext;

    public ScenarioContext() {
        scenarioContext = new HashMap<>();
    }

    public void setContext(Context key, Object value) {
        scenarioContext.put(key.toString(), value);
    }

    public Object getContext(Context key) {
        return scenarioContext.get(key.toString());
    }

    public Boolean isContains(Context key) {
        return scenarioContext.containsKey(key.toString());
    }
    
    /**
     * Clears all context values, resetting the scenario context to its initial state.
     * This is typically called during test cleanup to ensure a clean state for the next test.
     */
    public void clear() {
        scenarioContext.clear();
    }
}
