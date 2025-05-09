package org.akazukin.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field is mutable,
 * meaning that its state can be changed after the object is constructed.
 * This annotation serves as a marker to convey mutability at the design level.
 * <p>
 * The retention policy is set to CLASS, meaning this annotation will be present
 * in the compiled class file but will not be available at runtime via reflection.
 * It can only be applied to fields.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface Mutable {
}
