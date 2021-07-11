package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.event.BalmEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;", at = @At("RETURN"))
    public void openMenu(@Nullable MenuProvider menuProvider, CallbackInfoReturnable<OptionalInt> callbackInfo) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        BalmEvents.OPEN_MENU.invoker().handle(player, player.containerMenu);
    }
}
