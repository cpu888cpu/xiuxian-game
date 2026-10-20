package com.karios.xiuxian.data

/** 特殊事件效果类型 */
object EvtEffect {
    const val NOTHING = "nothing"
    const val HEAL = "heal"
    const val HURT = "hurt"
    const val STONES = "stones"
    const val EXP = "exp"
    const val DEMON_UP = "demon_up"
    const val DEMON_DOWN = "demon_down"
    const val ITEM = "item"
    const val BATTLE = "battle"
    const val COMPREHENSION = "comprehension"
}

data class SpecialEvent(
    val id: String,
    val weight: Int,
    val text: String,
    val effect: String,
    val value: Int = 0,
    val itemId: String = ""
)

data class RegionDef(
    val id: String,
    val name: String,
    val minRealm: Int,
    val minSub: Int,
    val days: Int,
    val desc: String,
    val enemies: List<String>,
    val herbs: List<String>,
    val ores: List<String>,
    val events: List<SpecialEvent>
)

object Regions {
    val ALL: List<RegionDef> = listOf(
        RegionDef(
            id = "r_qingniu",
            name = "青牛山",
            minRealm = 0, minSub = 0, days = 15,
            desc = "凡俗与仙途交界处的矮山，灵气稀薄，多野兔山鸡，是散修练手之地。",
            enemies = listOf("e_tu", "e_ji", "e_lang", "e_she"),
            herbs = listOf("herb_juling", "herb_juling", "herb_xueshen"),
            ores = listOf("ore_tie"),
            events = listOf(
                SpecialEvent("qn_1", 10, "你在山涧边捡到半枚残破玉简，虽无功法，却让你对吐纳之道略有领悟。",
                    EvtEffect.EXP, value = 30),
                SpecialEvent("qn_2", 8, "一位砍柴老翁看你面善，塞给你一串铜钱，说「后生，修仙路远，保重」。",
                    EvtEffect.STONES, value = 25),
                SpecialEvent("qn_3", 6, "你在崖壁下发现一处荒废小洞，里头竟有几味灵草。",
                    EvtEffect.ITEM, value = 2, itemId = "herb_juling"),
                SpecialEvent("qn_4", 6, "山雨骤至，你在山洞中枯坐一夜，忽觉心神澄澈。",
                    EvtEffect.HEAL, value = 40),
                SpecialEvent("qn_5", 4, "你踩空落入石缝，摔得浑身酸痛。",
                    EvtEffect.HURT, value = 15)
            )
        ),
        RegionDef(
            id = "r_heifeng",
            name = "黑风林",
            minRealm = 0, minSub = 4, days = 20,
            desc = "终年阴风不散的密林，树影如鬼，常有低阶妖兽出没，亦有散修在此猎妖换钱。",
            enemies = listOf("e_lang", "e_she", "e_yezhu", "e_shanxiao"),
            herbs = listOf("herb_juling", "herb_xueshen", "herb_zihou"),
            ores = listOf("ore_tie", "ore_chitong"),
            events = listOf(
                SpecialEvent("hf_1", 10, "林中深处有一株紫猴花，花气袭人，你小心翼翼地采了下来。",
                    EvtEffect.ITEM, value = 1, itemId = "herb_zihou"),
                SpecialEvent("hf_2", 8, "你遇到一名重伤的散修，他塞给你几块灵石，求你莫要声张。",
                    EvtEffect.STONES, value = 60),
                SpecialEvent("hf_3", 7, "林中回荡着若有若无的哭声，你心神一乱，气血翻涌。",
                    EvtEffect.HURT, value = 20),
                SpecialEvent("hf_4", 6, "两名散修争夺妖丹打得两败俱伤，你趁乱取了遗落之物。",
                    EvtEffect.ITEM, value = 1, itemId = "mat_yaodan"),
                SpecialEvent("hf_5", 5, "你在老树下悟得一丝风之意，修为小进。",
                    EvtEffect.EXP, value = 80),
                SpecialEvent("hf_6", 4, "一个黑衣人拦住去路，冷笑「留下买路钱」。",
                    EvtEffect.BATTLE)
            )
        ),
        RegionDef(
            id = "r_luoxia",
            name = "落霞谷",
            minRealm = 0, minSub = 8, days = 25,
            desc = "夕阳下如铺锦缎的山谷，灵气渐浓。谷中有前人洞府遗迹，亦有大妖盘踞。",
            enemies = listOf("e_shanxiao", "e_huoya", "e_shiyuan", "e_heidiao", "e_duyan"),
            herbs = listOf("herb_zihou", "herb_tianling", "herb_tianling"),
            ores = listOf("ore_chitong", "ore_xuantie"),
            events = listOf(
                SpecialEvent("lx_1", 10, "你在一处塌陷的洞府中翻出一柄断剑，虽残破却灵气未散。",
                    EvtEffect.ITEM, value = 1, itemId = "art_qingzhu"),
                SpecialEvent("lx_2", 8, "石壁上刻着半篇吐纳口诀，你照着运转，只觉灵力流转顺畅。",
                    EvtEffect.EXP, value = 150),
                SpecialEvent("lx_3", 7, "谷底灵田无人看管，你摘了两枚天灵果。",
                    EvtEffect.ITEM, value = 2, itemId = "herb_tianling"),
                SpecialEvent("lx_4", 6, "一位白发老者与你论道半日，临走时留你一册手抄本。",
                    EvtEffect.ITEM, value = 1, itemId = "book_changchun"),
                SpecialEvent("lx_5", 5, "你被谷中幻阵所困，耗费大半灵力方才脱身。",
                    EvtEffect.HURT, value = 25),
                SpecialEvent("lx_6", 5, "一具枯骨端坐石台，怀中抱着一只玉盒。你默念「晚辈借用」，取走了它。",
                    EvtEffect.ITEM, value = 1, itemId = "pill_pojing")
            )
        ),
        RegionDef(
            id = "r_youming",
            name = "幽冥沼泽",
            minRealm = 1, minSub = 0, days = 40,
            desc = "终日不见天日的沼泽，瘴气弥漫。据说曾有鬼修在此陨落，遗宝无数。",
            enemies = listOf("e_jinchan", "e_guiteng", "e_wugong", "e_shuigui"),
            herbs = listOf("herb_tianling", "herb_bainian", "herb_bainian"),
            ores = listOf("ore_xuantie", "ore_hanjing"),
            events = listOf(
                SpecialEvent("ym_1", 10, "沼泽深处浮出一面古镜，你伸手一捞，竟真个捞了上来。",
                    EvtEffect.ITEM, value = 1, itemId = "art_xuantian"),
                SpecialEvent("ym_2", 9, "你循着瘴气最薄处前行，意外拾得一枚温润妖丹。",
                    EvtEffect.ITEM, value = 2, itemId = "mat_yaodan"),
                SpecialEvent("ym_3", 8, "水底沉着半截石碑，上面刻着「慎之」二字，你心里发毛。",
                    EvtEffect.DEMON_UP, value = 8),
                SpecialEvent("ym_4", 7, "你在一具妖骸中取出一卷湿透的丹方，字迹尚可辨认。",
                    EvtEffect.ITEM, value = 1, itemId = "book_huichun"),
                SpecialEvent("ym_5", 6, "鬼气入体，你咳出一口黑血，却觉旧疾尽去。",
                    EvtEffect.HEAL, value = 30),
                SpecialEvent("ym_6", 5, "一头水行鬼修盯上了你。",
                    EvtEffect.BATTLE)
            )
        ),
        RegionDef(
            id = "r_wanshou",
            name = "万兽山脉",
            minRealm = 1, minSub = 2, days = 60,
            desc = "延绵万里的妖兽巢穴，人类修士在此只是食物链中段。结丹以下，慎入。",
            enemies = listOf("e_yaohu", "e_chiyan", "e_moluan"),
            herbs = listOf("herb_bainian", "herb_longlin"),
            ores = listOf("ore_hanjing", "ore_xuantie"),
            events = listOf(
                SpecialEvent("ws_1", 10, "你在崖洞中发现一具剑修遗蜕，身前插着一柄竹剑，剑气犹存。",
                    EvtEffect.ITEM, value = 1, itemId = "art_liehuo"),
                SpecialEvent("ws_2", 9, "兽潮过后，谷底散落着大量妖丹与残骸。",
                    EvtEffect.STONES, value = 800),
                SpecialEvent("ws_3", 8, "你被两头妖兽夹击，拼死突围，虽侥幸脱身却伤势不轻。",
                    EvtEffect.HURT, value = 35),
                SpecialEvent("ws_4", 7, "一只垂死的狐妖将内丹吐出赠你，只求你放过它的幼崽。你答应了。",
                    EvtEffect.EXP, value = 3000),
                SpecialEvent("ws_5", 6, "你在山腹深处寻得一块星尘砂。",
                    EvtEffect.ITEM, value = 1, itemId = "ore_xingchen"),
                SpecialEvent("ws_6", 5, "一名魔道散修拦路，眼里全是你身上的灵石。",
                    EvtEffect.BATTLE)
            )
        ),
        RegionDef(
            id = "r_luori",
            name = "落日荒原",
            minRealm = 2, minSub = 2, days = 90,
            desc = "上古大战遗留下的焦土，白骨盈野，怨气冲天。传闻此地埋着一座古修士的洞府。",
            enemies = listOf("e_dapeng", "e_shimo", "e_yuanying_mo"),
            herbs = listOf("herb_longlin", "herb_yinyang"),
            ores = listOf("ore_xingchen"),
            events = listOf(
                SpecialEvent("lr_1", 10, "荒原之下有座半塌的石殿，供台上一卷竹简静静躺着。",
                    EvtEffect.ITEM, value = 1, itemId = "book_dayan"),
                SpecialEvent("lr_2", 9, "你掘开一处古修士坟冢，取走了一柄残剑与若干灵石。",
                    EvtEffect.STONES, value = 4000),
                SpecialEvent("lr_3", 8, "怨气缠身，你脑中不断浮现往昔噩梦。",
                    EvtEffect.DEMON_UP, value = 15),
                SpecialEvent("lr_4", 7, "残阳如血，你望着无边的白骨，忽然明白了什么。",
                    EvtEffect.EXP, value = 20000),
                SpecialEvent("lr_5", 6, "古老阵法还未散尽，硬生生将你弹出百丈，五脏翻腾。",
                    EvtEffect.HURT, value = 30),
                SpecialEvent("lr_6", 5, "一具古尸忽然睁眼，从棺中坐起。",
                    EvtEffect.BATTLE)
            )
        ),
        RegionDef(
            id = "r_taixu",
            name = "太虚秘境",
            minRealm = 3, minSub = 0, days = 150,
            desc = "万年开启一次的破碎洞天，其中灵气浓如实质，也藏着足以斩杀化神的凶物。",
            enemies = listOf("e_xuangui", "e_leipeng", "e_tianmo"),
            herbs = listOf("herb_yinyang", "herb_longlin"),
            ores = listOf("ore_xingchen"),
            events = listOf(
                SpecialEvent("tx_1", 10, "秘境内一座石碑通体流光，上书《太虚真经》四字。你只记下半篇。",
                    EvtEffect.ITEM, value = 1, itemId = "book_taixu"),
                SpecialEvent("tx_2", 9, "一株亿万年阴阳花在虚空中静静绽放，你屏住呼吸采下。",
                    EvtEffect.ITEM, value = 3, itemId = "herb_yinyang"),
                SpecialEvent("tx_3", 8, "秘境法则碾过身躯，你几乎被压成血雾。",
                    EvtEffect.HURT, value = 40),
                SpecialEvent("tx_4", 7, "你在此处悟道三日，睁眼时已是另一番天地。",
                    EvtEffect.EXP, value = 150000),
                SpecialEvent("tx_5", 6, "域外天魔的气息渗入识海，你险些走火入魔。",
                    EvtEffect.DEMON_UP, value = 20),
                SpecialEvent("tx_6", 5, "一柄断裂的古剑从虚空中坠落，杀气冲霄。",
                    EvtEffect.ITEM, value = 1, itemId = "art_zhuxian")
            )
        )
    )

    fun get(id: String): RegionDef? = ALL.firstOrNull { it.id == id }
}