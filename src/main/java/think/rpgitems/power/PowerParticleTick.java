package think.rpgitems.power;

import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import think.rpgitems.I18n;
import think.rpgitems.commands.ArgumentPriority;
import think.rpgitems.commands.Validator;

import think.rpgitems.power.types.PowerTick;

/**
 * Power particletick.
 * <p>
 * When item held in hand, spawn some particles around the user.
 * With the time {@link #interval} given in ticks.
 * </p>
 */
public class PowerParticleTick extends Power implements PowerTick {
    /**
     * Name of particle effect
     */
    @ArgumentPriority(required = true)
    @Validator("acceptableEffect")
    public String effect = "FLAME";
    /**
     * Interval of particle effect
     */
    @ArgumentPriority(1)
    public int interval = 15;
    /**
     * Cost of this power
     */
    public int consumption = 0;

    /**
     * Acceptable effect boolean.
     *
     * @param effect the effect
     * @return the boolean
     */
    public boolean acceptableEffect(String effect) {
        try {
            Effect eff = Effect.valueOf(effect.toUpperCase());
            return eff.getType() == Effect.Type.VISUAL || eff.getType() == Effect.Type.PARTICLE;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public void init(ConfigurationSection s) {
        effect = s.getString("effect", "FLAME");
        interval = s.getInt("interval");
        consumption = s.getInt("consumption", 0);
    }

    @Override
    public void save(ConfigurationSection s) {
        s.set("effect", effect);
        s.set("interval", interval);
        s.set("consumption", consumption);
    }

    @Override
    public String getName() {
        return "particletick";
    }

    @Override
    public String displayText() {
        return I18n.format("power.particletick");
    }

    @Override
    public void tick(Player player, ItemStack stack) {
        if (!checkCooldown(player, interval, false))return;
        if (!item.consumeDurability(stack, consumption)) return;
        if (effect.equalsIgnoreCase("SMOKE")) {
            player.getWorld().playEffect(player.getLocation().add(0, 2, 0), Effect.valueOf(effect), 4);
        } else {
            player.getWorld().playEffect(player.getLocation(), Effect.valueOf(effect), 0);
        }
    }
}
