package validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Password {
    int value();
    String message() default "Mot de passe trop court. Longueur doit être supérieur à 8.";
}
