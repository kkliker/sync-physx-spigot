package ru.sheep.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.sheep.Main;
import ru.sheep.physx.PhysXUtils;


public class PlayerBlockBreakListener implements Listener {

    public static void register(){
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerBlockBreakListener(), Main.getJavaPlugin());
    }
    @EventHandler
    public void playerBlockPlaceEvent(BlockBreakEvent event){

        var pos = event.getBlock().getLocation().add(0.5,0.5,0.5);

        var simulation = Main.getSimulation();
        var item = simulation.getVec_staticbodies().get(pos.toVector());
        if (item == null) return;

        simulation.unregister(item);

    }
}
