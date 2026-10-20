package com.karios.xiuxian.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karios.xiuxian.data.CraftType
import com.karios.xiuxian.data.Items
import com.karios.xiuxian.data.Recipes
import com.karios.xiuxian.data.Sects
import com.karios.xiuxian.data.TechKind
import com.karios.xiuxian.data.Techniques
import com.karios.xiuxian.logic.GameViewModel
import com.karios.xiuxian.logic.atk
import com.karios.xiuxian.logic.caveMul
import com.karios.xiuxian.logic.def
import com.karios.xiuxian.logic.itemCount
import com.karios.xiuxian.logic.maxHp
import com.karios.xiuxian.logic.maxMp
import com.karios.xiuxian.logic.spd
import com.karios.xiuxian.model.ItemType
import com.karios.xiuxian.model.Player
import com.karios.xiuxian.ui.theme.BloodRed
import com.karios.xiuxian.ui.theme.Gold
import com.karios.xiuxian.ui.theme.GoldLight
import com.karios.xiuxian.ui.theme.InkLine
import com.karios.xiuxian.ui.theme.InkSurface
import com.karios.xiuxian.ui.theme.JadeGreen
import com.karios.xiuxian.ui.theme.MutedText
import com.karios.xiuxian.ui.theme.PaperText
import com.karios.xiuxian.ui.theme.SerifFont

// ==================== 背包 ====================

@Composable
fun InventoryPanel(vm: GameViewModel, p: Player) {
    var selected by remember { mutableStateOf<String?>(null) }
    val entries = p.inventory.entries
        .filter { it.value > 0 }
        .sortedBy { Items.get(it.key)?.type?.ordinal ?: 9 }

    if (entries.isEmpty()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                "囊中空空。\n去山里转转吧，总能捡到点什么。",
                fontFamily = SerifFont,
                fontSize = 13.sp,
                color = MutedText,
                lineHeight = 22.sp
            )
        }
        return
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(entries.toList(), key = { it.key }) { e ->
            val def = Items.get(e.key)
            if (def != null) {
                ListRow(
                    title = def.name,
                    subtitle = "${def.type.label} · " + def.desc,
                    trailing = "×${e.value}",
                    trailingColor = if (def.type == ItemType.ARTIFACT && p.artifact == e.key)
                        Gold else GoldLight,
                    onClick = { selected = e.key }
                )
            }
        }
    }

    val sel = selected
    if (sel != null) {
        val def = Items.get(sel)
        if (def != null) {
            val usable = def.type == ItemType.PILL ||
                def.type == ItemType.BOOK ||
                def.type == ItemType.ARTIFACT
            AlertDialog(
                onDismissRequest = { selected = null },
                title = {
                    Text(
                        "${def.name} ×${p.itemCount(sel)}",
                        color = Gold,
                        fontFamily = SerifFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column {
                        Text(
                            def.desc,
                            color = PaperText,
                            fontFamily = SerifFont,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "类别：${def.type.label}　品阶：${"一二三四五".getOrNull(def.grade - 1) ?: "凡"}阶",
                            color = MutedText,
                            fontFamily = SerifFont,
                            fontSize = 12.sp
                        )
                        Text(
                            "坊市售价：${def.price} 灵石（回收 ${vm.sellPrice(def)}）",
                            color = MutedText,
                            fontFamily = SerifFont,
                            fontSize = 12.sp
                        )
                        if (def.type == ItemType.ARTIFACT) {
                            Text(
                                "攻 +${def.atkBonus} 防 +${def.defBonus} 血 +${def.hpBonus} 灵 +${def.mpBonus}",
                                color = JadeGreen,
                                fontFamily = SerifFont,
                                fontSize = 12.sp
                            )
                            if (p.artifact == sel) {
                                Text(
                                    "（当前已祭炼）",
                                    color = Gold,
                                    fontFamily = SerifFont,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        if (def.type == ItemType.BOOK) {
                            Text(
                                "记载功法：" + Techniques.name(def.teaches),
                                color = JadeGreen,
                                fontFamily = SerifFont,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                confirmButton = {
                    if (usable) {
                        TextButton(onClick = {
                            vm.useItem(sel)
                            selected = null
                        }) {
                            Text(
                                when (def.type) {
                                    ItemType.PILL -> "服用"
                                    ItemType.BOOK -> "研读"
                                    else -> "祭炼"
                                },
                                color = Gold,
                                fontFamily = SerifFont
                            )
                        }
                    } else {
                        TextButton(onClick = { selected = null }) {
                            Text("关闭", color = MutedText, fontFamily = SerifFont)
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        vm.sellItem(sel, 1)
                        selected = null
                    }) {
                        Text("卖出 1 个", color = BloodRed, fontFamily = SerifFont)
                    }
                },
                containerColor = InkSurface
            )
        }
    }
}

// ==================== 炼制 ====================

@Composable
fun CraftPanel(vm: GameViewModel, p: Player) {
    var type by remember { mutableStateOf(CraftType.ALCHEMY) }
    val list = vm.recipeList(type)

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            GhostButton("炼丹", Modifier.weight(1f)) { type = CraftType.ALCHEMY }
            GhostButton("炼器", Modifier.weight(1f)) { type = CraftType.FORGE }
            GhostButton("制符", Modifier.weight(1f)) { type = CraftType.TALISMAN }
        }
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list, key = { it.id }) { r ->
                val ok = vm.canCraft(r)
                PanelCard {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            r.name,
                            fontFamily = SerifFont,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ok) Gold else MutedText
                        )
                        Text(
                            "灵石 ${r.stones}　${r.days}日",
                            fontFamily = SerifFont,
                            fontSize = 11.sp,
                            color = MutedText
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "材料：" + r.materials.entries.joinToString("　") {
                            val have = p.itemCount(it.key)
                            "${Items.name(it.key)} ${have}/${it.value}"
                        },
                        fontFamily = SerifFont,
                        fontSize = 12.sp,
                        color = PaperText
                    )
                    Text(
                        "成功率约 " + (r.baseSuccess + p.comprehension * 2 - p.demonHeart / 4)
                            .coerceIn(5, 95) + "%　产物：" + Items.name(r.result) + "×" + r.resultCount,
                        fontFamily = SerifFont,
                        fontSize = 11.sp,
                        color = MutedText
                    )
                    Spacer(Modifier.height(8.dp))
                    GoldButton(
                        if (ok) "开 炉" else "材料不足",
                        Modifier.fillMaxWidth(),
                        enabled = ok
                    ) {
                        vm.craft(r.id)
                    }
                }
            }
        }
    }
}

// ==================== 坊市 ====================

@Composable
fun MarketPanel(vm: GameViewModel, p: Player) {
    var mode by remember { mutableStateOf(0) }
    val market = remember(mode, p.realmIndex) { vm.marketItems() }
    val own = p.inventory.entries.filter { it.value > 0 }
        .sortedBy { Items.get(it.key)?.price ?: 0 }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            GhostButton("购 买", Modifier.weight(1f)) { mode = 0 }
            GhostButton("出 售", Modifier.weight(1f)) { mode = 1 }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "灵石 ${p.stones}" + if (p.sectId == "s_wanbao") "　（万宝阁弟子：九折进货，六五折出货）" else "",
            fontFamily = SerifFont,
            fontSize = 12.sp,
            color = GoldLight
        )
        Spacer(Modifier.height(8.dp))

        if (mode == 0) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(market, key = { it.id }) { def ->
                    val price = vm.buyPrice(def)
                    ListRow(
                        title = def.name,
                        subtitle = def.type.label + " · " + def.desc,
                        trailing = "$price 灵石",
                        trailingColor = if (p.stones >= price) Gold else MutedText,
                        enabled = p.stones >= price,
                        onClick = { vm.buyItem(def.id) }
                    )
                }
            }
        } else {
            if (own.isEmpty()) {
                Text("你没有可以出手的东西。", fontFamily = SerifFont, fontSize = 13.sp, color = MutedText)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(own.toList(), key = { it.key }) { e ->
                        val def = Items.get(e.key)
                        if (def != null) {
                            ListRow(
                                title = def.name,
                                subtitle = "持有 ×${e.value}",
                                trailing = "卖 ${vm.sellPrice(def)}",
                                onClick = { vm.sellItem(e.key, 1) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== 洞府 ====================

@Composable
fun CavePanel(vm: GameViewModel, p: Player) {
    val cost = vm.caveUpgradeCost()
    val period = vm.caveHarvestPeriod()
    val ripe = p.caveHarvestDays >= period

    Column(
        Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        PanelCard {
            Text(
                "洞府 · ${p.caveLevel} 级",
                fontFamily = SerifFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gold
            )
            Spacer(Modifier.height(6.dp))
            KeyValueRow("修炼加成", "×" + fmt2(p.caveMul()), JadeGreen)
            KeyValueRow("灵田周期", "$period 日", PaperText)
            KeyValueRow(
                "灵田状态",
                if (ripe) "已成熟（可收获 ${p.caveHarvestDays / period} 次）"
                else "还需 ${period - p.caveHarvestDays} 日",
                if (ripe) Gold else MutedText
            )
            KeyValueRow("可产灵草", if (p.caveLevel >= 5) "天灵果" else "紫猴花", PaperText)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GoldButton(
                    if (p.caveLevel >= 10) "已至极致" else "扩建（$cost 灵石）",
                    Modifier.weight(1f),
                    enabled = p.caveLevel < 10 && p.stones >= cost
                ) {
                    vm.upgradeCave()
                }
                GhostButton("收 获 灵 田", Modifier.weight(1f), enabled = ripe) {
                    vm.harvestCave()
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        PanelCard {
            Text("洞府说明", fontFamily = SerifFont, fontSize = 14.sp, color = Gold)
            Spacer(Modifier.height(6.dp))
            Text(
                "洞府等级越高，聚灵阵越强，静修所得修为越多；灵田周期也会缩短。\n" +
                    "扩建需要 15 日，收获灵田不额外消耗时间。\n\n" +
                    "在凡人修仙的世界里，一个安稳的洞府，往往比一件法宝更值钱。",
                fontFamily = SerifFont,
                fontSize = 12.sp,
                color = MutedText,
                lineHeight = 20.sp
            )
        }
    }
}

// ==================== 宗门 ====================

@Composable
fun SectPanel(vm: GameViewModel, p: Player) {
    val sect = Sects.get(p.sectId)

    Column(
        Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        if (sect == null) {
            Text(
                "你目前是散修。\n散修自在，但无宗门庇护，也无月俸与差事。",
                fontFamily = SerifFont,
                fontSize = 13.sp,
                color = MutedText,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Sects.ALL, key = { it.id }) { s ->
                    val can = p.realmIndex >= s.minRealm
                    PanelCard {
                        Text(
                            s.name,
                            fontFamily = SerifFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (can) Gold else MutedText
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            s.desc,
                            fontFamily = SerifFont,
                            fontSize = 12.sp,
                            color = MutedText,
                            lineHeight = 18.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "入门可得 ${s.joinReward} 灵石" + if (can) "" else "（修为不足）",
                            fontFamily = SerifFont,
                            fontSize = 11.sp,
                            color = if (can) JadeGreen else BloodRed
                        )
                        Spacer(Modifier.height(8.dp))
                        GoldButton("拜 入 门 下", Modifier.fillMaxWidth(), enabled = can) {
                            vm.joinSect(s.id)
                        }
                    }
                }
            }
        } else {
            PanelCard {
                Text(
                    sect.name,
                    fontFamily = SerifFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )
                Spacer(Modifier.height(6.dp))
                KeyValueRow("宗门贡献", "${p.contribution}", GoldLight)
                KeyValueRow("累计斩妖", "${p.killCount} 头", PaperText)
            }
            Spacer(Modifier.height(12.dp))
            PanelCard {
                Text("差事", fontFamily = SerifFont, fontSize = 15.sp, color = Gold)
                Spacer(Modifier.height(8.dp))
                val q = p.quest
                if (q == null) {
                    Text(
                        "暂无差事。接一桩活，换点灵石和贡献。",
                        fontFamily = SerifFont,
                        fontSize = 12.sp,
                        color = MutedText
                    )
                    Spacer(Modifier.height(8.dp))
                    GoldButton("接 取 差 事", Modifier.fillMaxWidth()) { vm.takeQuest() }
                } else {
                    Text(
                        q.name,
                        fontFamily = SerifFont,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PaperText
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        q.desc,
                        fontFamily = SerifFont,
                        fontSize = 12.sp,
                        color = MutedText
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (q.type == "COLLECT")
                            "进度：${p.itemCount(q.itemId)}/${q.targetCount}"
                        else
                            "进度：${q.progress}/${q.targetCount}",
                        fontFamily = SerifFont,
                        fontSize = 12.sp,
                        color = GoldLight
                    )
                    Text(
                        "报酬：灵石 ${q.rewardStones}，贡献 ${q.rewardContribution}",
                        fontFamily = SerifFont,
                        fontSize = 12.sp,
                        color = JadeGreen
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GoldButton("交 付", Modifier.weight(1f)) { vm.submitQuest() }
                        GhostButton("放 弃", Modifier.weight(1f)) { vm.abandonQuest() }
                    }
                }
            }
        }
    }
}

// ==================== 角色 ====================

@Composable
fun CharacterPanel(vm: GameViewModel, p: Player) {
    val techs = p.techniques
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            PanelCard {
                Text(
                    "${p.name} · ${p.realmText}",
                    fontFamily = SerifFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )
                Spacer(Modifier.height(6.dp))
                KeyValueRow("灵根", p.roots.display, GoldLight)
                KeyValueRow("悟性", "${p.comprehension} / 10", PaperText)
                KeyValueRow("气运", "${p.luck} / 10", PaperText)
                KeyValueRow("气血", "${p.hpCur} / ${p.maxHp()}", BloodRed)
                KeyValueRow("灵力", "${p.mpCur} / ${p.maxMp()}", JadeGreen)
                KeyValueRow("攻击", "${p.atk()}", PaperText)
                KeyValueRow("防御", "${p.def()}", PaperText)
                KeyValueRow("身法", "${p.spd()}", PaperText)
                KeyValueRow("心魔", "${p.demonHeart} / 100", if (p.demonHeart >= 60) BloodRed else MutedText)
                KeyValueRow("法宝", if (p.artifact.isEmpty()) "无" else Items.name(p.artifact), GoldLight)
            }
        }
        item {
            PanelCard {
                SectionTitle("功法")
                Spacer(Modifier.height(8.dp))
                if (techs.isEmpty()) {
                    Text("尚未习得任何功法。", fontFamily = SerifFont, fontSize = 12.sp, color = MutedText)
                } else {
                    for (id in techs) {
                        val t = Techniques.get(id) ?: continue
                        val isMain = p.mainTechnique == id
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "《${t.name}》${t.gradeName}" + if (isMain) "　主修" else "",
                                    fontFamily = SerifFont,
                                    fontSize = 14.sp,
                                    color = if (isMain) Gold else PaperText
                                )
                                Text(
                                    if (t.kind == TechKind.MAIN)
                                        "修炼 ×" + fmt2(t.expMul)
                                    else
                                        "耗灵 ${t.mpCost}　威力 ×" + fmt2(t.power),
                                    fontFamily = SerifFont,
                                    fontSize = 11.sp,
                                    color = MutedText
                                )
                            }
                            if (t.kind == TechKind.MAIN && !isMain) {
                                TextButton(onClick = { vm.setMainTechnique(id) }) {
                                    Text("主修", color = GoldLight, fontFamily = SerifFont, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            PanelCard {
                SectionTitle("随身之物")
                Spacer(Modifier.height(6.dp))
                Text(
                    "灵石 ${p.stones}　妖丹 ${p.itemCount("mat_yaodan")}　妖丹素材可卖可炼。",
                    fontFamily = SerifFont,
                    fontSize = 12.sp,
                    color = MutedText
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "累计突破 ${p.breakCount} 次　斩杀妖兽 ${p.killCount} 头",
                    fontFamily = SerifFont,
                    fontSize = 12.sp,
                    color = InkLine
                )
            }
        }
    }
}