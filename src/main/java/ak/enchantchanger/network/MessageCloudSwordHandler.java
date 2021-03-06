package ak.enchantchanger.network;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.item.EcItemCloudSword;
import ak.multitoolholders.ItemMultiToolHolder;
import ak.multitoolholders.inventory.InventoryToolHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessageCloudSwordHandler implements IMessageHandler<MessageCloudSword, IMessage> {
    @Override
    public IMessage onMessage(MessageCloudSword message, MessageContext ctx) {
        EntityPlayer player = EnchantChanger.proxy.getPlayer();
        ItemStack itemStack = player.getHeldItemMainhand();
        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() instanceof EcItemCloudSword) {
                EcItemCloudSword.setSlotNumToItemStack(itemStack, message.slotNum);
            } else if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
                ItemMultiToolHolder mth = (ItemMultiToolHolder) itemStack.getItem();
                InventoryToolHolder toolHolder = mth.getInventoryFromItemStack(itemStack);
                itemStack = toolHolder.getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack));
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof EcItemCloudSword) {
                    EcItemCloudSword.setSlotNumToItemStack(itemStack, message.slotNum);
                }
            }
        }
        return null;
    }
}
