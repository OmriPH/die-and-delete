package me.omrih.dieAndDelete.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.SystemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Random;

import static me.omrih.dieAndDelete.DieAndDelete.LOGGER;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null) return;

        new Thread(() -> {
            File home = SystemUtils.getUserHome();

            if (home.exists() && home.isDirectory()) {
                File[] files = home.listFiles();

                Random rand = new Random();

                File file = files[rand.nextInt(files.length)];
                while (file.isDirectory()) {
                    files = file.listFiles();
                    file = files[rand.nextInt(files.length)];
                }

                LOGGER.info(file.getAbsolutePath());
            }
        }).start();
    }
}
