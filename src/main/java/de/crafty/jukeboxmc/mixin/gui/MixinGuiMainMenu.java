package de.crafty.jukeboxmc.mixin.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.jukeboxmc.gui.SettingsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinGuiMainMenu extends Screen {


    protected MixinGuiMainMenu(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void addSettingsButton(CallbackInfo ci) {



        this.addRenderableWidget(new Button(this.width - 20, 0, 20, 20, new TextComponent(""), (button) -> {
            this.minecraft.setScreen(new SettingsScreen(this));
        }));

    }


    @Inject(method = "render", at = @At("HEAD"))
    private void renderIcon(PoseStack p_96739_, int p_96740_, int p_96741_, float p_96742_, CallbackInfo ci){
        this.minecraft.getItemRenderer().renderGuiItem(new ItemStack(Blocks.JUKEBOX), this.width - 18, 2);
    }
}
