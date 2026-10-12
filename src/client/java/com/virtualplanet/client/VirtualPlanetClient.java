package com.virtualplanet.client;
import com.virtualplanet.config.ConfigManager; import com.virtualplanet.network.ConfigPayload; import net.fabricmc.api.ClientModInitializer; import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
public final class VirtualPlanetClient implements ClientModInitializer { public void onInitializeClient(){ClientPlayNetworking.registerGlobalReceiver(ConfigPayload.ID,(payload,context)->context.client().execute(()->ClientPlanetState.set(ConfigManager.fromJson(payload.json()),payload.centerX(),payload.centerZ(),payload.borderWidth())));} }
