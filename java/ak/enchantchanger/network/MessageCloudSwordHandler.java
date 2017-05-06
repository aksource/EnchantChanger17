package ak.enchantchanger.network;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.item.EcItemCloudSword;
import ak.MultiToolHolders.ItemMultiToolHolder;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessageCloudSwordHandler implements IMessageHandler<MessageCloudSword, IMessage> {
    @Override
    public IMessage onMessage(MessageCloudSword message, MessageContext ctx) {
        EntityPlayer player = EnchantChanger.proxy.getPlayer();
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null) {
            if (itemStack.getItem() instanceof EcItemCloudSword) {
                EcItemCloudSword.setSlotNumToItemStack(itemStack, message.slotNum);
            } else if (itemStack.getItem() instanceof ItemMultiToolHolder) {
                ItemMultiToolHolder mth = (ItemMultiToolHolder) itemStack.getItem();
                if (mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)) != null && mth.getInventoryFromItemStack(itemStack).getStackInSlot(mth.getSlotNumFromItemStack(itemStack)).getItem() instanceof EcItemCloudSword) {
                    itemStack = mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack));
                    EcItemCloudSword.setSlotNumToItemStack(itemStack, message.slotNum);
                }
            }
        }
        return null;
    }
}
