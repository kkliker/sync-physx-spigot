package ru.sheep.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import ru.sheep.Main;
import ru.sheep.physx.PhysXBlock;
@CommandAlias("physx")
public class ClearDisplays extends BaseCommand {

    @Subcommand("clearStoneDisplays")
    public void onClearCommand(CommandSender p, String[] args) {

        Player player = (Player) p;
        player.getWorld().getEntities().forEach(e ->{
            if (!(e instanceof ItemDisplay display)) return;
            if (display.getItemStack() == null) return;
            if (display.getItemStack().getType() == Material.DIAMOND_BLOCK){
                display.remove();
            }

        });

    }
}
