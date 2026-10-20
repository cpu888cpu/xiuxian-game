package com.karios.xiuxian.data

/** 功法类型 */
object TechKind {
    const val MAIN = "MAIN"
    const val SPELL = "SPELL"
}

/** 特殊效果标识 */
object TechEffect {
    const val NONE = ""
    const val HEAL = "HEAL"
    const val SHIELD = "SHIELD"
    const val DODGE = "DODGE"
    const val WEAKEN = "WEAKEN"
}

data class TechniqueDef(
    val id: String,
    val name: String,
    val grade: Int,
    val gradeName: String,
    val kind: String,
    val desc: String,
    val expMul: Double = 1.0,
    val hpPct: Double = 0.0,
    val mpPct: Double = 0.0,
    val atkPct: Double = 0.0,
    val defPct: Double = 0.0,
    val mpCost: Int = 0,
    val power: Double = 1.0,
    val effect: String = TechEffect.NONE
)

object Techniques {
    val ALL: Map<String, TechniqueDef> = listOf(
        // ===== 主修功法 =====
        TechniqueDef(
            "tech_yinqi", "引气诀", 1, "黄阶", TechKind.MAIN,
            "最粗浅的引气法门，聊胜于无。", expMul = 1.10, mpPct = 0.15
        ),
        TechniqueDef(
            "tech_changchun", "长春功", 1, "黄阶", TechKind.MAIN,
            "养生固本，气血绵长，久修可延寿。", expMul = 1.20, hpPct = 0.20
        ),
        TechniqueDef(
            "tech_qingyuan", "青元剑诀", 2, "玄阶", TechKind.MAIN,
            "剑修入门心法，锋芒毕露，攻伐极强。", expMul = 1.35, atkPct = 0.15
        ),
        TechniqueDef(
            "tech_dayan", "大衍决", 3, "地阶", TechKind.MAIN,
            "推演天机，神识如海，修炼一日千里。", expMul = 1.65, mpPct = 0.40
        ),
        TechniqueDef(
            "tech_taixu", "太虚真经", 4, "天阶", TechKind.MAIN,
            "上古真经，直指大道，非大机缘不可得。",
            expMul = 2.10, hpPct = 0.30, mpPct = 0.30, atkPct = 0.20
        ),

        // ===== 攻伐法术 =====
        TechniqueDef(
            "spell_huoqiu", "火球术", 1, "黄阶", TechKind.SPELL,
            "凝气化火，掷出伤敌。", mpCost = 12, power = 1.6
        ),
        TechniqueDef(
            "spell_bingzhui", "冰锥术", 1, "黄阶", TechKind.SPELL,
            "凝水成锥，附带迟缓。", mpCost = 15, power = 1.8, effect = TechEffect.WEAKEN
        ),
        TechniqueDef(
            "spell_jinguang", "金光罩", 1, "黄阶", TechKind.SPELL,
            "护体金光，抵挡伤害。", mpCost = 10, power = 0.0, effect = TechEffect.SHIELD
        ),
        TechniqueDef(
            "spell_huichun", "回春术", 2, "玄阶", TechKind.SPELL,
            "以灵力续命，回复气血。", mpCost = 14, power = 0.0, effect = TechEffect.HEAL
        ),
        TechniqueDef(
            "spell_shendun", "神行遁法", 2, "玄阶", TechKind.SPELL,
            "遁走之术，保命要紧。", mpCost = 8, power = 0.0, effect = TechEffect.DODGE
        ),
        TechniqueDef(
            "spell_qingyuanjian", "青元剑气", 2, "玄阶", TechKind.SPELL,
            "一剑化气，破敌于十丈之外。", mpCost = 22, power = 2.3
        ),
        TechniqueDef(
            "spell_leiting", "五雷正法", 3, "地阶", TechKind.SPELL,
            "引天雷入掌，破尽邪祟。", mpCost = 35, power = 3.2
        ),
        TechniqueDef(
            "spell_wanjian", "万剑归宗", 4, "天阶", TechKind.SPELL,
            "万剑齐鸣，天地为之变色。", mpCost = 70, power = 5.0
        )
    ).associateBy { it.id }

    fun get(id: String): TechniqueDef? = ALL[id]

    fun name(id: String): String = ALL[id]?.name ?: id

    fun mainList(): List<TechniqueDef> = ALL.values.filter { it.kind == TechKind.MAIN }
}