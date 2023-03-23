package etu1954.framework.models;

import etu1954.framework.annotation.MyUrl;

/**
 *
 * @author ETU1954
 */
public class Myclass {
    @MyUrl.MyURL(url = "/my-url")
    public void myMethod() {
        System.out.println("Hello from myMethod!");
    }
}
