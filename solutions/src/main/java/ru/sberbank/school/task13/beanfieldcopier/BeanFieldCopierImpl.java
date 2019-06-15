package ru.sberbank.school.task13.beanfieldcopier;

import lombok.NonNull;
import ru.sberbank.school.task13.BeanFieldCopier;
import ru.sberbank.school.task13.beanfieldcopier.exception.BeanFieldCopierException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanFieldCopierImpl implements BeanFieldCopier {

    @Override
    public void copy(@NonNull Object from, @NonNull Object to) {
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for (Method method : from.getClass().getMethods()) {
            if (isGetter(method)) {
                if (!getters.containsKey(method.getName().substring(3))) {
                    getters.put(method.getName().substring(3), method);
                } else {
                    throw new BeanFieldCopierException("У поля более одного get-а!");
                }
            }
        }
        for (Method method : to.getClass().getMethods()) {
            if (isSetter(method)) {
                if (!setters.containsKey(method.getName().substring(3))) {
                    setters.put(method.getName().substring(3), method);
                } else {
                    throw new BeanFieldCopierException("У поля более одного set-а");
                }
            }
        }
        for (String field : getters.keySet()) {
            if (setters.containsKey(field)) {
                if (!isCorrectFields(setters.get(field), getters.get(field))) {
                    throw new BeanFieldCopierException("Несоответствие типов!");
                }
                try {
                    setters.get(field).invoke(to, getters.get(field).invoke(from));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new BeanFieldCopierException(e.getMessage());
                }
            }
        }
    }

    private static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        return !void.class.equals(method.getReturnType());
    }

    private static boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) {
            return false;
        }
        return method.getParameterTypes().length == 1;
    }

    public static boolean isCorrectFields(Method setter, Method getter) {
        Class setterClass = setter.getParameterTypes()[0];
        Class getterClass = getter.getReturnType();
        while (setterClass != null) {
            if (getterClass == setterClass) {
                return true;
            }
            setterClass = setterClass.getSuperclass();
        }
        return false;
    }
}
