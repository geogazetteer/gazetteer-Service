package top.geomatics.gazetteer.service.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenfa
 */
@RestController
@RequestMapping("/log")
public class LogController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/test")
    public String getlogs() {
        logger.info("日志测试 log info");
        logger.error("日志测试 log error");
        logger.debug("日志测试 log debug");
        return "OK";
    }
}

