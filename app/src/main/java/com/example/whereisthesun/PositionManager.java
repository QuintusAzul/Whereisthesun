package com.example.whereisthesun;

public class PositionManager {

    private double longitude;
    private double latitude;
    private double time;
    private double timezone;
    private double date;
    public double julianCentury;
    private int timeZone;
    public double zenith;
    public double elevation;
    public double azimuth;

    // jc stands for Julian Century
    public PositionManager(double longitude, double latitude, double date, double time, int timeZone) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.timeZone = timeZone;
        this.date = date;
        julianCentury = ((date + 2415018.5 + time - time/24) - 2451545)/36525;
        zenith = calculateZenith(julianCentury);
        elevation = calculateElevation(zenith);
        azimuth = calculateAzimuth();
    }

    public PositionManager(double longitude, double latitude) {
        this(longitude,latitude,40350,0.004166667,0);
    }

    public double calculateElevation(double zenith) {
        return 90 - zenith;
    }

    public double calculateAzimuth() {
        double hourAngle = calculateHourAngle(julianCentury);
        if (hourAngle > 0) {
            return (calculateAzimuthFast() + 180) % 360;
        } else {
            return (540 - calculateAzimuthFast()) % 360;
        }
    }

    public double calculateAzimuthFast() {
        double T2 = calculateSunDecline(julianCentury);
        return Math.toDegrees(Math.acos(((Math.sin(Math.toRadians(latitude)) * Math.cos(Math.toRadians(zenith)))
                - Math.sin(Math.toRadians(T2))) / (Math.cos(Math.toRadians(latitude))
                * Math.sin(Math.toRadians(zenith)))));
    }

    //c
    public double calculateZenith(double jc) {
        double T2 = calculateSunDecline(jc);
        double AC2 = calculateHourAngle(jc);
        return Math.toDegrees(Math.acos(Math.sin(Math.toRadians(latitude))*Math.sin(Math.toRadians(T2))
                +Math.cos(Math.toRadians(latitude))*Math.cos(Math.toRadians(T2))*Math.cos(Math.toRadians(AC2))));
    }

    //c
    public double calculateSunDecline(double jc) {
        return Math.toDegrees(Math.asin(Math.sin(Math.toRadians(calculateObliqCorr(jc))) *
                Math.sin(Math.toRadians(calculateSunAppLong(jc)))));
    }

    //c
    public double calculateObliqCorr(double jc) {
        return calculateMeanObliqEcliptic(jc) + 0.00256 * Math.cos(Math.toRadians(125.04-1934.136 * jc));
    }

    //c
    public double calculateMeanObliqEcliptic(double jc) {
        return 23 + (26 + ((21.448 - jc * (46.815 + jc * (0.00059 - jc * 0.001813))))/60)/60;
    }

    //c
    public double calculateSunAppLong(double jc) {
        return calculateSunTrueLong(jc) - 0.00569 - 0.00478 *
                Math.sin(Math.toRadians(125.04 - 1934.136 * jc));
    }

    //c
    public double calculateSunTrueLong(double jc) {
        return calculateGeomMeanLongSun(jc) + calculateSunEqOfCtr(jc);
    }

    //c
    public double calculateGeomMeanLongSun(double jc) {
        return (280.46646 + jc * (36000.76983 + jc * 0.000302)) % 360;
    }

    //c
    public double calculateSunEqOfCtr(double jc) {
        double geomMeanAnom = calculateGeomMeanAnomSun(jc);
        return (Math.sin(Math.toRadians(geomMeanAnom)) * (1.914602 - jc * (0.004817 + 0.000014 * jc))) +
                (Math.sin(Math.toRadians(2 * geomMeanAnom)) * (0.019993 - 0.000101 * jc)) +
                (Math.sin(Math.toRadians(2 * geomMeanAnom)) * 0.000289);
    }

    //c
    public double calculateGeomMeanAnomSun(double jc) {
        return 357.52911 + jc * (35999.05029 - 0.0001537 * jc);
    }

    //c
    public double calculateHourAngle(double jc) {
        double trueSolarTime = calculateTrueSolarTime(jc);
        if (trueSolarTime / 4 < 0) {
            return trueSolarTime / 4 + 180;
        } else {
            return trueSolarTime / 4 - 180;
        }
    }

    //c
    public double calculateTrueSolarTime(double jc) {
        return (time * 1440 + calculateEqOfTime(jc) + 4 * longitude - 60 * timeZone) % 1440;
    }

    //c
    public double calculateEqOfTime(double jc) {
        double K2 = calculateEccentEarthOrbit(jc);
        double U2 = calculateVarY(jc);
        double J2 = calculateGeomMeanAnomSun(jc);
        double I2 = calculateGeomMeanLongSun(jc);
        return 4 * (Math.toDegrees(U2 * Math.sin(2 * Math.toRadians(I2)) - 2 * K2 * Math.sin(Math.toRadians(J2))
                + 4 * K2 * U2 * Math.sin(Math.toRadians(J2)) * Math.cos(2 * Math.toRadians(I2))
                - 0.5 * U2 * U2 * Math.sin(4 * Math.toRadians(I2)) - 1.25 * K2 * K2 * Math.sin(2 * Math.toRadians(J2))));
    }

    //c
    public double calculateVarY(double jc) {
        double obliq = calculateObliqCorr(jc);
        return Math.tan(Math.toRadians(obliq/2)) * Math.tan(Math.toRadians(obliq / 2));
    }

    //c
    public double calculateEccentEarthOrbit(double jc) {
        return 0.016708634 - jc * (0.000042037 + 0.0000001267 * jc);
    }

}
