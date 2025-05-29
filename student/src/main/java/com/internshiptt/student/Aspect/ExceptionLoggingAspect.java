//package com.internshiptt.student.Aspect;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.JoinPoint;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class ExceptionLoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);
//
//    // Pointcut expression to target methods in controllers under 'com.example.student.controller' package
//    @Around("execution(* com.internshiptt.student.controller..*(..))")  // Adjust the package as per your module's structure
//    public Object logMethodExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            // Proceed with the method execution
//            Object result = joinPoint.proceed();
//            return result;
//        } catch (Exception ex) {
//            // Log the exception in the desired format
//            String methodName = joinPoint.getSignature().getName();
//            String className = joinPoint.getSignature().getDeclaringTypeName();
//            logger.error("Exception in {}.{}(): {}", className, methodName, ex.getMessage());
//
//            // Rethrow the exception after logging
//            throw ex;
//        }
//    }
//}
package com.internshiptt.student.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    // Around advice for logging successful executions and runtime exceptions
    @Around("execution(* com.internshiptt.student.Controller..*(..))")
    public Object logAroundExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Exception ex) {
            String methodName = joinPoint.getSignature().getName();
            logger.error("Exception in {}(): {}", methodName, ex.getMessage());
            throw ex;
        }
    }

    // AfterThrowing advice specifically to catch and log exceptions, including ResponseStatusException
    @AfterThrowing(pointcut = "execution(* com.internshiptt.student.controller..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        logger.error("Exception in {}.{}(): {}", className, methodName, ex.getMessage());
    }
}
