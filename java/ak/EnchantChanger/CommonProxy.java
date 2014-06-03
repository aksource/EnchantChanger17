package ak.EnchantChanger;

import ak.EnchantChanger.Client.gui.EcGuiHugeMateria;
import ak.EnchantChanger.Client.gui.EcGuiMateriaWindow;
import ak.EnchantChanger.Client.gui.EcGuiMaterializer;
import ak.EnchantChanger.Client.gui.EcGuiPortableEnchantment;
import ak.EnchantChanger.inventory.EcContainerHugeMateria;
import ak.EnchantChanger.inventory.EcContainerMateriaWindow;
import ak.EnchantChanger.inventory.EcContainerMaterializer;
import ak.EnchantChanger.inventory.EcContainerPortableEnchantment;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CommonProxy implements IGuiHandler {
    private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<>();

    public void registerRenderInformation() {
    }

    public void registerTileEntitySpecialRenderer() {
    }

    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        if (id == EnchantChanger.guiIdMaterializer) {
            return new EcContainerMaterializer(world, player.inventory);
        } else if (id == EnchantChanger.guiIdPortableEnchantmentTable) {
            return new EcContainerPortableEnchantment(player.inventory, world, x, y, z);
        } else if (id == EnchantChanger.guiIdHugeMateria) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcContainerHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
            else return null;
        } else if (id == EnchantChanger.guiIdMateriaWindow) {
            ItemStack heldItem = player.getCurrentEquippedItem();
            int slot = player.inventory.currentItem;
            if (heldItem != null) {
                return new EcContainerMateriaWindow(world, player.inventory, heldItem, slot);
            } else return null;
        } else return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == EnchantChanger.guiIdMaterializer) {
            return new EcGuiMaterializer(world, player.inventory);
        } else if (id == EnchantChanger.guiIdPortableEnchantmentTable) {
            return new EcGuiPortableEnchantment(player.inventory, world, x, y, z);
        } else if (id == EnchantChanger.guiIdHugeMateria) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcGuiHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
            else return null;
        } else if (id == EnchantChanger.guiIdMateriaWindow) {
            ItemStack heldItem = player.getCurrentEquippedItem();
            int slot = player.inventory.currentItem;
            if (heldItem != null) {
                return new EcGuiMateriaWindow(world, player.inventory, heldItem, slot);
            } else return null;
        } else return null;
    }

    public static void storeEntityData(String name, NBTTagCompound compound)
    {
        extendedEntityData.put(name, compound);
    }

    public static NBTTagCompound getEntityData(String name)
    {
        return extendedEntityData.remove(name);
    }

}