package ru.sberbank.school.task13;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BeanFieldCopierImpl implements BeanFieldCopier {

    @Override
    public void copy(Object from, Object to) {
        Objects.requireNonNull(from, "Объект копирования from не должен быть null");
        Objects.requireNonNull(to, "Обьект копирования to не должен быть null");

        Map<String, Method> getters = Arrays.stream(from.getClass().getMethods())
                .filter(BeanFieldCopierImpl::isGetter)
                .collect(Collectors.toMap(method -> method.getName().substring(3), method -> method));

        Map<String, Method> setters = Arrays.stream(to.getClass().getMethods())
                .filter(BeanFieldCopierImpl::isSetter)
                .collect(Collectors.toMap(method -> method.getName().substring(3), method -> method));

        for (String key : setters.keySet()) {
            Method getter = getters.get(key);
            Method setter = setters.get(key);

            if (getter == null) {
                continue;
            }

            if (getter.getReturnType() == setter.getParameterTypes()[0]) {

                try {
                    setter.invoke(to, getter.invoke(from));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static boolean isGetter(Method method) {
        return method.getName().startsWith("get")
                && method.getParameterTypes().length == 0
                && !void.class.equals(method.getReturnType());
    }

    private static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getParameterTypes().length == 1;
    }

}
