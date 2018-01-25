package admin.operation;

import java.lang.annotation.*;

/**
 * Created by jeremy on 2017/8/15.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationServiceLog {

    /** 操作的菜单项 **/
        public String menuItem() default "";

    /** 操作的菜单内容 **/
        public String menuName() default "";

    /** 操作的类型 **/
        public  int recordType() default 0;

    /** 操作的类型名称 **/
        public String typeName() default "";

}
