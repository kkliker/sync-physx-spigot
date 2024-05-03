package ru.sheep.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.sheep.Main;
import ru.sheep.physx.PhysXUtils;

public class PlayerTntPlaceListener  implements Listener {

    public static void register(){
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerTntPlaceListener(),Main.getJavaPlugin());
    }
    @EventHandler
    public void playerBlockPlaceEvent(BlockPlaceEvent event){

        var block = event.getBlock();
        if (block.getType() == Material.TNT) {
            System.out.println("BOOOM");
            PhysXUtils.boom(Main.getSimulation(), event.getBlock().getLocation());
        }
        }
}
