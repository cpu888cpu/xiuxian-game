package com.karios.xiuxian.logic

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.karios.xiuxian.data.EvtEffect
import com.karios.xiuxian.model.ItemType
import com.karios.xiuxian.data.Items
import com.karios.xiuxian.data.REALMS
import com.karios.xiuxian.data.Recipe
import com.karios.xiuxian.data.Recipes
import com.karios.xiuxian.data.RegionDef
import com.karios.xiuxian.data.Regions
import com.karios.xiuxian.data.Sects
import com.karios.xiuxian.data.SpecialEvent
import com.karios.xiuxian.data.TechKind
import com.karios.xiuxian.data.Techniques
import com.karios.xiuxian.model.ItemDef
import com.karios.xiuxian.model.Player
import com.karios.xiuxian.model.Quest
import com.karios.xiuxian.model.SpiritRoot
import com.karios.xiuxian.save.SaveManager
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

enum class Screen { MENU, CREATE, GAME, BATTLE }

data class LogLine(val text: String, val type: Int = 0)

class GameViewModel(app: Application) : AndroidViewModel(app) {

    var screen by mutableStateOf(Screen.MENU)
        private set

    var player by mutableStateOf<Player?>(null)
        private set

    var battle by mutableStateOf<com.karios.xiuxian.model.BattleState?>(null)
        private set

    val logs = mutableStateListOf<LogLine>()

    var gameOverTitle by mutableStateOf<String?>(null)
        private set

    var gameOverBody by mutableStateOf("")
        private set

    var message by mutableStateOf<String?>(null)

    // ================= 基础工具 =================

    fun addLog(text: String, type: Int = 0) {
        logs.add(LogLine(text, type))
        if (logs.size > 300) {
            repeat(logs.size - 300) { logs.removeAt(0) }
        }
    }

    internal fun autoSave() {
        player?.let { SaveManager.save(getApplication(), 0, it) }
    }

    internal fun grantItem(p: Player, id: String, count: Int) {
        p.inventory[id] = (p.inventory[id] ?: 0) + count
    }

    internal fun removeItem(p: Player, id: String, count: Int): Boolean {
        val cur = p.inventory[id] ?: 0
        if (cur < count) return false
        if (cur == count) p.inventory.remove(id) else p.inventory[id] = cur - count
        return true
    }

    private fun healFull(p: Player) {
        p.hpCur = p.maxHp()
        p.mpCur = p.maxMp()
    }

    internal fun advanceDays(p: Player, days: Int) {
        p.ageDays += days
        p.caveHarvestDays += days
        if (!p.dead && p.ageYears >= p.lifeYears) {
            p.dead = true
            p.ending = "寿元耗尽"
            gameOverTitle = "大限已至"
            gameOverBody = "${p.name}终究没能等到那一线生机。\n寿元 ${p.lifeYears} 年，止步于${p.realmText}。\n\n修仙路上，最不缺的就是白骨。"
        }
    }

    // ================= 开局 / 存档 =================

    fun startNewGame(name: String, roots: SpiritRoot) {
        val p = Player(
            name = name.ifBlank { "无名散修" },
            roots = roots,
            comprehension = 3 + Random.nextInt(6),
            luck = 3 + Random.nextInt(6),
            stones = 30
        )
        p.inventory["pill_liaoshang"] = 2
        p.inventory["herb_juling"] = 2
        p.inventory["mat_fuzhi"] = 2
        p.techniques.add("tech_yinqi")
        p.mainTechnique = "tech_yinqi"
        p.hpCur = p.maxHp()
        p.mpCur = p.maxMp()
        player = p
        logs.clear()
        gameOverTitle = null
        gameOverBody = ""
        addLog("你本是一介凡人，机缘巧合得了半部《引气诀》，自此踏入仙途。", 3)
        addLog("灵根：${roots.display}　悟性 ${p.comprehension}　气运 ${p.luck}", 1)
        addLog("炼气一层，寿元 ${p.lifeYears} 年。路还长，慢慢来。", 1)
        screen = Screen.GAME
        autoSave()
    }

    fun hasSave(slot: Int): Boolean = SaveManager.exists(getApplication(), slot)

    fun slotMeta(slot: Int): Player? = SaveManager.load(getApplication(), slot)

    fun saveTo(slot: Int) {
        val p = player ?: return
        if (slot == 0) return
        if (SaveManager.save(getApplication(), slot, p)) {
            addLog("已存入第 $slot 号存档。", 1)
            message = "已保存"
        } else {
            addLog("存档写入失败。", 2)
        }
    }

    fun loadFrom(slot: Int) {
        val p = SaveManager.load(getApplication(), slot)
        if (p == null) {
            addLog("该存档为空或已损坏。", 2)
            return
        }
        player = p
        logs.clear()
        gameOverTitle = null
        gameOverBody = ""
        addLog("读取存档：${p.name}（${p.realmText}）", 1)
        screen = Screen.GAME
    }

    fun deleteSlot(slot: Int) {
        SaveManager.delete(getApplication(), slot)
        message = "已删除第 $slot 号存档"
    }

    fun continueGame() {
        val p = SaveManager.load(getApplication(), 0)
        if (p == null) {
            message = "还没有自动存档"
            return
        }
        player = p
        logs.clear()
        gameOverTitle = null
        gameOverBody = ""
        addLog("你从沉睡中醒来，继续未完的路。", 1)
        screen = Screen.GAME
    }

    fun backToMenu() {
        screen = Screen.MENU
        battle = null
    }

    fun goCreate() {
        screen = Screen.CREATE
    }

    fun dismissGameOver() {
        gameOverTitle = null
        gameOverBody = ""
        screen = Screen.MENU
    }

    // ================= 修炼 =================

    fun meditate(isLong: Boolean) {
        val p = player ?: return
        if (p.dead) return
        if (p.exp >= p.expNeeded()) {
            addLog("修为已满，再坐下去也是徒劳，当先冲击瓶颈。", 2)
            return
        }
        val r = p.realmDef
        val times = if (isLong) 6 else 1
        var i = 0
        var gained = 0L
        var taken = 0
        while (i < times && p.exp < p.expNeeded()) {
            val need = p.expNeeded()
            val g = p.cultivateGain().toLong().coerceAtLeast(1L)
            val real = min(g, need - p.exp)
            p.exp += real
            gained += real
            taken += r.medDays
            i++
        }
        if (isLong) p.demonHeart = (p.demonHeart + 3).coerceAtMost(100)
        else p.demonHeart = (p.demonHeart - 1).coerceAtLeast(0)
        advanceDays(p, taken)
        p.hpCur = min(p.maxHp(), p.hpCur + p.maxHp() * taken / 300)
        p.mpCur = min(p.maxMp(), p.mpCur + p.maxMp() * taken / 300)
        if (isLong) {
            addLog("你闭关 ${taken} 日，出关时修为增长 ${gained}。", 1)
        } else {
            addLog("静修 ${taken} 日，修为增长 ${gained}。", 1)
        }
        if (p.demonHeart >= 60) addLog("心魔渐深，你偶尔会听见有人在耳边低语。", 2)
        player = p.copy()
        autoSave()
    }

    fun breakthrough() {
        val p = player ?: return
        if (p.dead) return
        if (p.isMaxRealm) {
            p.dead = true
            p.ending = "飞升"
            gameOverTitle = "白日飞升"
            gameOverBody = "最后一层桎梏碎尽，天地为之震动。\n${p.name}踏破虚空，飘然远去。\n\n凡人之躯，终至大道。"
            player = p.copy()
            SaveManager.delete(getApplication(), 0)
            return
        }
        if (p.exp < p.expNeeded()) {
            addLog("修为不足，突破无从谈起。", 2)
            return
        }
        val isBig = p.subIndex >= p.realmDef.subs.lastIndex
        val chance = p.breakChanceFor(isBig)
        val days = if (isBig) p.realmDef.medDays else p.realmDef.medDays / 2
        advanceDays(p, days)
        if (p.dead) {
            player = p.copy()
            return
        }
        if (Random.nextInt(100) < chance) {
            p.exp = 0L
            if (isBig) {
                p.realmIndex++
                p.subIndex = 0
                p.failBonus = 0
                p.breakBonus = 0
                p.demonHeart = (p.demonHeart - 10).coerceAtLeast(0)
                healFull(p)
                addLog("轰然一声，瓶颈破碎！你已是【${p.realmText}】，寿元增至 ${p.lifeYears} 年。", 3)
            } else {
                p.subIndex++
                p.breakBonus = 0
                p.demonHeart = (p.demonHeart - 3).coerceAtLeast(0)
                healFull(p)
                addLog("水到渠成，你进境至【${p.realmText}】。", 1)
            }
            p.breakCount++
        } else {
            p.exp = (p.exp * (if (isBig) 0.4 else 0.78)).toLong()
            p.hpCur = (p.hpCur * 0.4).toInt().coerceAtLeast(1)
            p.demonHeart = (p.demonHeart + (if (isBig) 5 else 3)).coerceAtMost(100)
                if (isBig) p.failBonus += 12
            p.breakBonus = 0
            advanceDays(p, days)
            addLog("冲关失败，灵力反噬，修为大损，你喷出一口鲜血。（成功率 $chance%）", 2)
        }
        player = p.copy()
        autoSave()
    }

    // ================= 探索 =================

    private fun pickEvent(region: RegionDef): SpecialEvent {
        val total = region.events.sumOf { it.weight }
        var r = Random.nextInt(max(1, total))
        for (e in region.events) {
            r -= e.weight
            if (r < 0) return e
        }
        return region.events.first()
    }

    private fun regionUnlocked(p: Player, region: RegionDef): Boolean {
        return p.realmIndex > region.minRealm ||
            (p.realmIndex == region.minRealm && p.subIndex >= region.minSub)
    }

    fun isUnlocked(region: RegionDef): Boolean {
        val p = player ?: return false
        return regionUnlocked(p, region)
    }

    fun explore(regionId: String) {
        val p = player ?: return
        if (p.dead) return
        val region = Regions.get(regionId) ?: return
        if (!regionUnlocked(p, region)) {
            addLog("${region.name}凶险异常，以你如今的修为贸然前往，无异于送死。", 2)
            return
        }
        advanceDays(p, region.days)
        if (p.dead) {
            player = p.copy()
            return
        }
        addLog("你踏入${region.name}，搜寻了 ${region.days} 日。")
        val roll = Random.nextInt(100)
        when {
            roll < 45 -> {
                val eid = region.enemies.random()
                player = p.copy()
                startBattle(eid)
                return
            }
            roll < 72 -> {
                if (Random.nextInt(100) < 60) {
                    val hid = region.herbs.random()
                    val cnt = 1 + Random.nextInt(2)
                    grantItem(p, hid, cnt)
                    addLog("你在草木间寻得 ${Items.name(hid)} ×$cnt。", 1)
                } else {
                    val oid = region.ores.random()
                    val cnt = 1 + Random.nextInt(2)
                    grantItem(p, oid, cnt)
                    addLog("你凿开岩壁，掘得 ${Items.name(oid)} ×$cnt。", 1)
                }
                if (Random.nextInt(100) < 28) {
                    val s = (15 + Random.nextInt(30)) * (region.minRealm + 1)
                    p.stones += s
                    addLog("归途还拾得灵石 $s 枚。", 1)
                }
            }
            roll < 93 -> {
                val evt = pickEvent(region)
                addLog(evt.text)
                when (evt.effect) {
                    EvtEffect.EXP -> {
                        p.exp = min(p.expNeeded(), p.exp + evt.value.toLong())
                        addLog("修为 +${evt.value}", 1)
                    }
                    EvtEffect.STONES -> {
                        p.stones += evt.value
                        addLog("灵石 +${evt.value}", 1)
                    }
                    EvtEffect.HEAL -> {
                        val add = p.maxHp() * evt.value / 100
                        p.hpCur = min(p.maxHp(), p.hpCur + add)
                        addLog("气血恢复 $add", 1)
                    }
                    EvtEffect.HURT -> {
                        val dmg = p.maxHp() * evt.value / 100
                        p.hpCur = max(1, p.hpCur - dmg)
                        p.demonHeart = (p.demonHeart + 3).coerceAtMost(100)
                        addLog("气血 -$dmg", 2)
                    }
                    EvtEffect.DEMON_UP -> {
                        p.demonHeart = (p.demonHeart + evt.value).coerceAtMost(100)
                        addLog("心魔 +${evt.value}", 2)
                    }
                    EvtEffect.DEMON_DOWN -> {
                        p.demonHeart = (p.demonHeart - evt.value).coerceAtLeast(0)
                        addLog("心魔 -${evt.value}", 1)
                    }
                    EvtEffect.ITEM -> {
                        grantItem(p, evt.itemId, max(1, evt.value))
                        addLog("获得 ${Items.name(evt.itemId)} ×${max(1, evt.value)}", 1)
                    }
                    EvtEffect.BATTLE -> {
                        player = p.copy()
                        startBattle(region.enemies.random())
                        return
                    }
                    else -> {}
                }
            }
            else -> addLog("此番一无所获，你悻悻而归。")
        }
        player = p.copy()
        autoSave()
    }

    // ================= 物品 =================

    fun useItem(id: String) {
        val p = player ?: return
        if (p.itemCount(id) <= 0) return
        val def = Items.get(id) ?: return
        when (def.type) {
            ItemType.PILL -> {
                when (id) {
                    "pill_liaoshang" -> {
                        val add = p.maxHp() * 30 / 100
                        p.hpCur = min(p.maxHp(), p.hpCur + add)
                        addLog("服下疗伤丹，气血恢复 $add。", 1)
                    }
                    "pill_huiling" -> {
                        val add = p.maxMp() * 30 / 100
                        p.mpCur = min(p.maxMp(), p.mpCur + add)
                        addLog("服下回灵丹，灵力恢复 $add。", 1)
                    }
                    "pill_juqi" -> {
                        p.exp = min(p.expNeeded(), p.exp + 120)
                        addLog("服下聚气丹，修为 +120。", 1)
                    }
                    "pill_jingxin" -> {
                        p.demonHeart = (p.demonHeart - 25).coerceAtLeast(0)
                        addLog("服下静心丹，心魔消退不少。", 1)
                    }
                    "pill_pojing" -> {
                        p.breakBonus = 15
                        addLog("破境丹药力化开，下次突破成功率 +15%。", 3)
                    }
                    "pill_zhuji" -> {
                        p.breakBonus = 35
                        addLog("筑基丹药力沉入丹田，下次突破成功率 +35%。", 3)
                    }
                    "pill_jiedan" -> {
                        p.breakBonus = 30
                        addLog("结丹丹药力化开，下次突破成功率 +30%。", 3)
                    }
                    "pill_yuanying" -> {
                        p.breakBonus = 25
                        addLog("元婴丹药力化开，下次突破成功率 +25%。", 3)
                    }
                    "pill_huashen" -> {
                        p.breakBonus = 20
                        addLog("化神丹药力化开，下次突破成功率 +20%。", 3)
                    }
                    "pill_xisui" -> {
                        p.comprehension = min(10, p.comprehension + 1)
                        addLog("洗髓丹伐毛洗髓，悟性提升至 ${p.comprehension}。", 3)
                    }
                    else -> {
                        addLog("此丹似乎无处可用。", 2)
                        return
                    }
                }
                removeItem(p, id, 1)
            }
            ItemType.BOOK -> {
                learnBook(p, id)
            }
            ItemType.ARTIFACT -> {
                equipArtifact(p, id)
            }
            else -> {
                addLog("此物需在相应之处使用。", 2)
                return
            }
        }
        player = p.copy()
        autoSave()
    }

    private fun learnBook(p: Player, id: String) {
        val def = Items.get(id) ?: return
        val techId = def.teaches
        if (techId.isEmpty()) return
        if (p.techniques.contains(techId)) {
            addLog("《${Techniques.name(techId)}》你已烂熟于心。", 2)
            return
        }
        p.techniques.add(techId)
        removeItem(p, id, 1)
        val td = Techniques.get(techId)
        if (p.mainTechnique.isEmpty() && td?.kind == TechKind.MAIN) {
            p.mainTechnique = techId
        }
        addLog("你研读${def.name}，习得《${Techniques.name(techId)}》。", 3)
    }

    private fun equipArtifact(p: Player, id: String) {
        if (p.artifact == id) {
            p.artifact = ""
            addLog("你收起了${Items.name(id)}。")
        } else {
            p.artifact = id
            addLog("你祭起${Items.name(id)}，气息为之一变。", 1)
        }
    }

    fun setMainTechnique(techId: String) {
        val p = player ?: return
        val td = Techniques.get(techId) ?: return
        if (td.kind != TechKind.MAIN) return
        if (!p.techniques.contains(techId)) return
        p.mainTechnique = techId
        addLog("你改修《${td.name}》。", 1)
        player = p.copy()
        autoSave()
    }

    // ================= 炼制 =================

    fun recipeList(type: String): List<Recipe> = Recipes.of(type)

    fun canCraft(r: Recipe): Boolean {
        val p = player ?: return false
        if (p.realmIndex < r.minRealm) return false
        if (p.stones < r.stones) return false
        for ((mid, cnt) in r.materials) {
            if (p.itemCount(mid) < cnt) return false
        }
        return true
    }

    fun craft(recipeId: String) {
        val p = player ?: return
        val r = Recipes.ALL.firstOrNull { it.id == recipeId } ?: return
        if (p.realmIndex < r.minRealm) {
            addLog("修为不足，尚不能掌控此术。", 2)
            return
        }
        if (p.stones < r.stones) {
            addLog("灵石不足，需 ${r.stones} 枚。", 2)
            return
        }
        for ((mid, cnt) in r.materials) {
            if (p.itemCount(mid) < cnt) {
                addLog("材料不足：${Items.name(mid)} ×$cnt", 2)
                return
            }
        }
        for ((mid, cnt) in r.materials) removeItem(p, mid, cnt)
        p.stones -= r.stones
        advanceDays(p, r.days)
        if (p.dead) {
            player = p.copy()
            return
        }
        val chance = (r.baseSuccess + p.comprehension * 2 - p.demonHeart / 4).coerceIn(5, 95)
        if (Random.nextInt(100) < chance) {
            grantItem(p, r.result, r.resultCount)
            addLog("${r.name}炼制成功，得 ${Items.name(r.result)} ×${r.resultCount}。", 1)
        } else {
            addLog("炉火失控，${r.name}化为灰烬。（成功率 $chance%）", 2)
        }
        player = p.copy()
        autoSave()
    }

    // ================= 坊市 =================

    fun marketItems(): List<ItemDef> {
        val p = player ?: return emptyList()
        return Items.ALL.values
            .filter { it.type != ItemType.BOOK || it.grade <= 2 }
            .filter { it.type != ItemType.ARTIFACT || it.grade <= p.realmIndex + 1 }
            .sortedWith(compareBy<ItemDef>({ it.type.ordinal }, { it.price }))
    }

    fun buyPrice(def: ItemDef): Int {
        val p = player ?: return def.price
        return if (p.sectId == "s_wanbao") (def.price * 0.85).toInt() else def.price
    }

    fun sellPrice(def: ItemDef): Int {
        val p = player ?: return def.price / 2
        return if (p.sectId == "s_wanbao") (def.price * 0.65).toInt() else def.price / 2
    }

    fun buyItem(id: String) {
        val p = player ?: return
        val def = Items.get(id) ?: return
        val price = buyPrice(def)
        if (p.stones < price) {
            addLog("灵石不足，此物价 $price。", 2)
            return
        }
        p.stones -= price
        grantItem(p, id, 1)
        addLog("购入 ${def.name}，花费 $price 灵石。", 1)
        player = p.copy()
        autoSave()
    }

    fun sellItem(id: String, count: Int = 1) {
        val p = player ?: return
        val def = Items.get(id) ?: return
        if (p.itemCount(id) < count) return
        val price = sellPrice(def) * count
        removeItem(p, id, count)
        p.stones += price
        addLog("售出 ${def.name} ×$count，得 $price 灵石。", 1)
        player = p.copy()
        autoSave()
    }

    // ================= 洞府 =================

    fun caveUpgradeCost(): Int {
        val p = player ?: return 0
        if (p.caveLevel >= 10) return 0
        return (200 * Math.pow(p.caveLevel.toDouble(), 1.8)).toInt()
    }

    fun upgradeCave() {
        val p = player ?: return
        if (p.caveLevel >= 10) {
            addLog("洞府已扩建至极致。", 2)
            return
        }
        val cost = caveUpgradeCost()
        if (p.stones < cost) {
            addLog("灵石不足，扩建需 $cost 枚。", 2)
            return
        }
        p.stones -= cost
        p.caveLevel++
        advanceDays(p, 15)
        addLog("洞府扩建至 ${p.caveLevel} 级，聚灵阵更强了，修炼效率 +${p.caveLevel * 10}%。", 1)
        player = p.copy()
        autoSave()
    }

    fun caveHarvestPeriod(): Int {
        val p = player ?: return 200
        return max(60, 200 - p.caveLevel * 10)
    }

    fun harvestCave() {
        val p = player ?: return
        val period = caveHarvestPeriod()
        if (p.caveHarvestDays < period) {
            addLog("灵田尚未成熟，还需 ${period - p.caveHarvestDays} 日。", 2)
            return
        }
        val rounds = p.caveHarvestDays / period
        val hidden = if (p.caveLevel >= 5) "herb_tianling" else "herb_zihou"
        var total = 0
        repeat(rounds) {
            total += 1 + Random.nextInt(2) + p.caveLevel / 3
        }
        grantItem(p, hidden, total)
        p.caveHarvestDays %= period
        addLog("灵田收获 ${Items.name(hidden)} ×$total。", 1)
        player = p.copy()
        autoSave()
    }

    // ================= 宗门 =================

    fun joinSect(id: String) {
        val p = player ?: return
        if (p.sectId.isNotEmpty()) {
            addLog("你已身属宗门，不可二心。", 2)
            return
        }
        val s = Sects.get(id) ?: return
        if (p.realmIndex < s.minRealm) {
            addLog("${s.name}不收修为不足之人。", 2)
            return
        }
        p.sectId = id
        p.stones += s.joinReward
        addLog("你拜入${s.name}，领得入门灵石 ${s.joinReward} 枚。", 3)
        player = p.copy()
        autoSave()
    }

    fun takeQuest() {
        val p = player ?: return
        if (p.sectId.isEmpty()) {
            addLog("尚未拜入宗门，何来差事。", 2)
            return
        }
        if (p.quest != null) {
            addLog("你手头还有未完成的差事。", 2)
            return
        }
        val pool = Regions.ALL.filter { regionUnlocked(p, it) }
        val r = if (pool.isEmpty()) Regions.ALL.first() else pool.random()
        val q: Quest = if (Random.nextBoolean()) {
            val item = r.herbs.random()
            val need = 3 + Random.nextInt(4)
            Quest(
                name = "采集·${Items.name(item)}",
                desc = "为宗门采集 ${Items.name(item)} ×$need。",
                type = "COLLECT",
                itemId = item,
                targetCount = need,
                rewardStones = (60 + Random.nextInt(80)) * (p.realmIndex + 1),
                rewardContribution = 10 + Random.nextInt(10)
            )
        } else {
            val need = 2 + Random.nextInt(4)
            Quest(
                name = "剿灭·${r.name}妖兽",
                desc = "前往${r.name}斩杀妖兽 $need 头。",
                type = "HUNT",
                targetCount = need,
                rewardStones = (80 + Random.nextInt(80)) * (p.realmIndex + 1),
                rewardContribution = 12 + Random.nextInt(10)
            )
        }
        p.quest = q
        addLog("你接下宗门差事：${q.name}。", 1)
        player = p.copy()
        autoSave()
    }

    fun submitQuest() {
        val p = player ?: return
        val q = p.quest ?: return
        if (q.type == "COLLECT") {
            if (p.itemCount(q.itemId) < q.targetCount) {
                addLog("尚未集齐 ${Items.name(q.itemId)}。", 2)
                return
            }
            removeItem(p, q.itemId, q.targetCount)
        } else {
            if (q.progress < q.targetCount) {
                addLog("妖兽还差 ${q.targetCount - q.progress} 头。", 2)
                return
            }
        }
        p.stones += q.rewardStones
        p.contribution += q.rewardContribution
        addLog("差事交讫，得灵石 ${q.rewardStones}、宗门贡献 ${q.rewardContribution}。", 3)
        p.quest = null
        player = p.copy()
        autoSave()
    }

    fun abandonQuest() {
        val p = player ?: return
        if (p.quest == null) return
        p.quest = null
        p.contribution = (p.contribution - 5).coerceAtLeast(0)
        addLog("你推掉了这桩差事，宗门对你的印象差了几分。", 2)
        player = p.copy()
        autoSave()
    }

    // ================= 战斗 =================

    fun startBattle(enemyId: String) {
        val e = com.karios.xiuxian.data.Enemies.get(enemyId) ?: return
        val b = com.karios.xiuxian.model.BattleState(
            enemyId = enemyId,
            enemyHp = e.hp,
            enemyMaxHp = e.hp
        )
        b.log.add("你遭遇了${e.name}。")
        battle = b
        screen = Screen.BATTLE
    }

    internal fun syncBattle() {
        battle = battle?.copy()
    }

    internal fun syncPlayer(p: Player) {
        player = p.copy()
    }

    fun endBattle() {
        battle = null
        screen = Screen.GAME
        autoSave()
    }

    // 给 UI 用的辅助
    fun realmListSize(): Int = REALMS.size
}