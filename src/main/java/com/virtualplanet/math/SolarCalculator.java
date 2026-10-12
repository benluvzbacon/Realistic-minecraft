package com.virtualplanet.math;
import com.virtualplanet.config.PlanetConfig;
public final class SolarCalculator {
 private SolarCalculator(){}
 /** Shared time defines rotation and year phase; longitude shifts local noon. Azimuth is clockwise from north. */
 public static SolarPosition calculate(long time,double tickDelta,double x,double z,PlanetGeometry g,PlanetConfig c){
  double days=(time+tickDelta)/c.dayLengthTicks(); double lon=g.longitude(x)+Math.toRadians(c.referenceLongitudeDegrees()); double lat=g.latitude(z);
  double decl=c.seasons()?Math.toRadians(c.axialTiltDegrees())*Math.sin(2*Math.PI*(days/365.2422-0.218)):0;
  double local=Angles.unit(days+lon/(2*Math.PI)); double hour=Angles.wrapPi(2*Math.PI*(local-0.5));
  double sinEl=Math.sin(lat)*Math.sin(decl)+Math.cos(lat)*Math.cos(decl)*Math.cos(hour); double el=Math.asin(clamp(sinEl));
  double az=Math.atan2(Math.sin(hour),Math.cos(hour)*Math.sin(lat)-Math.tan(decl)*Math.cos(lat))+Math.PI;
  RiseSet rs=riseSet(lat,decl);
  return new SolarPosition(lon,lat,decl,hour,el,Angles.wrapPi(az),local,twilight(el),rs.rise,rs.set,rs.polarDay,rs.polarNight);
 }
 public static RiseSet riseSet(double lat,double decl){double q=-Math.tan(lat)*Math.tan(decl); if(q<=-1)return new RiseSet(Double.NaN,Double.NaN,true,false);if(q>=1)return new RiseSet(Double.NaN,Double.NaN,false,true);double h=Math.acos(q)/(2*Math.PI);return new RiseSet(.5-h,.5+h,false,false);}
 public static SolarPosition.Twilight twilight(double e){double d=Math.toDegrees(e);return d>=0?SolarPosition.Twilight.DAY:d>=-6?SolarPosition.Twilight.CIVIL:d>=-12?SolarPosition.Twilight.NAUTICAL:d>=-18?SolarPosition.Twilight.ASTRONOMICAL:SolarPosition.Twilight.NIGHT;}
 /** Projects physical elevation onto vanilla's single celestial circle. The hour-angle sign preserves
  * east/west motion; using elevation (rather than merely offsetting ticks) gives latitude/season-correct
  * day length, rise/set and polar conditions despite vanilla's fixed visual track. */
 public static float vanillaSkyAngle(SolarPosition p){
  double phase=Math.acos(Math.max(-1,Math.min(1,Math.sin(p.elevation()))))/(2*Math.PI);
  return (float)Angles.unit(p.hourAngle()<0 ? 1-phase : phase);
 }
 private static double clamp(double v){return Math.max(-1,Math.min(1,v));}
 public record RiseSet(double rise,double set,boolean polarDay,boolean polarNight){}
}
