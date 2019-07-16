package com.ugleh.redstoneproximitysensor.configs;

import org.bukkit.entity.EntityType;

public enum Mobs {
	BAT(EntityType.BAT, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	BLAZE(EntityType.BLAZE, Nature.HOSTILE, Classification.NETHER, false),
	CAT(EntityType.CAT, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, Nature.HOSTILE, Classification.ARTHROPOD, false),
	CHICKEN(EntityType.CHICKEN, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	COD(EntityType.COD, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	COW(EntityType.COW, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	CREEPER(EntityType.CREEPER, Nature.HOSTILE, Classification.COMMON, false),
	DOLPHIN(EntityType.DOLPHIN, Nature.NEUTRAL_ANIMAL, Classification.UNDERWATER, false),
	DONKEY(EntityType.DONKEY, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	DROWNED(EntityType.DROWNED, Nature.HOSTILE, Classification.UNDEAD, true),
	ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, Nature.HOSTILE, Classification.UNDERWATER, false),
	ENDER_DRAGON(EntityType.ENDER_DRAGON, Nature.HOSTILE, Classification.BOSS, false),
	ENDERMAN(EntityType.ENDERMAN, Nature.NEUTRAL_MONSTER, Classification.COMMON, false),
	ENDERMITE(EntityType.ENDERMITE, Nature.HOSTILE, Classification.ARTHROPOD, false),
	EVOKER(EntityType.EVOKER, Nature.HOSTILE, Classification.ILLAGER, false),
	FOX(EntityType.FOX, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	GHAST(EntityType.GHAST, Nature.HOSTILE, Classification.NETHER, false),
	GIANT(EntityType.GIANT, Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, false),
	GUARDIAN(EntityType.GUARDIAN, Nature.HOSTILE, Classification.UNDERWATER, false),
	HORSE(EntityType.HORSE, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	HUSK(EntityType.HUSK, Nature.HOSTILE, Classification.UNDEAD, false),
	ILLUSIONER(EntityType.ILLUSIONER, Nature.HOSTILE, Classification.ILLAGER, false),
	IRON_GOLEM(EntityType.IRON_GOLEM, Nature.NEUTRAL_MONSTER, Classification.COMMON, false),
	LLAMA(EntityType.LLAMA, Nature.NEUTRAL_ANIMAL, Classification.COMMON, true),
	MULE(EntityType.MULE, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	MUSHROOM_COW(EntityType.MUSHROOM_COW, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	OCELOT(EntityType.OCELOT, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	PANDA(EntityType.PANDA, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	PARROT(EntityType.PARROT, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true),
	PHANTOM(EntityType.PHANTOM, Nature.HOSTILE, Classification.UNDEAD, false),
	PIG(EntityType.PIG, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	PIG_ZOMBIE(EntityType.PIG_ZOMBIE, Nature.NEUTRAL_MONSTER, Classification.NETHER, false),
	PILLAGER(EntityType.PILLAGER, Nature.HOSTILE, Classification.ILLAGER, false),
	POLAR_BEAR(EntityType.POLAR_BEAR, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	PUFFERFISH(EntityType.PUFFERFISH, Nature.PASSIVE_DEFENSIVE, Classification.UNDERWATER, false),
	RABBIT(EntityType.RABBIT, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	RAVAGER(EntityType.RAVAGER, Nature.HOSTILE, Classification.ILLAGER, false),
	SALMON(EntityType.SALMON, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	SHEEP(EntityType.SHEEP, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	SHULKER(EntityType.SHULKER, Nature.HOSTILE, Classification.COMMON, false),
	SILVERFISH(EntityType.SILVERFISH, Nature.HOSTILE, Classification.UNDERWATER, false),
	SKELETON(EntityType.SKELETON, Nature.HOSTILE, Classification.UNDEAD, false),
	SKELETON_HORSE(EntityType.SKELETON_HORSE, Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, true),
	SLIME(EntityType.SLIME, Nature.HOSTILE, Classification.COMMON, false),
	SNOWMAN(EntityType.SNOWMAN, Nature.NEUTRAL_MONSTER, Classification.COMMON, false),
	SPIDER(EntityType.SPIDER, Nature.HOSTILE, Classification.COMMON, false),
	SQUID(EntityType.SQUID, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	STRAY(EntityType.STRAY, Nature.HOSTILE, Classification.UNDEAD, false),
	TRADER_LLAMA(EntityType.TRADER_LLAMA, Nature.NEUTRAL_ANIMAL, Classification.COMMON, true),
	TROPICAL_FISH(EntityType.TROPICAL_FISH, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	
	TURTLE(EntityType.TURTLE, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false),
	VEX(EntityType.VEX, Nature.HOSTILE, Classification.ILLAGER, false),
	VILLAGER(EntityType.VILLAGER, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	VINDICATOR(EntityType.VINDICATOR, Nature.HOSTILE, Classification.ILLAGER, false),
	WANDERING_TRADER(EntityType.WANDERING_TRADER, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false),
	WITCH(EntityType.WITCH, Nature.HOSTILE, Classification.ILLAGER, false),
	WITHER(EntityType.WITHER, Nature.HOSTILE, Classification.BOSS, false),
	WITHER_SKELETON(EntityType.WITHER_SKELETON, Nature.HOSTILE, Classification.UNDEAD, false),
	
	WOLF(EntityType.WOLF, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false),
	ZOMBIE(EntityType.ZOMBIE, Nature.HOSTILE, Classification.UNDEAD, false),
	ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, false),
	ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, Nature.HOSTILE, Classification.UNDEAD, false),

	;
	
	private EntityType entityType;
	private Nature nature;
	private Classification classification;
    private boolean tameable;
    
private Mobs(EntityType entityType, Nature nature, Classification classification, boolean tameable) {
	this.entityType = entityType;
	this.nature = nature;
	this.classification = classification;
	this.tameable = tameable;
}

public EntityType getEntityType() {
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
	return (mob.getNature().equals(Nature.HOSTILE) || mob.getNature().equals(Nature.NEUTRAL_ANIMAL) || mob.getNature().equals(Nature.NEUTRAL_MONSTER));
}


public static boolean isPeaceful(EntityType entityType) {
	Mobs mob = getMob(entityType);
	if(mob == null) return false;
	return (mob.getNature().equals(Nature.PASSIVE_DEFENSIVE) || mob.getNature().equals(Nature.PASSIVE_PEACEFUL));
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