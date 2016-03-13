package dev.saber.backpack;
 
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
 
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


 
public class Backpack extends JavaPlugin implements Listener {
       
        private HashMap<UUID, Inventory> backpacks = new HashMap<UUID, Inventory>();
       
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
                Inventory inv = Bukkit.getServer().createInventory(e.getPlayer(), InventoryType.CHEST, "Backpack");
               
                if (getConfig().contains("backpacks." + e.getPlayer().getUniqueId())) {
                        for (String item : getConfig().getConfigurationSection("backpacks." + e.getPlayer().getUniqueId()).getKeys(false)) {
                                inv.addItem(loadItem(getConfig().getConfigurationSection("backpacks." + e.getPlayer().getUniqueId() + "." + item)));
                        }
                }
               
                backpacks.put(e.getPlayer().getUniqueId(), inv);
        }
       
        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent e) {
                if (!getConfig().contains("backpacks." + e.getPlayer().getUniqueId())) {
                        getConfig().createSection("backpacks." + e.getPlayer().getUniqueId());
                }
               
                char c = 'a';
                for (ItemStack itemStack : backpacks.get(e.getPlayer().getUniqueId())) {
                        if (itemStack != null) {
                                saveItem(getConfig().createSection("backpacks." + e.getPlayer().getUniqueId() + "." + c++), itemStack);
                        }
                }
               
                saveConfig();
        }
       
        public void onEnable() {
                Bukkit.getServer().getPluginManager().registerEvents(this, this);
        }
       
        public void onDisable() {
                for (Entry<UUID, Inventory> entry : backpacks.entrySet()) {
                        if (!getConfig().contains("backpacks." + entry.getKey())) {
                                getConfig().createSection("backpacks." + entry.getKey());
                        }
                       
                        char c = 'a';
                        for (ItemStack itemStack : entry.getValue()) {
                                if (itemStack != null) {
                                        saveItem(getConfig().createSection("backpacks." + entry.getKey() + "." + c++), itemStack);
                                }
                        }
                        saveConfig();
                }
        }
       
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
                if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "The console cannot have an backpack.");
                        return true;
                }
               
                Player p = (Player) sender;
               
                if (cmd.getName().equalsIgnoreCase("bp")) {
                	if(!p.hasPermission("backpack.bp")) {
                		p.sendMessage(ChatColor.RED + "You do not have access to that command.");
                	} else {
                        p.openInventory(backpacks.get(p.getUniqueId()));
                	}
                if(cmd.getName().equalsIgnoreCase("clearbp")) {
                	clearChestOnDeath(null);
                }
                }
               
                return true;
        }
        
        public void clearChestOnDeath(PlayerDeathEvent e){
            char c = 'a';
            for (ItemStack itemStack : backpacks.get(e.getEntity().getUniqueId())) {
            	World world = e.getEntity().getWorld();
                    if (itemStack != null) {
                            getConfig().get("backpacks." + e.getEntity().getUniqueId(), "");
                            world.dropItemNaturally(e.getEntity().getLocation(), itemStack);
                    }
            }
                         
        }
       
        private void saveItem(ConfigurationSection section, ItemStack itemStack) {
                section.set("type", itemStack.getType().name());
                section.set("amount", itemStack.getAmount());
                // Save more information.
        }
       
        private ItemStack loadItem(ConfigurationSection section) {
                return new ItemStack(Material.valueOf(section.getString("type")), section.getInt("amount"));
                // Load more information.
        }
}
