package org.fuyi.wukong.util;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2021/8/12 15:20
 * @since: 1.0
 **/
public class ExecutorTemplate {

    private static final int DEFAULT_CORE_SIZE = 4;
    private static final int DEFAULT_MAX_SIZE = 7;

    public static Executor executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE, DEFAULT_MAX_SIZE,
            30000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100));

}
