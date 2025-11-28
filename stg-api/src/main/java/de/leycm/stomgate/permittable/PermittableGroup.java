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
package de.leycm.stomgate.permittable;

import de.leycm.stomgate.PermissionServices;
import de.leycm.stomgate.perm.Permittable;
import lombok.NonNull;
import net.minestom.server.tag.TagHandler;
import net.minestom.server.tag.Taggable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a static permission carrier such as a group, role, or rank.
 *
 * <p>
 * A {@code PermittableGroup} provides a default implementation of {@link Permittable}
 * and integrates with the global {@link PermissionServices} for permission resolution.
 * </p>
 *
 * <p>
 * Groups are typically shared across multiple players and represent collections
 * of permissions. The {@link #permittableId()} provides a unique {@link UUID} per group.
 * </p>
 *
 * <p>
 * Static accessors {@link #of(UUID)} and {@link #of(String)} allow fetching
 * existing groups from the registered {@link PermissionServices} instance.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public record PermittableGroup(String id, UUID uuid, TagHandler tagHandler)
        implements Permittable, Taggable {


    /**
     * Returns an existing {@link PermittableGroup} by its {@link UUID}.
     *
     * @param uuid the unique ID of the group
     * @return the group if found, or {@code null} if no group exists with the given UUID
     */
    public static @Nullable PermittableGroup of(UUID uuid) {
        return PermissionServices.getInstance().permittableGroupOf(uuid);
    }

    /**
     * Returns an existing {@link PermittableGroup} by its string ID.
     *
     * @param id the string identifier of the group
     * @return the group if found, or {@code null} if no group exists with the given ID
     */
    public static @Nullable PermittableGroup of(String id) {
        return PermissionServices.getInstance().permittableGroupOf(id);
    }

    /**
     * Creates a new {@link PermittableGroup} with a random {@link UUID} and a new {@link TagHandler}.
     *
     * <p>
     * This constructor registers the new group automatically in {@link PermissionServices}.
     * </p>
     *
     * @param id the string identifier of the group
     */
    private PermittableGroup(String id) {
        this(id, UUID.randomUUID(), TagHandler.newHandler());
    }

    /**
     * Records constructor, automatically registers this instance in
     * {@link PermissionServices}.
     *
     * @param id         the string identifier of the group
     * @param uuid       the unique identifier of the group
     * @param tagHandler the tag handler for custom data
     */
    public PermittableGroup {
        PermissionServices.getInstance().registerPermittable(this);
    }

    /**
     * Returns the unique identifier of this permittable group.
     *
     * @return the UUID of the group
     */
    @Override
    public @NonNull UUID permittableId() {
        return uuid;
    }

    /**
     * Returns the string identifier of this group.
     *
     * @return the group ID
     */
    public @NonNull String groupId() {
        return id;
    }
}
