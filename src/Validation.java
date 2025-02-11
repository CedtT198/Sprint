package validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import validation.exception.*;
import validation.annotation.*;

public class Validation {
    public static void validate(Object object) throws ValidationException {
        String errors = "";
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                
                // Vérifie si nombre input inférieure à minimum
                if (field.isAnnotationPresent(Min.class)) {
                    Min minAnnotation = field.getAnnotation(Min.class);
                    Object value = field.get(object);
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        if (intValue < minAnnotation.value()) { 
                            errors += minAnnotation.message() + "\n";
                        }
                        // else {
                        //     errors += "Champ " +field.getName()+ " doit être de type number.\n";
                        // }
                    }
                }

                // Vérifie si nombre input supérieure à maximum
                if (field.isAnnotationPresent(Max.class)) {
                    Max maxAnnotation = field.getAnnotation(Max.class);
                    Object value = field.get(object);
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        if (intValue > maxAnnotation.value()) { 
                            errors += maxAnnotation.message() + "\n";
                        }
                        // else {
                        //     errors += "Champ " +field.getName()+ " doit être de type number.\n";
                        // }
                    }
                }
                
                // Le champ doit être != null
                if (field.isAnnotationPresent(NotEmpty.class)) {
                    NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
                    Object value = field.get(object);
                    if (value == null || value.toString().trim().isEmpty()) {
                        errors += notEmpty.message() + "\n";
                    }
                }
                
                // Format de mail doit être valable
                if (field.isAnnotationPresent(Email.class)) {
                    Email mailAnnotation = field.getAnnotation(Email.class);
                    Object value = field.get(object);
                    if (value instanceof String) {
                        String stringValue = (String) value;
                        if (!isMailOk(stringValue)) { 
                            errors += mailAnnotation.message() + "\n";
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
                            errors += passwordAnnotation.message() + "\n";
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                errors += "Impossible d\'accéder à ce champ "+field.getName()+".\n";
                throw new ValidationException(errors);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    } 

    public static boolean isMailOk(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
