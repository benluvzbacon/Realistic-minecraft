package com.virtualplanet.mixin;
import com.virtualplanet.client.ClientPlanetState; import com.virtualplanet.math.*; import net.minecraft.client.MinecraftClient; import net.minecraft.client.world.ClientWorld; import org.spongepowered.asm.mixin.Mixin; import org.spongepowered.asm.mixin.injection.*; import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/** Replaces the sky angle consumed by vanilla's sky, sun/moon, fog, cloud and lightmap rendering.
 * This is client-only and never mutates shared time. Vanilla has one tilted celestial track, so azimuth is
 * calculated accurately for diagnostics/compatible render extensions while baseline rendering projects it
 * onto that track. Elevation, local day/night, twilight and longitude remain coherent and per-player. */
@Mixin(ClientWorld.class) public abstract class ClientWorldSkyMixin {
 @Inject(method="getSkyAngle(F)F",at=@At("HEAD"),cancellable=true)
 private void virtualplanet$localSky(float tickDelta,CallbackInfoReturnable<Float> cir){var mc=MinecraftClient.getInstance();var c=ClientPlanetState.config();if(!c.enabled()||mc.player==null)return;ClientWorld world=(ClientWorld)(Object)this;var p=SolarCalculator.calculate(world.getTimeOfDay(),tickDelta,mc.player.getX(),mc.player.getZ(),ClientPlanetState.geometry(),c);cir.setReturnValue(SolarCalculator.vanillaSkyAngle(p));}
}
