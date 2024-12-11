package dev.lrxh.neptune.party.menu.buttons.events;

import dev.lrxh.neptune.configs.impl.MenusLocale;
import dev.lrxh.neptune.party.Party;
import dev.lrxh.neptune.party.impl.EventType;
import dev.lrxh.neptune.party.menu.PartyEventsKitMenu;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@AllArgsConstructor
public class PartyFightButton extends Button {
    private final Party party;
    private final EventType eventType;

    @Override
    public ItemStack getButtonItem(Player player) {

        if (eventType.equals(EventType.TEAM)) {
            return new ItemBuilder(MenusLocale.PARTY_EVENTS_SPLIT_MATERIAL.getString())
                    .name(MenusLocale.PARTY_EVENTS_SPLIT_TITLE.getString())
                    .lore(MenusLocale.PARTY_EVENTS_SPLIT_LORE.getStringList(), player)
                    .clearFlags()
                    .build();
        } else {
            return new ItemBuilder(MenusLocale.PARTY_EVENTS_FFA_MATERIAL.getString())
                    .name(MenusLocale.PARTY_EVENTS_FFA_TITLE.getString())
                    .lore(MenusLocale.PARTY_EVENTS_FFA_LORE.getStringList(), player)
                    .clearFlags()
                    .build();
        }
    }

    public void onClick(Player player, ClickType clickType) {
        new PartyEventsKitMenu(party, eventType).openMenu(player.getUniqueId());
    }
}
