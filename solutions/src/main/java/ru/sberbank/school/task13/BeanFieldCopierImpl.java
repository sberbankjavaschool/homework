package ru.sberbank.school.task13;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanFieldCopierImpl implements BeanFieldCopier {
    @Override
    public void copy(Object from, Object to) {
        Method[] methodsFrom = from.getClass().getMethods();
        Method[] methodsTo = to.getClass().getMethods();

        Map<String, String> getters = parseGetters(methodsFrom);
        Map<String, String> setters = parseSetters(methodsTo);
        Map<String, String> intersection = mapsIntersection(getters, setters);

        process(from, to, intersection);
    }

    private Map<String, String> parseGetters(Method[] methods) {
        Map<String, String> result = new HashMap<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get")) {
                name = name.replace("get","");
                result.put(name, method.getReturnType().getName());
            } else if (name.startsWith("is")) {
                name = name.replace("is","");
                result.put(name, method.getReturnType().getName());
            }
        }
        return result;
    }

    private Map<String, String> parseSetters(Method[] methods) {
        Map<String, String> result = new HashMap<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && method.getParameterCount() == 1) {
                name = name.replace("set","");
                result.put(name, method.getParameterTypes()[0].getTypeName());
            }
        }
        return result;
    }

    private Map<String, String> mapsIntersection(Map<String, String> getters, Map<String, String> setters) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> getter : getters.entrySet()) {
            String name = getter.getKey();
            String type = getter.getValue();
            if (setters.containsKey(name) && setters.get(name).equals(type)) {
                result.put(name, type);
            }
        }
        return result;
    }

    private void process(Object from, Object to, Map<String, String> fields) {
        for (String filed : fields.keySet()) {
            try {
                Object getResult = from.getClass().getMethod("get" + filed).invoke(from);
                to.getClass().getMethod("set" + filed, Class.forName(fields.get(filed))).invoke(to, getResult);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Ошибка при вызове метода", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Не найден метод", e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Не найден класс", e);
            }
        }
    }
}
