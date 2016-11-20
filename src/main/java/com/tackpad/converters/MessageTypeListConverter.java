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
public class MessageTypeListConverter {

    /** */
    public List<MessageType> convert(String text) {

        List<String> stringList = Arrays.asList(StringUtils.delimitedListToStringArray(text, ";"));

        if (stringList.isEmpty()) {
            return null;
        }

        return Lists.transform(stringList, new Function<String, MessageType>() {
            public MessageType apply(String s) {
                return MessageType.valueOf(s);
            }
        });
    }

}
