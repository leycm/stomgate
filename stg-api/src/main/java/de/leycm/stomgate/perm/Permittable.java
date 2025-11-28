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
package de.leycm.stomgate.perm;

import de.leycm.stomgate.PermissionServices;
import lombok.NonNull;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * Represents an object that can hold and evaluate permissions.
 *
 * <p>
 * A {@code Permittable} exposes a unique identifier and allows querying
 * permission weights. A weight follows this semantic:
 * </p>
 *
 * <ul>
 *     <li><b>positive</b> → permission granted</li>
 *     <li><b>zero</b> → explicitly denied</li>
 *     <li><b>negative</b> → unset / inherited / undefined depending on system</li>
 * </ul>
 *
 * <p>
 * The interface also includes convenience methods for checking permissions
 * by node or predicate.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public interface Permittable {

    /**
     * Returns the unique ID representing this permittable.
     *
     * @return UUID of the permittable object
     */
    @NonNull UUID permittableId();

    /**
     * Returns the permission weight for the given permission node.
     *
     * <p>
     * Implementations must resolve wildcard inheritance and group
     * propagation if supported.
     * </p>
     *
     * @param permission the permission to evaluate
     * @return an integer weight following the system rules
     */
    default int wight(final @NonNull Permission permission) {
        return PermissionServices.getInstance().resolvePermissionWeight(this, permission);
    }

    /**
     * Resolves a permission node and delegates to {@link #wight(Permission)}.
     *
     * @param node permission node string (e.g. "chat.color")
     * @return weight associated with that node
     */
    default int wight(final @NonNull String node) {
        return wight(Permission.of(node));
    }

    /**
     * Checks if this permittable has the permission (weight > 0).
     *
     * @param permission the permission to evaluate
     * @return true if weight > 0
     */
    default boolean has(final @NonNull Permission permission) {
        return is(permission, weight -> weight > 0);
    }

    /**
     * Checks if this permittable has the permission (weight > 0).
     *
     * @param node permission node string
     * @return true if permission weight is positive
     */
    default boolean has(final @NonNull String node) {
        return has(Permission.of(node));
    }

    /**
     * Evaluates the permission weight using a custom predicate.
     *
     * @param permission the permission to evaluate
     * @param predicate predicate used to validate the weight
     * @return result of predicate applied on weight
     */
    default boolean is(final @NonNull Permission permission,
                       final @NonNull Predicate<Integer> predicate) {
        return predicate.test(wight(permission));
    }

    /**
     * Evaluates the permission weight using a predicate.
     *
     * @param node permission node string
     * @param predicate predicate for weight evaluation
     * @return predicate result
     */
    default boolean is(final @NonNull String node,
                       final @NonNull Predicate<Integer> predicate) {
        return is(Permission.of(node), predicate);
    }
}
