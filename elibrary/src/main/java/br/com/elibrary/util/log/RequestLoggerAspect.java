package br.com.elibrary.util.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Aspect
public class RequestLoggerAspect {
    private static final Logger LOGGER = Logger.getLogger(RequestLoggerAspect.class.getName());

    @Around(value = "@annotation(statefulRequestLogger)")
    public Object beforeRequest(ProceedingJoinPoint joinPoint, StatefulRequestLogger statefulRequestLogger) throws Throwable {
        Log log = new Log(statefulRequestLogger.action());
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            Method method = methodSignature.getMethod();
            log.addProperty("method", method.getName());
        }
        long startedAt = System.nanoTime();
        Object result = joinPoint.proceed();
        long finishedAt = System.nanoTime() - startedAt;
        log.timeTaken(finishedAt);
        if (result instanceof ResponseEntity<?> responseEntity) {
            log.addProperty("statusCode", responseEntity.getStatusCode());
        }
        LOGGER.log(Level.INFO, "Request information: {0}", log);
        return result;
    }
}