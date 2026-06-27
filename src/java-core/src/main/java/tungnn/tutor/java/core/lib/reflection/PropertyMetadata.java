package tungnn.tutor.java.core.lib.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public record PropertyMetadata(
    Field field, Method accessor, Method mutator, boolean readable, boolean writable) {}
