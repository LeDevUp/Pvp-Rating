package me.devup.pvprating;

import java.util.HashMap;
import java.util.UUID;

import me.devup.pvprating.utils.PlayerConfig;
import me.devup.pvprating.utils.RatingAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	protected HashMap<UUID, PlayerConfig> playerConfigs = new HashMap<UUID, PlayerConfig>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			PlayerConfig pc = new PlayerConfig(p, this);
			
			playerConfigs.put(p.getUniqueId(), pc);
			
			if(pc.getConfig().contains("Rating")) {
				RatingAPI.setRating(p.getPlayer(), pc.getConfig().getInt("Rating"));
			} else {
				RatingAPI.setRating(p.getPlayer(), 0);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			PlayerConfig pc = playerConfigs.get(p.getUniqueId());
			
			if(pc.equals(null))
				return;
			
			pc.getConfig().set("Rating", RatingAPI.getRatingLevel(p));
			
			pc.save();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		PlayerConfig pc = new PlayerConfig(e.getPlayer(), this);
		
		playerConfigs.put(e.getPlayer().getUniqueId(), pc);
		
		if(pc.getConfig().contains("Rating")) {
			RatingAPI.setRating(e.getPlayer(), pc.getConfig().getInt("Rating"));
		} else {
			RatingAPI.setRating(e.getPlayer(), 0);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		PlayerConfig pc = playerConfigs.get(e.getPlayer().getUniqueId());
		
		pc.getConfig().set("Rating", RatingAPI.getRatingLevel(e.getPlayer()));
		
		pc.save();
		
		playerConfigs.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		
		String format = event.getFormat(), toReplace = ChatColor.translateAlternateColorCodes('&', "&8[&b+" + RatingAPI.getRatingLevel(p) + "&8]");
		
		if(format.contains(toReplace))
			format = format.replace("[rating]", toReplace);
		else
			format = toReplace + ChatColor.RESET + format;
		
		event.setFormat(format);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if(!(e.getEntity() instanceof Player))
			return;
		
		if(!(e.getEntity().getKiller() instanceof Player))
			return;
		
		Player killer = e.getEntity().getKiller();
		
		RatingAPI.setRating(killer, RatingAPI.getRatingLevel(killer) + 1);
	}

}
