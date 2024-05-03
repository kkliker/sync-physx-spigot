package ru.sheep.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sheep.Main;
import ru.sheep.physx.PhysXBlock;


@CommandAlias("physx")
public class SpawnCommand extends BaseCommand {

    @Subcommand("spawnBlock")
    @CommandCompletion("@range:1-9999")
    public void onSpawnBlockCommand(CommandSender p, String[] args) {

        Player player = (Player) p;
        Integer count = Integer.valueOf(args[0]);
        for (int i = 0; i < count; i++) {
            PhysXBlock.spawnAt(Main.getSimulation(),player.getLocation(), Material.DIAMOND_BLOCK);
        }

    }

    @Subcommand("spawnPlatform")
    public void onSpawnPlatformCommand(CommandSender p, String[] args) {

        Player player = (Player) p;
        var length = 5;
        var pos = player.getLocation();

        for (int i = 1; i < length; i++) {
            PhysXBlock.spawnAt(Main.getSimulation(),pos.clone().add(i,0,0),Material.DIAMOND_BLOCK);
        }


    }

}
