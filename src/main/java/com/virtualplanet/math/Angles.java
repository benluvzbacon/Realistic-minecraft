package com.virtualplanet.math;
public final class Angles { private Angles(){} public static double wrapPi(double v){double r=v%(2*Math.PI); return r<=-Math.PI?r+2*Math.PI:r>Math.PI?r-2*Math.PI:r;} public static double unit(double v){double r=v%1; return r<0?r+1:r;} }
