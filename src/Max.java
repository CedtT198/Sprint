package validation.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Max {
    int value();
    String message() default "La valeur ne doit pas être supérieure au maximum.";
}
