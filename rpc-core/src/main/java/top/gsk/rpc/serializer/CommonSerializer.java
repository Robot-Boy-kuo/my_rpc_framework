package top.gsk.rpc.serializer;


/**
 * @author gsk
 * @version 1.0
 */
public interface CommonSerializer {
    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JasonSerializer();
            default:
                return null;
        }
    }
}
