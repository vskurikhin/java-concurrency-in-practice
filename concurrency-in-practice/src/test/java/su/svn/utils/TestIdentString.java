package su.svn.utils;

import lombok.Builder;
import lombok.Value;
import su.svn.identification.Identification;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
class TestIdentString implements Identification<String> {

    String id;

    public String id() {
        return id;
    }

    @Override
    public Class<?> idClass() {
        return String.class;
    }
}
