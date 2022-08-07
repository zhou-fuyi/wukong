package org.fuyi.wukong.util;

import org.fuyi.wukong.core.constant.TransformConstant;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 7/8/2022 6:22 pm
 * @since: 1.0
 **/
public class KeyGenerator {

    public static String cacheKeyGenerate(String... segments) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < segments.length; index++) {
            buffer.append(segments[index]);
            buffer.append(TransformConstant.Cache.DELIMITER);
        }
        String cachedKey = buffer.toString();
        if (cachedKey.endsWith(TransformConstant.Cache.DELIMITER)) {
            cachedKey = cachedKey.substring(0, cachedKey.length() - 1);
        }
        return cachedKey;
    }
}
