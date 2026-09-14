package io.github.mofish.archaeologyultimine

import dev.ftb.mods.ftbultimine.api.blockselection.RegisterBlockSelectionHandlerEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge

@Mod(ArchaeologyUltimine.ID)
class ArchaeologyUltimine {
    companion object {
        const val ID = "archaeologyultimine"
    }

    init {
        RegisterBlockSelectionHandlerEvent.REGISTER.register { it.registerHandler(ArchaeologySelection) }
        NeoForge.EVENT_BUS.addListener(ArchaeologyBrushUse::onPlayerTick)
    }
}