package etu1954.framework;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ETU1954
 */
public class Modelview {
    String view;
    HashMap<String, Object> data;
    private HashMap<String, Object> session = new HashMap<>();
    private ArrayList<String> removingSession = new ArrayList<>();
    private boolean JSON = false;
    private boolean invalidateSession = false;

    public boolean isInvalidateSession() {
        return invalidateSession;
    }

    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }

    public ArrayList<String> getRemovingSession() {
        return removingSession;
    }

    public void setRemovingSession(ArrayList<String> removingSession) {
        this.removingSession = removingSession;
    }

    public void addSession(String key, Object value) {
        getSession().put(key, value);
    }

    public Modelview(String view) {
        setView(view);
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }


    public Modelview() {
        this.data = new HashMap<>();
    }

    public void addItem(String key, Object value) {
        this.data.put(key, value);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean isJSON() {
        return JSON;
    }

    public void setJSON(boolean jSON) {
        JSON = jSON;
    }
}
