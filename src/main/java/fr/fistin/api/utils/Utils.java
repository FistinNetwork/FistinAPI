package fr.fistin.api.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Utils
{
    @SuppressWarnings("unchecked")
    public static <T, C> T unsafeGet(Class<C> clazz, C instance, String memberName, TypeGet typeGet)
    {
        switch (typeGet)
        {
            case FIELD:
            {
                try {
                    final Field field = clazz.getDeclaredField(memberName);
                    field.setAccessible(true);
                    return (T)field.get(instance);
                } catch (Exception e)
                {
                    throw new FistinAPIException(e);
                }
            }
            case METHOD:
            {
                try {
                    final Method method = clazz.getDeclaredMethod(memberName);
                    method.setAccessible(true);
                    return (T)method.invoke(instance);
                }
                catch (Exception e)
                {
                    throw new FistinAPIException(e);
                }
            }
        }
        throw new FistinAPIException("Unknown TypeGet: " + typeGet);
    }

    public enum TypeGet {
        FIELD,
        METHOD
    }
}
