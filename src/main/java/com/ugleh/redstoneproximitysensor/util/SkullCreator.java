package com.ugleh.redstoneproximitysensor.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
/**
 * A library for the Bukkit API to create player skulls
 * from names, base64 strings, and texture URLs.
 * <p>
 * Does not use any NMS code, and should work across all versions.
 *
 * @author deanveloper on 12/28/2016.
 */
public class SkullCreator {
    private SkullCreator() {}
    private static boolean mutateWithNew = false;
    private static Method metaSetProfileMethod;
    private static Field metaProfileField;
    private static final String TEXTURES = "textures";
    /**
     * Creates a player skull, should work in both legacy and new Bukkit APIs.
     * @return Skull ItemStack
     */
    public static ItemStack createSkull() {
        return new ItemStack(Material.PLAYER_HEAD);
    }
    /**
     * Creates a player skull item with the skin based on a player's name.
     *
     * @param name The Player's name.
     * @return The head of the Player.
     * @deprecated names don't make for good identifiers.
     */
    public static ItemStack itemFromName(String name) {
        return itemWithName(createSkull(), name);
    }
    /**
     * Creates a player skull item with the skin at a Mojang URL.
     *
     * @param url The Mojang URL.
     * @return The head of the Player.
     */
    public static ItemStack itemFromUrl(String url) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return itemWithUrl(createSkull(), url);
    }

    
    /**
     * Creates a player skull item with the skin based on a base64 string.
     *
     * @param base64 The Base64 string.
     * @return The head of the Player.
     */
    public static ItemStack itemFromBase64(String base64) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return itemWithBase64(createSkull(), base64);
    }
    /**
     * Modifies a skull to use the skin of the player with a given name.
     *
     * @param item The item to apply the name to. Must be a player skull.
     * @param name The Player's name.
     * @return The head of the Player.
     * @deprecated names don't make for good identifiers.
     */
    @Deprecated
    public static ItemStack itemWithName(ItemStack item, String name) {
        notNull(item, "item");
        notNull(name, "name");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setOwner(name);
        }
        item.setItemMeta(meta);
        return item;
    }
    /**
     * Modifies a skull to use the skin at the given Mojang URL.
     *
     * @param item The item to apply the skin to. Must be a player skull.
     * @param url  The URL of the Mojang skin.
     * @return The head associated with the URL.
     */
    public static ItemStack itemWithUrl(ItemStack item, String url) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        notNull(item, "item");
        notNull(url, "url");
        return itemWithBase64(item, urlToBase64(url));
    }
    /**
     * Modifies a skull to use the skin based on the given base64 string.
     *
     * @param item   The ItemStack to put the base64 onto. Must be a player skull.
     * @param base64 The base64 string containing the texture.
     * @return The head with a custom texture.
     */
    public static ItemStack itemWithBase64(ItemStack item, String base64) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        notNull(item, "item");
        notNull(base64, "base64");
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            return null;
        }
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (mutateWithNew || !mutateItemMeta(meta, base64)) {
            mutateNewItemMeta(meta, base64);
            mutateWithNew = true;
        }
        item.setItemMeta(meta);
        return item;
    }
    /**
     * Sets the block to a skull with the given name.
     *
     * @param block The block to set.
     * @param name  The player to set it to.
     * @deprecated names don't make for good identifiers.
     */
    @Deprecated
    public static void blockWithName(Block block, String name) {
        notNull(block, "block");
        notNull(name, "name");
        Skull state = (Skull) block.getState();
        state.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        state.update(false, false);
    }
    private static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " should not be null!");
        }
    }
    private static String urlToBase64(String url) {
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl + "\"}}}";
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }
    private static GameProfile makeProfile(String b64) {
        UUID id = UUID.nameUUIDFromBytes(b64.getBytes(StandardCharsets.UTF_8));
        GameProfile profile = new GameProfile(id, "Player");
        return applyTextureProperty(profile, b64) ? profile : null;
    }

    private static boolean applyTextureProperty(GameProfile profile, String b64) {
        Property property = new Property(TEXTURES, b64);
        // First try the legacy API directly.
        try {
            profile.getProperties().put(TEXTURES, property);
            return true;
        } catch (NoSuchMethodError ignored) {
            // Fall through to reflection based attempts for newer Authlib versions.
        }

        if (tryApplyViaAccessor(profile, "getProperties", property)) {
            return true;
        }

        if (tryApplyViaAccessor(profile, "properties", property)) {
            return true;
        }

        if (invokeProfileMutator(profile, "setProperty", property)) {
            return true;
        }

        if (invokeProfileMutator(profile, "addProperty", property)) {
            return true;
        }

        try {
            Field field = GameProfile.class.getDeclaredField("properties");
            field.setAccessible(true);
            Object map = field.get(profile);
            if (tryPutTexture(map, property)) {
                return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            // Ignore and fall through to failure.
        }

        return false;
    }

    private static boolean tryApplyViaAccessor(GameProfile profile, String accessor, Property property) {
        try {
            Method accessorMethod = GameProfile.class.getMethod(accessor);
            accessorMethod.setAccessible(true);
            Object map = accessorMethod.invoke(profile);
            return tryPutTexture(map, property);
        } catch (NoSuchMethodException ignored) {
            return false;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    private static boolean invokeProfileMutator(GameProfile profile, String methodName, Property property) {
        try {
            Method method = GameProfile.class.getMethod(methodName, Property.class);
            method.setAccessible(true);
            method.invoke(profile, property);
            return true;
        } catch (NoSuchMethodException ignored) {
            return false;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    private static boolean tryPutTexture(Object map, Property property) {
        if (map == null) {
            return false;
        }

        try {
            Method put = map.getClass().getMethod("put", String.class, Property.class);
            put.setAccessible(true);
            put.invoke(map, TEXTURES, property);
            return true;
        } catch (NoSuchMethodException ignored) {
            // Try to locate a more generic put/add variant.
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }

        for (Method candidate : map.getClass().getMethods()) {
            if (!candidate.getName().equals("put") && !candidate.getName().equals("add")) {
                continue;
            }
            if (candidate.getParameterCount() != 2) {
                continue;
            }
            Class<?>[] params = candidate.getParameterTypes();
            if (!params[0].isAssignableFrom(String.class) || !params[1].isAssignableFrom(Property.class)) {
                continue;
            }
            try {
                candidate.setAccessible(true);
                candidate.invoke(map, TEXTURES, property);
                return true;
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                // Try next candidate.
            }
        }

        return false;
    }
    private static boolean mutateItemMeta(SkullMeta meta, String b64) {
        GameProfile profile = makeProfile(b64);
        if (profile == null) {
            return false;
        }
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(meta, profile);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, profile);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    private static void mutateNewItemMeta(SkullMeta meta, String b64) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        final UUID uuid = UUID.randomUUID();
        final Method method = Bukkit.getServer().getClass().getDeclaredMethod("createPlayerProfile", UUID.class, String.class);
        method.setAccessible(true);
        final Object playerProfile = method.invoke(Bukkit.getServer(), uuid, uuid.toString().substring(0, 16));
        Method setPropertyMethod = playerProfile.getClass().getDeclaredMethod("setProperty", String.class, Property.class);
        setPropertyMethod.setAccessible(true);
        setPropertyMethod.invoke(playerProfile, "textures", new Property("textures", b64));
        final Method setOwnerProfileMethod = meta.getClass().getMethod("setOwnerProfile", Class.forName("org.bukkit.profile.PlayerProfile"));
        setOwnerProfileMethod.setAccessible(true);
        setOwnerProfileMethod.invoke(meta, playerProfile);
    }
}