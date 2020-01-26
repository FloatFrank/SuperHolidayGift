package com.piaofu.owner.superholidaygift.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 此注解类为指令处理类
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerCommand {
    String[] arg();
    String msg() default "";
    String label();

    SenderType[] type() default { SenderType.ALL };
}
