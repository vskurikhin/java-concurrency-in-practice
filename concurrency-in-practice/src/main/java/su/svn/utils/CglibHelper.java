package su.svn.utils;

import org.springframework.aop.TargetSource;

import java.lang.reflect.Method;

public class CglibHelper<T> {
    private final T proxied;

    public CglibHelper(T proxied) {
        this.proxied = proxied;
    }

    public T getTargetObject() {
        String name = proxied.getClass().getName();
        if (name.toLowerCase().contains("cglib")) {
            return extractTargetObject(proxied);
        }
        return proxied;
    }

    private T extractTargetObject(T proxied) {
        try {
            //noinspection unchecked
            return (T) findSpringTargetSource(proxied).getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TargetSource findSpringTargetSource(T proxied) {
        Method[] methods = proxied.getClass().getDeclaredMethods();
        Method targetSourceMethod = findTargetSourceMethod(methods);
        targetSourceMethod.setAccessible(true);
        try {
            return (TargetSource)targetSourceMethod.invoke(proxied);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findTargetSourceMethod(Method[] methods) {
        for (Method method : methods) {
            if (method.getName().endsWith("getTargetSource")) {
                return method;
            }
        }
        throw new IllegalStateException(
                "Could not find target source method on proxied object [" + proxied.getClass() + "]"
        );
    }
}
