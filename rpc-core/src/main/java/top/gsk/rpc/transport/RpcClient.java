package top.gsk.rpc.transport;

import top.gsk.rpc.entity.RpcRequest;
import top.gsk.rpc.serializer.CommonSerializer;

/**
 * @author gsk
 * @version 1.0
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);



}
