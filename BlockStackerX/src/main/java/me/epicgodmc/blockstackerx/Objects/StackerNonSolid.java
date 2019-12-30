package me.epicgodmc.blockstackerx.Objects;

import de.tr7zw.nbtapi.NBTItem;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StackerNonSolid {
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;





    public StackerNonSolid(String type, Material mat, int value, UUID uuid) {
        Bukkit.getPlayer(uuid).getInventory().addItem(createBlock(type, value, mat));
    }


    private ItemStack createBlock(String type, int value, Material mat) {
        if (value == 0) {
            return plugin.blockFactory.getStacker(type, 1);
        }

        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(mm.applyCC(plugin.getConfig().getString("Stackers." + type + ".item.displayName")));

        List<String> lore = new ArrayList<>();
        for (String s : plugin.getConfig().getStringList("Stackers." + type + ".item.lore")) {
            lore.add(mm.applyCC(s.replace("%VALUE%", "" + value)));
        }
        meta.setLore(lore);

        if (plugin.getConfig().getBoolean("Stackers." + type + ".item.glow")) {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        stack.setItemMeta(meta);

        //NBT
        NBTItem nbtItem = new NBTItem(stack);
        nbtItem.setBoolean("BlockStackerX_nonSolid", true);
        nbtItem.setString("StackerType", type);
        nbtItem.setString("StackerMaterial", mat.toString());
        nbtItem.setInteger("StackerValue", value);

        return nbtItem.getItem();
    }


}
