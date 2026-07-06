package com.chen.intellectualproperty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel字段注解，用于标记实体类字段与Excel列的映射关系
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /** Excel列标题 */
    String value();

    /** 列顺序（从小到大排列），默认0 */
    int order() default 0;

    /** 日期格式化，仅对Date类型字段生效 */
    String dateFormat() default "yyyy-MM-dd";
}
