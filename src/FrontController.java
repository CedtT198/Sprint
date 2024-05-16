package mg.prom16.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import AnnotationController.AnnotationController;


public class FrontController extends HttpServlet {
    boolean controllerScanned = false;
    private String packageNames;
    private List<String> controllerNames = new ArrayList<>();
  
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        packageNames = config.getInitParameter("controllerPackage");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer requestURL = request.getRequestURL();
        PrintWriter out = response.getWriter();

        if (!controllerScanned) {
            scanControllers(packageNames, out);
        }
        response.setContentType("text/html");
        out.println("<p>" + requestURL.toString() + "</p>");
        out.close();
    }
    
    private void scanControllers(String packageName, PrintWriter out) {
        out.println("<p>ok</p>");
        out.println("<p>ok</p>");
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
                            if (clazz.isAnnotationPresent(AnnotationController.class)
                                    && !Modifier.isAbstract(clazz.getModifiers())) {
                                controllerNames.add(clazz.getSimpleName());
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            for (String controller : controllerNames) {
                out.println(controller);
            }
            controllerScanned = false;
        } catch (Exception e) {
            e.printStackTrace();
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