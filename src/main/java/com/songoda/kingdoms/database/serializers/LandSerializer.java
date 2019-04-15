package com.songoda.kingdoms.database.serializers;

import java.lang.reflect.Type;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.songoda.kingdoms.database.Serializer;
import com.songoda.kingdoms.objects.land.Land;
import com.songoda.kingdoms.objects.structures.Structure;
import com.songoda.kingdoms.objects.turrets.Turret;

public class LandSerializer implements Serializer<Land> {

	private final StructureSerializer structureSerializer;
	private final TurretSerializer turretSerializer;

	public LandSerializer() {
		this.structureSerializer = new StructureSerializer();
		this.turretSerializer = new TurretSerializer();
	}

	@Override
	public JsonElement serialize(Land land, Type type, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.add("structure", structureSerializer.serialize(land.getStructure(), Structure.class, context));
		json.addProperty("world", land.getWorld().getName());
		json.addProperty("claim-time", land.getClaimTime());
		json.addProperty("x", land.getX());
		json.addProperty("z", land.getZ());
		JsonArray turrets = new JsonArray();
		land.getTurrets().forEach(turret -> turrets.add(turretSerializer.serialize(turret, Turret.class, context)));
		json.add("turrets", turrets);
		return json;
	}

	@Override
	public Land deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		JsonElement worldElement = object.get("world");
		if (worldElement == null || worldElement.isJsonNull())
			return null;
		World world = Bukkit.getWorld(worldElement.getAsString());
		if (world == null)
			return null;
		int x = object.get("x").getAsInt();
		int z = object.get("z").getAsInt();
		Chunk chunk = world.getChunkAt(x, z);
		Land land = new Land(chunk);
		JsonElement claimElement = object.get("claim-time");
		if (claimElement != null && !claimElement.isJsonNull())
			land.setClaimTime(claimElement.getAsLong());
		JsonElement structureElement = object.get("structure");
		if (structureElement != null && !structureElement.isJsonNull()) {
			land.setStructure(structureSerializer.deserialize(structureElement, Structure.class, context));
		}
		JsonElement turretElement = object.get("turrets");
		if (turretElement != null && !turretElement.isJsonNull() && turretElement.isJsonArray()) {
			JsonArray array = turretElement.getAsJsonArray();
			array.forEach(element -> {
				Turret turret = turretSerializer.deserialize(element, Turret.class, context);
				if (turret != null)
					land.addTurret(turret);
			});
		}
		return land;
	}

}
