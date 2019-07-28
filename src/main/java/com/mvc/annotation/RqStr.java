package com.mvc.annotation;

import com.mvc.error.ParamException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RqStr {

    boolean required() default true;

    // 字符串格式类型
    Type type();

    // 字符串正则匹配
    String regex();


    enum Type {
        DATE(),
        PHONE(),
        EMAIL(),
        PHONE_OR_EMAIL
        ;
    }

    class Handler{

        public static void handle(RqStr rqStr, String fieldName, String arg) {
            if(rqStr.required() && arg == null){
                throw new ParamException(100, "参数不能为空:" + fieldName);
            }

            if(arg == null){
                return;
            }

            if(rqStr.regex() != null && !arg.matches(rqStr.regex())){
                throw new ParamException(101, "参数格式不正确:" + fieldName + ",正确格式:" + rqStr.regex());
            }else if(rqStr.type() != null){
                boolean b = false;
                switch (rqStr.type()){
                    case DATE:
                        SimpleDateFormat sdf;
                        if(arg.indexOf(" ") > 0){
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        }else {
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                        }
                        try {
                            sdf.parse(arg);
                            b = true;
                        }catch (Exception e){
                        }
                        break;
                    case PHONE:
                        b = isPhone(arg);
                        break;
                    case EMAIL:
                        b = isEmail(arg);
                        break;
                    case PHONE_OR_EMAIL:
                        b = isPhone(arg) || isEmail(arg);
                        break;
                }

                if(! b){
                    throw new ParamException(102, "参数格式不正确:" + fieldName);
                }
            }
        }

        /**
         * 检查是否为手机号码
         *
         * @param phone 手机号码
         * @return
         */
        private static boolean isPhone(String phone) {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            if (phone == null || phone.length() != 11) {
                return false;
            } else {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(phone);

                if (!m.matches()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 检查是否为电子邮箱号码
         *
         * @param email 电子邮箱号码
         * @return
         */
        private static boolean isEmail(String email) {
            boolean flag;
            try {
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }


    }
}
