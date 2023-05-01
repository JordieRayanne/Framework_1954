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
        try {
            view.setView("hello.jsp");
            view.addItem("nom", "soa");
            view.addItem("age", 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
