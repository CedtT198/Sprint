package mg.prom16.controller;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import AnnotationController.RequestParam;
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

    public static Object convertParameter(String value, Class<?> type) {
        Object object = null;
        if (type == String.class) {
            object = value;
        } else if (type == int.class || type == Integer.class) {
            object = Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            object = Long.parseLong(value);
        } else if (type == boolean.class || type == Boolean.class) {
            object = Boolean.parseBoolean(value);
        }
        return object;
    }

    public static Object[] extractParameters(HttpServletRequest request, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            if (annotation != null) {
                String paramName = annotation.value(); 
                String paramValue = request.getParameter(paramName); 
                args[i] = convertParameter(paramValue, parameter.getType()); 
            }
        }

        return args;
    }
}