package com.virtualplanet.config;

/** Server-authoritative settings. Circumference <= 0 means use current border width. */
public record PlanetConfig(boolean enabled, double circumference, long dayLengthTicks, double axialTiltDegrees,
 double referenceLongitudeDegrees, double referenceX, LatitudeMode latitudeMode, boolean seasons,
 boolean localMoon, boolean atmosphere, boolean debug, SolarTimeMode solarTimeMode) {
 public enum LatitudeMode { EQUIRECTANGULAR_CLAMPED, MERCATOR }
 public enum SolarTimeMode { APPARENT_SOLAR }
 public static PlanetConfig defaults() { return new PlanetConfig(true,0,24000,23.44,0,Double.NaN,LatitudeMode.EQUIRECTANGULAR_CLAMPED,true,true,true,false,SolarTimeMode.APPARENT_SOLAR); }
 public PlanetConfig validated() {
  return new PlanetConfig(enabled, finiteNonnegative(circumference,0), clamp(dayLengthTicks,1000,2_400_000), clampFinite(axialTiltDegrees,-45,45,23.44),
   clampFinite(referenceLongitudeDegrees,-180,180,0), Double.isFinite(referenceX)?referenceX:Double.NaN, latitudeMode==null?LatitudeMode.EQUIRECTANGULAR_CLAMPED:latitudeMode,
   seasons,localMoon,atmosphere,debug,solarTimeMode==null?SolarTimeMode.APPARENT_SOLAR:solarTimeMode);
 }
 private static double finiteNonnegative(double v,double d){ return Double.isFinite(v)&&v>=0?v:d; }
 private static double clampFinite(double v,double min,double max,double d){return Double.isFinite(v)?Math.max(min,Math.min(max,v)):d;}
 private static long clamp(long v,long min,long max){return Math.max(min,Math.min(max,v));}
}
