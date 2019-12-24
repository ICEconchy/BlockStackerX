package me.epicgodmc.blockstackerx.Objects;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Files.Worth;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.UUID;

public class StackerPlaced
{
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private Worth worth = plugin.worth;
    private MessageManager mm = plugin.messageManager;


    private String type;
    private UUID uuid;
    private Material chosenMaterial;
    private SLocation location;
    private ArmorStand display;
    private int value;
    private int id;


    public StackerPlaced(int id, String type, UUID uuid, Material chosenMaterial, SLocation location, ArmorStand display, int value)
    {
        this.type = type;
        this.uuid = uuid;
        this.chosenMaterial = chosenMaterial;
        this.location = location;
        this.display = display;
        this.value = value;
        this.id = id;

        plugin.placedStacks.addStack(location.toLocation(), this);
    }

    public StackerPlaced(String dataString)
    {
        String[] data0 = dataString.split(";");

        int id;
        String type = data0[1];
        UUID uuid = UUID.fromString(data0[2]);
        Material cmat;
        SLocation location = formatSlocation(data0[4].split(","));
        SLocation displayLoc = formatSlocation(data0[5].split(","));
        int value;

        if (data0[3].equalsIgnoreCase("unspecified"))
        {
            cmat = null;
        }else cmat = Material.valueOf(data0[3].toUpperCase());

        try{

            id = Integer.parseInt(data0[0]);
            value = Integer.parseInt(data0[6]);

        }catch (Exception e)
        {
            Bukkit.broadcastMessage(ChatColor.RED+mm.getMessage("stackerFormatError", false));
            e.printStackTrace();
            return;
        }
        ArmorStand display = plugin.util.createDisplay(type, value, displayLoc.toLocation().add(plugin.util.getOffset(type).add(new Vector(0,1,0))));

        this.type = type;
        this.uuid = uuid;
        this.chosenMaterial = cmat;
        this.location = location;
        this.display = display;
        this.value = value;
        this.id = id;

        plugin.placedStacks.addStack(location.toLocation(), this);
    }



    public SLocation formatSlocation(String[] data)
    {
        String world = data[0];
        int x = 0;
        int y = 0;
        int z = 0;

        try{
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);


        }catch (Exception e)
        {
            Bukkit.broadcastMessage(ChatColor.RED+mm.getMessage("stackerFormatError", false));
            e.printStackTrace();

        }
        return new SLocation(world, x, y, z);

    }

    public double computeAndGetLevels()
    {
        double blockValue = worth.getWorth(this.chosenMaterial);

        return (blockValue * this.value);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    public String toString()
    {
        SLocation displayLoc = new SLocation(display.getLocation());
        String cmat;
        if (chosenMaterial != null) cmat = chosenMaterial.toString();
        else cmat = "unspecified";

        return this.id+";"+type+";"+uuid.toString()+";"+cmat+";"+location.world+","+location.x+","+location.y+","+location.z+";"+displayLoc.world+","+displayLoc.x+","+displayLoc.y+","+displayLoc.z+";"+value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Material getChosenMaterial() {
        return chosenMaterial;
    }

    public void setChosenMaterial(Material chosenMaterial) {
        if (chosenMaterial == null)
        {
            this.chosenMaterial = null;
            this.location.toLocation().getWorld().getBlockAt(this.location.toLocation()).setType(Material.valueOf(plugin.config.getString("Stackers."+this.type+".itemType").toUpperCase()));
            return;
        }
        this.chosenMaterial = chosenMaterial;
        this.location.toLocation().getWorld().getBlockAt(this.location.toLocation()).setType(chosenMaterial);
    }

    public SLocation getLocation() {
        return location;
    }

    public void setLocation(SLocation location) {
        this.location = location;
    }

    public ArmorStand getDisplay() {
        return display;
    }

    public void setDisplay(ArmorStand display) {
        this.display = display;
    }

    public int getValue() {
        return value;
    }

    public void decrementValueby(int value)
    {
        this.value -= value;
        plugin.util.UpdateDisplay(this.type, this.display, this.value);
    }

    public void incrementValueBy(int value)
    {
        this.value += value;
        plugin.util.UpdateDisplay(this.type, this.display, this.value);
    }
    public void setValue(int value) {
        this.value = value;
        plugin.util.UpdateDisplay(this.type, this.display, this.value);

    }
}
