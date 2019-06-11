package ru.sberbank.school.task13;

public interface BeanFieldCopier {

    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param from Object which properties will be used to get values.
     * @param to   Object which properties will be set.
     */
    void copy(Object from, Object to);
}
