package top.gsk.rpc.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.gsk.rpc.enumeration.ResponseCode;

import java.io.Serializable;

/**
 * @author gsk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对应的请求号
     */
    private String requestId;
    /**
     * 相应状态码
     * */
    private Integer statusCode;


    /**
     * 相应状态补充信息
     */
    private String message;

    /**
     * 相应数据
     * */
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;

    }

    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }


}
