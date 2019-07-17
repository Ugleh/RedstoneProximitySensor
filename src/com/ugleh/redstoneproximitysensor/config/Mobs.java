package com.ugleh.redstoneproximitysensor.config;

import org.bukkit.entity.EntityType;

public enum Mobs {
	BAT("BAT", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	BLAZE("BLAZE", Nature.HOSTILE, Classification.NETHER, false),
	CAT("CAT", Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	CAVE_SPIDER("CAVE_SPIDER", Nature.HOSTILE, Classification.ARTHROPOD, false),
	CHICKEN("CHICKEN", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	COD("COD", Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	COW("COW", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	CREEPER("CREEPER", Nature.HOSTILE, Classification.COMMON, false),
	DOLPHIN("DOLPHIN", Nature.NEUTRAL_ANIMAL, Classification.UNDERWATER, false),
	DONKEY("DONKEY", Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	DROWNED("DROWNED", Nature.HOSTILE, Classification.UNDEAD, true),
	ELDER_GUARDIAN("ELDER_GUARDIAN", Nature.HOSTILE, Classification.UNDERWATER, false),
	ENDER_DRAGON("ENDER_DRAGON", Nature.HOSTILE, Classification.BOSS, false),
	ENDERMAN("ENDERMAN", Nature.NEUTRAL_MONSTER, Classification.COMMON, false),
	ENDERMITE("ENDERMITE", Nature.HOSTILE, Classification.ARTHROPOD, false),
	EVOKER("EVOKER", Nature.HOSTILE, Classification.ILLAGER, false),
	FOX("FOX", Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	GHAST("GHAST", Nature.HOSTILE, Classification.NETHER, false),
	GIANT("GIANT", Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, false),
	GUARDIAN("GUARDIAN", Nature.HOSTILE, Classification.UNDERWATER, false),
	HORSE("HORSE", Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	HUSK("HUSK", Nature.HOSTILE, Classification.UNDEAD, false),
	ILLUSIONER("ILLUSIONER", Nature.HOSTILE, Classification.ILLAGER, false),
	IRON_GOLEM("IRON_GOLEM", Nature.NEUTRAL_MONSTER, Classification.COMMON, false),
	LLAMA("LLAMA", Nature.NEUTRAL_ANIMAL, Classification.COMMON, true),
	MULE("MULE", Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	MUSHROOM_COW("MUSHROOM_COW", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	OCELOT("OCELOT", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	PANDA("PANDA", Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	PARROT("PARROT", Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	PHANTOM("PHANTOM", Nature.HOSTILE, Classification.UNDEAD, false),
	PIG("PIG", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	PIG_ZOMBIE("PIG_ZOMBIE", Nature.NEUTRAL_MONSTER, Classification.NETHER, false),
	PILLAGER("PILLAGER", Nature.HOSTILE, Classification.ILLAGER, false),
	POLAR_BEAR("POLAR_BEAR", Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	PUFFERFISH("PUFFERFISH", Nature.PASSIVE_DEFENSIVE, Classification.UNDERWATER, false),
	RABBIT("RABBIT", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	RAVAGER("RAVAGER", Nature.HOSTILE, Classification.ILLAGER, false),
	SALMON("SALMON", Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	SHEEP("SHEEP", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	SHULKER("SHULKER", Nature.HOSTILE, Classification.COMMON, false),
	SILVERFISH("SILVERFISH", Nature.HOSTILE, Classification.UNDERWATER, false),
	SKELETON("SKELETON", Nature.HOSTILE, Classification.UNDEAD, false),
	SKELETON_HORSE("SKELETON_HORSE", Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, true),
	SLIME("SLIME", Nature.HOSTILE, Classification.COMMON, false),
	SNOWMAN("SNOWMAN", Nature.NEUTRAL_MONSTER, Classification.COMMON, false),
	SPIDER("SPIDER", Nature.HOSTILE, Classification.COMMON, false),
	SQUID("SQUID", Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	STRAY("STRAY", Nature.HOSTILE, Classification.UNDEAD, false),
	TRADER_LLAMA("TRADER_LLAMA", Nature.NEUTRAL_ANIMAL, Classification.COMMON, true),
	TROPICAL_FISH("TROPICAL_FISH", Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	TURTLE("TURTLE", Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	VEX("VEX", Nature.HOSTILE, Classification.ILLAGER, false),
	VILLAGER("VILLAGER", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	VINDICATOR("VINDICATOR", Nature.HOSTILE, Classification.ILLAGER, false),
	WANDERING_TRADER("WANDERING_TRADER", Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	WITCH("WITCH", Nature.HOSTILE, Classification.ILLAGER, false),
	WITHER("WITHER", Nature.HOSTILE, Classification.BOSS, false),
	WITHER_SKELETON("WITHER_SKELETON", Nature.HOSTILE, Classification.UNDEAD, false),
	WOLF("WOLF", Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	ZOMBIE("ZOMBIE", Nature.HOSTILE, Classification.UNDEAD, false),
	ZOMBIE_HORSE("ZOMBIE_HORSE", Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, false),
	ZOMBIE_VILLAGER("ZOMBIE_VILLAGER", Nature.HOSTILE, Classification.UNDEAD, false),

	;
	
	private String entityType;
	private Nature nature;
	private Classification classification;
    private boolean tameable;
    
private Mobs(String entityType, Nature nature, Classification classification, boolean tameable) {
	this.entityType = entityType;
	this.nature = nature;
	this.classification = classification;
	this.tameable = tameable;
}

public String getEntityTypeName() {
	return entityType;
}

public Nature getNature() {
	return nature;
}

public Classification getClassification() {
	return classification;
}
public boolean isTameable() {
	return tameable;
}

public static Mobs getMob(EntityType entityType) {
	//I no longer check if it exists here, I do in the RPS class.
/*	for(Mobs mob : Mobs.values()) {
		if (mob.getEntityType().equals(entityType))
			return mob;
	}
	return null;*/
	return Mobs.valueOf(entityType.name());
}

public static boolean isHostile(EntityType entityType) {
	Mobs mob = getMob(entityType);
	if(mob == null) return false;
	return mob.getNature().equals(Nature.HOSTILE);
}


public static boolean isPeaceful(EntityType entityType) {
	Mobs mob = getMob(entityType);
	if(mob == null) return false;
	return (mob.getNature().equals(Nature.PASSIVE_DEFENSIVE) || mob.getNature().equals(Nature.PASSIVE_PEACEFUL));
}

public static boolean isNeutral(EntityType entityType) {
	Mobs mob = getMob(entityType);
	if(mob == null) return false;
	return (mob.getNature().equals(Nature.NEUTRAL_MONSTER) || mob.getNature().equals(Nature.NEUTRAL_ANIMAL));
}

enum Nature {
	PASSIVE_PEACEFUL,
	PASSIVE_DEFENSIVE,
	NEUTRAL_ANIMAL,
	NEUTRAL_MONSTER,
	HOSTILE,
}

enum Classification {
	COMMON,
	UNDEAD,
	UNDERWATER,
	NETHER,
	ARTHROPOD,
	ILLAGER,
	BOSS;
}

};