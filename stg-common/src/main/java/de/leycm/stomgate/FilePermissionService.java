package de.leycm.stomgate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.leycm.stomgate.perm.Permission;
import de.leycm.stomgate.permittable.PermittableGroup;
import de.leycm.stomgate.perm.Permittable;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File-based PermissionService with caching for players and groups.
 */
public class FilePermissionService implements PermissionServices {

    private final File folder;
    private final Gson gson = new Gson();

    /** Cache: UUID -> (node -> weight) */
    private final Map<UUID, Map<String, Integer>> cache = new HashMap<>();
    /** Parent cache: UUID -> Parent ID (String) */
    private final Map<UUID, String> parentCache = new HashMap<>();

    public FilePermissionService(@NonNull File folder) {
        this.folder = folder;
        if (!folder.exists()) folder.mkdirs();
    }

    private File fileFor(@NonNull UUID uuid) {
        return new File(folder, uuid + ".json");
    }

    private Map<String, Integer> loadPermissions(@NonNull UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);

        Map<String, Integer> perms = new HashMap<>();
        File file = fileFor(uuid);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, Integer>>(){}.getType();
                Map<String, Integer> loaded = gson.fromJson(reader, type);
                if (loaded != null) perms.putAll(loaded);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cache.put(uuid, perms);
        return perms;
    }

    private void savePermissions(@NonNull UUID uuid) {
        Map<String, Integer> perms = cache.get(uuid);
        if (perms == null) return;

        File file = fileFor(uuid);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(perms, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int resolvePermissionWeight(@NonNull Permittable permittable, @NonNull Permission permission) {
        Map<String, Integer> perms = loadPermissions(permittable.permittableId());
        return perms.getOrDefault(permission.node(), -1);
    }

    @Override
    public void updatePermissionWeight(@NonNull Permittable permittable, @NonNull Permission permission, int weight) {
        Map<String, Integer> perms = loadPermissions(permittable.permittableId());
        perms.put(permission.toString(), weight);
        cache.put(permittable.permittableId(), perms);
        savePermissions(permittable.permittableId());
    }

    @Override
    public @Nullable PermittableGroup permittableGroupOf(@NonNull String id) {
        return null; // Optional: implement group lookup
    }

    @Override
    public @Nullable PermittableGroup parentOf(@NonNull Permittable permittable) {
        String parentId = parentCache.get(permittable.permittableId());
        if (parentId == null) return null;
        return permittableGroupOf(parentId);
    }

    @Override
    public void setParentOf(@NonNull Permittable permittable, PermittableGroup parent) {
        if (parent == null) parentCache.remove(permittable.permittableId());
        else parentCache.put(permittable.permittableId(), parent.groupId());
    }

    @Override
    public @Nullable Permittable permittableOf(@NonNull UUID uuid) {
        return null; // Optional: implement player lookup
    }

    @Override
    public void registerPermittable(@NonNull Permittable permittable) {
        loadPermissions(permittable.permittableId());
    }

    @Override
    public void onInstall() {
        // Load all existing files into cache
        File[] files = folder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));
        if (files == null) return;
        for (File file : files) {
            try {
                String name = file.getName();
                UUID uuid = UUID.fromString(name.substring(0, name.length() - 5));
                loadPermissions(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUninstall() {
        // Save all cached permissions
        for (UUID uuid : cache.keySet()) {
            savePermissions(uuid);
        }
    }
}
