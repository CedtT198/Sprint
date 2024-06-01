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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import AnnotationController.AnnotationController;
import AnnotationController.Get;
import mapping.Mapping;
import modelandview.ModelAndView;

// import java.util.Set;


public class FrontController extends HttpServlet {
    private HashMap<String, Mapping> urlMapping = new HashMap<>();
    private String packageNames;
    private List<String> controllerNames = new ArrayList<>();
  
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        packageNames = config.getInitParameter("controllerPackage");
        scanControllers(packageNames);
        
        for (String key : urlMapping.keySet()) {
            System.out.println("Clé: " + key);
        }
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
        String methodSearched = requestUrlSplitted[requestUrlSplitted.length-1];
        String controllerSearched = requestUrlSplitted[requestUrlSplitted.length-2];
        
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (!controllerNames.contains(controllerSearched)) {
            out.println("<p>" + "Controller "+controllerSearched+" inexistant, verifier la syntaxe." + "</p>");
        }
        else {
            if (!checkMethodController(urlMapping, methodSearched, "Controller."+controllerSearched)) {
                out.println("<p>" + "Aucune méthode "+methodSearched+" associee à ce controller." + "</p>");
            }
            else {
                Mapping mapping = urlMapping.get(methodSearched);
                String methodName = mapping.getMethodName();
                String className = mapping.getClassName();

                executeMethod(out, request, response, methodName, className);
            }
        }
    }

    public void executeMethod(PrintWriter out, HttpServletRequest request, HttpServletResponse response, String methodName, String className) {
        try {
            Class<?> c = Class.forName(className);
            Object instance = c.getDeclaredConstructor().newInstance();        
            Method method = c.getMethod(methodName);
            Object retour = method.invoke(instance);

            if (retour instanceof String)  {
                String string = (String) retour;                
                out.println("<p>" + string + "</p>");
                out.close();
            }
            else if (retour instanceof ModelAndView) {
                ModelAndView m = (ModelAndView) retour;

                for (HashMap.Entry<String, Object> data : m.getData().entrySet()) {
                    String name = data.getKey();
                    Object value = data.getValue();

                    request.setAttribute(name, value);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher(m.getUrl());
                dispatcher.forward(request, response);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean checkMethodController(HashMap<String, Mapping> urlMapping, String methodName, String className) {
        for (HashMap.Entry<String, Mapping> u : urlMapping.entrySet()) {
            Mapping m = u.getValue();
            if (m.getClassName().equals(className) && m.getMethodName().equals(methodName)) {
                return true;
            }
        }
        return false;
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