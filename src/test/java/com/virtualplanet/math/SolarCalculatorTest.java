package com.virtualplanet.math;
import com.virtualplanet.config.PlanetConfig; import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class SolarCalculatorTest {
 PlanetConfig c=PlanetConfig.defaults(); PlanetGeometry g=new PlanetGeometry(1000,-500,40000,40000,1000,c.latitudeMode());
 @Test void referenceLongitudeIsZero(){assertEquals(0,g.longitude(1000),1e-12);}
 @Test void borderNormalization(){assertEquals(Math.PI,Math.abs(g.longitude(21000)),1e-12);assertEquals(Math.PI,Math.abs(g.longitude(-19000)),1e-12);}
 @Test void oppositeSides(){assertEquals(Math.PI,Math.abs(Angles.wrapPi(g.longitude(11000)-g.longitude(-9000))),1e-12);}
 @Test void localOffset(){var a=SolarCalculator.calculate(6000,0,1000,-500,g,c);var b=SolarCalculator.calculate(6000,0,11000,-500,g,c);assertEquals(.25,Math.abs(a.localDayFraction()-b.localDayFraction()),1e-9);}
 @Test void wrapMidnight(){assertEquals(.9,Angles.unit(-.1),1e-12);assertEquals(.1,Angles.unit(1.1),1e-12);}
 @Test void longitudesShiftRiseTimesInSharedTime(){var a=SolarCalculator.calculate(0,0,1000,-500,g,c);var b=SolarCalculator.calculate(0,0,11000,-500,g,c);assertNotEquals(a.hourAngle(),b.hourAngle());assertEquals(Math.PI/2,Math.abs(Angles.wrapPi(a.hourAngle()-b.hourAngle())),1e-9);}
 @Test void latitudeAndSeasonAffectElevation(){var equator=SolarCalculator.calculate(12000,0,1000,-500,g,c);var north=SolarCalculator.calculate(12000,0,1000,-10500,g,c);assertNotEquals(equator.elevation(),north.elevation());}
 @Test void nonOriginBorder(){assertEquals(0,g.latitude(-500));assertEquals(0,g.longitude(1000));}
 @Test void resizeRecalculates(){var h=PlanetGeometry.fromBorder(1000,-500,20000,c);assertEquals(20000,h.circumference());assertEquals(Math.PI,h.longitude(11000),1e-12);}
 @Test void configValidation(){var bad=new PlanetConfig(true,-2,1,99,999,Double.NaN,null,true,true,true,true,null).validated();assertEquals(0,bad.circumference());assertEquals(1000,bad.dayLengthTicks());assertEquals(45,bad.axialTiltDegrees());assertNotNull(bad.latitudeMode());}
 @Test void settingsProduceIdenticalClients(){var g2=PlanetGeometry.fromBorder(g.centerX(),g.centerZ(),g.borderWidth(),c);assertEquals(SolarCalculator.calculate(12345,0,42,88,g,c),SolarCalculator.calculate(12345,0,42,88,g2,c));}
 @Test void hugeCoordinatesStable(){var huge=new PlanetGeometry(20_000_000,-20_000_000,50_000_000,50_000_000,20_000_000,c.latitudeMode());var p=SolarCalculator.calculate(Long.MAX_VALUE/1000000,0,29_999_999,-1,huge,c);assertTrue(Double.isFinite(p.elevation()));}
 @Test void sameTickDifferentPlayers(){var a=SolarCalculator.calculate(6000,0,-9000,-500,g,c);var b=SolarCalculator.calculate(6000,0,11000,-500,g,c);assertEquals(.5,Math.abs(a.localDayFraction()-b.localDayFraction()),1e-9);assertEquals(Math.PI,Math.abs(Angles.wrapPi(a.hourAngle()-b.hourAngle())),1e-9);assertEquals(-a.elevation(),b.elevation(),1e-9);}
 @Test void polarDayAndNight(){double tilt=Math.toRadians(23.44),lat=Math.toRadians(89);assertTrue(SolarCalculator.riseSet(lat,tilt).polarDay());assertTrue(SolarCalculator.riseSet(lat,-tilt).polarNight());}
 @Test void twilightBands(){assertEquals(SolarPosition.Twilight.CIVIL,SolarCalculator.twilight(Math.toRadians(-3)));assertEquals(SolarPosition.Twilight.NIGHT,SolarCalculator.twilight(Math.toRadians(-20)));}
}
