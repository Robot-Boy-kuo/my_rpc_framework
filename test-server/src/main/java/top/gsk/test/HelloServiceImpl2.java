package top.gsk.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.api.HelloObject;
import top.gsk.rpc.api.HelloService;

/**
 * @author gsk
 * @version 1.0
 */
public class HelloServiceImpl2 implements HelloService {
    private static Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息：{}",object.getMessage());
        return "本次处理来自于Socket服务";
    }
}
