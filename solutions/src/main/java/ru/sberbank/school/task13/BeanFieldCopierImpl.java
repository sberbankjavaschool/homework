package ru.sberbank.school.task13;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class BeanFieldCopierImpl implements BeanFieldCopier {
    @Override
    public void copy(Object from, Object to) {

        Method[] classFromMethods = from.getClass().getMethods();
        Method[] classToMethods = to.getClass().getMethods();

        for (Method fromMethod : classFromMethods) {
            if (fromMethod.getParameterCount() == 0) {
                String toMethodName;
                if (Pattern.matches("^get[A-Z].*", fromMethod.getName())) {
                    toMethodName = fromMethod.getName().replaceFirst("get", "set");
                } else if (Pattern.matches("^is[A-Z].*", fromMethod.getName())) {
                    toMethodName = fromMethod.getName().replaceFirst("is", "set");
                } else {
                    continue;
                }
                for (Method toMethod : classToMethods) {
                    if  (toMethod.getName().equals(toMethodName)
                            && toMethod.getParameterCount() == 1
                            && toMethod.getParameterTypes()[0] == fromMethod.getReturnType()) {
                        try {
                            toMethod.invoke(to, fromMethod.invoke(from));
                        } catch (IllegalAccessException e) {
                            System.out.println("This shouldn't happen. All methods IS public, "
                                    + "because of .getMethods() returns only public methods.");
                        } catch (InvocationTargetException e) {
                            System.out.println("One of methods, " + fromMethod.getName() + " or " + toMethodName
                                    + ", thrown exception:" + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
