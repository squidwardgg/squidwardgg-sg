package me.squidward.sg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {

	// onEnable: this prints in the server console when the plugin is loaded.

	@Override
	public void onEnable() {
		Bukkit.getServer().getLogger().info("Survival Games enabled!");
	}

	// onDisable: this prints in the server console when the plugin is disabled.

	@Override
	public void onDisable() {
		Bukkit.getServer().getLogger().info("Survival Games disabled!");
	}

	// Event Stuff

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();
		if (Bukkit.getServer().getOnlinePlayers().size() => 16) {
			player.kickPlayer(event.getPlayer().getName());
		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		Player p = e.getEntity().getPlayer();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick" + p.getName());
	}

	// Commands

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sstart")) {
			sender.sendMessage(ChatColor.GRAY + "You have started" + ChatColor.YELLOW + " Survival Games.");
			Bukkit.getServer().broadcastMessage(ChatColor.RED + "Survival Games will start in 60 seconds.");
			for(Player p : Bukkit.getOnlinePlayers()) {
				Location location = new Location(Bukkit.getWorld("world"), 0, 64, 0);
				p.setGameMode(GameMode.CREATIVE);
				p.teleport(location);
				Bukkit.getScheduler ().runTaskLater (this, () -> p.setGameMode(GameMode.SURVIVAL), 1200); //20 ticks equal 1 second
				Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Survival Games has started get going! Grace period for 30 seconds."), 1200);
				Bukkit.getScheduler().runTaskLater(this, () -> p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,600,5)), 1200);
				Bukkit.getScheduler ().runTaskLater (this, () -> Bukkit.getServer().broadcastMessage(ChatColor.RED + " Grace Period is over, fight!"), 1800); //20 ticks equal 1 second
				Bukkit.getScheduler().runTaskLater(this, () -> p.teleport(location), 1200);
			}


			return true;
		}
		if (cmd.getName().equalsIgnoreCase("sstop")) {
			sender.sendMessage("You have stopped the game.");
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.kickPlayer("Survival Games has been stopped.");
				Bukkit.getScheduler ().runTaskLater (this, () -> Bukkit.getServer().shutdown(), 40); //20 ticks equal 1 second
			}
			return true;
		}
		return false;
	}
}
