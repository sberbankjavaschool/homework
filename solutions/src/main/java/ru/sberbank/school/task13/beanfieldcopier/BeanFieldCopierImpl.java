package ru.sberbank.school.task13.beanfieldcopier;

import lombok.NonNull;
import ru.sberbank.school.task13.BeanFieldCopier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BeanFieldCopierImpl implements BeanFieldCopier {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * The method takes care only about public methods.
     *
     * @param from Object which properties will be used to get values.
     * @param to   Object which properties will be set.
     */
    @Override
    public void copy(@NonNull Object from, @NonNull Object to) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        if (!fromClass.equals(toClass)) {
            throw new IllegalArgumentException("Objects types are different but they have to be equal");
        }

        for (Method fromMethod : fromClass.getDeclaredMethods()) {
            if (Modifier.isPrivate(fromMethod.getModifiers())) {
                continue;
            }
            if (fromMethod.getName().toLowerCase().startsWith("get")) {
                String nameOfGetter = fromMethod.getName().replace("get", "").toLowerCase();
                for (Method toMethod : toClass.getDeclaredMethods()) {
                    if (toMethod.getName().toLowerCase().contains(nameOfGetter)
                            && toMethod.getName().toLowerCase().startsWith("set")) {
                        if (Modifier.isPrivate(toMethod.getModifiers())) {
                            continue;
                        }
                        Class getterType = fromMethod.getReturnType();
                        Class setterType = toMethod.getParameterTypes()[0];
                        if (areEqualTypes(getterType, setterType)) {
                            try {
                                toMethod.invoke(to, fromMethod.invoke(from, null));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                System.out.println("Oops, can't apply setter with arguments from getter");
                            }
                            break;
                        }

                    }
                }
            }
        }

    }

    private boolean areEqualTypes(Class getterType, Class setterType) {
        while (setterType != null) {
            if (getterType.equals(setterType)) {
                return true;
            }
            setterType = setterType.getSuperclass();
        }
        return false;
    }
}
