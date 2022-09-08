package top.gsk.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author gsk
 * @version 1.0
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称查找服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    InetSocketAddress lookupService(String serviceName);

}
