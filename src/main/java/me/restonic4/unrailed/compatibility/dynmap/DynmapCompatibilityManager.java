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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DynmapCompatibilityManager {
    private static DynmapCommonAPI dynmapAPI;
    private static MarkerSet markerSet;

    private static boolean debugMode = true;

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
        markerSet = markerAPI.createMarkerSet("unrailed_minecarts", "Trains", null, false);

        log("Dynmap started");
    }

    public static boolean isDynmapEnabled() {
        return dynmapAPI != null;
    }

    public static MarkerIcon getMarkerIcon() {
        MarkerAPI markerAPI = dynmapAPI.getMarkerAPI();
        MarkerIcon customIcon = markerAPI.getMarkerIcon("train");

        if (customIcon == null) {
            log("Creating train icon");

            try (InputStream iconStream = DynmapCompatibilityManager.class.getResourceAsStream("/assets/unrailed/textures/icons/train.png")) {
                if (iconStream != null) {
                    customIcon = markerAPI.createMarkerIcon("train", "Train", iconStream);
                } else {
                    System.err.println("Failed to load icon file for train");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return customIcon;
    }

    public static void createMarker(MinecartTrain minecartTrain) {
        log("Creating train marker");

        if (DynmapCompatibilityManager.isDynmapEnabled()) {
            log("Dynmap is enabled");

            if (markerSet != null) {
                Marker marker = markerSet.createMarker(
                        "train_minecart_" + minecartTrain.getId(), // id
                        "Train", // name
                        getFixedWorldName(minecartTrain.level().dimension().location().toString()), // dimension
                        minecartTrain.getX(), minecartTrain.getY(), minecartTrain.getZ(), // start position
                        DynmapCompatibilityManager.getMarkerIcon(), // icon
                        false // persistent
                );

                trainMarkers.put(minecartTrain, marker);

                log("Marker created");
            }

            printDefaultMarkerDetails();
        }
    }

    public static void removeMarker(MinecartTrain minecartTrain) {
        Marker marker = trainMarkers.remove(minecartTrain);
        if (marker != null) {
            marker.deleteMarker();

            log("Marker removed");
        }
    }

    public static void updateMarker(MinecartTrain minecartTrain) {
        Marker marker = trainMarkers.get(minecartTrain);
        if (marker != null) {
            marker.setLocation(
                    getFixedWorldName(minecartTrain.level().dimension().location().toString()), // Dimensión como String
                    minecartTrain.getX(),
                    minecartTrain.getY(),
                    minecartTrain.getZ()
            );
        }
    }

    public static String getFixedWorldName(String correctName) {
        if (Objects.equals(correctName, "minecraft:overworld")) {
            return "world";
        } else if (Objects.equals(correctName, "minecraft:the_nether")) {
            return "DIM-1";
        } else if (Objects.equals(correctName, "minecraft:the_end")) {
            return "DIM1";
        }

        return correctName;
    }

    public static void printDefaultMarkerDetails() {
        if (isDynmapEnabled()) {
            MarkerAPI markerAPI = dynmapAPI.getMarkerAPI();

            markerSet.getMarkers().forEach(marker -> {
                String markerId = marker.getMarkerID();
                String markerLabel = marker.getLabel();
                String markerWorld = marker.getWorld();

                log("Marker ID: " + markerId);
                log("Marker Label: " + markerLabel);
                log("Marker World: " + markerWorld);
            });

            markerAPI.getMarkerSet("markers").getMarkers().forEach(marker -> {
                String markerId = marker.getMarkerID();
                String markerLabel = marker.getLabel();
                String markerWorld = marker.getWorld();

                log("Marker ID: " + markerId);
                log("Marker Label: " + markerLabel);
                log("Marker World: " + markerWorld);
            });
        } else {
            System.out.println("Dynmap is not enabled.");
        }
    }


    private static void log(String message) {
        if (debugMode) {
            System.out.println("[Unrailed | Dynmap debug info]: " + message);
        }
    }
}
