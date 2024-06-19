package mg.prom16.controller;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import Annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

public class Reflect {
    public static Method findMethodInClass(Class<?> clazz, String methodName) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
    
    public static Object[] extractParameters(HttpServletRequest request, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        if (!thereIsAnnotation(method)) {
            args = getParameters(request, method, parameters, args);
        }
        else {
            args = getParamWithParanamer(request, method, args);
        }
        return args;
    }

    public static Object[] getParamWithParanamer(HttpServletRequest request, Method method, Object[] args) {
        Paranamer paranamer = new AdaptiveParanamer();
        String[] parameterNames = paranamer.lookupParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();

       for (int i = 0; i < parameterNames.length; i++) {
            String paramName = parameterNames[i];            
            String paramValue = request.getParameter(paramName); 
            args[i] = convertParameter(paramValue, parameterTypes[i]); 
        }
        return args;
    }

    public static Object[] getParameters(HttpServletRequest request, Method method, Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            
            String paramName = annotation.value(); 
            String paramValue = request.getParameter(paramName); 
            args[i] = convertParameter(paramValue, parameter.getType()); 
        }

        return args;
    }

    public static Object convertParameter(String value, Class<?> type) {
        Object object = null;
        if (type == String.class) {
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
        else {
            object = getObjectValue(request, value, type);
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