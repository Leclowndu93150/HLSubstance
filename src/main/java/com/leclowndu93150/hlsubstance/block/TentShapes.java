package com.leclowndu93150.hlsubstance.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

final class TentShapes {

    static final VoxelShape MAT = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    private static final VoxelShape MAT_FULL = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    private static final VoxelShape POST_BACK = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 2.0);
    private static final VoxelShape RIDGE_FOOT = Block.box(7.0, 14.0, 2.0, 9.0, 16.0, 16.0);

    private static final VoxelShape TARP_RIGHT_FOOT = union(
            Block.box(12.0, 0.0, 2.0, 15.0, 4.0, 16.0),
            Block.box(10.0, 4.0, 2.0, 13.0, 8.0, 16.0),
            Block.box(8.0, 8.0, 2.0, 11.0, 12.0, 16.0),
            Block.box(6.0, 12.0, 2.0, 9.0, 16.0, 16.0)
    );
    private static final VoxelShape TARP_LEFT_FOOT = union(
            Block.box(1.0, 0.0, 2.0, 4.0, 4.0, 16.0),
            Block.box(3.0, 4.0, 2.0, 6.0, 8.0, 16.0),
            Block.box(5.0, 8.0, 2.0, 8.0, 12.0, 16.0),
            Block.box(7.0, 12.0, 2.0, 10.0, 16.0, 16.0)
    );

    private static final VoxelShape POST_FRONT = Block.box(7.0, 0.0, 14.0, 9.0, 16.0, 16.0);
    private static final VoxelShape RIDGE_HEAD = Block.box(7.0, 14.0, 0.0, 9.0, 16.0, 14.0);

    private static final VoxelShape TARP_RIGHT_HEAD = union(
            Block.box(12.0, 0.0, 0.0, 15.0, 4.0, 14.0),
            Block.box(10.0, 4.0, 0.0, 13.0, 8.0, 14.0),
            Block.box(8.0, 8.0, 0.0, 11.0, 12.0, 14.0),
            Block.box(6.0, 12.0, 0.0, 9.0, 16.0, 14.0)
    );
    private static final VoxelShape TARP_LEFT_HEAD = union(
            Block.box(1.0, 0.0, 0.0, 4.0, 4.0, 14.0),
            Block.box(3.0, 4.0, 0.0, 6.0, 8.0, 14.0),
            Block.box(5.0, 8.0, 0.0, 8.0, 12.0, 14.0),
            Block.box(7.0, 12.0, 0.0, 10.0, 16.0, 14.0)
    );

    private static final VoxelShape FOOT_SOUTH = union(MAT_FULL, POST_BACK, RIDGE_FOOT, TARP_RIGHT_FOOT, TARP_LEFT_FOOT);
    private static final VoxelShape HEAD_SOUTH = union(MAT_FULL, POST_FRONT, RIDGE_HEAD, TARP_RIGHT_HEAD, TARP_LEFT_HEAD);

    private static final VoxelShape SUPPORT_BACK_POST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    private static final VoxelShape SUPPORT_FRONT_POST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    private static final VoxelShape SUPPORT_RIDGE_FOOT = Block.box(7.0, 14.0, 7.0, 9.0, 16.0, 16.0);
    private static final VoxelShape SUPPORT_RIDGE_HEAD = Block.box(7.0, 14.0, 0.0, 9.0, 16.0, 9.0);

    private static final VoxelShape FOOT_SUPPORT_SOUTH = union(SUPPORT_BACK_POST, SUPPORT_RIDGE_FOOT);
    private static final VoxelShape HEAD_SUPPORT_SOUTH = union(SUPPORT_FRONT_POST, SUPPORT_RIDGE_HEAD);

    private static final Map<Direction, VoxelShape> FOOT_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> HEAD_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> FOOT_SUPPORT_SHAPES = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> HEAD_SUPPORT_SHAPES = new EnumMap<>(Direction.class);

    static {
        FOOT_SHAPES.put(Direction.SOUTH, FOOT_SOUTH);
        FOOT_SHAPES.put(Direction.WEST, rotateY(FOOT_SOUTH, 1));
        FOOT_SHAPES.put(Direction.NORTH, rotateY(FOOT_SOUTH, 2));
        FOOT_SHAPES.put(Direction.EAST, rotateY(FOOT_SOUTH, 3));

        HEAD_SHAPES.put(Direction.SOUTH, HEAD_SOUTH);
        HEAD_SHAPES.put(Direction.WEST, rotateY(HEAD_SOUTH, 1));
        HEAD_SHAPES.put(Direction.NORTH, rotateY(HEAD_SOUTH, 2));
        HEAD_SHAPES.put(Direction.EAST, rotateY(HEAD_SOUTH, 3));

        FOOT_SUPPORT_SHAPES.put(Direction.SOUTH, FOOT_SUPPORT_SOUTH);
        FOOT_SUPPORT_SHAPES.put(Direction.WEST, rotateY(FOOT_SUPPORT_SOUTH, 1));
        FOOT_SUPPORT_SHAPES.put(Direction.NORTH, rotateY(FOOT_SUPPORT_SOUTH, 2));
        FOOT_SUPPORT_SHAPES.put(Direction.EAST, rotateY(FOOT_SUPPORT_SOUTH, 3));

        HEAD_SUPPORT_SHAPES.put(Direction.SOUTH, HEAD_SUPPORT_SOUTH);
        HEAD_SUPPORT_SHAPES.put(Direction.WEST, rotateY(HEAD_SUPPORT_SOUTH, 1));
        HEAD_SUPPORT_SHAPES.put(Direction.NORTH, rotateY(HEAD_SUPPORT_SOUTH, 2));
        HEAD_SUPPORT_SHAPES.put(Direction.EAST, rotateY(HEAD_SUPPORT_SOUTH, 3));
    }

    private TentShapes() {}

    static VoxelShape get(BedPart part, Direction facing) {
        Direction key = facing.getAxis().isHorizontal() ? facing : Direction.SOUTH;
        return part == BedPart.HEAD ? HEAD_SHAPES.get(key) : FOOT_SHAPES.get(key);
    }

    static VoxelShape getSupport(BedPart part, Direction facing) {
        Direction key = facing.getAxis().isHorizontal() ? facing : Direction.SOUTH;
        return part == BedPart.HEAD ? HEAD_SUPPORT_SHAPES.get(key) : FOOT_SUPPORT_SHAPES.get(key);
    }

    private static VoxelShape union(VoxelShape first, VoxelShape... rest) {
        VoxelShape out = first;
        for (VoxelShape shape : rest) {
            out = Shapes.joinUnoptimized(out, shape, BooleanOp.OR);
        }
        return out.optimize();
    }

    private static VoxelShape rotateY(VoxelShape shape, int quarterTurns) {
        int turns = ((quarterTurns % 4) + 4) % 4;
        VoxelShape current = shape;
        for (int i = 0; i < turns; i++) {
            VoxelShape[] rotated = {Shapes.empty()};
            current.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                double nMinX = 1.0 - maxZ;
                double nMaxX = 1.0 - minZ;
                double nMinZ = minX;
                double nMaxZ = maxX;
                rotated[0] = Shapes.joinUnoptimized(rotated[0],
                        Shapes.box(nMinX, minY, nMinZ, nMaxX, maxY, nMaxZ), BooleanOp.OR);
            });
            current = rotated[0].optimize();
        }
        return current;
    }
}
