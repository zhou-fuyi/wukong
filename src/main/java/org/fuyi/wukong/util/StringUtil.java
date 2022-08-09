package org.fuyi.wukong.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 9/8/2022 11:31 pm
 * @since: 1.0
 **/
public class StringUtil {
    public static String replaceAllFirstNumber(String source){
        Matcher matcher = Pattern.compile("[\\D]").matcher(source);
        if (matcher.find()){
            return source.substring(matcher.start());
        }
        return source;
    }
}
