package me.zimzaza4.betterbedrockthirdperson.listener;

import me.zimzaza4.betterbedrockthirdperson.BetterBedrockThirdPerson;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BetterBedrockThirdPerson.ENABLED_PLAYERS.remove(event.getPlayer());
    }
}
