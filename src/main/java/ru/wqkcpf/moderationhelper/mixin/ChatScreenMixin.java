package ru.wqkcpf.moderationhelper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.wqkcpf.moderationhelper.chat.ChatClickHandler;
import ru.wqkcpf.moderationhelper.chat.ChatMessageExtractor;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    /**
     * Minecraft 1.21.11 changed mouseClicked signature:
     * old: mouseClicked(double mouseX, double mouseY, int button)
     * new: mouseClicked(Click click, boolean doubled)
     */
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mhg$onMiddleClick(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (click.button() != GLFW.GLFW_MOUSE_BUTTON_MIDDLE) return;

        String rawMessage = ChatMessageExtractor.extractMessageUnderMouse(
                MinecraftClient.getInstance(),
                click.x(),
                click.y()
        );

        ChatClickHandler.handleMiddleClick(rawMessage);
        cir.setReturnValue(true);
    }
}
