package mg.prom16.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import AnnotationController.AnnotationController;
import AnnotationController.Get;
import mapping.Mapping;
import java.util.Set;

import javax.swing.JOptionPane;

public class FrontController extends HttpServlet {
    HashMap<String, Mapping> urlMapping = new HashMap<>();
    private String packageNames;
    private List<String> controllerNames = new ArrayList<>();
  
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        packageNames = config.getInitParameter("controllerPackage");
        scanControllers(packageNames);
    }

    
    private void scanControllers(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            Path classPath = Paths.get(resource.toURI());
            Files.walk(classPath)
                    .filter(f -> f.toString().endsWith(".class"))
                    .forEach(f -> {
                        String className = packageName + "." + f.getFileName().toString().replace(".class", "");
                        try {
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(AnnotationController.class) && !Modifier.isAbstract(clazz.getModifiers()))
                            {
                                controllerNames.add(clazz.getSimpleName());
                                this.getMethodInController(className);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getMethodInController(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Get.class)) {
                Mapping mapping = new Mapping(className, method.getName());
                Get getAnnotation = method.getAnnotation(Get.class);
                String annotationValue = getAnnotation.value();

                urlMapping.put(annotationValue, mapping);
            }
        }
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer requestURL = request.getRequestURL();
        String[] requestUrlSplitted = requestURL.toString().split("/");
        String controllerSearched = requestUrlSplitted[requestUrlSplitted.length-1];
        // String controllerSearched = requestUrlSplitted[requestUrlSplitted.length-2];
        
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (!urlMapping.containsKey(controllerSearched)) {
            out.println("<p>" + "Il y n'a pas de méthode associée à ce chemin." + "</p>");
        }
        else {
            Mapping mapping = urlMapping.get(controllerSearched);
            
            out.println("<p>" + requestURL.toString() + "</p>");
            out.println("<p>" + mapping.getClassName() + "</p>");
            out.println("<p>" + mapping.getMethodName() + "</p>");

            out.close();
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}