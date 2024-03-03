package safro.auroral.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import safro.auroral.Auroral;
import safro.auroral.block.entity.FusionAltarBlockEntity;
import safro.auroral.registry.BlockRegistry;
import safro.auroral.util.BlockUtil;

public class FusionAltarBlock extends BlockWithEntity {

    public FusionAltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient() && hand == Hand.MAIN_HAND && world.getBlockEntity(pos) instanceof FusionAltarBlockEntity be && !be.isCrafting()) {
            ItemStack stack = player.getStackInHand(hand);
            if (!be.getStack(2).isEmpty()) {
                BlockUtil.removeStack(be, player, 2);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                be.update();
                return ActionResult.SUCCESS;
            } else if (be.getStack(0).isEmpty() && !stack.isEmpty()) {
                be.setStack(0, stack.copyWithCount(1));
                if (!player.getAbilities().creativeMode) {
                    player.getStackInHand(hand).decrement(1);
                }
                be.update();
                return ActionResult.SUCCESS;
            } else if (be.getStack(1).isEmpty() && !stack.isEmpty()) {
                be.setStack(1, stack.copyWithCount(1));
                if (!player.getAbilities().creativeMode) {
                    player.getStackInHand(hand).decrement(1);
                }
                be.update();
                return ActionResult.SUCCESS;
            } else if (stack.isEmpty()) {
                if (!be.getStack(0).isEmpty()) {
                    BlockUtil.removeStack(be, player, 0);
                    be.update();
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                } else if (!be.getStack(1).isEmpty()) {
                    BlockUtil.removeStack(be, player, 1);
                    be.update();
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FusionAltarBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? null : checkType(type, BlockRegistry.FUSION_ALTAR_BE, FusionAltarBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HorizontalFacingBlock.FACING);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof FusionAltarBlockEntity be) {
                ItemScatterer.spawn(world, pos, be);
                be.clear();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(HorizontalFacingBlock.FACING, rotation.rotate(state.get(HorizontalFacingBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(HorizontalFacingBlock.FACING)));
    }
}
