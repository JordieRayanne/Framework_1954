package etu1954.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import etu1954.framework.Mapping;
import etu1954.framework.Modelview;
import etu1954.framework.annotation.MyUrl.MyURL;

/**
 *
 * @author ETU1954
 */
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public void init() throws ServletException {
        HashMap<String, Mapping> mappings = this.getMappingsInDirectory();
        this.setMappingUrls(mappings);
    }

    private HashMap<String, Mapping> getMappingsInDirectory() {
        HashMap<String, Mapping> mappings = new HashMap<>();
        String directory = getServletContext().getRealPath("/WEB-INF/classes/etu1954/framework/models");
        File dir = new File(directory);
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                try {
                    String fileName = file.getName();
                    String classNameFromFile = fileName.substring(0, fileName.lastIndexOf("."));
                    Class<?> cls = Class.forName("etu1954.framework.models." + classNameFromFile);
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods) {
                        MyURL annotation = method.getAnnotation(MyURL.class);
                        if (annotation != null) {
                            String annotationName = annotation.url();
                            Mapping mapping = new Mapping(classNameFromFile, method.getName());
                            mappings.put(annotationName, mapping);
                        }
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }
        return mappings;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            String current = request.getRequestURI().replace(request.getContextPath(), "");
            response.getWriter().println("Current URI: " + current); // Debugging

            if (mappingUrls.containsKey(current)) {
                Mapping mapp = mappingUrls.get(current);
                Object result = executeMethod(mapp.getClassName(), mapp.getMethod());
                if (result instanceof Modelview) {
                    Modelview model = (Modelview) result;
                    forwardToView(request, response, model);
                } else {
                    out.println("Error: The method did not return a Modelview.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Une erreur est survenue : " + e.getMessage());
        }
    }

    private Object executeMethod(String className, String methodName) throws Exception {
        String packageName = "etu1954.framework.models";
        String fullClassName = packageName + "." + className;

        Object obj = Class.forName(fullClassName).getConstructor().newInstance();
        return obj.getClass().getMethod(methodName).invoke(obj);
    }

    private void forwardToView(HttpServletRequest request, HttpServletResponse response, Modelview model)
            throws ServletException, IOException {
        RequestDispatcher disp = request.getRequestDispatcher(model.getView());
        for (Map.Entry<String, Object> entry : model.getData().entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            request.setAttribute(key, value);
        }
        disp.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
