package etu1954.framework.models;

import etu1954.framework.Modelview;
import etu1954.framework.annotation.MyUrl;

/**
 *
 * @author ETU1954
 */
public class Myclass {
    @MyUrl.MyURL(url = "/my-url")
    public Modelview myMethod() {
        Modelview view = new Modelview();
        view.setView("hello.jsp");
        return view;
    }
}
