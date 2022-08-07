package org.fuyi.wukong.controller;

import org.fuyi.wukong.core.WuKongTransformManager;
import org.fuyi.wukong.core.command.TransformCommand;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 10:59 pm
 * @since: 1.0
 **/
@RestController
@RequestMapping("/v1/transform")
public class TransformController {

    private WuKongTransformManager transformManager;

    public TransformController(WuKongTransformManager transformManager) {
        this.transformManager = transformManager;
    }

    @PostMapping("/2021")
    public void transform2021(@RequestBody TransformCommand command){
        transformManager.execute(command);
    }
}
