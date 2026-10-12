package com.virtualplanet.math;

import com.virtualplanet.config.PlanetConfig;

/** Immutable geometry snapshot. A square border is treated as an equirectangular map: X wraps longitude;
 * Z maps linearly to latitude and is clamped at +/-90 degrees. This preserves east/west solar time but
 * necessarily distorts area near poles; corners collapse mathematically toward the poles. */
public record PlanetGeometry(double centerX,double centerZ,double borderWidth,double circumference,double referenceX, PlanetConfig.LatitudeMode latitudeMode) {
 public static final double MIN_SIZE=1.0;
 public PlanetGeometry { borderWidth=safe(borderWidth); circumference=safe(circumference); referenceX=Double.isFinite(referenceX)?referenceX:centerX; }
 private static double safe(double n){return Double.isFinite(n)&&n>=MIN_SIZE?n:MIN_SIZE;}
 public double radius(){return circumference/(2*Math.PI);}
 public double longitude(double x){return Angles.wrapPi(2*Math.PI*(x-referenceX)/circumference);}
 public double latitude(double z){
  double normalized=Math.max(-1,Math.min(1,(z-centerZ)/(borderWidth/2)));
  if(latitudeMode==PlanetConfig.LatitudeMode.MERCATOR) return Math.atan(Math.sinh(normalized*Math.PI));
  return normalized*Math.PI/2;
 }
 public static PlanetGeometry fromBorder(double cx,double cz,double width,PlanetConfig c){return new PlanetGeometry(cx,cz,width,c.circumference()>0?c.circumference():width,Double.isFinite(c.referenceX())?c.referenceX():cx,c.latitudeMode());}
}
