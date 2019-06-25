package ru.sberbank.school.task13.copier;

import ru.sberbank.school.task13.BeanFieldCopier;
import ru.sberbank.school.task13.copier.exception.CopierException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Copier implements BeanFieldCopier {
    @Override
    public void copy(Object from, Object to) {

        Map<String, Method> getters = findMethods(from, "(get|is)");
        Map<String, Method> setters = findMethods(to, "set");

        for (Map.Entry<String, Method> entry : getters.entrySet()) {
            String key = entry.getKey();
            if (setters.containsKey(key)) {
                Method method = setters.get(key);
                Class<?>[] parameterTypes = method.getParameterTypes();
                Method value = entry.getValue();
                if (parameterTypes[0].isAssignableFrom(value.getReturnType())) {
                    try {
                        method.invoke(to, value.invoke(from));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Map<String, Method> findMethods(Object object, String name) {
        Method[] methods = object.getClass().getMethods();
        Map<String, Method> result = new HashMap<>();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.matches("^" + name + "[A-Z].*")) {
                if (isSuitable(name, method)) {
                    Pattern pattern  = Pattern.compile("^" + name);
                    Matcher matcher = pattern.matcher(methodName);
                    String arg = matcher.replaceAll("");
                    result.put(arg, method);
                }
            }

        }
        return result;
    }

    private boolean isSuitable(String name, Method method) {
        if (name.equals("(get|is)")) {
            if (method.getParameterTypes().length != 0 || method.getReturnType().equals(void.class)) {
                throw new CopierException("У метода get/is не должно быть параметров "
                        + "и должен быть тип возвращаемого значения!");
            } else {
                return true;
            }
        } else if (name.equals("set")) {
            if (method.getParameterTypes().length != 1 || !method.getReturnType().equals(void.class)) {
                throw new CopierException("У метода set должен "
                        + "быть один параметр и не должно быть возвращаемого значения!");
            } else {
                return true;
            }
        }
        return false;
    }

}
