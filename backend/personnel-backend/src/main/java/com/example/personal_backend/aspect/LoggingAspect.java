package com.example.personal_backend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.personal_backend.service.*.*(..))")
    public void serviceLayer() {}

    @Pointcut("execution(* com.example.personal_backend.controller.*.*(..))")
    public void controllerLayer() {}

    @Around("serviceLayer()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info("SERVICE: {} metoduna {} sınıfında başlandı. Parametreler: {}", 
                   methodName, className, Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("SERVICE: {} metodu {} sınıfında tamamlandı. Süre: {} ms", 
                       methodName, className, (endTime - startTime));
            return result;
        } catch (Exception ex) {
            logger.error("SERVICE: {} metodunda {} sınıfında hata oluştu: {}", 
                        methodName, className, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Around("controllerLayer()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info("CONTROLLER: {} metoduna {} sınıfında başlandı", methodName, className);

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("CONTROLLER: {} metodu {} sınıfında tamamlandı. Süre: {} ms", 
                       methodName, className, (endTime - startTime));
            return result;
        } catch (Exception ex) {
            logger.error("CONTROLLER: {} metodunda {} sınıfında hata oluştu: {}", 
                        methodName, className, ex.getMessage(), ex);
            throw ex;
        }
    }

    @AfterThrowing(pointcut = "serviceLayer() || controllerLayer()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.error("EXCEPTION: {} metodunda {} sınıfında exception fırlatıldı: {}", 
                    methodName, className, ex.getMessage(), ex);
    }
}
