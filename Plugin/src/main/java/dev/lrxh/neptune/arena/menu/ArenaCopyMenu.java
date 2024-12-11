package dev.lrxh.neptune.arena.menu;

import dev.lrxh.neptune.arena.impl.StandAloneArena;
import dev.lrxh.neptune.arena.menu.button.ArenaCopyButton;
import dev.lrxh.neptune.arena.menu.button.ArenaCopyCreateButton;
import dev.lrxh.neptune.utils.menu.Button;
import dev.lrxh.neptune.utils.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ArenaCopyMenu extends Menu {
    private final StandAloneArena arena;

    @Override
    public String getTitle(Player player) {
        return "&7" + arena.getName() + " Copy Management";
    }

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public boolean isUpdateOnClick() {
        return true;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;

        if (!(arena.getCopies().isEmpty())) {
            for (StandAloneArena arena : arena.getCopies()) {
                buttons.put(i++, new ArenaCopyButton(arena, i));
            }
        }
        if (i <= 8) {
            buttons.put(i, new ArenaCopyCreateButton(arena));
        }
        return buttons;
    }
}
