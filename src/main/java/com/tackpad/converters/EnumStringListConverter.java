package com.tackpad.converters;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tackpad.models.enums.MessageType;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Przemek on 2016-10-08.
 */
public class EnumStringListConverter<E extends Enum<E>> {

    /** */
    public List<E> convert(final Class<E> enumType, String text) {

        List<String> stringList = Arrays.asList(StringUtils.delimitedListToStringArray(text, ";"));

        if (stringList.isEmpty()) {
            return null;
        }

        return Lists.transform(stringList, new Function<String, E>() {
            public E apply(String s) {
                return of(enumType, s);
            }
        });
    }

    private static <E extends Enum<E>> E of(Class<E> clazz, String name) {
        E value = Enum.valueOf(clazz, name);
        return value;
    }

}
