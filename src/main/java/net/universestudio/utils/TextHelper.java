package net.universestudio.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.text.DecimalFormat;

public final class TextHelper {
    public static final String PLUGIN_PREFIX = "<gradient:#20bdff:#a5fecb>Tweaks</gradient>";
    public static final DecimalFormat FORMAT = new DecimalFormat("###,###,###,###,###,###.#");

    public static Component text(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static Component empty() {
        return text(emptyLine());
    }

    public static String emptyLine() {
        return "\u00A0";
    }

    public static Component prefixMessage(String message) {
        return text("%s <gray>➤</gray> %s".formatted(PLUGIN_PREFIX, message));
    }
}
