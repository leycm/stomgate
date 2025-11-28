package de.leycm.stomgate;

import de.leycm.neck.instance.Initializable;
import de.leycm.stomgate.permittable.PermittableGroup;
import de.leycm.stomgate.perm.Permission;
import de.leycm.stomgate.perm.Permittable;
import de.leycm.stomgate.permittable.PermittablePlayer;
import lombok.NonNull;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Core service interface for the permission system.
 *
 * <p>
 * {@code PermissionServices} provides a singleton API for managing
 * permittables (players or groups) and resolving permissions.
 * It is designed to be thread-safe and initialized before use.
 * </p>
 *
 * <p>
 * Implementations of this interface are automatically registered
 * and retrieved via {@link #getInstance()} and can be initialized
 * through the {@link Initializable} lifecycle.
 * </p>
 *
 * <p>
 * Typical usage:
 * <pre>{@code
 * PermissionServices services = PermissionServices.getInstance();
 * int weight = services.resolvePermissionWeight(player, permission);
 * }</pre>
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public interface PermissionServices extends Initializable {

    /**
     * Returns the singleton instance of {@code PermissionServices}.
     *
     * <p>
     * Uses the {@link Initializable#getInstance(Class)} mechanism to
     * retrieve the registered implementation. Throws a {@link NullPointerException}
     * if no implementation has been registered yet.
     * </p>
     *
     * @return the registered singleton instance
     * @throws NullPointerException if no implementation is registered
     */
    @Contract(pure = true)
    static @NonNull PermissionServices getInstance() {
        return Initializable.getInstance(PermissionServices.class);
    }

    /**
     * Registers the provided {@link PermissionServices} instance
     * as the global singleton.
     *
     * @param services the implementation to register
     */
    static void init(final @NonNull PermissionServices services) {
        Initializable.register(services, PermissionServices.class);
    }

    /**
     * Resolves the permission weight for a given {@link Permittable}
     * and {@link Permission}.
     *
     * <p>
     * The weight determines the effective permission value according to the system rules:
     * <ul>
     *     <li>-1 = unset</li>
     *     <li>0 = false</li>
     *     <li>1 = true</li>
     * </ul>
     * </p>
     *
     * <p>
     * Implementations must consider wildcard inheritance and group propagation
     * if supported.
     * </p>
     *
     * @param permittable the permittable (player or group) to evaluate
     * @param permission  the permission node to resolve
     * @return the effective permission weight
     */
    int resolvePermissionWeight(final @NonNull Permittable permittable,
                                final @NonNull Permission permission);

    /**
     * Updates the permission weight for a given {@link Permittable}
     * and {@link Permission}.
     *
     * <p>
     * This method modifies the stored permission weight for the specified
     * permittable and permission node. The weight should follow the system rules:
     * <ul>
     *     <li>-1 = unset</li>
     *     <li>0 = false</li>
     *     <li>1 = true</li>
     * </ul>
     * </p>
     *
     * <p>
     *     Important: The implementation must ensure that -1 leads to the removal
     *     of the permission entry from the storage, effectively unsetting it.
     * </p>
     *
     * @param permittable the permittable (player or group) to update
     * @param permission  the permission node to update
     * @param weight      the new weight to set
     */
    void updatePermissionWeight(final @NonNull Permittable permittable,
                                final @NonNull Permission permission,
                                final int weight);

    /**
     * Retrieves a {@link PermittableGroup} by its string ID.
     *
     * @param id the string identifier of the group
     * @return the group if found, or {@code null} otherwise
     */
    @Nullable
    PermittableGroup permittableGroupOf(final @NonNull String id);

    /**
     * Retrieves a {@link PermittableGroup} by its {@link UUID}.
     *
     * <p>
     * If the permittable corresponding to the UUID is not a group, returns {@code null}.
     * </p>
     *
     * @param uuid the unique ID of the permittable
     * @return the group if the permittable is a group, or {@code null} otherwise
     */
    default @Nullable PermittableGroup permittableGroupOf(final @NonNull UUID uuid) {
        Permittable permittable = permittableOf(uuid);
        if (permittable instanceof PermittableGroup group)
            return group;
        return null;
    }

    /**
     * Retrieves a {@link Permittable} (player or group) by its {@link UUID}.
     *
     * @param uuid the unique identifier of the permittable
     * @return the permittable if found, or {@code null} otherwise
     */
    @Nullable
    Permittable permittableOf(final @NonNull UUID uuid);

    /**
     * Registers a new {@link Permittable} (player or group) in the system.
     *
     * <p>
     * Once registered, the permittable can be retrieved and evaluated
     * through the {@link PermissionServices} API.
     * </p>
     *
     * @param permittable the permittable to register
     */
    void registerPermittable(final @NonNull Permittable permittable);

    /**
     * Lifecycle callback invoked on installation of this service.
     *
     * <p>
     * Default implementation sets the {@link PermittablePlayer} as the
     * default player provider in {@link MinecraftServer}.
     * </p>
     */
    @Override
    default void onInstall() {
        MinecraftServer.getConnectionManager().setPlayerProvider(PermittablePlayer::new);
    }
}
