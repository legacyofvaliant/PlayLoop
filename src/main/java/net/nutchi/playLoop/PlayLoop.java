package net.nutchi.playLoop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class PlayLoop extends JavaPlugin {
    private final Map<UUID, String> playerSoundMap = new HashMap<>();

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("playloop")) {
            if (args.length == 5) {
                String playerName = args[0];
                String sound = args[1];

                try {
                    float volume = Float.parseFloat(args[2]);
                    float pitch = Float.parseFloat(args[3]);
                    long duration = Long.parseLong(args[4]);

                    Player player = getServer().getPlayer(playerName);
                    if (player != null) {
                        playLoop(player, sound, volume, pitch, duration);
                        return true;
                    } else {
                        sender.sendMessage("Player not found.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid number format.");
                    return false;
                }
            } else {
                return false;
            }
        } else if (command.getName().equals("stoploop")) {
            if (args.length == 1) {
                Player player = getServer().getPlayer(args[0]);
                if (player != null) {
                    stopLoop(player);
                    return true;
                } else {
                    sender.sendMessage("Player not found.");
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void playLoop(Player player, String sound, float volume, float pitch, long duration) {
        stopLoop(player);

        UUID playerId = player.getUniqueId();
        playerSoundMap.put(playerId, sound);
        player.playSound(player.getLocation(), sound, volume, pitch);
        getServer().getScheduler().runTaskLater(this, () -> {
            if (playerSoundMap.containsKey(playerId)) {
                playLoop(player, sound, volume, pitch, duration);
            }
        }, duration);
    }

    private void stopLoop(Player player) {
        UUID playerId = player.getUniqueId();
        if (playerSoundMap.containsKey(playerId)) {
            player.stopSound(playerSoundMap.get(playerId));
            playerSoundMap.remove(playerId);
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        } else {
            return Collections.emptyList();
        }
    }
}
