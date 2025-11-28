/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a> <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.stomgate;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.function.Predicate;

/**
 * Represents a parsed permission node consisting of multiple path parts.
 *
 * <p>
 * A permission like {@code "chat.color.red"} becomes:
 * </p>
 *
 * <pre>
 * ["chat", "color", "red"]
 * </pre>
 *
 * <p>
 * Each part is validated to contain no dots and must not be empty.
 * </p>
 *
 * <p>
 * Permissions are immutable and safe to reuse.
 * </p>
 *
 * @param node the permission path parts
 * @author LeyCM
 * @since 1.0.1
 */
public record Permission(@NonNull String... node) {

    /**
     * Parses a permission node using '.' as delimiter.
     *
     * @param node raw string (e.g. "server.admin.kick")
     * @return a new Permission instance
     */
    @Contract("_ -> new")
    public static @NonNull Permission of(@NonNull String node) {
        return of(node, "\\.");
    }

    /**
     * Parses a permission node using a custom delimiter regex.
     *
     * @param node raw node
     * @param delimiter regex used to split the node
     * @return new Permission instance
     */
    @Contract("_, _ -> new")
    public static @NonNull Permission of(@NonNull String node,
                                         @NonNull String delimiter) {
        return new Permission(node.split(delimiter));
    }

    /**
     * Validates the permission parts.
     *
     * @throws IllegalArgumentException if any segment is empty or contains dots
     */
    public Permission {
        if (node.length < 1) {
            throw new IllegalArgumentException("Permission node cannot be empty");
        }

        final String full = '"' + String.join("\", \"", node) + '"';

        for (String part : node) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Permission parts cannot be empty " + full);
            }
            if (part.contains(".")) {
                throw new IllegalArgumentException(
                        "Permission parts cannot contain dots \"" + part + "\" in permission " + full);
            }
        }
    }

    /**
     * Convenience wrapper for {@link Permittable#wight(Permission)}.
     *
     * @param permittable the permittable object
     * @return weight
     */
    public int wight(@NonNull Permittable permittable) {
        return permittable.wight(this);
    }

    /**
     * Convenience wrapper for {@link Permittable#has(Permission)}.
     *
     * @param permittable the permittable object
     * @return true if permission granted
     */
    public boolean has(@NonNull Permittable permittable) {
        return permittable.has(this);
    }

    /**
     * Evaluates the weight using a predicate.
     *
     * @param permittable the permittable
     * @param predicate predicate used for evaluation
     * @return predicate result
     */
    public boolean is(@NonNull Permittable permittable,
                      @NonNull Predicate<Integer> predicate) {
        return permittable.is(this, predicate);
    }

    /**
     * Returns a clone of the underlying node parts.
     *
     * @return array of parts
     */
    public @NonNull String[] toParts() {
        return node.clone();
    }

    /**
     * Joins the parts with the given delimiter.
     *
     * @param delimiter join delimiter
     * @return joined string
     */
    public @NonNull String toString(@NonNull String delimiter) {
        return String.join(delimiter, node);
    }

    /**
     * Returns the permission node as a dot-joined string.
     *
     * @return dot-separated permission
     */
    @Override
    public @NonNull String toString() {
        return String.join(".", node);
    }
}
