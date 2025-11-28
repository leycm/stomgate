package de.leycm.stomgate;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public record Permission(@NonNull String... node) {

    @Contract("_, _ -> new")
    public static @NotNull Permission of(final @NonNull String node, final @NonNull String delimiter) {
        return new Permission(node.split(delimiter));
    }

    @Contract("_ -> new")
    public static @NotNull Permission of(final @NonNull String node) {
        return new Permission(node.split("\\."));
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
