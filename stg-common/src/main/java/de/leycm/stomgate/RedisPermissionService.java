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

import de.leycm.stomgate.perm.Permission;
import de.leycm.stomgate.perm.Permittable;
import de.leycm.stomgate.permittable.PermittableGroup;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * RedisPermissionService
 *
 * <p>
 * Default implementation of {@link PermissionServices}. Serves as the entry
 * point for initializing and accessing the Template API.
 * </p>
 *
 * <p>
 * Can be used directly or extended for custom behavior.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public class RedisPermissionService implements PermissionServices {

    @Override
    public int resolvePermissionWeight(@NonNull Permittable permittable, @NonNull Permission permission) {
        return 0;
    }

    @Override
    public void updatePermissionWeight(@NonNull Permittable permittable, @NonNull Permission permission, int weight) {

    }


    @Override
    public @Nullable PermittableGroup permittableGroupOf(@NonNull String id) {
        return null;
    }

    @Override
    public @Nullable Permittable permittableOf(@NonNull UUID uuid) {
        return null;
    }

    @Override
    public void registerPermittable(@NonNull Permittable permittable) {

    }
}
