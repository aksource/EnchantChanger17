package ak.enchantchanger.item;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.utils.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;


public class EcItemCloudSwordCore extends EcItemSword {
    public static Entity attackEntity = null;
    private int nowAttackingSwordSlot;

    public EcItemCloudSwordCore(String name) {
        super(ToolMaterial.IRON, name);
    }

    public static ItemStack makeCloudSword(ItemStack stack) {
        ItemStack ChangeSword = new ItemStack(Items.itemCloudSword, 1, stack.getItemDamage());
        ChangeSword.setTagCompound(stack.getTagCompound());
        return ChangeSword;
    }

    public static boolean isActive(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound().getBoolean(Constants.NBT_ENCHANT_CHANGER_ACTIVEMODE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean advToolTip) {
        boolean mode = isActive(itemStack);
        String s = mode ? Constants.KEY_ENCHANTCHANGER_CLOUDSWORDCORE_MODE_ACTIVE : Constants.KEY_ENCHANTCHANGER_CLOUDSWORDCORE_MODE_INACTIVE;
        list.add(I18n.translateToLocal(s));
        super.addInformation(itemStack, player, list, advToolTip);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack heldItem, @Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                invertActive(heldItem);
                ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, (ItemSword) heldItem.getItem(), (float) ((isActive(heldItem)) ? 7 : 6), 0);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
        } else {
            if (!isActive(heldItem) && canUnion(playerIn)) {
                IInventory swordData = EcItemCloudSword.getInventoryFromItemStack(heldItem);
                unionSword(playerIn, swordData);
                return ActionResult.newResult(EnumActionResult.SUCCESS, makeCloudSword(heldItem));
            } else {
                playerIn.setActiveHand(handIn);
                return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
            }
        }
    }

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World worldIn, @Nonnull Entity entityIn,
                         int itemSlot, boolean isSelected) {
        super.onUpdate(itemStack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull Entity entity) {
        attackEntity = entity;
        if (isActive(stack)) {
            this.attackTargetEntityWithInventoryItem(entity, player);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    private void invertActive(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = itemStack.getTagCompound();
        nbt.setBoolean(Constants.NBT_ENCHANT_CHANGER_ACTIVEMODE, !nbt.getBoolean(Constants.NBT_ENCHANT_CHANGER_ACTIVEMODE));
    }

    public boolean canUnion(EntityPlayer player) {
        int Index = 0;
        int CurrentSlot = player.inventory.currentItem;
        ItemStack sword;
        for (int i = 0; i < 9; i++) {
            if (i == CurrentSlot)
                continue;
            sword = player.inventory.getStackInSlot(i);
            if (sword != null && sword.getItem() instanceof ItemSword) {
                Index++;
            }
        }
        return Index >= 5;
    }

    public void attackTargetEntityWithInventoryItem(Entity entity, EntityPlayer player) {
        ItemStack sword;
        int CurrentSlot = player.inventory.currentItem;
        for (int i = 0; i < 9; i++) {
            if (i == CurrentSlot)
                continue;
            sword = player.inventory.getStackInSlot(i);
            if (sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword)) {
                this.nowAttackingSwordSlot = i;
                this.attackTargetEntityWithTheItem(entity, player, sword, true);
            }
        }
    }

    public void unionSword(EntityPlayer player, IInventory swordData) {
        int Index = 0;
        int CurrentSlot = player.inventory.currentItem;
        ItemStack sword;
        for (int i = 0; i < 9; i++) {
            if (i == CurrentSlot)
                continue;
            sword = player.inventory.getStackInSlot(i);
            if (sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword) && Index < 5) {
                swordData.setInventorySlotContents(Index, sword);
                player.inventory.setInventorySlotContents(i, null);
                Index++;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public IPerspectiveAwareModel getPresentModel(@Nonnull ItemStack itemStack, @Nonnull List<IPerspectiveAwareModel> modelList) {
        return (isActive(itemStack) ? modelList.get(1) : modelList.get(0));
    }
}