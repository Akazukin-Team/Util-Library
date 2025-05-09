package org.akazukin.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field serves as an identifier.
 * This annotation is typically used to mark fields
 * that uniquely identify an object or entity within a system.
 * <p>
 * The retention policy is set to CLASS, meaning this annotation will
 * be present in the compiled class file but will not be available at runtime
 * via reflection. It can only be applied to fields.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface Id {
}
