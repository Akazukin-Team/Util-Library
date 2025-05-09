package org.akazukin.util.interfaces;

/**
 * Represents a contract for reloading the state or configuration of an object.
 * Classes implementing this interface are expected to provide an implementation
 * of the reload method, which defines how the object refreshes or updates its state.
 */
public interface Reloadable {
    /**
     * Reloads the state or configuration of the implementing object.
     * This method is used to refresh or update the object's current state,
     * ensuring it reflects the latest data or configuration. The specific
     * behavior of the reload operation is defined by the implementing class.
     */
    void reload();
}
