package combat.attack

import combat.DamageType
import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.stat.AGILITY
import kotlin.math.max

class StartAttackEvent(val source: Target, val sourcePart: BodyPart, val target: TargetAim, val type: DamageType, timeLeft: Int = -1) : Event, BattleAction {

    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            val weaponSize = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: 1
            val weaponWeight = sourcePart.getEquippedWeapon()?.getWeight() ?: 1
            val encumbrance = source.getEncumbrance()
            val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())
            val weaponScaleFactor = 10

            max(1, weaponSize * weaponWeight * weaponScaleFactor / agility)
        }
    }

    override fun getActionEvent(): AttackEvent {
        return AttackEvent(source, sourcePart, target, type)
    }
}
