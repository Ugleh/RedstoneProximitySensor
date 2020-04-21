package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public enum Mobs {
	BAT("BAT", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU5OWRlZWY5MTlkYjY2YWMyYmQyOGQ2MzAyNzU2Y2NkNTdjN2Y4YjEyYjlkY2E4ZjQxYzNlMGEwNGFjMWNjIn19fQ=="),
	BEE("BEE", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQyMGM5YzQzZTA5NTg4MGRjZDJlMjgxYzgxZjQ3YjE2M2I0NzhmNThhNTg0YmI2MWY5M2U2ZTEwYTE1NWYzMSJ9fX0="),
	BLAZE("BLAZE", null, Nature.HOSTILE, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ=="),
	CAT("CAT", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIyNTNmYzZiNjU2OTg4NDUzYTJkNzEzOGZjYTRkMWYyNzUyZjQ3NjkxZjBjNDM0ZTQzMjE4Mzc3MWNmZTEifX19"),
	CAVE_SPIDER("CAVE_SPIDER", null, Nature.HOSTILE, Classification.ARTHROPOD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2NDVkZmQ3N2QwOTkyMzEwN2IzNDk2ZTk0ZWViNWMzMDMyOWY5N2VmYzk2ZWQ3NmUyMjZlOTgyMjQifX19"),
	CHICKEN("CHICKEN", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ=="),
	COD("COD", null, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg5MmQ3ZGQ2YWFkZjM1Zjg2ZGEyN2ZiNjNkYTRlZGRhMjExZGY5NmQyODI5ZjY5MTQ2MmE0ZmIxY2FiMCJ9fX0="),
	COW("COW", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY2MGU5MWUxZjVmYzUxMGI1Zjg2NTIwYTU1MTZhOTIxZjM3NjU3Zjk2NWZkYmIyMzNkYWQ4NDc0MDI5YTk2ZiJ9fX0="),
	CREEPER("CREEPER", null, Nature.HOSTILE, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQyNTQ4MzhjMzNlYTIyN2ZmY2EyMjNkZGRhYWJmZTBiMDIxNWY3MGRhNjQ5ZTk0NDQ3N2Y0NDM3MGNhNjk1MiJ9fX0="),
	DOLPHIN("DOLPHIN", null, Nature.NEUTRAL_ANIMAL, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ=="),
	DONKEY("DONKEY", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IyNTI2NmQ0MGNlY2Q5M2QwNTMxNTZlNGE0YTc4NDE0MGQwMzQyNTVjNzIxY2MzNzVkMWMzNjQ4MzQyYjZmZCJ9fX0="),
	DROWNED("DROWNED", null, Nature.HOSTILE, Classification.UNDEAD, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmN2NjZjYxZGJjM2Y5ZmU5YTYzMzNjZGUwYzBlMTQzOTllYjJlZWE3MWQzNGNmMjIzYjNhY2UyMjA1MSJ9fX0="),
	ELDER_GUARDIAN("ELDER_GUARDIAN", null, Nature.HOSTILE, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19"),
	ENDER_DRAGON("ENDER_DRAGON", null, Nature.HOSTILE, Classification.BOSS, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRlY2MwNDA3ODVlNTQ2NjNlODU1ZWYwNDg2ZGE3MjE1NGQ2OWJiNGI3NDI0YjczODFjY2Y5NWIwOTVhIn19fQ=="),
	ENDERMAN("ENDERMAN", null, Nature.NEUTRAL_MONSTER, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTZjMGIzNmQ1M2ZmZjY5YTQ5YzdkNmYzOTMyZjJiMGZlOTQ4ZTAzMjIyNmQ1ZTgwNDVlYzU4NDA4YTM2ZTk1MSJ9fX0="),
	ENDERMITE("ENDERMITE", null, Nature.HOSTILE, Classification.ARTHROPOD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJjN2I5ZDM2ZmI5MmI2YmYyOTJiZTczZDMyYzZjNWIwZWNjMjViNDQzMjNhNTQxZmFlMWYxZTY3ZTM5M2EzZSJ9fX0="),
	EVOKER("EVOKER", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjMwY2U3NzVlZGI2NWRiOGMyNzQxYmRmYWU4NGYzYzBkMDI4NWFiYTkzYWZhZGM3NDkwMGQ1NWRmZDk1MDRhNSJ9fX0="),
	FOX("FOX", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZkYjdkNTA3Mzg5YTE0YmJlYzM5ZGU2OTIyMTY1YjMyZDQzNjU3YmNiNmFhZjRiNTE4MjgyNWIyMmI0In19fQ=="),
	GHAST("GHAST", null, Nature.HOSTILE, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E4YjcxNGQzMmQ3ZjZjZjhiMzdlMjIxYjc1OGI5YzU5OWZmNzY2NjdjN2NkNDViYmM0OWM1ZWYxOTg1ODY0NiJ9fX0="),
	GIANT("GIANT", null, Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ=="),
	GUARDIAN("GUARDIAN", null, Nature.HOSTILE, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk1MjkwZTA5MGMyMzg4MzJiZDc4NjBmYzAzMzk0OGM0ZDAzMTM1MzUzM2FjOGY2NzA5ODgyM2I3ZjY2N2YxYyJ9fX0="),
	HOGLIN("HOGLIN", null, Nature.HOSTILE, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJiOWJjMGYwMWRiZDc2MmEwOGQ5ZTc3YzA4MDY5ZWQ3Yzk1MzY0YWEzMGNhMTA3MjIwODU2MWI3MzBlOGQ3NSJ9fX0="),
	HORSE("HORSE", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU3OGM0NzYyNjc0ZGRlOGIxYTVhMWU4NzNiMzNmMjhlMTNlN2MxMDJiMTkzZjY4MzU0OWIzOGRjNzBlMCJ9fX0="),
	HUSK("HUSK", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3NGM2M2M4ZGI1ZjRjYTYyOGQ2OWEzYjFmOGEzNmUyOWQ4ZmQ3NzVlMWE2YmRiNmNhYmI0YmU0ZGIxMjEifX19"),
	ILLUSIONER("ILLUSIONER", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYyODgyZGQwOTcyM2U0N2MwYWI5NjYzZWFiMDgzZDZhNTk2OTI3MzcwNjExMGM4MjkxMGU2MWJmOGE4ZjA3ZSJ9fX0="),
	IRON_GOLEM("IRON_GOLEM", null, Nature.NEUTRAL_MONSTER, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19"),
	LLAMA("LLAMA", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc3NmE3OGY5NjI0NGUzZGE3MzJmYWZmZDkzYTMzOTgzNGRiMjdiNjk1NWJmN2E5YjI0YWU5ODEyNWI3ZWQifX19"),
	MAGMA_CUBE("MAGMA_CUBE", null, Nature.HOSTILE, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0="),
	MULE("MULE", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0ODZhNzQyZTdkZGEwYmFlNjFjZTJmNTVmYTEzNTI3ZjFjM2IzMzRjNTdjMDM0YmI0Y2YxMzJmYjVmNWYifX19"),
	MUSHROOM_COW("MUSHROOM_COW", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI1Mjg0MWYyZmQ1ODllMGJjODRjYmFiZjllMWMyN2NiNzBjYWM5OGY4ZDZiM2RkMDY1ZTU1YTRkY2I3MGQ3NyJ9fX0="),
	OCELOT("OCELOT", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY1N2NkNWMyOTg5ZmY5NzU3MGZlYzRkZGNkYzY5MjZhNjhhMzM5MzI1MGMxYmUxZjBiMTE0YTFkYjEifX19"),
	PANDA("PANDA", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNhMDk2ZWVhNTA2MzAxYmVhNmQ0YjE3ZWUxNjA1NjI1YTZmNTA4MmM3MWY3NGE2MzljYzk0MDQzOWY0NzE2NiJ9fX0="),
	PARROT("PARROT", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRiYThkNjZmZWNiMTk5MmU5NGI4Njg3ZDZhYjRhNTMyMGFiNzU5NGFjMTk0YTI2MTVlZDRkZjgxOGVkYmMzIn19fQ=="),
	PHANTOM("PHANTOM", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U5NTE1M2VjMjMyODRiMjgzZjAwZDE5ZDI5NzU2ZjI0NDMxM2EwNjFiNzBhYzAzYjk3ZDIzNmVlNTdiZDk4MiJ9fX0="),
	PIG("PIG", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0="),
	PIGLIN("PIGLIN", null, Nature.HOSTILE, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBiYzlkYmI0NDA0YjgwMGY4Y2YwMjU2MjIwZmY3NGIwYjcxZGJhOGI2NjYwMGI2NzM0ZjRkNjMzNjE2MThmNSJ9fX0="),
	PIG_ZOMBIE("PIG_ZOMBIE", "ZOMBIFIED PIGLIN", Nature.NEUTRAL_MONSTER, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTVmYjJkZjc1NGM5OGI3NDJkMzVlN2I4MWExZWVhYzlkMzdjNjlmYzhjZmVjZDNlOTFjNjc5ODM1MTZmIn19fQ=="),
	ZOMBIE_PIGMAN("ZOMBIE_PIGMAN", null, Nature.NEUTRAL_MONSTER, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTVmYjJkZjc1NGM5OGI3NDJkMzVlN2I4MWExZWVhYzlkMzdjNjlmYzhjZmVjZDNlOTFjNjc5ODM1MTZmIn19fQ=="),
	PILLAGER("PILLAGER", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0="),
	POLAR_BEAR("POLAR_BEAR", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFiMTc4ZjVjZGQ3NTBmMGUzNTY4NjBhYTU1MzkxNTNlYjJhYmVjMWUxNDZjYTU3YzY1ZDI1YTVkZjhmZGZlIn19fQ=="),
	PUFFERFISH("PUFFERFISH", null, Nature.PASSIVE_DEFENSIVE, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNiMTk1NWQzYjZlYjQyZjUwZTUzNmMxYTMyODVhYjczZWQ3ZTJiZTA1MWIwOWIyMWUxNzgxMWYxYTZkIn19fQ=="),
	RABBIT("RABBIT", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZlY2M2YjVlNmVhNWNlZDc0YzQ2ZTc2MjdiZTNmMDgyNjMyN2ZiYTI2Mzg2YzZjYzc4NjMzNzJlOWJjIn19fQ=="),
	RAVAGER("RAVAGER", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWNiOWYxMzlmOTQ4OWQ4NmU0MTBhMDZkOGNiYzY3MGM4MDI4MTM3NTA4ZTNlNGJlZjYxMmZlMzJlZGQ2MDE5MyJ9fX0="),
	SALMON("SALMON", null, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFlYjIxYTI1ZTQ2ODA2Y2U4NTM3ZmJkNjY2ODI4MWNmMTc2Y2VhZmU5NWFmOTBlOTRhNWZkODQ5MjQ4NzgifX19"),
	SHEEP("SHEEP", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19"),
	SHULKER("SHULKER", null, Nature.HOSTILE, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQzM2E0YjczMjczYTY0YzhhYjI4MzBiMGZmZjc3N2E2MWE0ODhjOTJmNjBmODNiZmIzZTQyMWY0MjhhNDQifX19"),
	SILVERFISH("SILVERFISH", null, Nature.HOSTILE, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI4ZTEwNjA5Yzc0NDlmYzdhYmM4MzJiMzBjYWE0YTBjZDlkZTZmMjU4NjA5N2M5NmMxYzhjNmU1Yzk1MDhmNyJ9fX0="),
	SKELETON("SKELETON", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNhNDQ1NzQ5MjUxYmRkODk4ZmI4M2Y2Njc4NDRlMzhhMWRmZjc5YTE1MjlmNzlhNDI0NDdhMDU5OTMxMGVhNCJ9fX0="),
	SKELETON_HORSE("SKELETON_HORSE", null, Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ=="),
	SLIME("SLIME", null, Nature.HOSTILE, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTIwZTg0ZDMyZDFlOWM5MTlkM2ZkYmI1M2YyYjM3YmEyNzRjMTIxYzU3YjI4MTBlNWE0NzJmNDBkYWNmMDA0ZiJ9fX0="),
	SNOWMAN("SNOWMAN", "SNOW GOLEM", Nature.NEUTRAL_MONSTER, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRiNzExZmY1MmVlZGRhNTljNDM0YmIwMzE2OTc2M2Q3YzQwYjViODkxMjc3NzhmZWFjZDYzYWE5NGRmYyJ9fX0="),
	SPIDER("SPIDER", null, Nature.HOSTILE, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q1NDE1NDFkYWFmZjUwODk2Y2QyNThiZGJkZDRjZjgwYzNiYTgxNjczNTcyNjA3OGJmZTM5MzkyN2U1N2YxIn19fQ=="),
	SQUID("SQUID", null, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkNjQ4NTg4Mzk3MTgxNDlkNWRhYzg5OWZmOTdiZmI1ODY1YWY3NTA1MWRlNjA1MWVkMzA5ZDQwY2E3MWNlIn19fQ=="),
	STRAY("STRAY", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM1MDk3OTE2YmMwNTY1ZDMwNjAxYzBlZWJmZWIyODcyNzdhMzRlODY3YjRlYTQzYzYzODE5ZDUzZTg5ZWRlNyJ9fX0="),
	STRIDER("STRIDER", null, Nature.PASSIVE_PEACEFUL, Classification.NETHER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I3ZmZkZGE2NTZjNjhkODg4NTFhOGUwNWI0OGNkMjQ5Mzc3M2ZmYzRhYjdkNjRlOTMwMjIyOWZlMzU3MTA1OSJ9fX0="),
	TRADER_LLAMA("TRADER_LLAMA", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, true, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQyNDc4MGIzYzVjNTM1MWNmNDlmYjViZjQxZmNiMjg5NDkxZGY2YzQzMDY4M2M4NGQ3ODQ2MTg4ZGI0Zjg0ZCJ9fX0="),
	TROPICAL_FISH("TROPICAL_FISH", null, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzlkZWQzNmEwYmQ1ZmI2N2IyNmQwMTllNmVmZTc5Yjc3OTM1YTg5ZTI3YzNjMzAzMGUzMDk1YzA4NzJkZmMifX19"),
	TURTLE("TURTLE", null, Nature.PASSIVE_PEACEFUL, Classification.UNDERWATER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGE0MDUwZTdhYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ=="),
	VEX("VEX", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlYzVhNTE2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ=="),
	VILLAGER("VILLAGER", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyZDhlNzUxYzhmMmZkNGM4OTQyYzQ0YmRiMmY1Y2E0ZDhhZThlNTc1ZWQzZWIzNGMxOGE4NmU5M2IifX19"),
	VINDICATOR("VINDICATOR", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRlYWVjMzQ0YWIwOTViNDhjZWFkNzUyN2Y3ZGVlNjFiMDYzZmY3OTFmNzZhOGZhNzY2NDJjODY3NmUyMTczIn19fQ=="),
	WANDERING_TRADER("WANDERING_TRADER", null, Nature.PASSIVE_PEACEFUL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0="),
	WITCH("WITCH", null, Nature.HOSTILE, Classification.ILLAGER, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0MTJlZWRlMjhiNDY0ZGVhNTY4M2I0OTdmNzAwODUzNWNjNjIzM2RhZDg3NDEyNmFlNDg2MWQ1NjE2MmE5NiJ9fX0="),
	WITHER("WITHER", null, Nature.HOSTILE, Classification.BOSS, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyODBjZWZlOTQ2OTExZWE5MGU4N2RlZDFiM2UxODMzMGM2M2EyM2FmNTEyOWRmY2ZlOWE4ZTE2NjU4ODA0MSJ9fX0="),
	WITHER_SKELETON("WITHER_SKELETON", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjVlYzk2NDY0NWE4ZWZhYzc2YmUyZjE2MGQ3Yzk5NTYzNjJmMzJiNjUxNzM5MGM1OWMzMDg1MDM0ZjA1MGNmZiJ9fX0="),
	WOLF("WOLF", null, Nature.NEUTRAL_ANIMAL, Classification.COMMON, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkMWQzMTEzZWM0M2FjMjk2MWRkNTlmMjgxNzVmYjQ3MTg4NzNjNmM0NDhkZmNhODcyMjMxN2Q2NyJ9fX0="),
	ZOGLIN("ZOGLIN", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJiOWJjMGYwMWRiZDc2MmEwOGQ5ZTc3YzA4MDY5ZWQ3Yzk1MzY0YWEzMGNhMTA3MjIwODU2MWI3MzBlOGQ3NSJ9fX0="),
	ZOMBIE("ZOMBIE", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ=="),
	ZOMBIE_HORSE("ZOMBIE_HORSE", null, Nature.PASSIVE_PEACEFUL, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIyOTUwZjJkM2VmZGRiMThkZTg2ZjhmNTVhYzUxOGRjZTczZjEyYTZlMGY4NjM2ZDU1MWQ4ZWI0ODBjZWVjIn19fQ=="),
	ZOMBIE_VILLAGER("ZOMBIE_VILLAGER", null, Nature.HOSTILE, Classification.UNDEAD, false, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGEyN2U0MjBmNDRmOWJiMDZjN2IzNjg0MDJiMjJhZjZkYzU3YmY5ZThjYTM0NzU4NDBmNmY3NWUyMTc4MDljMSJ9fX0="),
	;
	
	private String entityTypeName;
	private String overideName;
	private Nature nature;
	private Classification classification;
	private boolean tamable;
	private String skullID;

Mobs(String entityTypeName, String overrideName, Nature nature, Classification classification, boolean tamable, String skullID) {
	this.entityTypeName = entityTypeName;
	this.overideName = overrideName;
	this.nature = nature;
	this.classification = classification;
	this.tamable = tamable;
	this.skullID = skullID;
}

public String getEntityTypeName() {
	return entityTypeName;
}

public Nature getNature() {
	return nature;
}

public String getSkullBase64() {
	return skullID;
}
public Classification getClassification() {
	return classification;
}
public boolean isTamable() {
	return tamable;
}

public static Mobs getMob(EntityType entityType) {
	return Mobs.valueOf(entityType.name());
}

public static List<String> getMobNames() {
	List<String> mobs = new ArrayList<>();
	for (Mobs mob : Mobs.values()) {
		mobs.add(mob.entityTypeName);
	}
	return mobs;
}

public static Mobs[] getMobs(Nature nature) {
	List<Mobs> mobs = new ArrayList<>();
	for (Mobs mob : Mobs.values()) {
		if(mob.getNature() == nature)
			mobs.add(mob);
	}
	Mobs[] returnMobs = new Mobs[mobs.size()];
	return mobs.toArray(returnMobs);
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

public String getName() {
	return WordUtils.capitalizeFully(this.entityTypeName.replace("_", " "));
}

	public String getOverideName() {
		return overideName;
	}

	public enum Nature {
	PASSIVE_PEACEFUL("lang_mobs_title_passive_peaceful", "lang_mobs_desc_passive_peaceful"),
	PASSIVE_DEFENSIVE("lang_mobs_title_passive_defensive", "lang_mobs_desc_passive_defensive"),
	NEUTRAL_ANIMAL("lang_mobs_title_neutral_animals", "lang_mobs_desc_neutral_animals"),
	NEUTRAL_MONSTER("lang_mobs_title_neutral_mobs", "lang_mobs_desc_neutral_mobs"),
	HOSTILE("lang_mobs_title_hostile", "lang_mobs_desc_hostile"),
	;
	private String title;
	private String desc;
	Nature(String title, String desc) {
		this.title = RedstoneProximitySensor.getInstance().langStringColor(title);
		this.desc = RedstoneProximitySensor.getInstance().langStringColor(desc);
	}

	public String getTitle() {
		return title;
	}
	public String getDesc() {
		return desc;
	}
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

}