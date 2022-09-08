package top.gsk.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gsk.rpc.api.HelloObject;
import top.gsk.rpc.api.HelloService;

/**
 * @author gsk
 * @version 1.0
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object){
        logger.info("接收到：{}",object.getMessage());
        return "Hi, Client, id= " + object.getId();
    }

}
