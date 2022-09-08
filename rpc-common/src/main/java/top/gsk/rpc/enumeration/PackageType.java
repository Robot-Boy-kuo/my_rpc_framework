package top.gsk.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gsk
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
