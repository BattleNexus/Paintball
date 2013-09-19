package net.battlenexus.paintball.listeners;

import net.battlenexus.paintball.Paintball;
import net.battlenexus.paintball.entities.PBPlayer;
import net.battlenexus.paintball.game.PaintballGame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;

public class PlayerListener implements Listener {

    protected HashMap<String, String> deathMessages = new HashMap<String, String>();

    public PlayerListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //PBPlayer.newPlayer(event.getPlayer()); This really isnt need here..
        event.setJoinMessage("The faggot " + event.getPlayer().getDisplayName() + " has joined the game");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PBPlayer who;
        if ((who = PBPlayer.getPlayer(p)) == null) {
            return;
        }
        //Removes from queue if in queue when leaving
        Paintball.INSTANCE.getGameService().leaveQueue(who);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        PBPlayer who;
        if ((who = PBPlayer.getPlayer(p)) == null) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            //GUN
            if (who.getCurrentWeapon() != null && p.getInventory().getItemInHand().getType().equals(who.getCurrentWeapon().getMaterial())) {
                who.getCurrentWeapon().shoot();
            }
            //RELOAD
            if (who.getCurrentWeapon() != null && p.getInventory().getItemInHand().getType().equals(who.getCurrentWeapon().getReloadItem()) && who.getCurrentWeapon().clipeSize() > who.getCurrentWeapon().currentClipSize()) {
                who.getCurrentWeapon().reload();
            }
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Snowball) || !(event.getEntity() instanceof Player) || !(((Projectile) event.getDamager()).getShooter() instanceof Player)) {
            return;
        }
        Player victim = (Player) event.getEntity();
        PBPlayer pbvictim;
        if ((pbvictim = PBPlayer.getPlayer(victim)) == null) {
            return;
        }
        PBPlayer shooter = PBPlayer.toPBPlayer((Player) ((Snowball)event.getDamager()).getShooter());
        if (shooter.getCurrentTeam() != null) {
            return;
        }
        else if (shooter.getCurrentTeam().contains(pbvictim)) {
            //TODO Friendly fire
        } else {
            pbvictim.kill(shooter);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        if (deathMessages.containsKey(playerName)) {
            event.setDeathMessage(deathMessages.get(playerName));
            deathMessages.remove(playerName);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        PBPlayer player;
        if ((player = PBPlayer.getPlayer(event.getPlayer())) != null) {
            if (player.isFrozen())
                player.handleFrozen();
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        //Part of custom "adventure mode" to stop breaking of blocks
        Player p = event.getPlayer();
        PBPlayer who;
        if ((who = PBPlayer.getPlayer(p)) == null) {
            return;
        }
        //Stop them if they are ingame
        if(who.isInGame()) {
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        //Part of custom "adventure mode" to stop placing of blocks
        Player p = event.getPlayer();
        PBPlayer who;
        if ((who = PBPlayer.getPlayer(p)) == null) {
            return;
        }
        //Stop them if they are ingame
        if(who.isInGame()) {
            return;
        }
    }
}