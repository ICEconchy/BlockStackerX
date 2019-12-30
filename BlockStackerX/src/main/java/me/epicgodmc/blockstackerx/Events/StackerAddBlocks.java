package me.epicgodmc.blockstackerx.Events;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import de.tr7zw.nbtapi.NBTItem;
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
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class StackerAddBlocks implements Listener {

    private BlockStackerX plugin = BlockStackerX.getInstance();
    private Util util = plugin.util;
    private MessageManager mm = plugin.messageManager;

    private PlacedStacks placedStacks = plugin.placedStacks;

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location location = event.getClickedBlock().getLocation();
            if (placedStacks.contains(location)) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    StackerPlaced sp = placedStacks.getStacker(location);
                    Material mat = player.getItemInHand().getType();
                    NBTItem nbtItem = new NBTItem(player.getItemInHand());
                    List<Material> materialWhiteList = util.getBlockList(sp.getType());
                    if (player.hasPermission("bs.AddBlocks")) {
                        if (materialWhiteList.contains(mat)) {
                            if (nbtItem.hasNBTData() && nbtItem.hasKey("BlockStackerX") || nbtItem.hasKey("BlockStackerX_nonSolid")) {
                                player.sendMessage(mm.applyCC(mm.getMessage("cantStackStackers", true)));
                                return;
                            }
                            if (sp.getUuid().equals(uuid) || plugin.config.getBoolean("Stackers." + sp.getType() + ".teamStacking") && util.playerIsOnTeamOf(uuid, sp.getUuid())) {
                                if (sp.getValue() == 0) {
                                    sp.setChosenMaterial(mat);
                                    player.sendMessage(mm.applyCC(mm.getMessage("chosenMaterial", true).replace("%BLOCKTYPE%", mat.toString().toLowerCase())));
                                }
                                if (sp.getChosenMaterial().equals(mat)) {
                                    if (!player.isSneaking()) {

                                        if (sp.getValue() + 1 <= plugin.config.getInt("Stackers." + sp.getType() + ".maxStack")) {
                                            int decreaseAmt = decreaseItemInHandBy(player, 1);
                                            sp.incrementValueBy(decreaseAmt);
                                            player.sendMessage(mm.applyCC(mm.getMessage("incrementMessage", true).replace("%AMOUNT%", "" + decreaseAmt)));

                                        } else {
                                            player.sendMessage(mm.applyCC(mm.getMessage("maxStack", true).replace("%MAX%", "" + plugin.config.getInt("Stackers." + sp.getType() + ".maxStack"))));
                                            return;
                                        }
                                    }
                                    if (player.isSneaking()) {

                                        if (sp.getValue() + player.getItemInHand().getAmount() <= plugin.config.getInt("Stackers." + sp.getType() + ".maxStack")) {
                                            int decreaseAmt = decreaseItemInHandBy(player, 64);
                                            sp.incrementValueBy(decreaseAmt);
                                            player.sendMessage(mm.applyCC(mm.getMessage("incrementMessage", true).replace("%AMOUNT%", "" + decreaseAmt)));

                                        } else {
                                            player.sendMessage(mm.applyCC(mm.getMessage("maxStack", true).replace("%MAX%", "" + plugin.config.getInt("Stackers." + sp.getType() + ".maxStack"))));
                                            return;
                                        }
                                    }
                                } else {
                                    player.sendMessage(mm.applyCC(mm.getMessage("stackerOccupied", true).replace("%BLOCK%", sp.getChosenMaterial().toString().toLowerCase())));
                                    return;
                                }

                            } else {
                                player.sendMessage(mm.applyCC(mm.getMessage("notYourStacker", true)));
                            }
                        } else {
                            player.sendMessage(mm.applyCC(mm.getMessage("incorrectBlockInHand", true)));
                            return;
                        }
                    }else{
                        event.setCancelled(true);
                        player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
                        return;
                    }
                } else {
                    player.sendMessage(mm.applyCC(mm.getMessage("noBlockInHand", true)));
                    return;
                }
            }
        }
    }


    private int decreaseItemInHandBy(Player player, int amt) {
        int handAmt = player.getItemInHand().getAmount();
        if (amt >= handAmt) {
            player.setItemInHand(new ItemStack(Material.AIR));
            return handAmt;
        }
        ItemStack newAmt = new ItemStack(player.getItemInHand().getType(), handAmt - amt);
        player.setItemInHand(newAmt);
        return amt;
    }


}
