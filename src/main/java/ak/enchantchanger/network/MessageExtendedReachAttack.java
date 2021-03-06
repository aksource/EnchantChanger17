package ak.enchantchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by A.K. on 14/08/20.
 */
public class MessageExtendedReachAttack implements IMessage {

    private int entityId;

    public MessageExtendedReachAttack() {
    }

    public MessageExtendedReachAttack(Entity entity) {
        this.entityId = entity.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public Entity getEntityFromId(World world) {
        return world.getEntityByID(this.entityId);
    }
}
