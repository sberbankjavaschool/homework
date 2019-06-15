package ru.sberbank.school.task13.cacheproxy.utils;

import ru.sberbank.school.task13.cacheproxy.exception.CacheProxyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CacheProxyUtils {
    public static Object cutList(List result, int sizeCutList) {
        if (sizeCutList == -1) {
            sizeCutList = (result).size();
        } else {
            if (sizeCutList < 0 || sizeCutList >= (result).size()) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return new ArrayList<>(result.subList(0, sizeCutList));
    }

    public static String getKey(Class[] identityBy,
                                String annotationsKey,
                                String methodName,
                                Object[] args) throws CacheProxyException {
        String argsId;
        if (identityBy.length > 0) {
            List<Object> requiredArguments = new ArrayList<>();
            int fromIndex = 0;
            for (Class type : identityBy) {
                for (int i = fromIndex; i < args.length; i++) {
                    if (type == args[i].getClass()) {
                        requiredArguments.add(args[i]);
                        fromIndex = ++i;
                        break;
                    }
                }
            }
            if (requiredArguments.size() != identityBy.length) {
                throw new CacheProxyException("Некорректный identityBy!");
            }
            argsId = String.valueOf(requiredArguments.hashCode());
        } else {
            argsId = String.valueOf(Arrays.hashCode(args));
        }

        return (annotationsKey.equals("") ? methodName : annotationsKey) + argsId;
    }
}
