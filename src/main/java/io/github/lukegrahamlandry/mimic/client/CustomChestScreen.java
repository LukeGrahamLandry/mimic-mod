package io.github.lukegrahamlandry.mimic.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lukegrahamlandry.mimic.MimicMain;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CustomChestScreen extends ContainerScreen<MimicContainer> implements IHasContainer<MimicContainer> {
    private ResourceLocation CONTAINER_BACKGROUND;
    private final int containerRows;

    public CustomChestScreen(MimicContainer a, PlayerInventory b, ITextComponent c, String type) {
        super(a, b, c);
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.containerRows = a.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;

        this.CONTAINER_BACKGROUND = new ResourceLocation(MimicMain.MOD_ID, "textures/gui/" + type + ".png");
    }

    public static CustomChestScreen createEvil(MimicContainer a, PlayerInventory b, ITextComponent c){
        return new CustomChestScreen(a, b, c, "evil_mimic");
    }

    public static CustomChestScreen createTame(MimicContainer a, PlayerInventory b, ITextComponent c){
        return new CustomChestScreen(a, b, c, "tame_mimic");
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.containerRows * 3 * 18 + 17);
        // this.blit(p_230450_1_, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}