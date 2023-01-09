package nl.sbdeveloper.showcontrol.api.triggers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TriggerIdentifier {
    String value();
    int minArgs() default 0;
    String argDesc() default "";
}
