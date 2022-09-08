package top.gsk.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gsk
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO(0),
    JSON(1);

    private final int code;

}
