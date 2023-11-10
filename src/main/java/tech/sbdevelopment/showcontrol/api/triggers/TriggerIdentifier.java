package tech.sbdevelopment.showcontrol.api.triggers;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to identify a trigger.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TriggerIdentifier {
    /**
     * The identifier of the trigger, used in the add command.
     *
     * @return The identifier of the trigger.
     */
    String value();

    /**
     * The minimum amount of arguments the trigger needs.
     *
     * @return The minimum amount of arguments the trigger needs.
     */
    int minArgs() default 0;

    /**
     * The description of the arguments the trigger needs.
     *
     * @return The description of the arguments the trigger needs.
     */
    String argDesc() default "";

    /**
     * The item shown in the GUI for a show.
     *
     * @return The item shown in the GUI for a show.
     */
    Material item() default Material.NOTE_BLOCK;
}
