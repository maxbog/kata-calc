package calc;

import java.math.BigInteger;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public interface ValueResolver {
    BigInteger resolveVariable(String varName);
}
