package dev.lrxh.neptune.utils;

import dev.lrxh.neptune.configs.impl.MessagesLocale;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class CC {
    private final Pattern COLOR_PATTERN = Pattern.compile("&[0-9a-fA-Fk-oK-OrR]");
    private final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    public String error(String message) {
        return color(MessagesLocale.ERROR_MESSAGE.getString().replace("<error>", message));
    }

    public String success(String text) {
        return color("&a[+] " + text);
    }

    public String info(String text) {
        return color("&7[~] " + text);
    }

    public String color(String text) {
        Matcher match = HEX_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder(text);

        while (match.find()) {
            String color = match.group(1);
            String coloredText = getColor(color) + "";
            int start = match.start();
            int end = match.end();

            result.replace(start, end, coloredText);
        }

        return result.toString().replace('&', '§');
    }

    public static ChatColor getColor(String string) {
        return ChatColor.of(new Color(Integer.parseInt(string, 16)));
    }

    public List<Object> color(List<Object> input) {
        return formatColors(input);
    }

    public List<Object> formatColors(List<Object> input) {
        List<Object> result = new ArrayList<>();
        String lastColor = null;

        for (Object object : input) {
            if (object instanceof String str) {

                Matcher matcher = COLOR_PATTERN.matcher(str);
                while (matcher.find()) {
                    lastColor = matcher.group();
                }

                if (lastColor != null && !str.isEmpty()) {
                    result.add(color(lastColor + str));
                } else {
                    result.add(color(str));
                }
            } else if (object instanceof TextComponent textComponent) {
                String str = textComponent.content();

                Matcher matcher = COLOR_PATTERN.matcher(str);
                while (matcher.find()) {
                    lastColor = matcher.group();
                }

                if (lastColor != null && !str.isEmpty()) {
                    TextComponent temp = Component.text(color(lastColor + str))
                            .clickEvent(textComponent.clickEvent())
                            .hoverEvent(textComponent.hoverEvent());
                    result.add(temp);
                } else {
                    result.add(textComponent);
                }
            } else {
                result.add(object);
            }
        }
        return result;
    }
}
