package me.zimzaza4.betterbedrockthirdperson.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
    public static Location getOffsetLocation(Location origin, double x, double y, double z) {
        Location arrival = origin.clone();
        double[] res = new double[3];

        res[0] = x;
        res[1] = y;
        res[2] = z;

        Vector dirX = new Location(arrival.getWorld(), 0, 0, 0, Location.normalizeYaw(arrival.getYaw()-90),
                arrival.getPitch()).getDirection().normalize();
        Vector dirY = new Location(arrival.getWorld(), 0, 0, 0, arrival.getYaw(),
                arrival.getPitch()-90).getDirection().normalize();
        Vector dirZ = arrival.getDirection().normalize();

        arrival = arrival.add(dirX.multiply(res[0])).add(dirY.multiply(res[1])).add(dirZ.multiply(res[2]));


        return arrival;
    }

    public static List<Location> line(Location locAO, Location locBO, double rate) {
        Location locA = locAO.clone();
        Location locB = locBO.clone();
        rate = Math.abs(rate);
        Vector vectorAB = locB.clone().subtract(locA).toVector();
        double vectorLength = vectorAB.length();
        vectorAB.normalize();
        List<Location> points = new ArrayList<>();
        for (double i = 0; i <= vectorLength; i += rate) {
            Vector vector = vectorAB.clone().multiply(i);
            locA.add(vector);
            points.add(locA.clone());
            locA.subtract(vector);
        }

        return points;
    }
}
