package com.yanyan.core.spring.core.convert.support;

import com.yanyan.core.util.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Spring bean converter, convert String to Date.
 * User: Saintcy
 * Date: 2017/1/10
 * Time: 9:26
 */
public class StringToDateConverter implements Converter<String, Date> {
    public Date convert(String source) {
        return DateUtils.parseDate(source);
    }
}
