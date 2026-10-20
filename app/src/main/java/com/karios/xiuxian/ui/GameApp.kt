package com.karios.xiuxian.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karios.xiuxian.data.randomSpiritRoot
import com.karios.xiuxian.logic.GameViewModel
import com.karios.xiuxian.logic.Screen
import com.karios.xiuxian.logic.ageYears
import com.karios.xiuxian.logic.lifeYears
import com.karios.xiuxian.logic.realmText
import com.karios.xiuxian.ui.theme.Gold
import com.karios.xiuxian.ui.theme.GoldLight
import com.karios.xiuxian.ui.theme.InkBlack
import com.karios.xiuxian.ui.theme.InkLine
import com.karios.xiuxian.ui.theme.InkSurface
import com.karios.xiuxian.ui.theme.MutedText
import com.karios.xiuxian.ui.theme.PaperText
import com.karios.xiuxian.ui.theme.SerifFont

@Composable
fun GameApp(vm: GameViewModel = viewModel()) {
    when (vm.screen) {
        Screen.MENU -> MenuScreen(vm)
        Screen.CREATE -> CreateScreen(vm)
        Screen.GAME -> GameScreen(vm)
        Screen.BATTLE -> BattleScreen(vm)
    }

    val title = vm.gameOverTitle
    if (title != null) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = { vm.dismissGameOver() }) {
                    Text("回主菜单", color = Gold, fontFamily = SerifFont)
                }
            },
            title = {
                Text(
                    title,
                    color = Gold,
                    fontFamily = SerifFont,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    vm.gameOverBody,
                    color = PaperText,
                    fontFamily = SerifFont,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            },
            containerColor = InkSurface
        )
    }
}

@Composable
fun MenuScreen(vm: GameViewModel) {
    var showLoad by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var hasAuto by remember { mutableStateOf(vm.hasSave(0)) }

    Column(
        Modifier
            .fillMaxSize()
            .background(InkBlack)
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "凡人问道",
            fontFamily = SerifFont,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Gold,
            letterSpacing = 6.sp
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "—— 灵根凡骨，亦可逆天 ——",
            fontFamily = SerifFont,
            fontSize = 14.sp,
            color = MutedText,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(44.dp))

        if (hasAuto) {
            GoldButton("继 续 修 行", Modifier.fillMaxWidth()) {
                vm.continueGame()
            }
            Spacer(Modifier.height(12.dp))
        }

        GoldButton("重 开 一 世", Modifier.fillMaxWidth()) {
            vm.goCreate()
        }
        Spacer(Modifier.height(12.dp))

        GhostButton("读 取 存 档", Modifier.fillMaxWidth()) {
            hasAuto = vm.hasSave(0)
            showLoad = true
        }
        Spacer(Modifier.height(12.dp))

        GhostButton("关 于 本 作", Modifier.fillMaxWidth()) {
            showAbout = true
        }

        Spacer(Modifier.height(40.dp))
        Text(
            "此间世界，寿元为限，心魔为敌。\n你能走多远，全看你怎么取舍。",
            fontFamily = SerifFont,
            fontSize = 12.sp,
            color = MutedText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }

    if (showLoad) {
        val metas = remember { (1..3).map { it to vm.slotMeta(it) } }
        AlertDialog(
            onDismissRequest = { showLoad = false },
            confirmButton = {
                TextButton(onClick = { showLoad = false }) {
                    Text("关闭", color = Gold, fontFamily = SerifFont)
                }
            },
            title = {
                Text("存档", color = Gold, fontFamily = SerifFont, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    for ((i, meta) in metas) {
                        if (meta == null) {
                            Text(
                                "第 $i 号　—— 空 ——",
                                fontFamily = SerifFont,
                                fontSize = 14.sp,
                                color = MutedText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            )
                        } else {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    "第 $i 号　${meta.name}　${meta.realmText}",
                                    fontFamily = SerifFont,
                                    fontSize = 15.sp,
                                    color = PaperText
                                )
                                Text(
                                    "${meta.ageYears} 岁 / 寿元 ${meta.lifeYears}",
                                    fontFamily = SerifFont,
                                    fontSize = 12.sp,
                                    color = MutedText
                                )
                                TextButton(onClick = {
                                    vm.loadFrom(i)
                                    showLoad = false
                                }) {
                                    Text("读取", color = GoldLight, fontFamily = SerifFont)
                                }
                            }
                        }
                    }
                }
            },
            containerColor = InkSurface
        )
    }

    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("知道了", color = Gold, fontFamily = SerifFont)
                }
            },
            title = {
                Text("凡人问道", color = Gold, fontFamily = SerifFont, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "一款纯离线的文字修仙游戏。\n\n" +
                        "· 随机灵根，资质决定起点\n" +
                        "· 打坐、闭关、突破、渡劫\n" +
                        "· 回合制战斗：法术、法宝、符箓、丹药、逃跑\n" +
                        "· 采集、炼丹、炼器、制符、坊市、宗门、洞府\n" +
                        "· 寿元有限，心魔缠身\n\n" +
                        "存档自动保存，另有 3 个手动存档槽。",
                    color = PaperText,
                    fontFamily = SerifFont,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            },
            containerColor = InkSurface
        )
    }
}

@Composable
fun CreateScreen(vm: GameViewModel) {
    var name by remember { mutableStateOf("") }
    var roots by remember { mutableStateOf(randomSpiritRoot()) }
    var rollCount by remember { mutableStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .background(InkBlack)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            "测 灵 根",
            fontFamily = SerifFont,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Gold,
            letterSpacing = 4.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "灵根决定你这一生的上限。有人天生天灵根，有人只有伪灵根——但路是自己走的。",
            fontFamily = SerifFont,
            fontSize = 13.sp,
            color = MutedText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))

        PanelCard {
            Text(
                roots.display,
                fontFamily = SerifFont,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight
            )
            Spacer(Modifier.height(8.dp))
            KeyValueRow("修炼速度", "×" + fmt2(roots.speedMul), Gold)
            KeyValueRow(
                "资质评价",
                when {
                    roots.speedMul >= 2.3 -> "万中无一的妖孽"
                    roots.speedMul >= 1.4 -> "上等资质，可入大宗门"
                    roots.speedMul >= 1.0 -> "中等资质，勤能补拙"
                    else -> "凡骨，唯有死磕"
                },
                if (roots.speedMul >= 1.4) Gold else MutedText
            )
            if (rollCount > 0) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "已重测 $rollCount 次",
                    fontFamily = SerifFont,
                    fontSize = 11.sp,
                    color = MutedText
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        GhostButton("重 测 灵 根", Modifier.fillMaxWidth()) {
            roots = randomSpiritRoot()
            rollCount++
        }

        Spacer(Modifier.height(24.dp))
        PanelCard {
            Text("道号", fontFamily = SerifFont, fontSize = 14.sp, color = MutedText)
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { if (it.length <= 8) name = it },
                placeholder = {
                    Text("无名散修", color = MutedText, fontFamily = SerifFont)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Gold,
                    unfocusedBorderColor = InkLine,
                    focusedTextColor = PaperText,
                    unfocusedTextColor = PaperText,
                    cursorColor = Gold,
                    focusedContainerColor = InkBlack,
                    unfocusedContainerColor = InkBlack
                )
            )
        }

        Spacer(Modifier.height(28.dp))
        GoldButton("踏 入 仙 途", Modifier.fillMaxWidth()) {
            vm.startNewGame(name, roots)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "十六岁，炼气一层。寿元一百三十年。",
            fontFamily = SerifFont,
            fontSize = 12.sp,
            color = MutedText
        )
        Spacer(Modifier.height(20.dp))
        TextButton(onClick = { vm.backToMenu() }) {
            Text("返回", color = MutedText, fontFamily = SerifFont)
        }
        Spacer(Modifier.height(20.dp))
    }
}

// fmt2 见 GameScreen.kt