package ru.sheep;

import co.aikar.commands.BukkitCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.sheep.commands.BoomCommand;
import ru.sheep.commands.ClearDisplays;
import ru.sheep.commands.SpawnCommand;
import ru.sheep.events.PlayerBlockBreakListener;
import ru.sheep.events.PlayerTntPlaceListener;
import ru.sheep.physx.PhysXSimulation;
import ru.sheep.physx.PlayerBodyHandler;

public class Main extends JavaPlugin {

    @Getter
    private static PhysXSimulation simulation;
    @Getter
    private static JavaPlugin javaPlugin;
    private static Integer task = null;

    @Override
    public void onEnable() {
        super.onEnable();

        javaPlugin = this;
        simulation = new PhysXSimulation();

        PlayerBodyHandler.enable();
        var bodyhandler = new PlayerBodyHandler();

        // simulation scheduler
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,() -> {
            simulation.update(1f / (float) 20);
            bodyhandler.update_player_bodies(simulation);
        },0,1);

        var cmds = new BukkitCommandManager(this);
        cmds.registerCommand(new SpawnCommand());
        cmds.registerCommand(new ClearDisplays());
        cmds.registerCommand(new BoomCommand());

        PlayerTntPlaceListener.register();
        PlayerBlockBreakListener.register();

    }

    @Override
    public void onDisable() {
        if (task != null && Bukkit.getScheduler().isCurrentlyRunning(task)){
            Bukkit.getScheduler().cancelTask(task);
        }
    }
}
