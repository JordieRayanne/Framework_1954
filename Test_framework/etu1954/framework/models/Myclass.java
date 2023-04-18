package etu1954.framework.models;

import java.util.Date;

import etu1954.framework.Modelview;
import etu1954.framework.annotation.MyUrl;
import etu1954.framework.annotation.MyUrl.MyURL;

/**
 *
 * @author ETU1954
 */
public class Myclass {
    String nom;
    int age;
    Double poids;
    Date date;

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

    @MyURL(url = "/Myclass-save")
    public void save() {
        System.out.println("Nom:" + this.getNom() + "age:" + this.getAge());
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
