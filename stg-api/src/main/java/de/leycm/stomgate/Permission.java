/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a> l <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.stomgate;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.function.Predicate;

public record Permission(@NonNull String... node) {

    @Contract("_ -> new")
    public static @NonNull Permission of(final @NonNull String node) {
        return of(node, "\\.");
    }

    @Contract("_, _ -> new")
    public static @NonNull Permission of(final @NonNull String node,
                                         final @NonNull String delimiter) {
        return new Permission(node.split(delimiter));
    }

    public Permission {
        for (final String part : node) {
            if (part.contains("."))
                throw new IllegalArgumentException("Permission parts cannot contain dots "
                        + part + " in permission " + String.join(", ", node));
        }
    }

    public int wight(final @NonNull Permittable permittable) {
        return permittable.wight(this);
    }

    public boolean has(final @NonNull Permittable permittable) {
        return permittable.has(this);
    }

    public boolean is(final @NonNull Permittable permittable,
                       final @NonNull Predicate<Integer> predicate) {
        return permittable.is(this, predicate);
    }

    public @NonNull String[] toParts() {
        return node.clone();
    }

    public @NonNull String toString(final @NonNull String delimiter) {
        return String.join(delimiter, node);
    }

    @Override
    public @NonNull String toString() {
        return String.join(".", node);
    }

}
