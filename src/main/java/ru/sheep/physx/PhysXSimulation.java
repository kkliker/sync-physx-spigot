package ru.sheep.physx;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryStack;
import physx.PxTopLevelFunctions;
import physx.common.*;
import physx.geometry.PxBoxGeometry;
import physx.physics.*;
import ru.sheep.Main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PhysXSimulation {

    @Getter
    private final @NotNull Set<PhysXBlock> items = new HashSet<>();
    @Getter
    private final @NotNull Map<Vector, PxRigidStatic> vec_staticbodies = new HashMap<>(); // vec : body
    @Getter
    private final PxScene scene;
    @Getter
    private final PxPhysics physics;


    public PhysXSimulation() {

        int version = PxTopLevelFunctions.getPHYSICS_VERSION();

        // create PhysX foundation object
        PxDefaultAllocator allocator = new PxDefaultAllocator();
        PxDefaultErrorCallback errorCb = new PxDefaultErrorCallback();
        PxFoundation foundation = PxTopLevelFunctions.CreateFoundation(version, allocator, errorCb);

        // create PhysX main physics object
        PxTolerancesScale tolerances = new PxTolerancesScale();
        this.physics = PxTopLevelFunctions.CreatePhysics(version, foundation, tolerances);
        this.scene = PhysXUtils.createScene(this);

        // clean up
        tolerances.destroy();
    }

    public PxRigidDynamic spawnRigidBody(Vector pos, Vector size, int mass){

        try (MemoryStack mem = MemoryStack.stackPush()) {

            PxFilterData tmpFilterData = PxFilterData.createAt(mem,MemoryStack::nmalloc,1,1,0,0);
            PxShapeFlags shapeFlags = PxShapeFlags.createAt(mem,MemoryStack::nmalloc,(byte) (PxShapeFlagEnum.eSCENE_QUERY_SHAPE.value | PxShapeFlagEnum.eSIMULATION_SHAPE.value));
            PxMaterial material = physics.createMaterial(0.5f, 0.5f, 0.5f);

            PxVec3 tmpVec = PxVec3.createAt(mem, MemoryStack::nmalloc, (float) pos.getX(),(float) pos.getY(),(float) pos.getZ());
            PxTransform tmpPose = PxTransform.createAt(mem, MemoryStack::nmalloc, PxIDENTITYEnum.PxIdentity);

            tmpPose.setP(tmpVec);

            PxBoxGeometry boxGeometry = PxBoxGeometry.createAt(mem,MemoryStack::nmalloc,(float) size.getX(),(float) size.getY(),(float) size.getZ());
            PxShape boxShape = physics.createShape(boxGeometry, material, true, shapeFlags);

            boxShape.setSimulationFilterData(tmpFilterData);

            PxRigidDynamic box = physics.createRigidDynamic(tmpPose);
            box.getGlobalPose().setP(tmpVec);

            box.attachShape(boxShape);
            box.setMass(mass);

            scene.addActor(box);


            return box;
        }
    }


    public PxRigidStatic spawnStaticCube(Vector size, Vector pos) {

        try(MemoryStack mem = MemoryStack.stackPush()) {

            PxFilterData tmpFilterData = PxFilterData.createAt(mem,MemoryStack::nmalloc,1,1,0,0);
            PxTransform tmpPose = PxTransform.createAt(mem, MemoryStack::nmalloc, PxIDENTITYEnum.PxIdentity);
            PxVec3 tmpVec = PxVec3.createAt(mem, MemoryStack::nmalloc, (float) pos.getX(),(float) pos.getY(),(float) pos.getZ());
            PxMaterial material = physics.createMaterial(0.5f, 0.5f, 0.5f);
            PxShapeFlags shapeFlags = PxShapeFlags.createAt(mem,MemoryStack::nmalloc,(byte) (PxShapeFlagEnum.eSCENE_QUERY_SHAPE.value | PxShapeFlagEnum.eSIMULATION_SHAPE.value));
            PxBoxGeometry groundGeometry = PxBoxGeometry.createAt(mem,MemoryStack::nmalloc,(float) size.getX(),(float) size.getY(),(float) size.getZ());

            tmpPose.setP(tmpVec);

            PxShape groundShape = physics.createShape(groundGeometry, material, true, shapeFlags);
            PxRigidStatic ground = physics.createRigidStatic(tmpPose);
            groundShape.setSimulationFilterData(tmpFilterData);
            ground.attachShape(groundShape);

            synchronized (scene) {
                scene.addActor(ground);
            }

            vec_staticbodies.put(pos, ground);

            return ground;
        }
    }

    public void unregister(PxRigidStatic actor){
        scene.removeActor(actor);
        actor.release();
    }

    public void update(float delta) {

            scene.simulate(delta);
            scene.fetchResults(true);


            new HashSet<>(items).forEach(i ->{
                i.update(this);
            });
    }

}
