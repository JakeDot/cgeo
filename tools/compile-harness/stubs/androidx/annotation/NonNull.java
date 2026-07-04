// Minimal stand-in for androidx.annotation.NonNull, which lives in Google's Maven repository
// (not Maven Central) and is unreachable from this harness's sandboxed build. It is a marker
// annotation only used by the checkstyle/javac frontend here, never shipped or linked into the
// real app - the real app keeps resolving the genuine androidx artifact via main/build.gradle.
package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE})
public @interface NonNull {
}
