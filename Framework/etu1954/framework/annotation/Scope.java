package etu1954.framework.annotation;

import java.lang.annotation.*;
/**
 *
 * @author ETU1954
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    boolean singleton();
}
