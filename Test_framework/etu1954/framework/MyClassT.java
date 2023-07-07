package etu1954.framework;

import etu1954.framework.annotation.Auth;
import etu1954.framework.annotation.MyUrl;
import etu1954.framework.annotation.Session;
import etu1954.framework.annotation.restAPI;

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

    @Auth("admin")
    @MyUrl.MyURL(url ="/session-act")
    public Modelview emp(String id, String dept) {
        Modelview mv = new Modelview("list.jsp");
        mv.addItem("first_name", id);
        mv.addItem("last_name", dept);
        return mv;
    }

    @Auth
    @MyUrl.MyURL(url ="/find-act")
    public Modelview findAll() {
        Modelview mv = new Modelview("list.jsp");
        mv.addItem("first_name", "you");
        mv.addItem("last_name", "youyou");
        return mv;
    }

    @Session
    @MyUrl.MyURL(url ="/session-act")
    public Modelview testSession(String key, Object value) {
        Modelview mv = new Modelview("session.jsp");
        mv.addSession(key, value);
        System.out.println("SESSION->"+getSession());
        return mv;
    }

    @restAPI
    @MyUrl.MyURL(url ="/apirest-act")
    public Object apiRest() {
        return this;
    }

    @Session
     @MyUrl.MyURL(url ="/removing-session-act")
    public Modelview removeSession(String key) {
        Modelview mv = new Modelview("session.jsp");
        mv.addRemovingSession("test-session");
        System.out.println("SESSION->"+getSession());
        return mv;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
}
