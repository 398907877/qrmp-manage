package com.yanyan.core.spring.web.servlet.mvc.method.annotation;

import com.yanyan.core.util.GenericsUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

/**
 * 控制器处理方法切面，主要是拦截方法来写日志
 * User: Saintcy
 * Date: 2015/12/2
 * Time: 23:44
 */
public class HandlerMethodAspect {
    protected Logger logger = LoggerFactory.getLogger(HandlerMethodAspect.class);
    protected ThreadLocal<Method> methodThreadLocal = new NamedThreadLocal<Method>("handler-method");//当前调用的方法对象
    protected ThreadLocal<Boolean> printedThreadLocal = new NamedThreadLocal<Boolean>("printed");//是否打印方法信息
    protected ThreadLocal<Long> startTimeLocal = new NamedThreadLocal<Long>("start-time");//开始调用时间

    /**
     * Controller方法调用前
     * TODO: 此处可以增加写操作日志
     *
     * @param joinPoint
     */
    public void beforeInvoke(JoinPoint joinPoint) throws MethodArgumentNotValidException {
        startTimeLocal.set(System.currentTimeMillis());
        if (logger.isDebugEnabled()) {
            printMethodInvocationInfo(joinPoint);
            logger.debug("Parameters: {}", ArrayUtils.toString(joinPoint.getArgs()));
        }

        //checkErrors(joinPoint);//在方法调用前检查下是否有绑定错误信息
    }

    /**
     * Controller方法调用后（无论有没有抛出异常）
     *
     * @param joinPoint
     */
    public void afterInvoke(JoinPoint joinPoint) {
        if (logger.isDebugEnabled()) {
            printMethodInvocationInfo(joinPoint);
            long startTime = startTimeLocal.get();
            long endTime = System.currentTimeMillis();
            logger.debug("Method {} invoke complete in {}ms.", getMethod(joinPoint).getName(), endTime - startTime);
        }
        //checkErrors(joinPoint);//有可能在方法执行过程中加入了Errors，所以返回后也得检查下是否有错误信息
    }

    /**
     * Controller方法调用返回后（没有抛出异常）
     *
     * @param joinPoint
     * @param returnValue
     */
    public void afterReturning(JoinPoint joinPoint, Object returnValue) throws MethodArgumentNotValidException {
        if (logger.isDebugEnabled()) {
            printMethodInvocationInfo(joinPoint);
            logger.debug("Return: {}", returnValue);
        }
    }

    /**
     * Controller方法抛出异常
     *
     * @param joinPoint
     * @param throwable
     */
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
        if (logger.isDebugEnabled()) {
            printMethodInvocationInfo(joinPoint);
            logger.debug("Throw: {}", throwable);
        }

        throw throwable;
    }

    /**
     * 环绕方法调用
     *
     * @param joinPoint
     * @throws Throwable
     */
    public Object aroundInvoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        beforeInvoke(joinPoint);

        Throwable error = null;
        try {
            result = joinPoint.proceed();

            afterReturning(joinPoint, result);
        } catch (Throwable throwable) {
            try {
                afterThrowing(joinPoint, throwable);
            } catch (Throwable throwable1) {
                error = throwable1;
            }
        }

        afterInvoke(joinPoint);//无论如何都要执行

        if (error != null) {//如果有异常需要抛出
            throw error;
        }

        return result;
    }

   /* protected final List<Errors> findErrorsArguments(JoinPoint joinPoint) {
        List<Errors> errors = new ArrayList<Errors>();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {//查找Errors参数
            if (joinPoint.getArgs()[i] instanceof Errors) {
                errors.add((Errors) joinPoint.getArgs()[i]);
            }
        }

        return errors;
    }

    protected final Model findModelArgument(JoinPoint joinPoint) {
        Model model = null;
        for (int i = 0; i < joinPoint.getArgs().length; i++) {//查找Errors参数
            if (joinPoint.getArgs()[i] instanceof Model) {
                model = (Model) joinPoint.getArgs()[i];
            } else if (joinPoint.getArgs()[i] instanceof Map) {//也是BindingAwareModelMap对象，实现了Model接口
                model = (Model) joinPoint.getArgs()[i];
            }
        }

        return model;
    }*/

    /*protected final List<Object> findAttributeArguments(JoinPoint joinPoint) {
        List<Object> arguments = new ArrayList<Object>();
        Method method = getMethod(joinPoint);
        for (int i = 0; i < method.getParameterTypes().length; i++) {//查找Errors参数
            if (method.getParameterTypes()[i].getAnnotation(RequestModel.class) != null
                    && method.getParameterTypes()[i].getAnnotation(ModelAttribute.class) != null) {
                arguments.add(joinPoint.getArgs()[i]);
            }
        }

        return arguments;
    }*/


    protected final void printMethodInvocationInfo(JoinPoint joinPoint) {
        Boolean printed = printedThreadLocal.get();
        if (printed == null || !printed) {
            Method method = getMethod(joinPoint);
            logger.debug("Invoke method: {}" , method.toGenericString());
            logger.debug("Method Annotations: {}", ArrayUtils.toString(method.getAnnotations()));
            logger.debug("Parameter Annotations: {}", ArrayUtils.toString(method.getParameterAnnotations()));
            printedThreadLocal.set(true);
        }
    }

    /**
     * 获取调用方法的完整信息
     *
     * @param point
     * @return
     */
    protected Method getMethod(JoinPoint point) {
        Method m = methodThreadLocal.get();//从本地变量里面获取

        if (m != null) {
            return m;
        }

        //拦截的实体类
        Object target = point.getTarget();
        //拦截的方法名称
        String methodName = point.getSignature().getName();
        //拦截的方法参数
        Object[] args = point.getArgs();
        //拦截的放参数类型
        Class[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();

        try {
            //通过反射获得拦截的method
            m = target.getClass().getMethod(methodName, parameterTypes);
            //如果是桥则要获得实际拦截的method
            if (m.isBridge()) {
                for (int i = 0; i < args.length; i++) {
                    //获得泛型类型
                    Class genClazz = GenericsUtils.getSuperClassGenericType(target.getClass());
                    //根据实际参数类型替换parameterType中的类型
                    if (args[i].getClass().isAssignableFrom(genClazz)) {
                        parameterTypes[i] = genClazz;
                    }
                }
                //获得parameterType参数类型的方法
                m = target.getClass().getMethod(methodName, parameterTypes);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        methodThreadLocal.set(m);//缓存到本地变量

        return m;
    }
}
