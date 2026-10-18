package com.learning.automation.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ScenarioContext provides a lightweight, thread-safe key-value store
 * to pass data dynamically between different Cucumber step definitions
 * within the lifecycle of a single scenario.
 *
 * For example: Saving a generated order ID, auth token, or dynamic user email
 * in one Step Definition class and retrieving it in another Step Definition class.
 *
 * In our current single-class scenario, direct Page Object reference is sufficient,
 * but ScenarioContext is included as an architectural building block for multi-step
 * enterprise BDD test suites.
 */
public class ScenarioContext {

    private final Map<String, Object> contextData = new HashMap<>();

    /**
     * Stores an object in the scenario context by key.
     *
     * @param key the identifier string
     * @param value the data object to store
     */
    public void setContext(String key, Object value) {
        contextData.put(key, value);
    }

    /**
     * Retrieves an object from the scenario context by key.
     *
     * @param key the identifier string
     * @return the stored object, or null if key does not exist
     */
    public Object getContext(String key) {
        return contextData.get(key);
    }

    /**
     * Checks if a key exists in the scenario context.
     *
     * @param key the identifier string
     * @return true if the key exists
     */
    public boolean contains(String key) {
        return contextData.containsKey(key);
    }

    /**
     * Clears all stored data at the end of scenario execution.
     */
    public void clearContext() {
        contextData.clear();
    }
}
