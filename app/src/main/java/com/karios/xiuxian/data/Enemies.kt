package com.karios.xiuxian.data

data class DropDef(
    val itemId: String,
    val chance: Int,
    val min: Int = 1,
    val max: Int = 1
)

data class EnemyDef(
    val id: String,
    val name: String,
    val desc: String,
    val hp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val expReward: Long,
    val stoneReward: Int,
    val drops: List<DropDef> = emptyList()
)

object Enemies {
    val ALL: Map<String, EnemyDef> = listOf(
        // ===== 炼气低阶 =====
        EnemyDef("e_tu", "灰毛妖兔", "山林间蹦跳的妖兔，胆子极小。",
            25, 6, 1, 12, 25, 1,
            listOf(DropDef("mat_shoupi", 40))),
        EnemyDef("e_ji", "赤冠山鸡", "羽毛如火的野鸡，啄人极痛。",
            35, 9, 2, 10, 30, 2,
            listOf(DropDef("mat_shoupi", 35))),
        EnemyDef("e_lang", "青背狼", "成群结队的野狼，落单时反而更加凶悍。",
            55, 12, 4, 9, 50, 4,
            listOf(DropDef("mat_shoupi", 60), DropDef("mat_lingmo", 20))),
        EnemyDef("e_she", "赤练蛇", "通体赤红的毒蛇，行动如风。",
            45, 15, 3, 15, 55, 5,
            listOf(DropDef("mat_lingmo", 45))),

        // ===== 炼气中阶 =====
        EnemyDef("e_yezhu", "铁背野猪", "皮糙肉厚，獠牙如刀。",
            110, 16, 10, 6, 90, 8,
            listOf(DropDef("mat_shoupi", 70), DropDef("mat_yaodan", 15))),
        EnemyDef("e_shanxiao", "山魈", "山中精怪，力大无穷，喜食人。",
            150, 22, 12, 10, 130, 14,
            listOf(DropDef("mat_yaodan", 30), DropDef("ore_tie", 40))),
        EnemyDef("e_huoya", "火鸦", "口吐火苗的黑色乌鸦，最是记仇。",
            130, 27, 8, 20, 150, 16,
            listOf(DropDef("mat_lingmo", 50), DropDef("mat_yaodan", 25))),

        // ===== 炼气高阶 =====
        EnemyDef("e_shiyuan", "石猿", "身披岩石的巨大猿猴，一拳可裂巨石。",
            300, 32, 22, 9, 260, 30,
            listOf(DropDef("mat_yaodan", 50), DropDef("ore_tie", 70))),
        EnemyDef("e_heidiao", "黑风雕", "掀风作浪的巨雕，专叼修士。",
            240, 35, 10, 24, 280, 28,
            listOf(DropDef("mat_shoupi", 80), DropDef("mat_yaodan", 40))),
        EnemyDef("e_duyan", "赤目毒狼", "双目赤红的狼王，成群出没。",
            330, 38, 16, 22, 320, 35,
            listOf(DropDef("mat_yaodan", 55), DropDef("mat_lingmo", 50))),

        // ===== 筑基 =====
        EnemyDef("e_jinchan", "碧眼金蟾", "沼泽霸主，一声蛙鸣震人心魄。",
            900, 65, 30, 16, 900, 120,
            listOf(DropDef("mat_yaodan", 90), DropDef("herb_zihou", 40))),
        EnemyDef("e_guiteng", "幽冥鬼藤", "活着的藤蔓，缠上便难脱身。",
            1200, 55, 45, 8, 950, 130,
            listOf(DropDef("herb_bainian", 35), DropDef("ore_hanjing", 25))),
        EnemyDef("e_wugong", "铁甲蜈蚣", "甲壳如铁，百足齐动。",
            1000, 78, 40, 22, 1000, 140,
            listOf(DropDef("mat_yaodan", 80), DropDef("ore_xuantie", 30))),
        EnemyDef("e_shuigui", "水行鬼修", "葬身泽底的修士残魂，仍执着于法宝。",
            1100, 85, 35, 25, 1100, 200,
            listOf(DropDef("mat_yaodan", 60), DropDef("book_jinguang", 10))),

        // ===== 结丹 =====
        EnemyDef("e_yaohu", "三尾妖狐", "已开灵智的狐妖，幻术伤人于无形。",
            3500, 220, 90, 55, 6000, 600,
            listOf(DropDef("mat_yaodan", 100), DropDef("herb_longlin", 30))),
        EnemyDef("e_chiyan", "赤焰狮", "万兽山脉的霸主，一吼山摇。",
            5200, 260, 130, 40, 7500, 800,
            listOf(DropDef("mat_yaodan", 100), DropDef("ore_xingchen", 25))),
        EnemyDef("e_moluan", "魔道散修·血手", "以人血练功的散修，杀人如麻。",
            4200, 240, 100, 60, 8000, 1500,
            listOf(DropDef("mat_yaodan", 90), DropDef("book_leiting", 20), DropDef("ore_hanjing", 40))),

        // ===== 元婴 =====
        EnemyDef("e_dapeng", "金翅大鹏", "展翅遮天，爪裂山岳。",
            28000, 900, 420, 110, 60000, 5000,
            listOf(DropDef("ore_xingchen", 100), DropDef("mat_yaodan", 100))),
        EnemyDef("e_shimo", "远古石魔", "万年石胎孕育的凶物，坚不可摧。",
            45000, 780, 800, 60, 75000, 6000,
            listOf(DropDef("ore_xingchen", 100), DropDef("ore_hanjing", 100))),
        EnemyDef("e_yuanying_mo", "玄阴老魔", "元婴期魔修，专夺人元婴。",
            36000, 1000, 500, 120, 80000, 9000,
            listOf(DropDef("book_wanjian", 15), DropDef("mat_yaodan", 100))),

        // ===== 化神 =====
        EnemyDef("e_xuangui", "玄龟", "寿逾万年，龟甲自成天地。",
            160000, 2600, 2400, 70, 500000, 30000,
            listOf(DropDef("ore_xingchen", 100), DropDef("book_taixu", 10))),
        EnemyDef("e_leipeng", "雷鹏", "御雷而行，快如闪电。",
            130000, 3300, 1600, 220, 550000, 35000,
            listOf(DropDef("ore_xingchen", 100), DropDef("mat_yaodan", 100))),
        EnemyDef("e_tianmo", "域外天魔", "自界外而来，专噬修士元神。",
            180000, 3000, 1800, 180, 700000, 40000,
            listOf(DropDef("book_taixu", 15), DropDef("mat_yaodan", 100)))
    ).associateBy { it.id }

    fun get(id: String): EnemyDef? = ALL[id]
}