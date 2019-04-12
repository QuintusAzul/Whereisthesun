package com.example.whereisthesun;

public class PositionTestClient {

    public static void main(String[] args) {
        PositionManager positionManager = new PositionManager(-105,40,40350,0.008333333
, 0);
        System.out.println(positionManager.zenith);
        System.out.println(positionManager.elevation);
        System.out.println(positionManager.azimuth);
        System.out.println(positionManager.julianCentury);
        System.out.println(positionManager.calculateHourAngle(positionManager.julianCentury));
    }
}
