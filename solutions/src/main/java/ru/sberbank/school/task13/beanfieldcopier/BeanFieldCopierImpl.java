package ru.sberbank.school.task13.beanfieldcopier;

import lombok.NonNull;
import ru.sberbank.school.task13.BeanFieldCopier;
import ru.sberbank.school.task13.beanfieldcopier.exceptions.BeanFieldCopierException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFieldCopierImpl implements BeanFieldCopier {
    @Override
    public void copy(@NonNull Object from, @NonNull Object to) {
        Map<Method, Method> suitable = findSuitable(from, to);

        if (suitable.size() == 0) {
            throw new BeanFieldCopierException("Отсутвуют подходящие сеттеры и геттеры");
        }

        for (Map.Entry<Method, Method> entry : suitable.entrySet()) {
            try {
                entry.getValue().invoke(to, entry.getKey().invoke(from));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeanFieldCopierException(e);
            }
        }
    }

    private Map<Method, Method> findSuitable(Object from, Object to) {
        List<Method> getters = findGetters(from);
        List<Method> setters = findSetters(to);

        Map<Method, Method> suitable = new HashMap<>();

        for (Method setter : setters) {
            Class parameterType = setter.getParameterTypes()[0];
            int i = 0;

            for (; i < getters.size(); i++) {
                Class returnType = getters.get(i).getReturnType();
                if (checkNames(setter, getters.get(i)) && checkType(parameterType, returnType)) {
                    suitable.put(getters.get(i), setter);
                    break;
                }
            }

            if (getters.size() > i) {
                getters.remove(i);
            }
        }

        return suitable;
    }

    private boolean checkNames(Method setter, Method getter) {
        String setterName = setter.getName();
        String getterName = getter.getName();


        return getterName.substring(3).equals(setterName.substring(3))
                || getterName.substring(2).equals(setterName.substring(3));
    }

    private boolean checkType(Class parameterType, Class returnType) {
        if (returnType.equals(Object.class) && !parameterType.equals(Object.class)) {
            return false;
        }
        if (returnType.equals(parameterType)) {
            return true;
        } else {
            return checkType(returnType.getSuperclass(), parameterType);
        }
    }

    private List<Method> findGetters(Object from) {
        Class fClass = from.getClass();
        Method[] methods = fClass.getMethods();
        List<Method> result = new ArrayList<>();

        for (Method method : methods) {
            if (isGetter(method)) {
                result.add(method);
            }
        }

        return result;
    }

    private List<Method> findSetters(Object to) {
        Class fClass = to.getClass();
        Method[] methods = fClass.getMethods();
        List<Method> result = new ArrayList<>();

        for (Method method : methods) {
            if (isSetter(method)) {
                result.add(method);
            }
        }

        return result;
    }


    private boolean isGetter(Method method) {
        if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }

        return !void.class.equals(method.getReturnType());
    }

    private boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) {
            return false;
        }

        return method.getParameterTypes().length == 1;
    }
}
