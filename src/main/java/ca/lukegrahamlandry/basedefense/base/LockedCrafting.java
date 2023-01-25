package ca.lukegrahamlandry.basedefense.base;

import ca.lukegrahamlandry.basedefense.base.teams.Team;
import ca.lukegrahamlandry.basedefense.base.teams.TeamManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

public class LockedCrafting {
    public static boolean allow(Player player, CraftingContainer craftingInputItems) {
        Optional<CraftingRecipe> optional = player.level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInputItems, player.level);
        if (optional.isEmpty()) return true;
        ItemStack result = optional.get().assemble(craftingInputItems);

        Team team = TeamManager.get(player);
        // TODO: this is just for testing since i haven't implemented base upgrading yet
        int tier = player.experienceLevel;  // team.getBaseTier()
        BaseTier base = BaseTier.get(tier);

        return base.canCraft(result);
    }
}
