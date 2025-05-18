package library.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CachingAspect {

    private final Map<Object, Object> cache = new HashMap<>();

//    @Around("execution(* library.service.BookService.getBookById(int)) || " +
//            "execution(* library.service.BookService.bookExists(int)) || " +
//            "execution(* library.service.BookService.getAllBooks())")
    public Object cacheResult(ProceedingJoinPoint joinPoint) throws Throwable {
        // Create cache key: method name + arguments
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object cacheKey = args.length == 0 ? methodName : Arrays.asList(methodName, Arrays.asList(args));

        // Check cache
        if (cache.containsKey(cacheKey)) {
            System.out.println("Cache hit for: " + methodName + " with key: " + cacheKey);
            return cache.get(cacheKey);
        }

        // Execute method and cache result
        Object result = joinPoint.proceed();
        cache.put(cacheKey, result);
        System.out.println("Cache miss, stored result for: " + methodName + " with key: " + cacheKey);
        return result;
    }
}