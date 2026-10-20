package com.karios.xiuxian.logic

import com.karios.xiuxian.data.Enemies
import com.karios.xiuxian.data.EnemyDef
import com.karios.xiuxian.data.Items
import com.karios.xiuxian.data.TechEffect
import com.karios.xiuxian.data.Techniques
import com.karios.xiuxian.model.BattleState
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

// ============ 战斗逻辑（GameViewModel 的扩展） ============

fun GameViewModel.playerAttack() {
    val p = player ?: return
    val b = battle ?: return
    if (b.over) return
    val e = Enemies.get(b.enemyId) ?: return
    val atk = p.atk() * b.atkBuff
    val dmg = max(1, (atk * Random.nextDouble(0.9, 1.1) - e.def * 0.5).toInt())
    b.enemyHp -= dmg
    b.log.add("你抢身而上，一击命中，造成 $dmg 点伤害。")
    if (b.enemyHp <= 0) {
        winBattle(b, e)
        return
    }
    enemyTurn(b, e)
}

fun GameViewModel.playerUseArtifact() {
    val p = player ?: return
    val b = battle ?: return
    if (b.over) return
    if (p.artifact.isEmpty()) {
        b.log.add("你身上并无法宝可用。")
        syncBattle()
        return
    }
    val e = Enemies.get(b.enemyId) ?: return
    val cost = 15
    if (p.mpCur < cost) {
        b.log.add("灵力不足，法宝难以催动。")
        syncBattle()
        return
    }
    p.mpCur -= cost
    val dmg = max(1, (p.atk() * 1.8 * Random.nextDouble(0.9, 1.1) - e.def * 0.5).toInt())
    b.enemyHp -= dmg
    b.log.add("你祭起${Items.name(p.artifact)}，光华一闪，造成 $dmg 点伤害。")
    if (b.enemyHp <= 0) {
        winBattle(b, e)
        return
    }
    enemyTurn(b, e)
}

fun GameViewModel.playerCastSpell(techId: String) {
    val p = player ?: return
    val b = battle ?: return
    if (b.over) return
    val e = Enemies.get(b.enemyId) ?: return
    val t = Techniques.get(techId) ?: return
    if (p.mpCur < t.mpCost) {
        b.log.add("灵力不足，${t.name}施展不出。")
        syncBattle()
        return
    }
    p.mpCur -= t.mpCost
    when (t.effect) {
        TechEffect.HEAL -> {
            val heal = p.maxHp() * 30 / 100
            p.hpCur = min(p.maxHp(), p.hpCur + heal)
            b.log.add("${t.name}生效，回复 $heal 点气血。")
        }
        TechEffect.SHIELD -> {
            val shield = p.maxHp() * 30 / 100
            b.playerShield += shield
            b.log.add("${t.name}浮现，获得 $shield 点护体。")
        }
        TechEffect.DODGE -> {
            b.over = true
            b.fled = true
            b.log.add("${t.name}催动，你身形一晃，遁出百丈。")
            syncBattle()
            return
        }
        TechEffect.WEAKEN -> {
            val dmg = max(1, (p.atk() * t.power * Random.nextDouble(0.9, 1.1) - e.def * 0.5).toInt())
            b.enemyHp -= dmg
            b.enemyAtkDown = (b.enemyAtkDown * 0.8).coerceAtLeast(0.5)
            b.log.add("${t.name}命中，造成 $dmg 点伤害，${e.name}动作一滞。")
        }
        else -> {
            val dmg = max(1, (p.atk() * t.power * Random.nextDouble(0.9, 1.1) - e.def * 0.5).toInt())
            b.enemyHp -= dmg
            b.log.add("你施展${t.name}，造成 $dmg 点伤害。")
        }
    }
    if (b.enemyHp <= 0) {
        winBattle(b, e)
        return
    }
    enemyTurn(b, e)
}

fun GameViewModel.useTalisman(itemId: String) {
    val p = player ?: return
    val b = battle ?: return
    if (b.over) return
    if (p.itemCount(itemId) <= 0) return
    val e = Enemies.get(b.enemyId) ?: return
    when (itemId) {
        "tal_huoqiu" -> {
            val dmg = max(1, (p.atk() * 2.0 * Random.nextDouble(0.9, 1.1) - e.def * 0.3).toInt())
            b.enemyHp -= dmg
            b.log.add("火球符炸开，烈焰吞没${e.name}，造成 $dmg 点伤害。")
        }
        "tal_jingang" -> {
            val shield = p.maxHp() * 40 / 100
            b.playerShield += shield
            b.log.add("金刚符化为金光，获得 $shield 点护体。")
        }
        "tal_shenxing" -> {
            b.over = true
            b.fled = true
            b.log.add("神行符燃尽，你踏风而去，转眼无踪。")
            syncBattle()
            return
        }
        else -> {
            b.log.add("此符在此刻无用。")
            syncBattle()
            return
        }
    }
    removeItem(p, itemId, 1)
    syncPlayer(p)
    if (b.enemyHp <= 0) {
        winBattle(b, e)
        return
    }
    enemyTurn(b, e)
}

fun GameViewModel.playerFlee() {
    val p = player ?: return
    val b = battle ?: return
    if (b.over) return
    val e = Enemies.get(b.enemyId) ?: return
    val chance = (40 + (p.spd() - e.spd) * 2).coerceIn(10, 92)
    if (Random.nextInt(100) < chance) {
        b.over = true
        b.fled = true
        b.log.add("你且战且退，终于脱身。（成功率 $chance%）")
        syncBattle()
    } else {
        b.log.add("逃跑失败，${e.name}扑了上来！")
        enemyTurn(b, e)
    }
}

private fun GameViewModel.enemyTurn(b: BattleState, e: EnemyDef) {
    val p = player ?: return
    b.turn++
    if (b.buffTurns > 0) {
        b.buffTurns--
        if (b.buffTurns == 0) {
            b.atkBuff = 1.0
            b.defBuff = 1.0
        }
    }
    val eatk = e.atk * b.enemyAtkDown
    var dmg = max(1, (eatk * Random.nextDouble(0.85, 1.15) - p.def() * b.defBuff * 0.5).toInt())
    if (b.playerShield > 0) {
        val absorbed = min(b.playerShield, dmg)
        b.playerShield -= absorbed
        dmg -= absorbed
        if (absorbed > 0) b.log.add("护体之力挡下 $absorbed 点伤害。")
    }
    if (dmg > 0) {
        p.hpCur -= dmg
        b.log.add("${e.name}反扑，你受到 $dmg 点伤害。")
    }
    if (p.hpCur <= 0) {
        loseBattle(b)
        return
    }
    syncPlayer(p)
    syncBattle()
}

private fun GameViewModel.winBattle(b: BattleState, e: EnemyDef) {
    val p = player ?: return
    b.over = true
    b.victory = true
    b.enemyHp = 0
    val before = p.exp
    p.exp = min(p.expNeeded(), p.exp + e.expReward)
    val realExp = p.exp - before
    p.stones += e.stoneReward
    p.killCount++
    p.quest?.let { q ->
        if (q.type == "HUNT") {
            p.quest = q.copy(progress = q.progress + 1)
        }
    }
    b.rewardExp = realExp
    b.rewardStones = e.stoneReward
    b.log.add("${e.name}伏诛。修为 +$realExp，灵石 +${e.stoneReward}。")
    val drops = mutableListOf<String>()
    for (d in e.drops) {
        if (Random.nextInt(100) < d.chance) {
            val cnt = d.min + Random.nextInt(d.max - d.min + 1)
            grantItem(p, d.itemId, cnt)
            drops.add("${Items.name(d.itemId)}×$cnt")
            b.rewardItems.add(d.itemId)
        }
    }
    if (drops.isNotEmpty()) {
        b.log.add("战利品：${drops.joinToString("、")}")
    }
    syncPlayer(p)
    syncBattle()
    autoSave()
}

private fun GameViewModel.loseBattle(b: BattleState) {
    val p = player ?: return
    b.over = true
    b.victory = false
    b.enemyHp = max(1, b.enemyHp)
    val lost = p.stones / 2
    p.stones -= lost
    p.hpCur = max(1, p.maxHp() / 4)
    p.demonHeart = (p.demonHeart + 10).coerceAtMost(100)
    advanceDays(p, 30)
    b.log.add("你重伤倒地，拼死遁走。损失灵石 $lost 枚，心魔 +10。")
    if (p.dead) {
        b.log.add("你没能撑过去。")
    }
    syncPlayer(p)
    syncBattle()
    autoSave()
}

/** 战斗中可用的法术列表 */
fun GameViewModel.battleSpells(): List<String> {
    val p = player ?: return emptyList()
    return p.techniques.filter { Techniques.get(it)?.kind == com.karios.xiuxian.data.TechKind.SPELL }
}

/** 战斗中可用的丹药 */
fun GameViewModel.battlePills(): List<String> {
    val p = player ?: return emptyList()
    return p.inventory.keys.filter { Items.get(it)?.type == com.karios.xiuxian.model.ItemType.PILL }
}

/** 战斗中可用的符箓 */
fun GameViewModel.battleTalismans(): List<String> {
    val p = player ?: return emptyList()
    return p.inventory.keys.filter { Items.get(it)?.type == com.karios.xiuxian.model.ItemType.TALISMAN }
}