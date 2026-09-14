package io.github.mofish.archaeologyultimine

import dev.ftb.mods.ftbultimine.api.blockselection.BlockSelectionHandler
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BrushItem
import net.minecraft.world.level.block.BrushableBlock
import net.minecraft.world.level.block.state.BlockState

object ArchaeologySelection : BlockSelectionHandler {
    override fun customSelectionCheck(
        player: Player,
        origPos: BlockPos,
        pos: BlockPos,
        origState: BlockState,
        state: BlockState
    ): BlockSelectionHandler.Result {
        if (player.mainHandItem.item !is BrushItem && player.offhandItem.item !is BrushItem) {
            return BlockSelectionHandler.Result.PASS
        }

        if (origState.block !is BrushableBlock) {
            return BlockSelectionHandler.Result.PASS
        }

        return BlockSelectionHandler.Result.of(state.block is BrushableBlock)
    }
}