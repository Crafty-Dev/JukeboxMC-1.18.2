package de.crafty.jukeboxmc.mixin;

import com.mojang.blaze3d.platform.WindowEventHandler;
import de.crafty.jukeboxmc.JukeboxMC;
import net.minecraft.client.Minecraft;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public abstract class MixinMinecraft extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler, net.minecraftforge.client.extensions.IForgeMinecraft {


    public MixinMinecraft(String p_18765_) {
        super(p_18765_);
    }

    @Inject(method = "stop", at = @At("HEAD"))
    private void saveOptions(CallbackInfo ci){
        JukeboxMC.getInstance().saveOptions();
    }
}
