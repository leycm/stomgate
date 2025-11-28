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
import de.leycm.stomgate.permittable.PermittableGroup;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable PermittableGroup getParent();

    @Nullable PermittableGroup setParent();


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
    default int permissionWeight(final @NonNull Permission permission) {
        return PermissionServices.getInstance().resolvePermissionWeight(this, permission);
    }

    /**
     * Resolves a permission node and delegates to {@link #permissionWeight(Permission)}.
     *
     * @param node permission node string (e.g. "chat.color")
     * @return weight associated with that node
     */
    default int permissionWeight(final @NonNull String node) {
        return permissionWeight(Permission.of(node));
    }

    /**
     * Checks if this permittable has the permission (weight > 0).
     *
     * @param permission the permission to evaluate
     * @return true if weight > 0
     */
    default boolean hasPermission(final @NonNull Permission permission) {
        return isPermitted(permission, weight -> weight > 0);
    }

    /**
     * Checks if this permittable has the permission (weight > 0).
     *
     * @param node permission node string
     * @return true if permission weight is positive
     */
    default boolean hasPermission(final @NonNull String node) {
        return hasPermission(Permission.of(node));
    }

    /**
     * Evaluates the permission weight using a custom predicate.
     *
     * @param permission the permission to evaluate
     * @param predicate predicate used to validate the weight
     * @return result of predicate applied on weight
     */
    default boolean isPermitted(final @NonNull Permission permission,
                       final @NonNull Predicate<Integer> predicate) {
        return predicate.test(permissionWeight(permission));
    }

    /**
     * Evaluates the permission weight using a predicate.
     *
     * @param node permission node string
     * @param predicate predicate for weight evaluation
     * @return predicate result
     */
    default boolean isPermitted(final @NonNull String node,
                       final @NonNull Predicate<Integer> predicate) {
        return isPermitted(Permission.of(node), predicate);
    }

    /**
     * Sets the permission weight explicitly.
     *
     * @param permission the permission to set
     * @param weight the weight to assign (positive = granted, 0 = denied, negative = unset)
     */
    default void setPermission(final @NonNull Permission permission, int weight) {
        PermissionServices.getInstance().updatePermissionWeight(this, permission, weight);
    }

    /**
     * Sets a permission by node string.
     *
     * @param node permission node string
     * @param weight weight to assign
     */
    default void setPermission(final @NonNull String node, int weight) {
        setPermission(Permission.of(node), weight);
    }

    /**
     * Grants a permission (sets positive weight, e.g. 1).
     *
     * @param permission permission to grant
     */
    default void grantPermission(final @NonNull Permission permission) {
        setPermission(permission, 1);
    }

    /**
     * Grants a permission by node string.
     *
     * @param node node string
     */
    default void grantPermission(final @NonNull String node) {
        grantPermission(Permission.of(node));
    }

    /**
     * Revokes a permission (sets weight to -1).
     *
     * <p>
     *     Important: This does not set the weight to 0 (denied), but remove the permission entirely.
     *     what leads to -1 (unset) weight if not overwritten by a parent.
     * </p>
     *
     * @param permission permission to revoke
     */
    default void revokePermission(final @NonNull Permission permission) {
        setPermission(permission, -1);
    }

    /**
     * Revokes a permission by node string.
     *
     * @param node node string
     */
    default void revokePermission(final @NonNull String node) {
        revokePermission(Permission.of(node));
    }

    /**
     * Promotes a permission weight by a given delta.
     *
     * @param permission permission to promote
     * @param delta amount to increase weight by
     */
    default void promotePermission(final @NonNull Permission permission, int delta) {
        int current = permissionWeight(permission);
        setPermission(permission, current + delta);
    }

    /**
     * Promotes a permission by node string.
     *
     * @param node node string
     * @param delta weight increment
     */
    default void promotePermission(final @NonNull String node, int delta) {
        promotePermission(Permission.of(node), delta);
    }

}
