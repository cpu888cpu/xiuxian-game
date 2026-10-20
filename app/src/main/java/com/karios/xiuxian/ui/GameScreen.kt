package com.karios.xiuxian.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import com.karios.xiuxian.data.Regions
import com.karios.xiuxian.logic.GameViewModel
import com.karios.xiuxian.logic.LogLine
import com.karios.xiuxian.logic.ageYears
import com.karios.xiuxian.logic.breakChanceFor
import com.karios.xiuxian.logic.expNeeded
import com.karios.xiuxian.logic.isMaxRealm
import com.karios.xiuxian.logic.lifeYears
import com.karios.xiuxian.logic.maxHp
import com.karios.xiuxian.logic.maxMp
import com.karios.xiuxian.logic.realmDef
import com.karios.xiuxian.logic.realmText
import com.karios.xiuxian.model.Player
import com.karios.xiuxian.ui.theme.BloodRed
import com.karios.xiuxian.ui.theme.Gold
import com.karios.xiuxian.ui.theme.GoldLight
import com.karios.xiuxian.ui.theme.InkBlack
import com.karios.xiuxian.ui.theme.InkLine
import com.karios.xiuxian.ui.theme.InkSurface
import com.karios.xiuxian.ui.theme.JadeGreen
import com.karios.xiuxian.ui.theme.MutedText
import com.karios.xiuxian.ui.theme.PaperText
import com.karios.xiuxian.ui.theme.SerifFont

@Composable
fun GameScreen(vm: GameViewModel) {
    val p = vm.player ?: return
    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("修行", "探索", "背包", "炼制", "坊市", "洞府", "宗门", "角色")

    Column(
        Modifier
            .fillMaxSize()
            .background(InkBlack)
    ) {
        StatusHeader(p)

        Box(Modifier.fillMaxWidth()) {
            ScrollableTabRow(
                selectedTabIndex = tab,
                containerColor = InkSurface,
                contentColor = Gold,
                edgePadding = 0.dp
            ) {
                tabs.forEachIndexed { i, s ->
                    Tab(
                        selected = tab == i,
                        onClick = { tab = i },
                        selectedContentColor = Gold,
                        unselectedContentColor = MutedText,
                        text = {
                            Text(
                                s,
                                fontFamily = SerifFont,
                                fontSize = 14.sp,
                                fontWeight = if (tab == i) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        Box(Modifier.weight(1f)) {
            when (tab) {
                0 -> CultivatePanel(vm, p)
                1 -> ExplorePanel(vm, p)
                2 -> InventoryPanel(vm, p)
                3 -> CraftPanel(vm, p)
                4 -> MarketPanel(vm, p)
                5 -> CavePanel(vm, p)
                6 -> SectPanel(vm, p)
                else -> CharacterPanel(vm, p)
            }
        }
    }
}

@Composable
private fun StatusHeader(p: Player) {
    val expNeed = p.expNeeded().coerceAtLeast(1L)
    val expRatio = (p.exp.toFloat() / expNeed.toFloat()).coerceIn(0f, 1f)
    val hpRatio = (p.hpCur.toFloat() / p.maxHp().toFloat()).coerceIn(0f, 1f)
    val mpRatio = (p.mpCur.toFloat() / p.maxMp().toFloat()).coerceIn(0f, 1f)
    val danger = p.demonHeart >= 60

    Column(
        Modifier
            .fillMaxWidth()
            .background(InkSurface)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${p.name} · ${p.realmText}",
                fontFamily = SerifFont,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Gold
            )
            Text(
                "灵石 ${p.stones}",
                fontFamily = SerifFont,
                fontSize = 13.sp,
                color = GoldLight
            )
        }
        Spacer(Modifier.height(3.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${p.ageYears} 岁 / 寿元 ${p.lifeYears}",
                fontFamily = SerifFont,
                fontSize = 11.sp,
                color = MutedText
            )
            Text(
                "心魔 ${p.demonHeart}/100",
                fontFamily = SerifFont,
                fontSize = 11.sp,
                color = if (danger) BloodRed else MutedText
            )
        }
        Spacer(Modifier.height(6.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("修为", fontFamily = SerifFont, fontSize = 10.sp, color = MutedText)
            Text(
                "${p.exp} / ${p.expNeeded()}",
                fontFamily = SerifFont,
                fontSize = 10.sp,
                color = Gold
            )
        }
        Spacer(Modifier.height(2.dp))
        InkProgress(expRatio, Gold, height = 6.dp)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    "气血 ${p.hpCur}/${p.maxHp()}",
                    fontFamily = SerifFont,
                    fontSize = 10.sp,
                    color = MutedText
                )
                Spacer(Modifier.height(2.dp))
                InkProgress(hpRatio, BloodRed, height = 4.dp)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    "灵力 ${p.mpCur}/${p.maxMp()}",
                    fontFamily = SerifFont,
                    fontSize = 10.sp,
                    color = MutedText
                )
                Spacer(Modifier.height(2.dp))
                InkProgress(mpRatio, JadeGreen, height = 4.dp)
            }
        }
    }
}

@Composable
private fun LogRow(line: LogLine) {
    val color = when (line.type) {
        1 -> JadeGreen
        2 -> BloodRed
        3 -> GoldLight
        else -> PaperText
    }
    Text(
        line.text,
        fontFamily = SerifFont,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = color,
        modifier = Modifier.padding(vertical = 3.dp)
    )
}

@Composable
private fun CultivatePanel(vm: GameViewModel, p: Player) {
    val isBig = p.subIndex >= p.realmDef.subs.lastIndex
    val chance = p.breakChanceFor(isBig)
    val canBreak = p.exp >= p.expNeeded()

    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        PanelCard {
            Text(
                if (isBig) "距下一境：" + nextRealmName(p) else "下一层：" + nextSubName(p),
                fontFamily = SerifFont,
                fontSize = 14.sp,
                color = GoldLight
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "修炼倍率 ×" + fmt2(p.roots.speedMul) +
                    "　洞府加成 ×" + fmt2(1.0 + 0.1 * (p.caveLevel - 1)) +
                    "　心魔压制 " + (100 - p.demonHeart / 4) + "%",
                fontFamily = SerifFont,
                fontSize = 11.sp,
                color = MutedText
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GoldButton("静修（${fmtDays(p.realmDef.medDays)}）", Modifier.weight(1f)) {
                    vm.meditate(false)
                }
                GhostButton("闭关（6倍）", Modifier.weight(1f)) {
                    vm.meditate(true)
                }
            }
            Spacer(Modifier.height(8.dp))
            GoldButton(
                if (canBreak) "冲击瓶颈（$chance%）" else "修为不足，无法突破",
                Modifier.fillMaxWidth(),
                enabled = canBreak
            ) {
                vm.breakthrough()
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            GhostButton("存 1", Modifier.weight(1f)) { vm.saveTo(1) }
            GhostButton("存 2", Modifier.weight(1f)) { vm.saveTo(2) }
            GhostButton("存 3", Modifier.weight(1f)) { vm.saveTo(3) }
            GhostButton("主菜单", Modifier.weight(1f)) { vm.backToMenu() }
        }

        Spacer(Modifier.height(10.dp))
        SectionTitle("近事")
        Spacer(Modifier.height(6.dp))
        if (vm.logs.isEmpty()) {
            Text(
                "尚无记载。",
                fontFamily = SerifFont,
                fontSize = 13.sp,
                color = MutedText
            )
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(vm.logs.reversed()) { line ->
                    LogRow(line)
                }
            }
        }
    }
}

private fun nextRealmName(p: Player): String {
    val idx = p.realmIndex + 1
    return if (idx < com.karios.xiuxian.data.REALMS.size)
        com.karios.xiuxian.data.REALMS[idx].name + "初期"
    else "飞升"
}

private fun nextSubName(p: Player): String {
    val subs = p.realmDef.subs
    val idx = p.subIndex + 1
    return if (idx < subs.size) p.realmDef.name + subs[idx] else "下一境"
}

@Composable
private fun ExplorePanel(vm: GameViewModel, p: Player) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "外出历练\n每一次探索都会消耗寿元，也可能遇到妖兽、灵草、机缘或者尸体。",
                fontFamily = SerifFont,
                fontSize = 12.sp,
                color = MutedText,
                lineHeight = 20.sp
            )
        }
        items(Regions.ALL) { region ->
            val unlocked = vm.isUnlocked(region)
            PanelCard(borderColor = if (unlocked) InkLine else InkSurface) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        region.name,
                        fontFamily = SerifFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (unlocked) Gold else MutedText
                    )
                    Text(
                        "${region.days} 日",
                        fontFamily = SerifFont,
                        fontSize = 12.sp,
                        color = MutedText
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    region.desc,
                    fontFamily = SerifFont,
                    fontSize = 12.sp,
                    color = MutedText,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (unlocked) "凶险：可进入" else
                            "需 " + com.karios.xiuxian.data.REALMS[region.minRealm].name +
                                (com.karios.xiuxian.data.REALMS[region.minRealm].subs.getOrNull(region.minSub) ?: ""),
                        fontFamily = SerifFont,
                        fontSize = 11.sp,
                        color = if (unlocked) JadeGreen else BloodRed
                    )
                    GoldButton(
                        "探 索",
                        enabled = unlocked
                    ) {
                        vm.explore(region.id)
                    }
                }
            }
        }
    }
}

internal fun fmt2(v: Double): String {
    val n = (v * 100).toInt()
    return "${n / 100}.${(n % 100).toString().padStart(2, '0')}"
}

/** 天数格式化：不足一年显示日，超过显示年 */
internal fun fmtDays(d: Int): String {
    return if (d >= 365) {
        val tenths = (d * 10L / 365).toInt()
        "${tenths / 10}.${tenths % 10}年"
    } else {
        "${d}日"
    }
}