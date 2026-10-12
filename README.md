# Virtual Planet: Local Skies

A Fabric **Minecraft Java 1.21.1** mod that keeps terrain completely flat and ordinary while treating the world-border rectangle as a mathematical planet. Players at different longitudes receive different local sun, moon, twilight, fog, cloud and lightmap phases at the same shared server tick. It never changes global time, terrain, chunks, player positions, redstone, or dimensions.

## Geometry and astronomy

The authoritative server reads the Overworld border center and current width. Unless overridden, width is circumference and `R=C/(2π)`. Longitude is `2π(x-referenceX)/C`, wrapped at ±180°. The default equirectangular-clamped mapping sends border-center Z to the equator and north/south edges to ±90°. This deliberately preserves east-west solar-time differences. A rectangle cannot cover a sphere without distortion: scale grows toward the poles and each pair of border corners mathematically converges at a pole. `MERCATOR` is an alternative which avoids reaching the poles.

The model combines shared time, longitude, latitude, a 365.2422-local-day seasonal cycle, axial tilt/declination and spherical hour-angle equations. It calculates elevation, azimuth, sunrise/sunset, polar day/night, and civil (-6°), nautical (-12°), and astronomical (-18°) twilight. Border snapshots are synchronized only on join/change/reload; clients calculate locally.

Vanilla's `ClientWorld.getSkyAngle` is mixed in, so the actual vanilla renderer, moon, sky brightness, fog/cloud phases and lightmap consumers use a player-local phase. This is not a screen overlay. Minecraft's baseline renderer constrains bodies to one celestial track: full physical azimuth is calculated and shown by debug, but baseline visuals project it onto that track.

## Install

Install Java 21, Fabric Loader 0.16.5+ and Fabric API **0.116.5+1.21.1** on server and every client. Put the release JAR and Fabric API in `mods/`. The mod is required on both sides. Set a border normally (`/worldborder center`, `/worldborder set`) and reconnect or wait one second for synchronization.

On first launch `config/virtualplanet.json` is created. Important options:

* `enabled`; `circumference` (`0` follows border width); `dayLengthTicks` (default 24000)
* `axialTiltDegrees`, `seasons`, `referenceLongitudeDegrees`, `referenceX` (`NaN`/omitted uses border center)
* `latitudeMode`: `EQUIRECTANGULAR_CLAMPED` or `MERCATOR`
* `localMoon`, `atmosphere`, `debug`, `solarTimeMode`

JSON does not standardize NaN; the generated file may encode it as `NaN` through Gson. To select border center, leave `referenceX` as generated or remove it. Invalid/risky values are clamped on reload.

## Commands

* `/virtualplanet info` – dimensions and settings
* `/virtualplanet debug` – your longitude, latitude, local time, elevation, azimuth, twilight and rise/set
* `/virtualplanet recalculate` – operator: resend current border geometry
* `/virtualplanet reload` – operator: reload and synchronize configuration

## Build and test

```bash
./gradlew test build
```

The playable remapped JAR is under `build/libs/` (not the `-sources` JAR). GitHub Actions runs tests and compilation on pushes, PRs and manual dispatches, then exposes **virtual-planet-local-skies** under the run's Artifacts section.

## Compatibility and honest limitations

* Vanilla rendering is the baseline. Sodium generally retains world sky-angle consumers, but this cannot guarantee compatibility with every version.
* Iris/shader packs that replace celestial/sky/lightmap calculations can ignore the mixin. Disable the pack or use a pack exposing vanilla sky inputs. The mod cannot reposition shader-owned bodies.
* Block/skylight propagation and mob spawning are server-global and remain tied to shared time. Local effects are client atmospheric rendering, not separate per-player server lighting or physical shadows.
* Weather remains globally authoritative. Local brightness influences vanilla rendering, but precipitation logic does not become a separate climate.
* `localMoon` and `atmosphere` are synchronization/compatibility controls reserved for renderer integrations; vanilla sun/moon are inherently opposite and use the local sky angle together.

## Troubleshooting

A server/client registry mismatch means the mod or Fabric API is missing on one side. If sky does not move locally, verify `enabled`, run debug at separated X positions, then disable sky-replacing shaders. Very small borders are clamped to one block for numerical safety. Report crashes with the game log, exact Loader/API/Sodium/Iris versions, and shader name.

Licensed under MIT.
