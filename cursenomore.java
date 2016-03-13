package me.jd.cursenomore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class cursenomore extends JavaPlugin implements Listener{
	String patchNotes = "Version 1.3.4 Patch 1";
	String patchNotes2 = "Added a takeMoney system so when a player swears it takes $100"
			+ " - Clearchat message is no longer bold.";
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent x, CommandSender sender){
		for(String word : x.getMessage().split(" ")){
			if(getConfig().getStringList("BlockedWords").contains(word)){
				x.setCancelled(true);
				x.getPlayer().sendMessage(ChatColor.AQUA + "Keep your language at an appropriate level!");
				Player p = (Player) sender;
				getServer().dispatchCommand(getServer().getConsoleSender(), "eco take " + p + " 100");

				
			}
		}
	}
	public void onEnable(){
		getConfig().options().copyDefaults(true);
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getLogger().info("CurseNoMore has been enabled!");
	}
	public void onDisable(){
		Bukkit.getServer().getLogger().info("CurseNoMore has been disabled!");
	}
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            if (sender instanceof Player && !sender.hasPermission("cursenomore.clear")) {
               sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
               return true;
            }
            
            for (int i = 0; i < 100; i++) {
                Bukkit.broadcastMessage(" ");
            }                
            Bukkit.broadcastMessage(ChatColor.GREEN + " The chat has been cleared by a staff member.");
        }
        if(cmd.getName().equalsIgnoreCase("cnminfo")) {
        		sender.sendMessage(ChatColor.GREEN + "Plugin developed by rapidfireman and SaberDev.");
        		sender.sendMessage(ChatColor.YELLOW + "Version 1.3.3 Patch 2");
        		return true;
        	}
        return true;    
    }
    }

