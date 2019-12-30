package me.epicgodmc.blockstackerx.Events;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.PlacedStacks;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import me.epicgodmc.blockstackerx.Utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StackerRemoveBlocks implements Listener {
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private Util util = plugin.util;
    private MessageManager mm = plugin.messageManager;

    private PlacedStacks placedStacks = plugin.placedStacks;


    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location location = event.getClickedBlock().getLocation();
            if (placedStacks.contains(location)) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                StackerPlaced sp = placedStacks.getStacker(location);
                if (sp.getValue() > 0) {
                    if (sp.getUuid().equals(uuid) || plugin.config.getBoolean("Stackers." + sp.getType() + ".teamStacking") && util.playerIsOnTeamOf(uuid, sp.getUuid())) {
                        if (player.hasPermission("bs.takeblocks")) {
                            if (!util.isPickaxe(player.getItemInHand().getType())) {
                                if (!player.isSneaking()) {

                                    if (hasAvaliableSlot(player, sp.getChosenMaterial(), 1)) {
                                        sp.decrementValueby(1);
                                        player.getInventory().addItem(new ItemStack(sp.getChosenMaterial(), 1));
                                        player.sendMessage(mm.applyCC(mm.getMessage("decrementMessage", true).replace("%AMOUNT%", "1")));
                                    } else {
                                        player.sendMessage(mm.applyCC(mm.getMessage("noInventorySpace", true)));
                                    }
                                }
                                if (player.isSneaking()) {
                                    if (sp.getValue() >= 64) {
                                        if (hasAvaliableSlot(player, sp.getChosenMaterial(), 64)) {
                                            sp.decrementValueby(64);
                                            player.getInventory().addItem(new ItemStack(sp.getChosenMaterial(), 64));
                                            player.sendMessage(mm.applyCC(mm.getMessage("decrementMessage", true).replace("%AMOUNT%", "64")));
                                        } else {
                                            player.sendMessage(mm.applyCC(mm.getMessage("noInventorySpace", true)));
                                            return;
                                        }
                                    } else {
                                        if (hasAvaliableSlot(player, sp.getChosenMaterial(), sp.getValue())) {
                                            player.getInventory().addItem(new ItemStack(sp.getChosenMaterial(), sp.getValue()));
                                            player.sendMessage(mm.applyCC(mm.getMessage("decrementMessage", true).replace("%AMOUNT%", "" + sp.getValue())));
                                            sp.setValue(0);
                                        } else {
                                            player.sendMessage(mm.applyCC(mm.getMessage("noInventorySpace", true)));
                                            return;
                                        }
                                    }
                                }
                                if (sp.getValue() == 0) {
                                    sp.setChosenMaterial(null);
                                }

                            } else return;
                        }else{
                            event.setCancelled(true);
                            player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
                            return;
                        }
                    } else {
                        player.sendMessage(mm.applyCC(mm.getMessage("notYourStacker", true)));
                        return;
                    }
                } else {
                    player.sendMessage(mm.applyCC(mm.getMessage("stackerEmpty", true)));
                    return;
                }
            }


        }

    }


    public boolean hasAvaliableSlot(Player player, Material mat, int amt) {
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().equals(mat) && item.getAmount() <= 64 - amt) {
                return true;
            }
        }
        return false;
    }


}
