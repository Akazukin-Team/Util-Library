package org.akazukin.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class is not thread-safe.
 * <p>
 * This annotation serves as a marker to communicate that instances of the annotated
 * class should not be used concurrently from multiple threads without external synchronization.
 * It is a design-level annotation that provides additional insight into the intended
 * usage of a class and does not enforce thread-safety at runtime or compile time.
 * <p>
 * The retention policy is set to CLASS, meaning this annotation will be present in
 * the compiled class file but will not be available at runtime via reflection.
 * It can only be applied to types.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface NonThreadSafe {
}
