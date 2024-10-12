package mg.prom16.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import Annotation.Get;
import Annotation.Post;
import Annotation.Controller;
import Annotation.Restapi;
import mapping.Mapping;
import util.Mapper;
import util.ModelAndView;
import util.VerbAction;


public class FrontController extends HttpServlet {
    private HashMap<String, Mapping> urlMapping = new HashMap<>();
    private String packageNames;
    private List<String> controllerNames = new ArrayList<>();
    private List<String> errorList = new ArrayList<>();
  
    @Override
    public void init(ServletConfig config) {
        try {
            super.init(config);
            packageNames = config.getInitParameter("controllerPackage");
            packageNames.isEmpty();
            scanControllers(packageNames);
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace.length > 0) {
                StackTraceElement element = stackTrace[0];
                String error = "ERROR : Package "+packageNames+" introuvable.\nFaute de nom, ou le package est non existant.\n";
                error += "Sur la ligne : "+element.getLineNumber()+"\n";
                error += "Dans la classe : "+element.getClassName()+"\n";
                error += "Fonction : "+element.getMethodName();

                errorList.add(error);
            }
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
                            if (clazz.isAnnotationPresent(Controller.class) && !Modifier.isAbstract(clazz.getModifiers()))
                            {
                                controllerNames.add(clazz.getSimpleName());
                                this.getMethodInController(className);
                            }
                        } catch (Exception e) {
                            System.out.println("OK1");
                            e.printStackTrace();
                        }
                    });
            if (controllerNames.size() < 0) {
                String error = "ERROR : Classe(s) introuvable(s).\nAucun controller n'a été trouvé dans le package : '"+packageNames+"'. Ajouter vos controllers annoté de la classe AnnotationController.AnnotationController.";
                errorList.add(error);
            }
            
            // Affichage urlMapping
            // System.out.println("\nURL MAPPING APRES : ");
            // for (HashMap.Entry<String, Mapping> data : urlMapping.entrySet()) { 
            //     String urlName = data.getKey();

            //     Mapping mapping = data.getValue();
            //     System.out.println(mapping.getClassName());
            //     System.out.println(urlName+" : ");

            //     for (VerbAction va : mapping.getVerbAction()) {
            //         System.out.println("- "+va.getVerb());
            //         System.out.println("- "+va.getMethodName());
            //     }
            //     System.out.println("\n");
            // }   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMethodInController(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method[] methods = clazz.getDeclaredMethods();

        for (int i=0; i < methods.length; i++) {
            for (int j=0; j < methods.length; j++) {
                if (i == j) {
                    continue;
                }
                else {
                    if (methods[i].isAnnotationPresent(Get.class) && methods[j].isAnnotationPresent(Get.class)) {
                        String mname = methods[i].getAnnotation(Get.class).value();
                        String m2name = methods[j].getAnnotation(Get.class).value();
                        if (mname == m2name) {
                            String error = "ERROR : Valeur des annotations similaires.\nGet(value=\""+mname+"\") revient plusieurs fois. La valeur de l'annotation de chaque controller doit être unique.";
                            errorList.add(error);
                            break;
                        }
                    }
                    else if (methods[i].isAnnotationPresent(Post.class) && methods[j].isAnnotationPresent(Post.class)) {
                        String mname = methods[i].getAnnotation(Post.class).value();
                        String m2name = methods[j].getAnnotation(Post.class).value();
                        if (mname == m2name) {
                            String error = "ERROR : Valeur des annotations similaires.\nPost(value=\""+mname+"\") revient plusieurs fois. La valeur de l'annotation de chaque controller doit être unique.";
                            errorList.add(error);
                            break;
                        }
                    }
                }
            }
        }

        // Affichage des methods
        // System.out.println("Class : "+className);
        // for (int i = 0; i < methods.length; i++) {
        //     System.out.println("Method : "+methods[i].getName());
        // }

        for (Method method : methods) {
            String methodName = method.getName();
            
            System.out.println(className);
            System.out.println(methodName);

            if (method.isAnnotationPresent(Get.class)) {
                System.out.println("GET ty");
                Get annotation = method.getAnnotation(Get.class);
                String annotationValue = annotation.value();
                processMapping(methodName, "GET", annotationValue, urlMapping, className);
            }
            else if (method.isAnnotationPresent(Post.class)) {
                System.out.println("POST ty");
                Post annotation = method.getAnnotation(Post.class);
                String annotationValue = annotation.value();
                processMapping(methodName, "POST", annotationValue, urlMapping, className);
            }
            // else { // Rehefa tsy misy Verb définie
            //     System.out.println("Aucun VERB ty");
            //     ArrayList<VerbAction> listVerb = new ArrayList<>();
            //     listVerb.add(new VerbAction(methodName, "GET"));
            //     Mapping mapping = new Mapping(className, listVerb);
            //     urlMapping.put(methodName, mapping);
            // }

            // Mapping mapping = new Mapping(className, method.getName());
            // String annotationValue = method.getName();
        }
    }
    
    private void processMapping(String methodName, String httpVerb, String annotationValue, HashMap<String, Mapping> urlMapping, String className) throws Exception {
        System.out.println("Annotation value : "+annotationValue);
        System.out.println("1");
        Mapping existingMapping = urlMapping.get(annotationValue);
        
        System.out.println("2");
        if (existingMapping != null) {
            System.out.println("3");
            for (VerbAction verbAction : existingMapping.getVerbAction()) {
                if (verbAction.getVerb().equals(httpVerb)) {
                    System.out.println("4");
                    throw new ServletException("La méthode " + methodName +
                    " est déjà mappée avec le verbe " + httpVerb + " à l'URL : " + annotationValue);
                }
            }
            urlMapping.get(annotationValue).getVerbAction().add(new VerbAction(methodName, httpVerb));
        }
        else {
            System.out.println("ok");
            ArrayList<VerbAction> listVerb = new ArrayList<>();
            listVerb.add(new VerbAction(methodName, httpVerb));
            Mapping mapping = new Mapping(className, listVerb);
            urlMapping.put(annotationValue, mapping);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuffer requestURL = request.getRequestURL();
            String[] requestUrlSplitted = requestURL.toString().split("/");

            String methodSearched = requestUrlSplitted[requestUrlSplitted.length-1];
            
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");

            if (!urlMapping.containsKey(methodSearched)) {
                // out.println("<p>Aucune méthode associe a ce chemin.</p>");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }
            else {
                Mapping mapping = urlMapping.get(methodSearched);
                // String methodName = mapping.getMethodName();

                String methodName = "";
                ArrayList<VerbAction> verbActions = mapping.getVerbAction();
                
                String verb = request.getMethod();
                for (VerbAction verbAction : verbActions) {
                    if (verbAction.getVerb().equals(verb))
                        methodName = verbAction.getMethodName();
                }

                System.out.println("Method associé à l'url : "+methodName);

                String className = mapping.getClassName();
                executeMethod(out, request, response, methodName, className);
            }

            if (errorList.size() > 0) {
                request.setAttribute("list_error", errorList);
                errorList = new ArrayList<>();
                RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void executeMethod(PrintWriter out, HttpServletRequest request, HttpServletResponse response, String methodName, String className) throws Exception {
        Class<?> c = Class.forName(className);
        try {
            Object[] retourArray = invokeMethod(c, methodName, request);
            Object retour = retourArray[0];
            boolean rest = (boolean) retourArray[1];

            String string = "";
            ModelAndView m = null;
    
            Gson gson = new Gson();
            String json = "";

            if (retour instanceof String) {
                string = (String) retour;
                json = gson.toJson(string);

                System.out.println(json);
            }
            else if (retour instanceof ModelAndView) {
                m = (ModelAndView) retour;
    
                for (HashMap.Entry<String, Object> data : m.getData().entrySet()) { 
                    String name = data.getKey();
                    Object value = data.getValue();

                    request.setAttribute(name, value);
                }
                json = gson.toJson(m.getData());
                
                System.out.println(json);
            }
            else {
                String error = "ERROR : Type de retour non reconnu.\nLe type de retour d'une fonction doit obligatoirement etre de type java.lang.String ou modelandview.ModelAndView.";
                errorList.add(error);
            }

            if (rest) {
                response.setContentType("text/json");
                out.println(json);
            }
            else {
                response.setContentType("text/html");
                if (retour instanceof String)  out.println("<p>" + string + "</p>");
                else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher(m.getUrl());
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception e) {
            out.println(e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
            // String error = "ETU 002715 - ERROR : "+e.getMessage();
            // out.println("<p>" + error + "</p>");
        }
        finally {
            out.close();
        }
    }

    public Object[] invokeMethod(Class<?> c, String methodName, HttpServletRequest request) throws Exception {
        Object instance = c.getDeclaredConstructor().newInstance();        
        Method method = Mapper.findMethodInClass(c, methodName);

        Object[] result = new Object[2];
        if (method != null) {
            Object[] parameters = Mapper.extractParameters(request, method);
            result[0] = method.invoke(instance, parameters);
        }

        if (method.isAnnotationPresent(Restapi.class)) result[1] = true;
        else result[1] = false;
        
        return result;
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