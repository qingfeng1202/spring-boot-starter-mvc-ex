package com.mvc.aop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvc.annotation.NotNull;
import com.mvc.annotation.RqStr;
import com.mvc.error.ParamException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 方法参数处理切片
 */
@Aspect
@Configuration
public class WebDataAspect {


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void pointcut(){

    }

//    @Before(value = "pointcut()")
    public void doBefore(JoinPoint joinPoint){

    }

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        // 得到拦截的方法
        Method method = signature.getMethod();
        // 获取参数变量
        Parameter[] parameters = method.getParameters();
        // 参数
        Object[] args = joinPoint.getArgs();
        // 遍历参数变量
        for(int i=0; i<parameters.length; i++){
            Parameter parameter = parameters[i];
            // 参数为json时
            if(parameter.getAnnotation(RequestBody.class) != null){
                jsonParamHandle(args[i]);
            }else {
                args[i] = paramHandle(args[i]);

                RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                if(annotation != null && annotation.required() && args[i] == null){
                    throw new ParamException(102, "参数不能为空:" + parameter.getName());
                }

                RqStr rqStr = parameter.getAnnotation(RqStr.class);
                if(rqStr != null && args[i] instanceof String){
                    RqStr.Handler.handle(rqStr, parameter.getName(), (String)args[i]);
                }
            }
        }

        if(request.getHeader(WebResponseBodyAdvice.ignore) != null){
            response.addHeader(WebResponseBodyAdvice.ignore, "aaa");
        }

        return joinPoint.proceed(args);
    }


    private void jsonParamHandle(Object arg) throws Exception {
        if(arg instanceof Collection){
            Collection<Object> co = (Collection) arg;

            for(Object o : co){
                jsonParamHandle(o);
            }
        }else {
            Class<?> aClass = arg.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for(Field field : fields){
                try {
                    field.setAccessible(true);
                    Object o = field.get(arg);
                    o = paramHandle(o);

                    NotNull notNull = field.getAnnotation(NotNull.class);
                    if(notNull != null && o == null){
                        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                        throw new ParamException(102, "参数不能为空:" + jsonProperty != null ? jsonProperty.value() : field.getName());
                    }

                    RqStr rqStr = field.getAnnotation(RqStr.class);
                    if(rqStr != null && arg instanceof String){
                        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                        RqStr.Handler.handle(rqStr, jsonProperty != null ? jsonProperty.value() : field.getName(), (String)arg);
                    }

                    field.set(arg, o);
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    private static Object paramHandle(Object arg){
        if(arg == null){
            return null;
        }else if(arg.getClass().isArray()){
            Object[] os = (Object[]) arg;
            List<Object> list = new ArrayList<>();
            for(int i=0; i<os.length; i++){
                Object o = paramHandle(os[i]);
                if(o != null){
                    list.add(o);
                }
            }
            if(list.size() == 0){
                return null;
            }else {
                return list.toArray(new Object[list.size()]);
            }
        }else if(arg instanceof Collection){
            Collection<Object> co = (Collection) arg;
            List<Object> list = new ArrayList<>();
            for(Object o : co){
                o = paramHandle(o);
                if(o != null){
                    list.add(o);
                }
            }
            if(list.size() == 0){
                return null;
            }else {
                co.clear();
                co.addAll(list);
                return co;
            }
        }else if(arg instanceof String){
            String trim = ((String) arg).trim();
            if(trim.length() == 0){
                return null;
            }else {
                return trim;
            }
        }else {
            return arg;
        }
    }

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.mvc.annotation.IgnorePack)")
    private void pointcutAfter(){

    }

    @After("pointcutAfter()")
    public void doAfter(){
        response.addHeader(WebResponseBodyAdvice.ignore, "true");
    }



}
