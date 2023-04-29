package etu1954.framework;

/**
 *
 * @author ETU1954
 */
public class Mapping {
    String className;
    String Method;

    public Mapping(String className, String method) {
        this.setClassName(className);
        this.setMethod(method);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String Method) {
        this.Method = Method;
    }

}
