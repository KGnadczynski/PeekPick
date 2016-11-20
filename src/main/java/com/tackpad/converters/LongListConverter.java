package com.tackpad.converters;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Przemek on 2016-10-08.
 */
public class LongListConverter {

    /** */
    public List<Long> convert(String text) {

        List<String> stringList = Arrays.asList(StringUtils.delimitedListToStringArray(text, ";"));

        if (stringList.isEmpty()) {
            return null;
        }

        return Lists.transform(stringList, new Function<String, Long>() {
            public Long apply(String s) {
                return Long.valueOf(s);
            }
        });
    }

}
