package ru.sberbank.school.task13;

import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class Copier implements BeanFieldCopier {


    @Override
    public void copy(@NonNull Object from, @NonNull Object to) {
        Map<String, Method> getters = getGetters(from.getClass().getMethods());
        Map<String, Method> setters = getSetters(to.getClass().getMethods());

        for (Map.Entry<String, Method> getMethod : getters.entrySet()) {
            for (Map.Entry<String, Method> setMethod : setters.entrySet()) {
                if (getMethod.getKey().equals(setMethod.getKey())) {
                    Method getter = getMethod.getValue();
                    Method setter = setMethod.getValue();

                    if (equalsClass(setter.getParameterTypes()[0], getter.getReturnType())) {

                        try {
                            setter.invoke(to, getter.invoke(from));
                        } catch (IllegalAccessException e) {
                            throw new BeanFieldCopierException("No access to the method", e);
                        } catch (InvocationTargetException e) {
                            throw new BeanFieldCopierException("Method throws an exception", e);
                        }
                    }
                }
            }
        }
    }

    private Map<String, Method> getGetters(Method[] methods) {
        Map<String, Method> map = new HashMap<>();

        for (Method method : methods) {
            if (method.getName().matches("^get[A-Z].+")) {
                if (method.getParameterTypes().length > 0) {
                    continue;
                }
                if (method.getReturnType().equals(Void.TYPE)) {
                    continue;
                }
                map.put(method.getName().substring(3), method);
            }
        }
        return map;
    }

    private Map<String, Method> getSetters(Method[] methods) {
        Map<String, Method> map = new HashMap<>();

        for (Method method : methods) {
            if (method.getName().matches("^set[A-Z].+")) {
                if (method.getParameterTypes().length == 0) {
                    continue;
                }
                if (!method.getReturnType().equals(Void.TYPE)) {
                    continue;
                }
                map.put(method.getName().substring(3), method);
            }
        }
        return map;
    }

    private boolean equalsClass(Class parameterType, Class returnType) {
        if (parameterType == returnType) {
            return true;
        }

        Class superReturnType = returnType.getSuperclass();

        while (superReturnType != null) {
            if (parameterType == superReturnType) {
                return true;
            }
            superReturnType = superReturnType.getSuperclass();
        }

        return false;
    }
}
