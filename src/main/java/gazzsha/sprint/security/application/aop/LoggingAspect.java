package gazzsha.sprint.security.application.aop;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class LoggingAspect {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }




    @Pointcut("execution(* gazzsha.sprint.security.application.controller.*.*(..))")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }



    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), exception.getCause() != null ? exception.getCause() : "NULL");
    }

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Call: {}() with argument[s] = {}",
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Completion: {}.{}() with HTTP Status = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),((ResponseEntity<?>) result).getStatusCode().value());
            return result;
        } catch (AuthenticationCredentialsNotFoundException | AccessDeniedException exception) {
            log.error("Authentication error: {} with reason = {}", Arrays.toString(joinPoint.getArgs()),
                    exception.getMessage());
            throw exception;
        }
    }
}
