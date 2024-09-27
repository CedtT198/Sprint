package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import util.MySession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import Annotation.RequestMapping;
import Annotation.RequestField;
import Annotation.RequestParam;

public class Mapper {

    public static Method findMethodInClass(Class<?> clazz, String methodName) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
    
    public static Object[] extractParameters(HttpServletRequest request, Method method) throws Exception {
        Parameter[] parameters = method.getParameters();  
        // checkAnnotedParameter(parameters);

        Object[] args = new Object[parameters.length];

        // Pour l'usage de paranamer raha tsy misy annotation le paramètre anle fonction
        // Paranamer paranamer = new AdaptiveParanamer();
        // String[] parameterNames = paranamer.lookupParameterNames(method);
        // Class<?>[] parameterTypes = method.getParameterTypes();
    
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameter.getAnnotation(RequestParam.class); 
                String paramName = annotation.value();  
                String paramValue = request.getParameter(paramName);  
                args[i] = convertParameter(paramValue, parameter.getType()); 
            } 
            else if (parameter.isAnnotationPresent(RequestMapping.class)) {
                Class<?> parameterType = parameter.getType();  
                Object parameterObject = parameterType.getDeclaredConstructor().newInstance();  
    
                for (Field field : parameterType.getDeclaredFields()) {
                    String fieldName = field.getName();  
                    String paramName = parameterType.getSimpleName().toLowerCase() + "." + fieldName;  
                    String paramValue = request.getParameter(paramName);  

                    if (paramValue != null) {
                        Object convertedValue = convertParameter(paramValue, field.getType());  
                        
                        System.out.println(convertedValue);
                        
                        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                        Method setter = parameterType.getMethod(setterName, field.getType()); 
                        setter.invoke(parameterObject, convertedValue);  
                    }
                }
                args[i] = parameterObject;  
            }
            else {
                if (parameter.getType() ==  MySession.class) {
                    HttpSession session = request.getSession();
                    MySession mySession = new MySession(session);
                    args[i] = mySession;

                    System.out.println("----------------------------------------\n");
                    System.out.println("OKKKKKKKKKKKKKKKKKKKKKKKKKKK");
                    System.out.println("----------------------------------------\n");
                }
                else {
                    throw new Exception("Touts les arguments d'une méthode doivent être annoté de Annotation.RequestParam ou doivent être de type util.MySession si non annoté.");
                }
                // String paramName = parameterNames[i];
                // System.out.println(paramName);
                // String paramValue = request.getParameter(paramName); 
                // args[i] = convertParameter(paramValue, parameterTypes[i]); 
            }
        }
        return args; 
    }

    // public static void checkAnnotedParameter(Parameter[] parameters) throws Exception {
    //     for (Parameter parameter : parameters) {
    //         if (parameter.isAnnotationPresent(RequestParam.class)) {
    //             throw new Exception("Touts les arguments d'une méthode doivent être annoté de Annotation.RequestParam");
    //         }
    //     }
    // }

    // public static Object[] extractParameters(HttpServletRequest request, Method method) throws Exception {
    //     Parameter[] parameters = method.getParameters();
    //     Object[] args = new Object[parameters.length];

    //     if (!thereIsAnnotation(method)) {
    //         args = getParameters(request, method, parameters, args);
    //     }
    //     else {
    //         args = getParamWithParanamer(request, method, args);
    //     }
    //     return args;
    // }

    // public static Object[] getParamWithParanamer(HttpServletRequest request, Method method, Object[] args) throws Exception {
    //     Paranamer paranamer = new AdaptiveParanamer();
    //     String[] parameterNames = paranamer.lookupParameterNames(method);
    //     Class<?>[] parameterTypes = method.getParameterTypes();

    //    for (int i = 0; i < parameterNames.length; i++) {
    //         String paramName = parameterNames[i];            
    //         System.out.println(paramName);
    //         String paramValue = request.getParameter(paramName); 
    //         args[i] = convertParameter(paramValue, parameterTypes[i]); 
    //     }
    //     return args;
    // }

    // public static Object[] getParameters(HttpServletRequest request, Method method, Parameter[] parameters, Object[] args) throws Exception {
    //     for (int i = 0; i < parameters.length; i++) {
    //         Parameter parameter = parameters[i];
    //         RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            
    //         String paramName = annotation.value();
    //         System.out.println(paramName);
    //         String paramValue = request.getParameter(paramName); 
    //         args[i] = convertParameter(paramValue, request, parameter.getType()); 
    //     }

    //     return args;
    // }

    public static Object convertParameter(String value, Class<?> type) throws Exception {
        Object object = null;
        if (value == null) {
            return null;
        }
        else if (type == String.class) {
            object = value;
        }
        else if (type == int.class || type == Integer.class) {
            object = Integer.parseInt(value);
        }
        else if (type == float.class || type == Float.class) {
            object = Float.parseFloat(value);
        }
        else if (type == double.class || type == Double.class) {
            object = Double.parseDouble(value);
        }
        else if (type == boolean.class || type == Boolean.class) {
            object = Boolean.parseBoolean(value);
        }
        return object;
    }
    
    public static boolean thereIsAnnotation(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (Parameter parameter : parameters) {
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            
            if (annotation == null) {
                return false;
            }
        }
        return true;
    }
}