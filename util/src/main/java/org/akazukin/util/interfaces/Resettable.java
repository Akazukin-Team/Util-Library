package org.akazukin.util.interfaces;

/**
 * Represents a contract for resetting the state of an object.
 * Classes implementing this interface are expected to provide an implementation
 * of the reset method, which defines how the object is restored to its initial state.
 */
public interface Resettable {
    /**
     * Resets the current state of the object to its default or initial values.
     * <p>
     * This method is expected to clear or initialize the internal state, ensuring
     * the object behaves as if it has been newly created or reset.
     * The exact behavior is determined by the implementing class.
     */
    void reset();
}
