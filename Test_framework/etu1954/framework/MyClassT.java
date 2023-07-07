package etu1954.framework;

import etu1954.framework.annotation.MyUrl;

import java.util.HashMap;

import etu1954.framework.Modelview;


public class MyClassT {
    HashMap<String, Object> session = new HashMap<>();
    String username;
    String mdp;

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @MyUrl.MyURL(url = "/login-act")
    public Modelview logIn() {
        String username = getUsername();
        String mdp = getMdp();
        Modelview mv = new Modelview();
        mv.setView("connect.jsp");
        if (username.equals("admin") && mdp.equals("admin")) {
            mv.addSession("isconnected", true);
            mv.addSession("profile", "admin");
            mv.addItem("user", "ADMIN");
            return mv;
        }
        if (username.equals("user") && mdp.equals("user")) {
            mv.addSession("isconnected", true);
            mv.addSession("profile", "user");
            mv.addItem("user", "USER");
            return mv;
        }
        return new Modelview("not_connect.jsp");
    } 
}
