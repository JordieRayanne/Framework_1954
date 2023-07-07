# Framework_1954
PACKAGE:

import etu1954.framework.annotation.Auth;
import etu1954.framework.annotation.MyUrl;
import etu1954.framework.annotation.Session;
import etu1954.framework.annotation.restAPI;
import etu1954.framework.ModelView;
import etu2025.framework.FileUpload;

FORMAT DU WEB.XML: par defaut

<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1954.framework.servlet.FrontServlet</servlet-class>
        <init-param>
            <param-name>session-name-isconnected</param-name>
            <param-value>isconnected</param-value>
        </init-param>
        <init-param>
            <param-name>session-name-profile</param-name>
            <param-value>profile</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
</web-app>

PACKAGE DU MODEL:
etu1954.framework.models;

FORME MODEL:
public class Model {
    // url: une annotation qui décrit quelle Url mènent vers la fonction
    // la fonction doit impérativement retourner un ModelView
    @MyUrl.MyURL(url = "/my-url")
    public ModelView nameFunction() {
        ModelView mv = new ModelView("view_name.jsp");
        return mv;
    }
}

AJOUT ATTRIBUT DANS LE MODEL:
// Le nom dans les formulaires doivent correspondre au nom d' attribut)
// Les types d'attributs ne doivent surtout pas être des types primitives
// getter et setter requis
public class Model {
    String nom;
    int age;
    Double poids;
    Date date;
    UploadFile upload;
}

POUR LA METHODE QUI SET lA VALEUR DES ATTRIBUTS D'UNE MODEL:
// les parametre du methode doit être annoté par @MyParam(name = "nom") avec 'name' le nom de l'attribut dans la classe
 @MyURL(url = "/Model-save")  //url= le nom du modele plus -save
    public void Methodname(@MyParam(name = "nom") String nom,@MyParam(name = "age") Integer[] age,@MyParam(name = "ville") String ville) {
        System.out.println("Nom:" + nom);
        System.out.println("Age:" + age[0]);
        System.out.println("Ville:" + ville);
        System.out.println("this date:" + this.getDate());
    }

  ENVOIE DE VALEUR VERS LA VUE:
   @MyUrl.MyURL(url = "/my-url")
    public Modelview Methodname() {
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

AJOUT SESSION:
@url("/url.action")
public ModelView nameFunction() {
    ModelView mv = new ModelView("view_name.jsp");
    Object object = new Object();
    mv.addSession("session_name", object);
    return mv;
}

RECUPERER SESSION:
public class Model {
    HashMap<String, Object> session = new HashMap<>(); // doit inclure attributs et ses getters et setters
    // Les fonctions voulant utilisés les sessions doit inclure cette annotation
    @session
    @url("/url.action")
    public ModelView nameFunction() {
        ModelView mv = new ModelView("view_name.jsp");
        Object object = getSession().get("session_name");
        return mv;
    }
}

REMOVE SESSION: 
public class Model {
    @url("/url.action")
    public ModelView nameFunction() {
        ModelView mv = new ModelView("view_name.jsp");
        mv.addRemovingSession("session_name");
        return mv;
    }
}

REMOVE ALL SESSION:
@url("/url.action")
public ModelView nameFunction() {
    ModelView mv = new ModelView("view_name.jsp");
    mv.invalidateSession(true);
    return mv;
}

AUTHENTIFICATION:
//  la fonction est toujours accessible
@url("/login.action")
public ModelView login() {
    ModelView mv = new ModelView("loged.jsp");
    mv.addSession("profile", "admin");
    mv.addSession("isconnecter", true);
    return mv;
}

//  la fonction est toujours accessible
@auth
@url("/login.action")
public ModelView login() {
    ModelView mv = new ModelView("logout.jsp");
    mv.invalidateSession(true);
    return mv;
}

//  le profile de l'utilisateur connecté doit être admin
@auth("admin")
@url("/url.action")
public ModelView nameFunction() {
    ModelView mv = new ModelView("view_name.jsp");
    return mv;
}

//  l'utilisateur doit être connecté
@auth
@url("/url.action")
public ModelView nameFunction() {
    ModelView mv = new ModelView("view_name.jsp");
    return mv;
}

// changement dans web.xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1954.framework.servlet.FrontServlet</servlet-class>
        <init-param>
            <param-name>model-package</param-name>
            <param-value>etu2025.model</param-value>
        </init-param>
        <init-param>
            <param-name>session-name-isconnected</param-name> 
            <param-value>isconnected</param-value> // modifiable, le nom de la session pour dire qu'un utilisateur est connecté
        </init-param>
        <init-param>
            <param-name>session-name-profile</param-name>
            <param-value>profile</param-value> // modifiable, le nom de la session pour dire le profile de l'utilisateur connecté
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
</web-app>

UPLOAD FILE:
public class Model {
    FileUpload myFiles; // plus getter et setter
    // Les fonctions voulant utilisés les sessions doit inclure cette annotation
    @MyUrl.MyURL(url = "/url-act")
    public ModelView nameFunction() {
        ModelView mv = new ModelView("view_name.jsp");
        Object object = getSession().get("session_name");
        return mv;
    }
} 
avec le web.xml:
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1954.framework.servlet.FrontServlet</servlet-class>
        <init-param>
            <param-name>session-name-isconnected</param-name>
            <param-value>isconnected</param-value>
        </init-param>
        <init-param>
            <param-name>session-name-profile</param-name>
            <param-value>profile</param-value>
        </init-param>
        <multipart-config>
        <!-- Taille maximale des données multipart en octets -->
        <max-file-size>5242880</max-file-size>
        <!-- Taille maximale totale des données multipart en octets -->
        <max-request-size>10485760</max-request-size>
        <!-- Emplacement où les fichiers temporaires seront stockés -->
    </multipart-config>
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
</web-app>

SINGLETON:
// Une classe singleton est une classe qui n'est instancier qu'une fois
@Scope(singleton =true)
public class Model {
    FileUpload myFiles; // plus getter et setter
    // Les fonctions voulant utilisés les sessions doit inclure cette annotation
    @session
    @url("/url.action")
    public ModelView nameFunction() {
        ModelView mv = new ModelView("view_name.jsp");
        Object object = getSession().get("session_name");
        return mv;
    }
}

JSON AVEC MODELVIEW:
@url("/json.action")
public ModelView nameFunction() {
    ModelView mv = new ModelView();
    mv.activeJSON(); // Cette fonction permet de dire au framework de renvoyer au format JSON les Items envoyés
    mv.addItem("name", "Tiavina");
    mv.addItem("last_name", "Malalaniaina");
    return mv;
}

JSON SANS MODELVIEW:
@restAPI //Cette annotation renvoie le JSON de la valeur de retour de la fonction
@url("/apirest.action")
public Object apiRest() {
    return this;
}


