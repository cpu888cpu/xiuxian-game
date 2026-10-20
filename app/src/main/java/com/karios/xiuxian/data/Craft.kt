package com.karios.xiuxian.data

object CraftType {
    const val ALCHEMY = "ALCHEMY"
    const val FORGE = "FORGE"
    const val TALISMAN = "TALISMAN"
}

data class Recipe(
    val id: String,
    val name: String,
    val type: String,
    val result: String,
    val resultCount: Int,
    val materials: Map<String, Int>,
    val stones: Int,
    val minRealm: Int,
    val baseSuccess: Int,
    val days: Int
)

object Recipes {
    val ALL: List<Recipe> = listOf(
        // ===== 炼丹 =====
        Recipe("r_liaoshang", "疗伤丹", CraftType.ALCHEMY, "pill_liaoshang", 1,
            mapOf("herb_xueshen" to 2), 5, 0, 80, 10),
        Recipe("r_huiling", "回灵丹", CraftType.ALCHEMY, "pill_huiling", 1,
            mapOf("herb_juling" to 2, "mat_lingmo" to 1), 5, 0, 80, 10),
        Recipe("r_juqi", "聚气丹", CraftType.ALCHEMY, "pill_juqi", 1,
            mapOf("herb_juling" to 3, "herb_xueshen" to 1), 10, 0, 70, 15),
        Recipe("r_jingxin", "静心丹", CraftType.ALCHEMY, "pill_jingxin", 1,
            mapOf("herb_zihou" to 2), 20, 0, 65, 15),
        Recipe("r_pojing", "破境丹", CraftType.ALCHEMY, "pill_pojing", 1,
            mapOf("herb_tianling" to 1, "herb_bainian" to 1, "mat_yaodan" to 1), 100, 0, 45, 30),
        Recipe("r_zhuji", "筑基丹", CraftType.ALCHEMY, "pill_zhuji", 1,
            mapOf("herb_tianling" to 2, "herb_longlin" to 1, "mat_yaodan" to 2), 200, 0, 40, 30),
        Recipe("r_xisui", "洗髓丹", CraftType.ALCHEMY, "pill_xisui", 1,
            mapOf("herb_yinyang" to 1, "herb_bainian" to 2, "mat_yaodan" to 2), 500, 1, 30, 45),
        Recipe("r_jiedan", "结丹丹", CraftType.ALCHEMY, "pill_jiedan", 1,
            mapOf("herb_tianling" to 3, "herb_longlin" to 2, "mat_yaodan" to 3, "herb_yinyang" to 1), 800, 1, 30, 60),
        Recipe("r_yuanying", "元婴丹", CraftType.ALCHEMY, "pill_yuanying", 1,
            mapOf("herb_yinyang" to 2, "herb_longlin" to 3, "mat_yaodan" to 6), 2000, 2, 25, 120),
        Recipe("r_huashen", "化神丹", CraftType.ALCHEMY, "pill_huashen", 1,
            mapOf("herb_yinyang" to 5, "ore_xingchen" to 2, "mat_yaodan" to 12), 5000, 3, 20, 240),

        // ===== 炼器 =====
        Recipe("f_qingzhu", "青竹蜂云剑", CraftType.FORGE, "art_qingzhu", 1,
            mapOf("ore_tie" to 3, "mat_lingmo" to 2), 50, 0, 60, 20),
        Recipe("f_jinzhuan", "金砖", CraftType.FORGE, "art_jinzhuan", 1,
            mapOf("ore_chitong" to 3, "ore_tie" to 2), 80, 0, 55, 20),
        Recipe("f_xuantian", "玄天镜", CraftType.FORGE, "art_xuantian", 1,
            mapOf("ore_xuantie" to 2, "ore_hanjing" to 1, "mat_yaodan" to 1), 300, 1, 40, 40),
        Recipe("f_liehuo", "烈火扇", CraftType.FORGE, "art_liehuo", 1,
            mapOf("ore_chitong" to 4, "ore_xuantie" to 1, "mat_yaodan" to 1), 350, 1, 40, 40),
        Recipe("f_taiji", "太极图", CraftType.FORGE, "art_taiji", 1,
            mapOf("ore_hanjing" to 3, "ore_xingchen" to 1, "mat_yaodan" to 3), 1500, 2, 30, 60),

        // ===== 制符 =====
        Recipe("t_huoqiu", "火球符", CraftType.TALISMAN, "tal_huoqiu", 2,
            mapOf("mat_fuzhi" to 2, "mat_lingmo" to 1), 10, 0, 75, 8),
        Recipe("t_jingang", "金刚符", CraftType.TALISMAN, "tal_jingang", 2,
            mapOf("mat_fuzhi" to 2, "ore_tie" to 1), 10, 0, 75, 8),
        Recipe("t_shenxing", "神行符", CraftType.TALISMAN, "tal_shenxing", 2,
            mapOf("mat_fuzhi" to 2, "herb_zihou" to 1), 20, 0, 65, 10)
    )

    fun of(type: String): List<Recipe> = ALL.filter { it.type == type }
}

/** 宗门 */
data class SectDef(
    val id: String,
    val name: String,
    val desc: String,
    val minRealm: Int,
    val joinReward: Int
)

object Sects {
    val ALL: List<SectDef> = listOf(
        SectDef(
            "s_taiyi", "太一门",
            "正道大宗，门规森严，讲究循序渐进。入门可得月俸与功法指点，但须完成宗门差事。",
            0, 100
        ),
        SectDef(
            "s_xuesha", "血煞宗",
            "魔道大宗，以杀证道。入门即有丰厚灵石，但宗门任务多是杀人夺宝，心魔易生。",
            1, 400
        ),
        SectDef(
            "s_wanbao", "万宝阁",
            "横跨数州的商会，不问出身，只看灵石。入门可学商道，买卖价格更优。",
            0, 200
        )
    )

    fun get(id: String): SectDef? = ALL.firstOrNull { it.id == id }
}