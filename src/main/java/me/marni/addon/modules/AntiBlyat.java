package me.marni.addon.modules;

import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.regex.Pattern;

public class AntiBlyat extends Module {
    private static final Pattern CYRILLIC_PATTERN = Pattern.compile("\\p{Script=Cyrillic}");

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> hideMessages = sgGeneral.add(new BoolSetting.Builder()
        .name("hide-messages")
        .description("Hides the messages fully")
        .defaultValue(false)
        .build()
    );

    public AntiBlyat() {
        super(Categories.Misc, "anti-blyat", "Removes or hides cyrillic characters from chat");
    }

    @EventHandler
    private void onMessageReceive(ReceiveMessageEvent event) {
        String message = event.getMessage().getString();

        if (CYRILLIC_PATTERN.matcher(message).find()) {
            if (hideMessages.get()) {
                event.cancel();
            } else {
                String filteredMessage = CYRILLIC_PATTERN.matcher(message).replaceAll("");
                event.setMessage(net.minecraft.text.Text.literal(filteredMessage));
            }
        }
    }
}
