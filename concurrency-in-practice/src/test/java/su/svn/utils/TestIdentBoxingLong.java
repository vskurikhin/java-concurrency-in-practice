package su.svn.utils;

import lombok.Builder;
import lombok.Value;
import su.svn.identification.Identification;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
class TestIdentBoxingLong implements Identification<Long> {

    Long id;

    public Long id() {
        return id;
    }

    @Override
    public Class<?> idClass() {
        return Long.class;
    }
}
