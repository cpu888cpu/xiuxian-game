package com.karios.xiuxian.data

import com.karios.xiuxian.model.ItemDef
import com.karios.xiuxian.model.ItemType
import com.karios.xiuxian.model.SpiritRoot
import kotlin.random.Random

/** 大境界定义 */
data class RealmDef(
    val name: String,
    val subs: List<String>,
    val lifeYears: Int,
    val baseHp: Int,
    val baseMp: Int,
    val baseAtk: Int,
    val baseDef: Int,
    val baseSpd: Int,
    val expBase: Long,
    val expStep: Long,
    val gainBase: Double,
    val gainStep: Double,
    val medDays: Int,
    val breakChance: Int
)

val REALMS: List<RealmDef> = listOf(
    RealmDef(
        name = "炼气",
        subs = listOf("一层", "二层", "三层", "四层", "五层", "六层", "七层",
            "八层", "九层", "十层", "十一层", "十二层", "十三层"),
        lifeYears = 120,
        baseHp = 60, baseMp = 40, baseAtk = 12, baseDef = 6, baseSpd = 8,
        expBase = 150, expStep = 25,
        gainBase = 55.0, gainStep = 8.0,
        medDays = 182, breakChance = 20
    ),
    RealmDef(
        name = "筑基",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 300,
        baseHp = 320, baseMp = 240, baseAtk = 60, baseDef = 30, baseSpd = 22,
        expBase = 1000, expStep = 250,
        gainBase = 420.0, gainStep = 100.0,
        medDays = 1826, breakChance = 14
    ),
    RealmDef(
        name = "结丹",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 600,
        baseHp = 1200, baseMp = 900, baseAtk = 200, baseDef = 100, baseSpd = 45,
        expBase = 8000, expStep = 2000,
        gainBase = 2800.0, gainStep = 700.0,
        medDays = 3652, breakChance = 12
    ),
    RealmDef(
        name = "元婴",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 1500,
        baseHp = 4500, baseMp = 3500, baseAtk = 700, baseDef = 350, baseSpd = 80,
        expBase = 50000, expStep = 12000,
        gainBase = 18000.0, gainStep = 4000.0,
        medDays = 7305, breakChance = 10
    ),
    RealmDef(
        name = "化神",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 2500,
        baseHp = 16000, baseMp = 12000, baseAtk = 2400, baseDef = 1200, baseSpd = 130,
        expBase = 300000, expStep = 80000,
        gainBase = 110000.0, gainStep = 25000.0,
        medDays = 14610, breakChance = 8
    ),
    RealmDef(
        name = "炼虚",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 4000,
        baseHp = 60000, baseMp = 45000, baseAtk = 8000, baseDef = 4000, baseSpd = 200,
        expBase = 1800000, expStep = 450000,
        gainBase = 650000.0, gainStep = 140000.0,
        medDays = 29220, breakChance = 6
    ),
    RealmDef(
        name = "合体",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 8000,
        baseHp = 220000, baseMp = 180000, baseAtk = 28000, baseDef = 14000, baseSpd = 320,
        expBase = 10000000, expStep = 2500000,
        gainBase = 3600000.0, gainStep = 800000.0,
        medDays = 54787, breakChance = 5
    ),
    RealmDef(
        name = "大乘",
        subs = listOf("初期", "中期", "后期", "大圆满"),
        lifeYears = 20000,
        baseHp = 800000, baseMp = 700000, baseAtk = 90000, baseDef = 50000, baseSpd = 500,
        expBase = 60000000, expStep = 15000000,
        gainBase = 21000000.0, gainStep = 5000000.0,
        medDays = 109575, breakChance = 0
    )
)

/** 随机灵根 */
fun randomSpiritRoot(): SpiritRoot {
    val all = listOf("金", "木", "水", "火", "土")
    val roll = Random.nextInt(100)
    if (roll < 5) {
        val mut = listOf("雷", "冰", "风").random()
        return SpiritRoot(listOf(mut), "变异灵根", 3.5)
    }
    val count = when {
        roll < 13 -> 1
        roll < 28 -> 2
        roll < 55 -> 3
        roll < 80 -> 4
        else -> 5
    }
    val els = all.shuffled().take(count)
    val name = when (count) {
        1 -> "天灵根"
        2 -> "双灵根"
        3 -> "三灵根"
        4 -> "四灵根"
        else -> "伪灵根"
    }
    val mul = when (count) {
        1 -> 3.0
        2 -> 2.0
        3 -> 1.4
        4 -> 1.0
        else -> 0.65
    }
    return SpiritRoot(els, name, mul)
}

/** 全物品表 */
object Items {
    val ALL: Map<String, ItemDef> = listOf(
        // ===== 灵草 =====
        ItemDef("herb_juling", "聚灵草", ItemType.HERB, 1, "山野常见灵草，可引动微弱灵气。", 30),
        ItemDef("herb_xueshen", "血参", ItemType.HERB, 1, "色如凝血，疗伤圣品。", 45),
        ItemDef("herb_zihou", "紫猴花", ItemType.HERB, 2, "花形如猴面，可安神定魄。", 90),
        ItemDef("herb_tianling", "天灵果", ItemType.HERB, 2, "百年一熟，蕴有精纯灵力。", 180),
        ItemDef("herb_bainian", "百年灵芝", ItemType.HERB, 3, "生于绝壁，药性温和醇厚。", 320),
        ItemDef("herb_longlin", "龙鳞草", ItemType.HERB, 3, "叶似龙鳞，坚不可摧。", 520),
        ItemDef("herb_yinyang", "阴阳花", ItemType.HERB, 4, "一花开双色，阴阳交汇。", 900),

        // ===== 矿石 =====
        ItemDef("ore_tie", "铁精", ItemType.ORE, 1, "凡铁提炼之精，可铸法器雏形。", 40),
        ItemDef("ore_chitong", "赤铜", ItemType.ORE, 1, "色赤性烈，通火属。", 80),
        ItemDef("ore_xuantie", "玄铁", ItemType.ORE, 2, "沉重无比，可镇气机。", 140),
        ItemDef("ore_hanjing", "寒晶", ItemType.ORE, 3, "极寒之地所产，触之刺骨。", 280),
        ItemDef("ore_xingchen", "星尘砂", ItemType.ORE, 4, "陨星碎屑，能引天雷。", 480),

        // ===== 材料 =====
        ItemDef("mat_shoupi", "兽皮", ItemType.MATERIAL, 1, "妖兽皮毛，可制符纸。", 20),
        ItemDef("mat_fuzhi", "符纸", ItemType.MATERIAL, 1, "以灵木浆制成，可承灵墨。", 15),
        ItemDef("mat_lingmo", "灵墨", ItemType.MATERIAL, 1, "妖血调和之墨，通灵性。", 30),
        ItemDef("mat_yaodan", "妖丹", ItemType.MATERIAL, 2, "妖兽体内凝结的灵力核心。", 120),

        // ===== 丹药 =====
        ItemDef("pill_liaoshang", "疗伤丹", ItemType.PILL, 1, "服下回复三成气血。", 60, heal = 30),
        ItemDef("pill_huiling", "回灵丹", ItemType.PILL, 1, "服下回复三成灵力。", 60, mana = 30),
        ItemDef("pill_juqi", "聚气丹", ItemType.PILL, 1, "服下立获大量修为。", 120, expGain = 120),
        ItemDef("pill_jingxin", "静心丹", ItemType.PILL, 2, "压制心魔，涤荡灵台。", 150, demonDown = 25),
        ItemDef("pill_pojing", "破境丹", ItemType.PILL, 3, "下次突破成功率提升一成五。", 800),
        ItemDef("pill_zhuji", "筑基丹", ItemType.PILL, 3, "筑基时服下，成功率提升三成五。", 3000),
        ItemDef("pill_jiedan", "结丹丹", ItemType.PILL, 4, "结丹时服下，成功率提升三成。", 5000),
        ItemDef("pill_yuanying", "元婴丹", ItemType.PILL, 4, "元婴时服下，成功率提升两成五。", 15000),
        ItemDef("pill_huashen", "化神丹", ItemType.PILL, 5, "化神时服下，成功率提升两成。", 40000),
        ItemDef("pill_xisui", "洗髓丹", ItemType.PILL, 4, "伐毛洗髓，悟性永久 +1。", 1200),

        // ===== 符箓 =====
        ItemDef("tal_huoqiu", "火球符", ItemType.TALISMAN, 1, "掷出化为一团烈焰。", 70),
        ItemDef("tal_jingang", "金刚符", ItemType.TALISMAN, 1, "护体金光，抵挡伤害。", 80),
        ItemDef("tal_shenxing", "神行符", ItemType.TALISMAN, 2, "身法如风，脱身必成。", 100),

        // ===== 法宝 =====
        ItemDef("art_qingzhu", "青竹蜂云剑", ItemType.ARTIFACT, 1, "以青竹炼成的飞剑，锋锐轻灵。", 300, atkBonus = 10, mpBonus = 20),
        ItemDef("art_jinzhuan", "金砖", ItemType.ARTIFACT, 1, "一块土黄色金砖，专砸人脑袋。", 400, atkBonus = 14, defBonus = 6),
        ItemDef("art_xuantian", "玄天镜", ItemType.ARTIFACT, 2, "古镜一枚，可映照万法。", 1200, defBonus = 60, mpBonus = 100, atkBonus = 40),
        ItemDef("art_liehuo", "烈火扇", ItemType.ARTIFACT, 2, "扇动生焰，火属法宝。", 1400, atkBonus = 75, mpBonus = 60),
        ItemDef("art_taiji", "太极图", ItemType.ARTIFACT, 3, "阴阳流转，攻守兼备。", 5000, atkBonus = 220, defBonus = 180, hpBonus = 600),
        ItemDef("art_zhuxian", "诛仙残剑", ItemType.ARTIFACT, 4, "断剑一截，杀气冲霄。", 20000, atkBonus = 900, defBonus = 300, hpBonus = 2000),

        // ===== 功法书 =====
        ItemDef("book_yinqi", "《引气诀》", ItemType.BOOK, 1, "最基础的引气入体法门。", 100, teaches = "tech_yinqi"),
        ItemDef("book_changchun", "《长春功》", ItemType.BOOK, 1, "养生固本，气血绵长。", 200, teaches = "tech_changchun"),
        ItemDef("book_huoqiu", "《火球术》", ItemType.BOOK, 1, "入门级攻伐法术。", 150, teaches = "spell_huoqiu"),
        ItemDef("book_bingzhui", "《冰锥术》", ItemType.BOOK, 1, "凝水成锥，缓敌行动。", 180, teaches = "spell_bingzhui"),
        ItemDef("book_jinguang", "《金光罩》", ItemType.BOOK, 1, "护体法术，可挡刀剑。", 160, teaches = "spell_jinguang"),
        ItemDef("book_qingyuan", "《青元剑诀》", ItemType.BOOK, 2, "剑修入门，攻伐凌厉。", 600, teaches = "tech_qingyuan"),
        ItemDef("book_huichun", "《回春术》", ItemType.BOOK, 2, "以灵力续命疗伤。", 500, teaches = "spell_huichun"),
        ItemDef("book_shendun", "《神行遁法》", ItemType.BOOK, 2, "遁走之术，保命要紧。", 450, teaches = "spell_shendun"),
        ItemDef("book_leiting", "《五雷正法》", ItemType.BOOK, 3, "引天雷入掌，破尽邪祟。", 3000, teaches = "spell_leiting"),
        ItemDef("book_dayan", "《大衍决》", ItemType.BOOK, 3, "推演天机，神识大增。", 4000, teaches = "tech_dayan"),
        ItemDef("book_wanjian", "《万剑归宗》", ItemType.BOOK, 4, "万剑齐鸣，天地变色。", 15000, teaches = "spell_wanjian"),
        ItemDef("book_taixu", "《太虚真经》", ItemType.BOOK, 4, "上古真经，直指大道。", 20000, teaches = "tech_taixu")
    ).associateBy { it.id }

    fun get(id: String): ItemDef? = ALL[id]

    fun name(id: String): String = ALL[id]?.name ?: id
}