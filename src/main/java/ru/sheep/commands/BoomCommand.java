package ru.sheep.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sheep.Main;
import ru.sheep.physx.PhysXBlock;
import ru.sheep.physx.PhysXUtils;

@CommandAlias("physx")
public class BoomCommand extends BaseCommand {

    @Subcommand("boom")
    public void onBoomCommand(CommandSender p, String[] args) {

        Player player = (Player) p;
        PhysXUtils.boom(Main.getSimulation(),player.getLocation());


    }
}
