package com.karios.xiuxian.save

import android.content.Context
import com.karios.xiuxian.model.Player
import kotlinx.serialization.json.Json
import java.io.File

/**
 * 存档管理：
 * slot 0 = 自动存档；slot 1..3 = 手动存档槽
 */
object SaveManager {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    private fun dir(ctx: Context): File {
        val d = File(ctx.filesDir, "saves")
        if (!d.exists()) d.mkdirs()
        return d
    }

    private fun fileOf(ctx: Context, slot: Int): File = File(dir(ctx), "slot$slot.json")

    fun save(ctx: Context, slot: Int, p: Player): Boolean {
        return try {
            val text = json.encodeToString(Player.serializer(), p)
            // 写临时文件再改名，避免写入中断导致存档损坏
            val tmp = File(dir(ctx), "slot$slot.tmp")
            tmp.writeText(text)
            val target = fileOf(ctx, slot)
            if (target.exists()) target.delete()
            tmp.renameTo(target)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun load(ctx: Context, slot: Int): Player? {
        return try {
            val f = fileOf(ctx, slot)
            if (!f.exists()) return null
            val text = f.readText()
            if (text.isBlank()) return null
            json.decodeFromString(Player.serializer(), text)
        } catch (e: Exception) {
            null
        }
    }

    fun exists(ctx: Context, slot: Int): Boolean = fileOf(ctx, slot).exists()

    fun delete(ctx: Context, slot: Int) {
        try {
            fileOf(ctx, slot).delete()
        } catch (_: Exception) {
        }
    }

    /** 读取用于预览的元信息 */
    fun meta(ctx: Context, slot: Int): Player? = load(ctx, slot)

    fun allSlots(ctx: Context): List<Player?> = (1..3).map { meta(ctx, it) }
}