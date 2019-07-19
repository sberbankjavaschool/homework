package ru.sberbank.school.task13.Copier;


import ru.sberbank.school.task13.BeanFieldCopier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFieldCopierImpl implements BeanFieldCopier {

    @Override
    public void copy(Object from, Object to) {

        Map<String, Method> getters = Arrays.stream(from.getClass().getMethods())
                .filter(method ->
                        method.getName().startsWith("get")
                                && (method.getParameterCount() == 0)
                                && !(method.getReturnType().equals(Void.TYPE)))
                .collect(Collectors.toMap(method -> method.getName().substring(3), method -> method));

        Map<String, Method> setters = Arrays.stream(to.getClass().getMethods())
                .filter((method) ->
                        method.getName().startsWith("set")
                                && (method.getParameterCount() == 1)
                                && (method.getReturnType().equals(Void.TYPE)))
                .collect(Collectors.toMap(method -> method.getName().substring(3), method -> method));

        for (String s : setters.keySet()) {

            Method getter = getters.get(s);
            Method setter = setters.get(s);

            if (getter != null && getter.getReturnType().equals(setter.getParameterTypes()[0])) {
                try {
                    setter.invoke(to, getter.invoke(from));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
