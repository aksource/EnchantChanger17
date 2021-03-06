package ak.enchantchanger.client.gui;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.inventory.EcContainerMateriaWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * マテリア設定画面のGUIクラス
 * Created by A.K. on 14/02/27.
 */
public class EcGuiMateriaWindow extends GuiContainer {
    private static final ResourceLocation GUI = new ResourceLocation(Constants.EcAssetsDomain, Constants.TEXTURES_GUI_MATERIALIZING);

    public EcGuiMateriaWindow(InventoryPlayer inventoryPlayer, ItemStack item, int slotnum) {
        super(new EcContainerMateriaWindow(inventoryPlayer, item, slotnum));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        int itemRenderPosX = (int) ((x + 11) / 4.0f);
        int itemRenderPosY = (int) ((y + 11) / 4.0f);
        GlStateManager.pushMatrix();
        itemRender.zLevel = 100.0F;
        GlStateManager.scale(4.0f, 4.0f, 1);
        ItemStack openItem = ((EcContainerMateriaWindow) this.inventorySlots).getOpenItem();
        mc.getRenderItem().renderItemAndEffectIntoGUI(openItem, itemRenderPosX, itemRenderPosY);//Itemの描画
        mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, openItem, itemRenderPosX, itemRenderPosY);//耐久値の描画
        itemRender.zLevel = 0.0F;
        GlStateManager.popMatrix();
    }

    protected void keyTyped(char c, int keycode) throws IOException {
        if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() || keycode == Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode()) {
            Minecraft.getMinecraft().player.closeScreen();
        } else super.keyTyped(c, keycode);
    }

    public void updateScreen() {
        super.updateScreen();
        EntityPlayer player = mc.player;
        if (!player.isEntityAlive() || player.isDead) {
            player.closeScreen();
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
