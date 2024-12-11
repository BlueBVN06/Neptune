package dev.lrxh.neptune.profile.data;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.divisions.impl.Division;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KitData {
    private int wins = 0;
    private int losses = 0;
    private int bestStreak = 0;
    private int currentStreak = 0;
    private List<ItemStack> kitLoadout = new ArrayList<>();
    private Division division;
    private Neptune plugin;

    public KitData(Neptune plugin) {
        this.plugin = plugin;
    }

    public double getKdr() {
        if (losses == 0) return wins;
        double kd = (double) wins / losses;
        BigDecimal bd = new BigDecimal(kd);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateDivision() {
        division = plugin.getDivisionManager().getDivisionByWinCount(wins);
    }
}

