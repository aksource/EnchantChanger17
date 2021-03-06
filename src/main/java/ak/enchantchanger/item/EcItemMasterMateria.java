package ak.enchantchanger.item;

import ak.enchantchanger.api.MasterMateriaType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EcItemMasterMateria extends EcItem {

    public EcItemMasterMateria(String name) {
        super(name);
    }

    @Override
    @Nonnull
    public String getTranslationKey(@Nonnull ItemStack itemStack) {
        int itemDmg = itemStack.getItemDamage();
        return "ItemMasterMateria." + itemDmg;
    }

    @Override
    public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for (MasterMateriaType type : MasterMateriaType.values()) {
                subItems.add(new ItemStack(this, 1, type.getMeta()));
            }
        }
    }
}