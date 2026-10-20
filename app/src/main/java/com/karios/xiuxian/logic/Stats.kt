package com.karios.xiuxian.logic

import com.karios.xiuxian.data.REALMS
import com.karios.xiuxian.data.RealmDef
import com.karios.xiuxian.data.Items
import com.karios.xiuxian.data.Techniques
import com.karios.xiuxian.model.Player

val Player.realmDef: RealmDef
    get() = REALMS[realmIndex.coerceIn(0, REALMS.lastIndex)]

val Player.subName: String
    get() = realmDef.subs.getOrNull(subIndex) ?: realmDef.subs.last()

val Player.realmText: String
    get() = realmDef.name + subName

val Player.isMaxRealm: Boolean
    get() = realmIndex >= REALMS.lastIndex && subIndex >= REALMS.lastIndex

/** 当前年龄（年） */
val Player.ageYears: Int
    get() = 16 + ageDays / 365

/** 寿元上限（年） */
val Player.lifeYears: Int
    get() = realmDef.lifeYears

val Player.mainTech: com.karios.xiuxian.data.TechniqueDef?
    get() = Techniques.get(mainTechnique)

fun Player.expNeeded(): Long = realmDef.expBase + realmDef.expStep * subIndex

fun Player.caveMul(): Double = 1.0 + 0.1 * (caveLevel - 1)

/** 综合修炼倍率 */
fun Player.expMul(): Double {
    val t = mainTech
    return roots.speedMul * (t?.expMul ?: 1.0) * caveMul()
}

/** 单次静修的修为收益 */
fun Player.cultivateGain(): Double {
    val r = realmDef
    val base = r.gainBase + r.gainStep * subIndex
    val demonPenalty = 1.0 - demonHeart / 400.0
    return (base * expMul() * demonPenalty).coerceAtLeast(1.0)
}

private fun Player.subScale(): Double = 1.0 + 0.18 * subIndex

fun Player.maxHp(): Int {
    val r = realmDef
    val t = mainTech
    val base = r.baseHp * subScale()
    val mul = 1.0 + (t?.hpPct ?: 0.0)
    val art = Items.get(artifact)?.hpBonus ?: 0
    return (base * mul).toInt().coerceAtLeast(1) + art
}

fun Player.maxMp(): Int {
    val r = realmDef
    val t = mainTech
    val base = r.baseMp * subScale()
    val mul = 1.0 + (t?.mpPct ?: 0.0)
    val art = Items.get(artifact)?.mpBonus ?: 0
    return (base * mul).toInt().coerceAtLeast(1) + art
}

fun Player.atk(): Int {
    val r = realmDef
    val t = mainTech
    val base = r.baseAtk * subScale()
    val mul = 1.0 + (t?.atkPct ?: 0.0)
    val art = Items.get(artifact)?.atkBonus ?: 0
    return (base * mul).toInt().coerceAtLeast(1) + art
}

fun Player.def(): Int {
    val r = realmDef
    val t = mainTech
    val base = r.baseDef * subScale()
    val mul = 1.0 + (t?.defPct ?: 0.0)
    val art = Items.get(artifact)?.defBonus ?: 0
    return (base * mul).toInt().coerceAtLeast(0) + art
}

fun Player.spd(): Int {
    val r = realmDef
    val art = Items.get(artifact)?.spdBonus ?: 0
    return (r.baseSpd * subScale()).toInt() + art
}

/** 突破成功率：大境界吃基础值，小境界较高；心魔重压，失败有累积 */
fun Player.breakChanceFor(isBigRealm: Boolean): Int {
    val base = if (isBigRealm) realmDef.breakChance else 85
    var c = base + (comprehension * 1.5).toInt() - demonHeart / 6 + breakBonus + failBonus
    c = c.coerceIn(3, 99)
    return c
}

/** 物品是否可用（法宝/功法书判定在引擎里） */
fun Player.hasItem(id: String): Boolean = (inventory[id] ?: 0) > 0

fun Player.itemCount(id: String): Int = inventory[id] ?: 0