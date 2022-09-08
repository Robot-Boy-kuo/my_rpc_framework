package top.gsk.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gsk
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求号
     */
    private String requestId;

    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 调用方法的方法名
     *
     * */

    private String methodName;
    /**
     * 调用的参数的具体参数
     *
     *
     * */
    private Object[] parameters;

    /**
     *
     * Request要求方法的参数类型，防止调用重载的方法
     * 通过方法名 methodName 和 参数类型 paramTypes 唯一确定要调用哪个方法
     *
     * */
    private Class<?>[] paramTypes;


    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

}
