package com.virtualplanet;
import com.virtualplanet.command.PlanetCommands; import com.virtualplanet.config.ConfigManager; import com.virtualplanet.network.ConfigPayload;
import net.fabricmc.api.ModInitializer; import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents; import net.fabricmc.fabric.api.networking.v1.*; import net.minecraft.server.MinecraftServer; import net.minecraft.server.network.ServerPlayerEntity; import net.minecraft.world.border.WorldBorder;
public final class VirtualPlanetMod implements ModInitializer {
 public static final String MOD_ID="virtualplanet"; private static double cx=Double.NaN,cz=Double.NaN,size=Double.NaN; private static int poll;
 @Override public void onInitialize(){ConfigManager.load();PayloadTypeRegistry.playS2C().register(ConfigPayload.ID,ConfigPayload.CODEC);PlanetCommands.register();ServerPlayConnectionEvents.JOIN.register((handler,sender,server)->send(handler.player));ServerTickEvents.END_SERVER_TICK.register(server->{if(++poll>=20){poll=0;checkBorder(server);}});}
 public static void checkBorder(MinecraftServer server){WorldBorder b=server.getOverworld().getWorldBorder();if(b.getCenterX()!=cx||b.getCenterZ()!=cz||b.getSize()!=size){cx=b.getCenterX();cz=b.getCenterZ();size=b.getSize();broadcast(server);}}
 public static void send(ServerPlayerEntity p){WorldBorder b=p.getServer().getOverworld().getWorldBorder();ServerPlayNetworking.send(p,new ConfigPayload(ConfigManager.json(),b.getCenterX(),b.getCenterZ(),b.getSize()));}
 public static void broadcast(MinecraftServer s){for(ServerPlayerEntity p:s.getPlayerManager().getPlayerList())send(p);}
}
