package ru.sheep.physx;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import physx.common.PxTransform;
import physx.physics.PxRigidDynamic;
import ru.sheep.Main;

public class PhysXBlock {

    private final PhysXSimulation simulation;
    @Getter
    private final ItemDisplay entity;
    @Getter
    private final PxRigidDynamic body;

    protected PhysXBlock(PhysXSimulation simulation, ItemDisplay entity, PxRigidDynamic body) {
        this.simulation = simulation;
        this.entity = entity;
        this.body = body;
    }

    public static PhysXBlock spawnAt(PhysXSimulation simulation, Location location,Material material){

        ItemDisplay display;
        PxRigidDynamic body;

        display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        body = simulation.spawnRigidBody(location.toVector(),new Vector(0.5,0.5,0.5),1);

        display.setInterpolationDuration(1);
        display.setInterpolationDelay(-1);

        display.setGravity(false);
        display.setItemStack(new ItemStack(material));

        var cube = new PhysXBlock(simulation,display,body);

        simulation.getItems().add(cube);
        return cube;
    }

    public void destroy(){
        System.out.println("DESTROYED");
        simulation.getItems().remove(this);
        simulation.getScene().removeActor(body);
        body.release();
        entity.remove();
    }

    public void update(PhysXSimulation simulation){

        var current_chunk = entity.getLocation().getChunk();
        if (!current_chunk.isLoaded()) {
            destroy();
            return;
        }

        final PxTransform pose;
        final boolean sleeping;

        var scene = simulation.getScene();
        synchronized (scene) {
            sleeping = body.isSleeping();
            pose = body.getGlobalPose();
        }

        if (sleeping) return;

        var t = new Transformation(
                new Vector3f(0.0f,0.0f,0.0f),
                PhysXUtils.toQuaternionf(pose.getQ()),
                new Vector3f(1.0f,1.0f,1.0f),
                new Quaternionf(0.0,0.0,0.0,1.0));
        var poss = pose.getP();
        var i = entity.getWorld();
        entity.setTransformation(t);
        entity.teleport(new Location(i,poss.getX(),poss.getY(),poss.getZ()));

        var pos = entity.getLocation();

        // parsing world around to physics
        for (int x = -1; x <= 1; x++){
                for (int y = -1; y <= 1 ; y++) {
                    for (int z = -1; z <= 1; z++) {

                        if (!entity.getLocation().getChunk().isLoaded()) return;
                        var processed = new Location(i,pos.getBlockX(),pos.getBlockY(),pos.getBlockZ()).add(new Vector(x,y,z));
                        var pros2 = processed.add(new Vector(0.5,0.5,0.5));
                        if (processed.getBlock().getType().isSolid()){
                            if (simulation.getVec_staticbodies().containsKey(pros2.toVector())){
                                continue;
                            }
                            simulation.spawnStaticCube(new Vector(0.5,0.5,0.5),pros2.toVector());
                        }

                    }
                }
            }

    }
}
