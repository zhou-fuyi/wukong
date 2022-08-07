package org.fuyi.wukong.core;

import static org.fuyi.wukong.core.constant.PriorityConstant.DEFAULT_PRIORITY;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 3:59 pm
 * @since: 1.0
 **/
public interface Priority {

    /**
     * 获取权值, 默认为 100
     *
     * @return
     */
    default int obtainPriority() {
        return DEFAULT_PRIORITY;
    }
}
