package validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpServletRequest;
import validation.exception.*;
import validation.annotation.*;

public class Validation {
    public static Map<String, String> validate(HttpServletRequest request, Object object, Map<String, String> errors) throws Exception {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                // Vérifie si nombre input inférieure à minimum
                if (field.isAnnotationPresent(Min.class)) {
                    Min minAnnotation = field.getAnnotation(Min.class);
                    Object value = field.get(object);
                    int intValue = castToInteger(value);
                    if (intValue < minAnnotation.value()) { 
                        System.out.println("Contrainte Minimum");
                        System.out.println(field.getName()+" : "+value);
                        errors.put(field.getName(), minAnnotation.message());
                    }
                    // else {
                    //     errors += "Champ " +field.getName()+ " doit être de type number.\n";
                    // }
                }

                // Vérifie si nombre input supérieure à maximum
                if (field.isAnnotationPresent(Max.class)) {
                    Max maxAnnotation = field.getAnnotation(Max.class);
                    Object value = field.get(object);
                    int intValue = castToInteger(value);
                    if (intValue > maxAnnotation.value()) { 
                        System.out.println("Contrainte Maximum");
                        System.out.println(field.getName()+" : "+value);
                        errors.put(field.getName(), maxAnnotation.message());
                    }
                    // else {
                    //     errors += "Champ " +field.getName()+ " doit être de type number.\n";
                    // }
                }
                
                // Le champ doit être != null
                if (field.isAnnotationPresent(NotEmpty.class)) {
                    NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
                    Object value = field.get(object);
                    if (value == null || value.toString().trim().isEmpty()) {
                        System.out.println("Contrainte NotEmpy");
                        System.out.println(field.getName()+" : "+value);
                        errors.put(field.getName(), notEmpty.message());
                    }
                }
                
                // Format de mail doit être valable
                if (field.isAnnotationPresent(Email.class)) {
                    Email mailAnnotation = field.getAnnotation(Email.class);
                    Object value = field.get(object);
                    if (value instanceof String) {
                        String stringValue = (String) value;
                        if (!isMailOk(stringValue)) { 
                            System.out.println("Contrainte Email");
                            System.out.println(field.getName()+" : "+value);
                            errors.put(field.getName(), mailAnnotation.message());
                        }
                    }
                }
                
                // Vérification longueur mot de passe
                if (field.isAnnotationPresent(Password.class)) {
                    Password passwordAnnotation = field.getAnnotation(Password.class);
                    Object value = field.get(object);
                    if (value instanceof String) {
                        String stringValue = (String) value;
                        if (stringValue.length() < passwordAnnotation.value()) { 
                            System.out.println("Contrainte Password");
                            errors.put(field.getName(), passwordAnnotation.message());
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                throw new Exception("Impossible d\'accéder à ce champ "+field.getName());
            }
        }

        return errors;
    } 

    // private static void addError(Map<String, List<String>> errors, String fieldName, String message) {
    //     errors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(message);
    // }

    public static int castToInteger(Object numeric) {
        int response = 0;
        if (numeric instanceof Integer) {
            response = (Integer) numeric;
        }
        else if (numeric instanceof Double) {
            Double n  = (Double) numeric;
            response = n.intValue();
        }
        else if (numeric instanceof Float) {
            Float n  = (Float) numeric;
            response = n.intValue();
        }
        return response;
    }

    public static boolean isMailOk(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
