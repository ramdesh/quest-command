package status.effects

import core.body.Body
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


fun getPersisted(dataObject: Effect): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["amount"] = dataObject.amount
    data["duration"] = dataObject.duration
    data["originalValue"] = dataObject.originalValue
    data["bodyParts"] = dataObject.bodyPartTargets.map { it.name }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Effect {
    val bodyParts = (data["bodyParts"] as List<String>).map { body.getPart(it) }
    return EffectManager.getEffect(
            data["name"] as String,
            data["amount"] as Int,
            data["duration"] as Int,
            bodyParts
    )
}

class EffectPersister : KSerializer<Effect> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Effect", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Effect) =
        encoder.encodeSerializableValue(EffectP.serializer(), EffectP(value))

    override fun deserialize(decoder: Decoder): Effect =
        decoder.decodeSerializableValue(EffectP.serializer()).parsed()
}

@kotlinx.serialization.Serializable
data class EffectP(
    val base: EffectBase,
    val amount: Int,
    val duration: Int,
    val bodyPartTargets: List<String>
    ){
    constructor(b: Effect): this(b.base, b.amount, b.duration, b.bodyPartTargets.map { it.name })

    fun parsed(): Effect {
        return Effect(base, amount, duration, bodyPartTargetNames = bodyPartTargets)
    }
}