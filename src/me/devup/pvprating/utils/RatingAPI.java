package me.devup.pvprating.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class RatingAPI {
	
	protected static HashMap<UUID, Rating> ratings = new HashMap<UUID, Rating>();

	protected static Rating getRating(Player p) {
		return ratings.get(p.getUniqueId());
	}
	
	public static void setRating(Player p, int amount) {
		if(getRating(p) == null)
			ratings.put(p.getUniqueId(), new Rating());
			
		getRating(p).setLevel(amount);
	}
	
	public static void addRating(Player p, int amount) {
		if(getRating(p) == null)
			ratings.put(p.getUniqueId(), new Rating());
			
		getRating(p).setLevel(getRating(p).getLevel() + amount);
	}
		
	public static void removeRating(Player p, int amount) {
		if(getRating(p) == null)
			return;
			
		getRating(p).setLevel(getRating(p).getLevel() - amount);
	}
	
	public static int getRatingLevel(Player p) {
		return getRating(p).getLevel();
	}
	
}
