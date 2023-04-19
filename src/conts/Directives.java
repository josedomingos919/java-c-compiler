package conts;

import java.util.List;
import java.util.Arrays;

public class Directives {
    public static final String INCLUDE = "#include";
    public static final String DEFINE = "#define";

    public static final List<String> ALL = Arrays.asList(
        INCLUDE,
        DEFINE
    );
}
