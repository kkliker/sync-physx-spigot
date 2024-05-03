package ru.sheep.physx;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.lwjgl.system.MemoryStack;
import physx.PxTopLevelFunctions;
import physx.common.PxDefaultCpuDispatcher;
import physx.common.PxQuat;
import physx.common.PxVec3;
import physx.physics.PxPruningStructureTypeEnum;
import physx.physics.PxScene;
import physx.physics.PxSceneDesc;
import ru.sheep.Main;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PhysXUtils {

    public static PxScene createScene(PhysXSimulation simulation) {

        PxDefaultCpuDispatcher cpuDispatcher = PxTopLevelFunctions.DefaultCpuDispatcherCreate(16);

        PxSceneDesc sceneDesc = new PxSceneDesc(simulation.getPhysics().getTolerancesScale());
        sceneDesc.setGravity(new PxVec3(0f, -9.81f, 0f));
        sceneDesc.setCpuDispatcher(cpuDispatcher);
        sceneDesc.setFilterShader(PxTopLevelFunctions.DefaultFilterShader());
        //sceneDesc.setCudaContextManager(cudaMgr);
        sceneDesc.setStaticStructure(PxPruningStructureTypeEnum.eDYNAMIC_AABB_TREE);
        //sceneDesc.getFlags().raise(PxSceneFlagEnum.eENABLE_PCM);
        //sceneDesc.getFlags().raise(PxSceneFlagEnum.eENABLE_GPU_DYNAMICS);
        //sceneDesc.setBroadPhaseType(PxBroadPhaseTypeEnum.eGPU);
        //sceneDesc.setSolverType(PxSolverTypeEnum.eTGS);
        return simulation.getPhysics().createScene(sceneDesc);
    }

    public static void boom(PhysXSimulation simulation, Location tntPos){

            // Calculus from https://github.com/emortaldev/BlockPhysics/blob/physx/src/main/java/dev/emortal/Main.java

            Random rand = new Random();

            tntPos.getBlock().setType(Material.AIR);

            /* Крашает код дальше. Если хотите, найдите ошибку, но мои полномочия здесь окончены
            for (PhysXBlock cube : simulation.getItems()) {

                var pos = cube.getEntity().getLocation();
                if (pos.distanceSquared(tntPos.add(0.5,0.5,0.5)) > 5*5) continue;
                Vector velocity = pos.clone().subtract(tntPos.add(0.5,0.5,0.5)).toVector().normalize().multiply(new Vector(4, 8, 4)).multiply(rand.nextDouble(1, 2.5));
                cube.getBody().setLinearVelocity(toPxVec(velocity));
                cube.getBody().setAngularVelocity(toPxVec(velocity));
            }
             */

            final var radius = 5;
            for (double x = -radius; x <= radius; x++) {
                for (double y = -radius; y <= radius; y++) {
                    for (double z = -radius; z <= radius; z++) {
                        if ((x * x) + (y * y) + (z * z) > radius * radius) continue;


                        var loc = tntPos.clone().add(x,y,z);
                        var block = loc.getBlock();

                        if (block.getType().isAir() || block.getType() == Material.GRASS_BLOCK) continue;

                        Vector velocity = loc.clone().subtract(tntPos.clone().add(0.5,0.5,0.5)).toVector().normalize().multiply(new Vector(4, 8, 4)).multiply(rand.nextDouble(1, 2.5));
                        var cube = PhysXBlock.spawnAt(simulation,loc,block.getType());
                        cube.getBody().setLinearVelocity(toPxVec(velocity));
                        cube.getBody().setAngularVelocity(toPxVec(velocity));

                        block.setType(Material.AIR);
                    }
                }
            }

    }

    public static PxVec3 toPxVec(Vector vector){
        try(MemoryStack mem = MemoryStack.stackPush()){
            return PxVec3.createAt(mem,MemoryStack::nmalloc,(float) vector.getX(),(float) vector.getY(),(float) vector.getZ());
        }
    }

    public static Vector toVec(PxVec3 pxVec) {
        return new Vector(pxVec.getX(), pxVec.getY(), pxVec.getZ());
    }

    public static Quaternionf toQuaternionf(PxQuat quat){
        return  new Quaternionf(quat.getX(),quat.getY(),quat.getZ(),quat.getW());
    }

    public static float[] toFloats(PxQuat pxQuat) {
        return new float[]{pxQuat.getX(), pxQuat.getY(), pxQuat.getZ(), pxQuat.getW()};
    }

}
