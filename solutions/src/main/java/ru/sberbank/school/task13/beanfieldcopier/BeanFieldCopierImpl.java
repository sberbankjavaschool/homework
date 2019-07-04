package ru.sberbank.school.task13.beanfieldcopier;

import ru.sberbank.school.task13.BeanFieldCopier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanFieldCopierImpl implements BeanFieldCopier {

    /**
     * Сканирует объект "from" для поиска всех геттеров.
     * Если объект "to" содержит соответствующий сеттер, он будет вызван ,
     * чтобы установить значение свойства для «to», равное свойству «from».
     * Тип в сеттере должен быть совместим со значением,
     * возвращаемым гетером (если нет, вызов не выполняется).
     * Совместимость означает, что тип параметра в сеттере должен быть
     * таким же или быть суперклассом возвращаемого типа геттера.
     * Метод заботится только о публичных методах.
     *
     * @param from Object, свойства которого будут использоваться для получения значений.
     * @param to   Object, свойства которого будут установлены.
     *                
     */

    @Override
    public void copy(Object from, Object to) {

        Class fromClass = from.getClass();
        Class toClass = to.getClass();

        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();

        for (Method method : fromClass.getMethods()) {
            if (isGetter(method)) {
                String name = method.getName().toLowerCase().substring(3);
                getters.put(name, method);
            } else if (isBooleanGetter(method)) {
                String name = method.getName().toLowerCase().substring(2);
                getters.put(name, method);
            }
        }

        for (Method method : toClass.getMethods()) {
            if (isSetter(method)) {
                String name = method.getName().toLowerCase().substring(3);
                setters.put(name, method);
            }
        }

        if (getters.keySet().containsAll(setters.keySet())) {
            setters.forEach((name, method) -> setProperty(to, method, from, getters.get(name)));
        }
    }

    private boolean isBooleanGetter(Method method) {
        return method.getName().toLowerCase().startsWith("is")
                && method.getParameterCount() == 0
                && !method.getReturnType().equals(void.class);
    }

    private boolean isSetter(Method method) {
        return method.getName().toLowerCase().startsWith("set")
                && method.getParameterCount() == 1;
    }

    private boolean isGetter(Method method) {
        return method.getName().toLowerCase().startsWith("get")
                && method.getParameterCount() == 0
                && !method.getReturnType().equals(void.class);
    }

    private void setProperty(Object to, Method setter, Object from, Method getter) {

        if (getter.getReturnType().equals(setter.getParameterTypes()[0])
                || isSuperClass(setter.getParameterTypes()[0], getter.getReturnType())) {
            try {
                setter.invoke(to, getter.invoke(from));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSuperClass(Class<?> parent, Class<?> child) {

        if (child.getSuperclass() != null) {
            if (child.getSuperclass().equals(parent)) {
                return true;
            } else {
                return isSuperClass(parent, child.getSuperclass());
            }
        } else {
            return false;
        }
    }
}
