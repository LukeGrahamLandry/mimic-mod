package ca.lukegrahamlandry.mimic.client;

import ca.lukegrahamlandry.mimic.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CustomChestScreen extends AbstractContainerScreen<MimicContainer> implements MenuAccess<MimicContainer> {
    private ResourceLocation CONTAINER_BACKGROUND;
    private final int containerRows;

    public CustomChestScreen(MimicContainer a, Inventory b, Component c, String type) {
        super(a, b, c);
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.containerRows = a.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;

        this.CONTAINER_BACKGROUND = new ResourceLocation(Constants.MOD_ID, "textures/gui/" + type + ".png");
    }

    public static CustomChestScreen createEvil(MimicContainer a, Inventory b, Component c){
        return new CustomChestScreen(a, b, c, "evil_mimic");
    }

    public static CustomChestScreen createTame(MimicContainer a, Inventory b, Component c){
        return new CustomChestScreen(a, b, c, "tame_mimic");
    }

    public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.containerRows * 3 * 18 + 17);
        // this.blit(p_230450_1_, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}