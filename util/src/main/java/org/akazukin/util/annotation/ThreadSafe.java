package org.akazukin.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class is thread-safe.
 * <p>
 * A thread-safe class ensures that its instances can be used safely by multiple
 * threads concurrently without the need for external synchronization. This annotation
 * serves as a marker to convey thread safety at the design level and is primarily for
 * documentation purposes. It does not enforce thread-safety at runtime or compile time.
 * <p>
 * The retention policy is set to CLASS, meaning this annotation will be present in
 * the compiled class file but will not be available at runtime via reflection.
 * It can only be applied to types.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface ThreadSafe {
}
