package com.virtualplanet.math;
public record SolarPosition(double longitude,double latitude,double declination,double hourAngle,double elevation,double azimuth,double localDayFraction,Twilight twilight, double sunriseFraction,double sunsetFraction, boolean polarDay,boolean polarNight) {
 public enum Twilight { DAY,CIVIL,NAUTICAL,ASTRONOMICAL,NIGHT }
}
