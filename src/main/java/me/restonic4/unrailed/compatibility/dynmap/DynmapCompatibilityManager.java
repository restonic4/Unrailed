package me.restonic4.unrailed.compatibility.dynmap;

import me.restonic4.unrailed.entity.MinecartTrain;
import me.restonic4.unrailed.util.FabricUtil;
import net.fabricmc.loader.api.FabricLoader;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.util.HashMap;
import java.util.Map;

public class DynmapCompatibilityManager {
    private static DynmapCommonAPI dynmapAPI;
    private static MarkerSet markerSet;

    private static final Map<MinecartTrain, Marker> trainMarkers = new HashMap<>();

    public static void register() {
        DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {
            @Override
            public void apiEnabled(DynmapCommonAPI api) {
                dynmapAPI = api;
                initializeDynmapIntegration(api);
            }

            @Override
            public void apiDisabled(DynmapCommonAPI api) {
                dynmapAPI = null;
            }
        });
    }

    private static void initializeDynmapIntegration(DynmapCommonAPI api) {
        MarkerAPI markerAPI = api.getMarkerAPI();
        MarkerSet markerSet = markerAPI.createMarkerSet("unrailed_minecarts", "Trains", null, false);

    }

    public static boolean isDynmapEnabled() {
        return dynmapAPI != null;
    }

    public static MarkerSet getMarkerSet() {
        return markerSet;
    }

    public static MarkerIcon getMarkerIcon() {
        return dynmapAPI.getMarkerAPI().getMarkerIcon("train_icon");
    }

    public static void createMarker(MinecartTrain minecartTrain) {
        if (DynmapCompatibilityManager.isDynmapEnabled()) {
            MarkerSet markerSet = DynmapCompatibilityManager.getMarkerSet();
            if (markerSet != null) {
                Marker marker = markerSet.createMarker(
                        "train_minecart_" + minecartTrain.getId(), // id
                        "Train", // name
                        minecartTrain.level().dimension().location().toString(), // dimension
                        minecartTrain.getX(), minecartTrain.getY(), minecartTrain.getZ(), // start position
                        DynmapCompatibilityManager.getMarkerIcon(), // icon
                        true // visible
                );

                trainMarkers.put(minecartTrain, marker);
            }
        }
    }

    public static void removeMarker(MinecartTrain minecartTrain) {
        Marker marker = trainMarkers.remove(minecartTrain);
        if (marker != null) {
            marker.deleteMarker();
        }
    }

    public static void updateMarker(MinecartTrain minecartTrain) {
        Marker marker = trainMarkers.get(minecartTrain);
        if (marker != null) {
            marker.setLocation(
                    minecartTrain.level().dimension().location().toString(), // Dimensión como String
                    minecartTrain.getX(),
                    minecartTrain.getY(),
                    minecartTrain.getZ()
            );
        }
    }
}
