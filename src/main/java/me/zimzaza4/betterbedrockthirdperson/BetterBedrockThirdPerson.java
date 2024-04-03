package me.zimzaza4.betterbedrockthirdperson;

import me.zimzaza4.betterbedrockthirdperson.listener.EventListener;
import me.zimzaza4.betterbedrockthirdperson.util.LocationUtils;
import me.zimzaza4.geyserutils.common.camera.data.CameraPreset;
import me.zimzaza4.geyserutils.common.camera.data.Ease;
import me.zimzaza4.geyserutils.common.camera.data.EaseType;
import me.zimzaza4.geyserutils.common.camera.data.Rot;
import me.zimzaza4.geyserutils.common.camera.instruction.ClearInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.SetInstruction;
import me.zimzaza4.geyserutils.common.util.Pos;
import me.zimzaza4.geyserutils.spigot.api.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BetterBedrockThirdPerson extends JavaPlugin {
    public static Set<Player> ENABLED_PLAYERS = new HashSet<>();
    private static BetterBedrockThirdPerson instance;
    @Override
    public void onEnable() {

        instance = this;
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        getCommand("tr").setExecutor((CommandExecutor) (commandSender, command, s, strings) -> {
            if (commandSender instanceof Player) {
                switchThirdPerson((Player) commandSender);
            }
            return true;
        });
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : ENABLED_PLAYERS) {
                    if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {


                        Location loc;
                        @NotNull Location target;


                        loc = LocationUtils.getOffsetLocation(player.getEyeLocation(), (player.isSneaking() ? -1.5 : -0.7), (player.getEyeLocation().getPitch() > 80 ? -0.1 : 0.7), (player.isSneaking() ? -0.5 : -2));

                        target = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(40));


                        List<Location> line = LocationUtils.line(player.getEyeLocation(), target, 0.5);

                            /*
                            if (!line.isEmpty()) {
                                target = line.get(line.size() - 1);

                                int d = 0;
                                for (Location location : line) {
                                    d++;
                                    if (!location.getBlock().isEmpty()) {
                                        if (d < 9) {
                                            target = location;
                                        }
                                        break;
                                    }
                                }

                            } else {
                                target = player.getEyeLocation();
                            }

                             */

                            /*
                            @Nullable RayTraceResult result = player.rayTraceEntities(line.size(), false);

                            if (result != null && result.getHitEntity() != null) {
                                target = result.getHitEntity().getLocation().add(0, result.getHitEntity().getHeight() * 0.6, 0);
                            }

                             */

                        List<Location> cameraLine = LocationUtils.line(loc, target, 0.1);


                        if (!line.isEmpty()) {
                            int i = 0;
                            for (Location location : cameraLine.subList(0, Math.min(cameraLine.size() - 1, 20))) {
                                if (location.getBlock().isEmpty()) {
                                    loc = cameraLine.get(i);
                                    break;
                                }
                                i++;
                            }
                        }


                        //target.setDirection(player.getEyeLocation().getDirection());
                        //target = LocationUtils.getOffsetLocation(target, 0,0,0);
                        Pos pos = new Pos((float) target.getX(), (float) target.getY(), (float) target.getZ());


                        PlayerUtils.sendCameraInstruction(player, SetInstruction.builder()
                                .ease(new Ease(0.1F, EaseType.LINEAR))
                                .preset(CameraPreset.FREE)
                                .pos(new Pos((float) loc.getX(), (float) loc.getY(), (float) loc.getZ()))
                                .facing(pos)
                                .rot(new Rot(0, 0))
                                .build()
                        );

                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 1, 1);
    }

    public static void switchThirdPerson(Player player) {
        if (ENABLED_PLAYERS.contains(player)) {
            ENABLED_PLAYERS.remove(player);
            new BukkitRunnable() {
                @Override
                public void run() {

                    PlayerUtils.sendCameraInstruction(player, ClearInstruction.instance());
                }
            }.runTaskLaterAsynchronously(instance, 2);
            return;
        }
        ENABLED_PLAYERS.add(player);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
