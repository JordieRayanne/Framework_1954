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

import etu1954.framework.Mapping;
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
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Liste des mappings :</h1>");
        for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            out.println("<p>URL: " + entry.getKey() + "</p>");
            out.println("<p>Class: " + entry.getValue().getClassName() + "</p>");
            out.println("<p>Method: " + entry.getValue().getMethod() + "</p><br>");
        }
        out.println("</body></html>");
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
