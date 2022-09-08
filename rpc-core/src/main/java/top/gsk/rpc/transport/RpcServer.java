package top.gsk.rpc.transport;

import top.gsk.rpc.serializer.CommonSerializer;

/**
 * @author gsk
 * @version 1.0
 */
public interface RpcServer {
    void start();

    void setSerializer(CommonSerializer serializer);

    <T> void publishService(Object service, Class<T> serviceClass);

}
