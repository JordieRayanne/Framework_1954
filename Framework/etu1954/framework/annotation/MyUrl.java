package etu1954.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author ETU1954
 */
public class MyUrl {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)

    public @interface MyURL {
        String url();
    }

}
