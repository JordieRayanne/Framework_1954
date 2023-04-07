package etu1954.framework;

import java.util.HashMap;

/**
 *
 * @author ETU1954
 */
public class Modelview {
    String view;
    HashMap<String, Object> data;

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
}
