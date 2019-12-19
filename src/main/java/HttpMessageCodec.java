import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-19 11:10
 * @description
 **/
public class HttpMessageCodec extends ByteToMessageCodec {


    @Override
    protected boolean decode(List<Byte> in, Object out) {
        return false;
    }

    @Override
    protected boolean encode(Object in, List<Byte> out) {
        return false;
    }
}
