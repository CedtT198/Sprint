package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import Annotation.RequestMapping;
import Annotation.RequestField;
import Annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

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
        Object[] args = new Object[parameters.length];

        if (!thereIsAnnotation(method)) {
            args = getParameters(request, method, parameters, args);
        }
        else {
            args = getParamWithParanamer(request, method, args);
        }
        return args;
    }

    public static Object[] getParamWithParanamer(HttpServletRequest request, Method method, Object[] args) throws Exception {
        Paranamer paranamer = new AdaptiveParanamer();
        String[] parameterNames = paranamer.lookupParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();

       for (int i = 0; i < parameterNames.length; i++) {
            String paramName = parameterNames[i];            
            System.out.println(paramName);
            String paramValue = request.getParameter(paramName); 
            args[i] = convertParameter(paramValue, request, parameterTypes[i]); 
        }
        return args;
    }

    public static Object[] getParameters(HttpServletRequest request, Method method, Parameter[] parameters, Object[] args) throws Exception {
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            
            String paramName = annotation.value();
            System.out.println(paramName);
            String paramValue = request.getParameter(paramName); 
            args[i] = convertParameter(paramValue, request, parameter.getType()); 
        }

        return args;
    }

    public static Object convertParameter(String value, HttpServletRequest request, Class<?> type) throws Exception {
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
        else if (!type.isPrimitive()) {
            return mapRequestToObject(request, type); 
        }
        return object;
    }
    
    public static <T> T mapRequestToObject(HttpServletRequest request, Class<T> clazz) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();

        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                String paramName = field.getName();
                if (field.isAnnotationPresent(RequestField.class)) {
                    RequestField annotation = field.getAnnotation(RequestField.class);
                    if (!annotation.value().isEmpty()) {
                        paramName = annotation.value();
                    }
                }

                String paramValue = request.getParameter(paramName);
                field.setAccessible(true);
                field.set(obj, convertParameter(paramValue, request, field.getType()));
            }
        }
        return obj;
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