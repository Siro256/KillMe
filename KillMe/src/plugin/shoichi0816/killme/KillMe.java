package plugin.shoichi0816.killme;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class KillMe extends JavaPlugin implements Listener{

	public void onEnable() {

		getServer().getPluginManager().registerEvents(this, this);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onKill(PlayerInteractEvent e) {

		Action a = e.getAction();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();

		if (item.getType().equals(Material.STICK)) {

			if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {

				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);

				p.addScoreboardTag("Falling");

				Location loc = p.getBedSpawnLocation().add(0.5, 0, 0.5);

				p.teleport(loc);
				p.setHealth(20);
				p.setFireTicks(0);

				p.sendMessage("§bRespawned!");

				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

					@Override
					public void run() {

						p.removeScoreboardTag("Falling");
					}
				},10 * 1);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		ItemStack stick = new ItemStack(Material.STICK);

		ItemMeta im = stick.getItemMeta();
		im.setDisplayName("§2RightClick to Death");
		stick.setItemMeta(im);

		p.getInventory().setItem(0, stick);

		p.sendMessage("§6お手持ちの棒を右クリックすることでスポーン地点に戻ることができます!!");
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();
			if (e.getCause().equals(DamageCause.FALL)) {

				if (p.getScoreboardTags().contains("Falling")) {

					e.setCancelled(true);
					p.removeScoreboardTag("Falling");
				}
			}
		}
	}

	@EventHandler
	public void onDropStick(PlayerDropItemEvent e) {

		ItemStack item = e.getItemDrop().getItemStack();

		if (item.getType().equals(Material.STICK)) {

			e.setCancelled(true);
		}
	}
}
