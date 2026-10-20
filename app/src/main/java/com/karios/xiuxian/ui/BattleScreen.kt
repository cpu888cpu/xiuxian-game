package com.karios.xiuxian.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karios.xiuxian.data.Enemies
import com.karios.xiuxian.data.Items
import com.karios.xiuxian.data.Techniques
import com.karios.xiuxian.logic.GameViewModel
import com.karios.xiuxian.logic.battlePills
import com.karios.xiuxian.logic.battleSpells
import com.karios.xiuxian.logic.battleTalismans
import com.karios.xiuxian.logic.itemCount
import com.karios.xiuxian.logic.maxHp
import com.karios.xiuxian.logic.maxMp
import com.karios.xiuxian.logic.playerAttack
import com.karios.xiuxian.logic.playerCastSpell
import com.karios.xiuxian.logic.playerFlee
import com.karios.xiuxian.logic.playerUseArtifact
import com.karios.xiuxian.logic.useTalisman
import com.karios.xiuxian.ui.theme.BloodRed
import com.karios.xiuxian.ui.theme.Gold
import com.karios.xiuxian.ui.theme.GoldLight
import com.karios.xiuxian.ui.theme.InkBlack
import com.karios.xiuxian.ui.theme.InkSurface
import com.karios.xiuxian.ui.theme.JadeGreen
import com.karios.xiuxian.ui.theme.MutedText
import com.karios.xiuxian.ui.theme.PaperText
import com.karios.xiuxian.ui.theme.SerifFont

@Composable
fun BattleScreen(vm: GameViewModel) {
    val p = vm.player ?: return
    val b = vm.battle ?: return
    val e = Enemies.get(b.enemyId) ?: return
    val spells = vm.battleSpells()
    val talismans = vm.battleTalismans()
    val pills = vm.battlePills()

    Column(
        Modifier
            .fillMaxSize()
            .background(InkBlack)
            .padding(12.dp)
    ) {
        // 敌人
        PanelCard(borderColor = BloodRed) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    e.name,
                    fontFamily = SerifFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BloodRed
                )
                Text(
                    "${b.enemyHp} / ${b.enemyMaxHp}",
                    fontFamily = SerifFont,
                    fontSize = 13.sp,
                    color = MutedText
                )
            }
            Spacer(Modifier.height(4.dp))
            InkProgress(
                b.enemyHp.toFloat() / b.enemyMaxHp.toFloat().coerceAtLeast(1f),
                BloodRed,
                height = 8.dp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                e.desc,
                fontFamily = SerifFont,
                fontSize = 11.sp,
                color = MutedText,
                lineHeight = 16.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        // 自己
        PanelCard {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${p.name} · ${p.realmText}",
                    fontFamily = SerifFont,
                    fontSize = 14.sp,
                    color = Gold
                )
                if (b.playerShield > 0) {
                    Text(
                        "护体 ${b.playerShield}",
                        fontFamily = SerifFont,
                        fontSize = 11.sp,
                        color = JadeGreen
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "气血 ${p.hpCur}/${p.maxHp()}",
                        fontFamily = SerifFont,
                        fontSize = 10.sp,
                        color = MutedText
                    )
                    Spacer(Modifier.height(2.dp))
                    InkProgress(p.hpCur.toFloat() / p.maxHp().toFloat(), BloodRed, height = 6.dp)
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        "灵力 ${p.mpCur}/${p.maxMp()}",
                        fontFamily = SerifFont,
                        fontSize = 10.sp,
                        color = MutedText
                    )
                    Spacer(Modifier.height(2.dp))
                    InkProgress(p.mpCur.toFloat() / p.maxMp().toFloat(), JadeGreen, height = 6.dp)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 战报
        Text("战况", fontFamily = SerifFont, fontSize = 13.sp, color = Gold)
        Spacer(Modifier.height(4.dp))
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(InkSurface)
                .padding(8.dp)
        ) {
            items(b.log.reversed()) { line ->
                Text(
                    line,
                    fontFamily = SerifFont,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = if (line.startsWith("你")) PaperText else MutedText,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (b.over) {
            if (b.victory) {
                Text(
                    "胜！",
                    fontFamily = SerifFont,
                    fontSize = 16.sp,
                    color = Gold,
                    fontWeight = FontWeight.Bold
                )
            } else if (b.fled) {
                Text("你活着离开了。有时候这就够了。", fontFamily = SerifFont, fontSize = 13.sp, color = JadeGreen)
            } else {
                Text("败。", fontFamily = SerifFont, fontSize = 16.sp, color = BloodRed, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            GoldButton("收 拾 离 开", Modifier.fillMaxWidth()) {
                vm.endBattle()
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GoldButton("攻 击", Modifier.weight(1f)) { vm.playerAttack() }
                GhostButton("法 宝", Modifier.weight(1f)) { vm.playerUseArtifact() }
                GhostButton("逃 跑", Modifier.weight(1f)) { vm.playerFlee() }
            }

            if (spells.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("法术", fontFamily = SerifFont, fontSize = 11.sp, color = MutedText)
                    for (id in spells) {
                        val t = Techniques.get(id) ?: continue
                        GhostButton("${t.name} ${t.mpCost}灵") { vm.playerCastSpell(id) }
                    }
                }
            }

            if (talismans.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("符箓", fontFamily = SerifFont, fontSize = 11.sp, color = MutedText)
                    for (id in talismans) {
                        GhostButton("${Items.name(id)}×${p.itemCount(id)}") { vm.useTalisman(id) }
                    }
                }
            }

            if (pills.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("丹药", fontFamily = SerifFont, fontSize = 11.sp, color = MutedText)
                    for (id in pills) {
                        GhostButton("${Items.name(id)}×${p.itemCount(id)}") { vm.useItem(id) }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))
            Text(
                "第 ${b.turn + 1} 回合 · 心魔 ${p.demonHeart}/100",
                fontFamily = SerifFont,
                fontSize = 10.sp,
                color = GoldLight
            )
        }
    }
}