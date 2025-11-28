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
