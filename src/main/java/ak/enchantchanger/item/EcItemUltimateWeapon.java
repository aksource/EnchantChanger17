package ak.enchantchanger.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;

public class EcItemUltimateWeapon extends EcItemSword {
    public EcItemUltimateWeapon(String name) {
        super(ToolMaterial.DIAMOND, name);
    }

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack itemstack, @Nonnull EntityPlayer player, @Nonnull Entity entity) {
        if (player.world.isRemote) return false;
        float ultimateWeaponDamage;
        if (entity instanceof EntityLivingBase) {
            float mobmaxhealth = ((EntityLivingBase) entity).getMaxHealth() / 3 + 1;
            float weaponDmgFromHP = WeaponDamagefromHP(player);
            ultimateWeaponDamage = (mobmaxhealth > weaponDmgFromHP) ? mobmaxhealth : weaponDmgFromHP;
        } else if (entity instanceof IEntityMultiPart) {
            ultimateWeaponDamage = 100;
        } else {
            ultimateWeaponDamage = 10;
        }
        ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, (ItemSword) itemstack.getItem(), ultimateWeaponDamage, 0);
        player.getAttributeMap().applyAttributeModifiers(itemstack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
        return super.onLeftClickEntity(itemstack, player, entity);
    }

    public float WeaponDamagefromHP(EntityPlayer player) {
        float nowHP = player.getHealth();
        float maxHP = player.getMaxHealth();
        float hpRatio = nowHP / maxHP;
        float damageratio;
        if (hpRatio >= 0.8) {
            damageratio = 1;
        } else if (hpRatio >= 0.5) {
            damageratio = 0.7F;
        } else if (hpRatio >= 0.2) {
            damageratio = 0.5F;
        } else {
            damageratio = 0.3F;
        }
        int EXPLv = player.experienceLevel;
        return MathHelper.floor((10 + EXPLv / 5) * damageratio);
    }
}
