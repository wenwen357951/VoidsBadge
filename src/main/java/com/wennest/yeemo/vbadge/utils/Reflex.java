package com.wennest.yeemo.vbadge.utils;

import com.wennest.yeemo.vbadge.VBadge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class Reflex {

    @Nullable
    public static Field getField(@NotNull Class<?> clazz, @NotNull String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                return null;
            }
            return getField(superClass, fieldName);
        }
    }

    @Nullable
    public static Object getFieldValue(@NotNull Object from, @NotNull String fieldName) {
        try {
            Class<?> clazz = from instanceof Class<?> ? (Class<?>) from : from.getClass();

            Field field = getField(clazz, fieldName);
            if (field == null) {
                return null;
            }

            field.setAccessible(true);
            return field.get(from);
        } catch (IllegalAccessException exception) {
            VBadge.getInstance().getSLF4JLogger().error("Could not access field '{}' in class {}.", fieldName, from.getClass().getName(), exception);
        }

        return null;
    }
}
