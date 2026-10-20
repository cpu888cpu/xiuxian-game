package com.karios.xiuxian.model

import kotlinx.serialization.Serializable

/** 灵根 */
@Serializable
data class SpiritRoot(
    val elements: List<String> = listOf("水"),
    val gradeName: String = "伪灵根",
    val speedMul: Double = 0.7
) {
    val display: String get() = "$gradeName·${elements.joinToString("")}灵根"
}

/** 物品类别 */
enum class ItemType(val label: String) {
    HERB("灵草"),
    ORE("矿石"),
    PILL("丹药"),
    TALISMAN("符箓"),
    ARTIFACT("法宝"),
    BOOK("功法"),
    MATERIAL("材料")
}

/** 物品定义（静态数据） */
data class ItemDef(
    val id: String,
    val name: String,
    val type: ItemType,
    val grade: Int,
    val desc: String,
    val price: Int,
    val hpBonus: Int = 0,
    val mpBonus: Int = 0,
    val atkBonus: Int = 0,
    val defBonus: Int = 0,
    val spdBonus: Int = 0,
    val heal: Int = 0,
    val mana: Int = 0,
    val expGain: Long = 0L,
    val demonDown: Int = 0,
    val teaches: String = "",
    val minRealm: Int = 0
)

/** 宗门任务 */
@Serializable
data class Quest(
    var name: String = "",
    var desc: String = "",
    var type: String = "",
    var itemId: String = "",
    var targetCount: Int = 0,
    var progress: Int = 0,
    var rewardStones: Int = 0,
    var rewardContribution: Int = 0,
    var rewardItem: String = "",
    var rewardCount: Int = 0
)

/** 玩家全部状态（唯一存档对象） */
@Serializable
data class Player(
    var name: String = "无名散修",
    var roots: SpiritRoot = SpiritRoot(),
    var comprehension: Int = 5,
    var luck: Int = 5,
    var realmIndex: Int = 0,
    var subIndex: Int = 0,
    var exp: Long = 0L,
    var stones: Int = 30,
    var hpCur: Int = 0,
    var mpCur: Int = 0,
    var demonHeart: Int = 0,
    var ageDays: Int = 0,
    var inventory: MutableMap<String, Int> = mutableMapOf(),
    var techniques: MutableList<String> = mutableListOf(),
    var mainTechnique: String = "",
    var artifact: String = "",
    var sectId: String = "",
    var contribution: Int = 0,
    var caveLevel: Int = 1,
    var caveHarvestDays: Int = 0,
    var quest: Quest? = null,
    var killCount: Int = 0,
    var breakCount: Int = 0,
    var breakBonus: Int = 0,
    var failBonus: Int = 0,
    var dead: Boolean = false,
    var ending: String = ""
)

/** 战斗中的临时状态（不存档） */
data class BattleState(
    var enemyId: String = "",
    var enemyHp: Int = 0,
    var enemyMaxHp: Int = 0,
    var turn: Int = 0,
    var log: MutableList<String> = mutableListOf(),
    var over: Boolean = false,
    var victory: Boolean = false,
    var fled: Boolean = false,
    var playerShield: Int = 0,
    var atkBuff: Double = 1.0,
    var defBuff: Double = 1.0,
    var buffTurns: Int = 0,
    var enemyAtkDown: Double = 1.0,
    var rewardExp: Long = 0L,
    var rewardStones: Int = 0,
    var rewardItems: MutableList<String> = mutableListOf()
)
