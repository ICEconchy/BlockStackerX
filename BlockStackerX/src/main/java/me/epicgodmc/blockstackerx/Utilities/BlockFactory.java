package me.epicgodmc.blockstackerx.Utilities;

import de.tr7zw.nbtapi.NBTItem;
import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BlockFactory
{
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;



    public ItemStack getStacker(String type, int amt)
    {
        String path = "Stackers."+type+".";
        String itemValue = plugin.config.getString(path+"itemType");
        String displayName = plugin.config.getString(path+"displayName");
        ArrayList<String> lore = new ArrayList<>();

        ItemStack stack = new ItemStack(Material.valueOf(itemValue.toUpperCase()), amt);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(mm.applyCC(displayName));

        for (String s : plugin.config.getStringList(path+"lore"))
        {
            lore.add(mm.applyCC(s));
        }
        meta.setLore(lore);

        if (plugin.config.getBoolean(path+"glow"))
        {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        stack.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(stack);

        nbtItem.setString("BlockStackerX", type);


        return nbtItem.getItem();
    }

}
