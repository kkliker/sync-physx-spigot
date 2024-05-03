package ru.sheep.physx;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;
import org.lwjgl.system.MemoryStack;
import physx.common.PxIDENTITYEnum;
import physx.common.PxTransform;
import physx.common.PxVec3;
import physx.physics.PxActorFlagEnum;
import physx.physics.PxRigidDynamic;
import ru.sheep.Main;

import java.util.*;

public class PlayerBodyHandler implements Listener {

    @Getter
    private static final Map<UUID, PxRigidDynamic> player_body_map = new HashMap<>();

    public static void enable(){
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerBodyHandler(), Main.getJavaPlugin());
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        final Player p =  event.getPlayer();

        var box = player_body_map.get(p.getUniqueId());
        if(box != null) return;

        var pos = p.getLocation();
        var boxnew = Main.getSimulation().spawnRigidBody(pos.toVector(),new Vector(p.getBoundingBox().getWidthX() / 2,p.getEyeHeight(),p.getBoundingBox().getWidthZ() / 2),1);
        boxnew.getActorFlags().raise(PxActorFlagEnum.eDISABLE_GRAVITY);
        player_body_map.put(p.getUniqueId(), boxnew);
    }


    public void update_player_bodies(PhysXSimulation simulation){
        List<UUID> toremove = new ArrayList<>();
        player_body_map.forEach((plasq,body) ->{

            var player = Bukkit.getPlayer(plasq);

            if (player == null || !player.isOnline()){
                simulation.getScene().removeActor(body);
                body.release();
                toremove.add(plasq);
                return;
            }

            var p1 = player.getLocation();
            try(MemoryStack mem = MemoryStack.stackPush()){
                var pos = new Vector(p1.getX(),p1.getY() + player.getBoundingBox().getHeight() / 2,p1.getZ());
                PxVec3 tmpVec = PxVec3.createAt(mem, MemoryStack::nmalloc, (float) pos.getX(),(float) pos.getY(),(float) pos.getZ());
                PxTransform tmpPose = PxTransform.createAt(mem, MemoryStack::nmalloc, PxIDENTITYEnum.PxIdentity);
                tmpPose.setP(tmpVec);
                body.setGlobalPose(tmpPose);
            }

        });
        toremove.forEach(player_body_map::remove);
    }
}
