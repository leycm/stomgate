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

import java.util.function.Predicate;

public interface Permittable {

    int wight(final @NonNull Permission permission);
    
    default int wight(final @NonNull String node) {
        return wight(Permission.of(node));
    }
    
    default boolean has(final @NonNull Permission permission) {
        return is(permission, weight -> weight > 0);
    }

    default boolean has(final @NonNull String node) {
        return has(Permission.of(node));
    }
    
    default boolean is(final @NonNull Permission permission, 
                       final @NonNull Predicate<Integer> predicate) {
        return predicate.test(wight(permission));
    }

    default boolean is(final @NonNull String node, 
                       final @NonNull Predicate<Integer> predicate) {
        return is(Permission.of(node), predicate);
    }
    
}
