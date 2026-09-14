package io.github.mofish.archaeologyultimine

import dev.ftb.mods.ftbultimine.FTBUltiminePlayerData
import dev.ftb.mods.ftbultimine.api.FTBUltimineAPI
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.BrushItem
import net.minecraft.world.level.block.BrushableBlock
import net.minecraft.world.level.block.entity.BrushableBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.event.tick.PlayerTickEvent

object ArchaeologyBrushUse {
    fun onPlayerTick(event: PlayerTickEvent.Pre) {
        val player = event.entity as? ServerPlayer ?: return
        if (!player.isUsingItem) {
            return
        }

        val hand = player.usedItemHand
        val brush = player.getItemInHand(hand)

        if (brush.item !is BrushItem || player.useItemRemainingTicks <= 0) {
            return
        }

        val usedTicks = brush.item.getUseDuration(brush, player) - player.useItemRemainingTicks + 1
        if (usedTicks % 10 != 5) {
            return
        }

        val hitResult = FTBUltiminePlayerData.rayTrace(player) as? BlockHitResult ?: return
        val positions = FTBUltimineAPI.api().currentBlockSelection(player).orElse(null) ?: return
        val level = player.level()
        val equipmentSlot = if (hand == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND

        for (pos in positions) {
            if (brush.isEmpty) {
                break
            }

            if (pos == hitResult.blockPos) {
                continue
            }

            val state = level.getBlockState(pos)
            val brushableBlock = state.block as? BrushableBlock ?: continue
            val blockEntity = level.getBlockEntity(pos) as? BrushableBlockEntity ?: continue

            level.playSound(null, pos, brushableBlock.brushSound, SoundSource.BLOCKS)
            val completed = blockEntity.brush(level.gameTime, player, hitResult.direction)

            if (completed && !player.isCreative) {
                brush.hurtAndBreak(1, player, equipmentSlot)
            }
        }
    }
}