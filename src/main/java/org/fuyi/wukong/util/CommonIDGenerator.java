package org.fuyi.wukong.util;

import java.util.UUID;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 7/8/2022 10:48 pm
 * @since: 1.0
 **/
public class CommonIDGenerator {
    public static String next(){
        return UUID.randomUUID().toString();
    }
}
