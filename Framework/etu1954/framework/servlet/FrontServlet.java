package etu1954.framework.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;

import etu1954.framework.Mapping;
import etu1954.framework.Modelview;
import etu1954.framework.UploadFile;
import etu1954.framework.annotation.MyParam;
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

    private static Object convertValue(String value, Class<?> type) {
        try {
            if (type == String.class) {
                return value;
            } else if (type == int.class || type == Integer.class) {
                return Integer.parseInt(value);
            } else if (type == long.class || type == Long.class) {
                return Long.parseLong(value);
            } else if (type == boolean.class || type == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (type == double.class || type == Double.class) {
                return Double.parseDouble(value);
            } else if (type == float.class || type == Float.class) {
                return Float.parseFloat(value);
            } else if (type == char.class || type == Character.class) {
                return value.charAt(0);
            } else {
                throw new IllegalArgumentException("Unsupported parameter type: " + type.getName());
            }
        } catch (NumberFormatException e) {
              // En cas d'erreur de conversion, afficher un message d'erreur
              System.err.println("Erreur de conversion pour la valeur " + value + " en type " + type.getSimpleName() + ": " + e.getMessage());
        }
        return null;    
        }

           //get name file upload    
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

    //get bytes file upload
    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteOutput.write(buffer, 0, bytesRead);
        }
        
        return byteOutput.toByteArray();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        UploadFile fileupload=null;

        PrintWriter out = response.getWriter();
        try {
            String current = request.getRequestURI().replace(request.getContextPath(), "");
            // response.getWriter().println("Current URI: " + current); // Debugging
              
            //upload file 
            if(request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")){
                List<Part> parts=(List<Part>) request.getParts();
                for (Part part : parts) {
                    if (part.getName().equals("file")) {
                        String fileName = getFileName(part);
                        byte[] fileBytes = readBytes(part.getInputStream());
                        fileupload=new UploadFile(fileName, fileBytes);
                                                    
                        break;
                    } 
                }
            }

            if (mappingUrls.containsKey(current)) {
                Mapping mapp = mappingUrls.get(current);

                 // get parameterNames
                    ArrayList<String> names = new ArrayList<String>();
                    Enumeration<String> paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = (String) paramNames.nextElement();
                        names.add(paramName);
                    }
                // --end

                String methodName = mapp.getMethod();
                String className = mapp.getClassName();
                String packageName = "etu1954.framework.models.";
                Class<?> clazz = Class.forName(packageName + className);
                Object obj = clazz.getConstructor().newInstance();

                Field[] fields = clazz.getDeclaredFields();
                // set value of field
                setMatchingFieldValue(request, response, names, fields, obj);
                // --end
                
                //if field class instanceof UploadFile
                for(Field field:fields){
                    if(field.getType()==UploadFile.class){
                        field.setAccessible(true);  // Utiliser setAccessible(true)
                        if (fileupload != null) {
                            field.set(obj, fileupload);
                            UploadFile uploadedFile=(UploadFile) field.get(obj);
                            response.getWriter().println("Nom du fichier : " + uploadedFile.getName()+"<br>");
                            response.getWriter().println("Taille du fichier : " + uploadedFile.getBytes().length + " bytes");
                        }
                    }
                }
                //--end

                String saveUrl = "/" + mapp.getClassName() + "-save";

                if (current.compareTo(saveUrl) == 0) {

                Method saveMethod = null;
                ArrayList<String> paramsMeth = new ArrayList<>();
                // Rechercher la méthode save dans la classe
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        saveMethod = method;

                        // Récupérer les annotations MyParam
                        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                        for (Annotation[] annotations : parameterAnnotations) {
                            for (Annotation annotation : annotations) {
                                if (annotation instanceof MyParam) {
                                    MyParam myParamAnnotation = (MyParam) annotation;
                                    String paramName = myParamAnnotation.name();
                                    paramsMeth.add(paramName);
                                }
                            }
                        }
                        break;
                    }
                }

                // Class<?>[] parameterTypes = saveMethod.getParameterTypes();
                ArrayList<Object> args = setMatchingMethodParamValue( request,paramsMeth,saveMethod,names);
                // List<Integer> ageList = Arrays.asList((Integer[]) args.get(1)); // Conversion du tableau en une liste
                // response.getWriter().println("args: [soa, " + ageList + ", null]");

                for (int i = 0; i < args.size(); i++) {
                    Object arg = args.get(i);
                    Parameter parameter = saveMethod.getParameters()[i];
                    Class<?> parameterType = parameter.getType();
                    
                    if (arg != null && !parameterType.isInstance(arg)) {
                        arg = convertValue(arg.toString(), parameterType);
                        args.set(i, arg);
                    }
                }

                response.getWriter().println("args: " +Arrays.toString(args.toArray()));
                // Invoker la méthode save avec les valeurs des paramètres
                saveMethod.invoke(obj, args.toArray());

            }else{
                Object result = executeMethod(mapp.getClassName(), mapp.getMethod());
                // return model view
                if (result instanceof Modelview){  
                    Modelview model = (Modelview) result;
                    forwardToView(request, response, model);
                }
                //--end
            }

        } else {
            out.println("Error: the url is false");
        }
    
    } catch (Exception e) {
        e.printStackTrace(out);
        // response.getWriter().println("Une erreur est survenue : " + e.getMessage());
    }
}         

    // set the value of a field if match with parameter name
    public static void setMatchingFieldValue(HttpServletRequest request, HttpServletResponse response,
            ArrayList<String> parameterName, Field[] fields, Object obj) throws ServletException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        for (String paramName : parameterName) {
            for (Field field : fields) {
                // out.println(paramName + "..." + field);
                if (compare(paramName, field)) {
                    String paramValue = request.getParameter(paramName);
                    field.setAccessible(true);

                    if (field.getType() == String.class) {
                        field.set(obj, paramValue);
                        out.println(field.getName() + ":" + field.get(obj));
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(obj, Integer.parseInt(paramValue));
                        out.println(field.getName() + ":" + field.get(obj));
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        field.set(obj, Double.parseDouble(paramValue));
                        out.println(field.getName() + ":" + field.get(obj));
                    } else if (field.getType() == Date.class) {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date daty = (Date) format.parse(paramValue);
                        field.set(obj, daty);
                        out.println(field.getName() + ":" + field.get(obj));
                    }
                }
            }
        }
    }

        // set the value of a field if match with parameter name for methode parameter
        public static ArrayList<Object> setMatchingMethodParamValue(HttpServletRequest request,ArrayList<String> paramsMeth,Method saveMethod,ArrayList<String> names) throws ServletException, Exception {
            ArrayList<Object> args = new ArrayList<>();
                        for (String methoparameter : paramsMeth) {
                            if (names.contains(methoparameter)) {
                                String[] parameterValues = request.getParameterValues(methoparameter);
                                Class<?> parameterType = saveMethod.getParameterTypes()[paramsMeth.indexOf(methoparameter)];
                        
                                if (parameterType.isArray()) {
                                    Object[] parameterArray = (Object[]) Array.newInstance(parameterType.getComponentType(), parameterValues.length);
                                    for (int i = 0; i < parameterValues.length; i++) {
                                        String parameterValue = parameterValues[i];
                                        Object elementValue = convertValue(parameterValue, parameterType.getComponentType());
                                        parameterArray[i] = elementValue;
                                    }
                                    args.add(parameterArray);
                                } else {
                                    if (parameterType.isAssignableFrom(List.class)) {
                                        List<Object> parameterList = new ArrayList<>();
                                        for (String parameterValue : parameterValues) {
                                            Object elementValue = convertValue(parameterValue, parameterType);
                                            parameterList.add(elementValue);
                                        }
                                        args.add(parameterList);
                                    } else {
                                        Object arg = convertValue(parameterValues[0], parameterType);
                                        args.add(arg);
                                    }
                                }
                            } else {
                                args.add(null);
                            }
                        }
                    return args;
        }

    // compare two string
    public static boolean compare(String parameterName, Field field) {
        if (field.getName().equals(parameterName)) {
            return true;
        }
        return false;
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
