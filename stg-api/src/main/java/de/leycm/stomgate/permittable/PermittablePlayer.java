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
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;

import java.util.UUID;

/**
 * Represents a {@link Player} in the permission system, implementing the
 * {@link Permittable} interface.
 *
 * <p>
 * This class wraps a standard Minestom {@link Player} but integrates
 * it with the {@link PermissionServices} system. Upon creation, each
 * player is automatically registered in the global permission service.
 * </p>
 *
 * <p>
 * The {@link #permittableId()} method returns the player's {@link UUID},
 * which is used as the unique identifier in the permission system.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 */
public class PermittablePlayer extends Player implements Permittable {

    /**
     * Constructs a new {@code PermittablePlayer}.
     *
     * <p>
     * The player is automatically registered in {@link PermissionServices} upon creation.
     * </p>
     *
     * @param playerConnection the connection for this player
     * @param gameProfile      the game profile containing player data
     */
    public PermittablePlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        PermissionServices.getInstance().registerPermittable(this);
    }

    /**
     * Returns the unique identifier of this permittable, which is
     * the same as the player's {@link UUID}.
     *
     * @return the UUID of the player
     */
    @Override
    public @NonNull UUID permittableId() {
        return getUuid();
    }
}
