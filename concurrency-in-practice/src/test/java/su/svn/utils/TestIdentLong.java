package su.svn.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import su.svn.identification.IdentificationLong;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
class TestIdentLong implements IdentificationLong {

    long id;

    public long id() {
        return id;
    }
}
