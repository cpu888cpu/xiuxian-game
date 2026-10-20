# 凡人问道 · 文字修仙

一款**纯离线**的安卓文字修仙游戏。Jetpack Compose + Kotlin 编写，古风水墨深色界面，主打「凡人流」——资质平庸，步步为营，寿元有限，心魔缠身。

---

## 一、已实现的内容

### 修仙体系
- 8 个大境界：**炼气（13 层）→ 筑基 → 结丹 → 元婴 → 化神 → 炼虚 → 合体 → 大乘**
- 每个大境界有 4 个小境界（炼气为 13 层），大境界突破成功率显著低于小境界
- 突破失败会损失修为、受反噬伤、心魔上涨
- 寿元随境界提升，**年龄超过寿元即死亡**（真·凡人流）
- 大乘大圆满后冲击飞升，游戏通关

### 开局
- **随机灵根**：变异灵根（雷/冰/风）、天灵根（单属性）、双/三/四灵根、伪灵根
- 灵根直接决定修炼速度倍率（0.7 ~ 2.4）
- 悟性、气运随机生成；可无限重测灵根（想刷就刷）

### 核心循环
| 系统 | 说明 |
|---|---|
| 修炼 | 静修（当前境界 30~2000 日）／闭关（6 倍收益，心魔 +3） |
| 突破 | 修为满后可冲关，显示实时成功率（受悟性、心魔、丹药影响） |
| 探索 | 7 张地图，45% 遇妖兽 / 27% 采集 / 21% 特殊事件 / 7% 空手 |
| 战斗 | 回合制：普通攻击、法术、法宝、符箓、丹药、逃跑 |
| 炼丹/炼器/制符 | 15 个配方，成功率受悟性与心魔影响，失败材料尽毁 |
| 坊市 | 买/卖双向，万宝阁弟子有折扣 |
| 宗门 | 太一门 / 血煞宗 / 万宝阁，可接「采集」「剿灭」两类差事 |
| 洞府 | 1~10 级，提升修炼效率，灵田定期产灵草 |
| 心魔 | 0~100，降低修炼收益与突破成功率，静心丹可压制 |
| 法宝 | 6 件法宝，可祭炼，战斗中可催动 |

### 战斗系统
- 玩家伤害 = 攻击 × 随机浮动 − 敌人防御 × 0.5
- 法术消耗灵力，含单体伤害、护盾、治疗、逃跑、降攻五类效果
- 符箓一次性消耗；丹药可在战斗中直接服用
- 逃跑成功率取决于身法差
- 战败不会直接死亡，但会损失一半灵石、重伤、心魔 +10、白耗 30 天

### 存档
- **自动存档**：每次行动后写入 `slot0`
- **3 个手动存档槽**：主界面可在「修行」页存 1/2/3
- 存档格式为 JSON，位于 App 私有目录 `files/saves/slotN.json`
- 写入采用「临时文件 + 重命名」，中断不会损坏旧档

---

## 二、项目结构

```
xiuxian-game/
├── README.md
├── LICENSE
├── .github/workflows/android.yml   # GitHub Actions：推送即自动构建 APK
├── releases/
│   └── 凡人问道-v1.1.apk            # 已签名的成品，可直接安装
├── apk-webview/                     # WebView 版（无需 Android SDK 也能构建）
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
├── gradle/wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
└── app/                             # Kotlin + Compose 主工程
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/
        │   ├── drawable/ic_launcher.xml
        │   ├── values/strings.xml
        │   └── values/themes.xml
        └── java/com/karios/xiuxian/
            ├── MainActivity.kt
            ├── model/Models.kt          # Player / Quest / BattleState / 灵根
            ├── data/
            │   ├── GameData.kt          # 境界表、灵根生成、全物品表
            │   ├── Techniques.kt        # 功法与法术
            │   ├── Enemies.kt           # 妖兽
            │   ├── Regions.kt           # 地图与随机事件
            │   └── Craft.kt             # 配方、宗门
            ├── logic/
            │   ├── Stats.kt             # 属性计算（扩展函数）
            │   ├── GameViewModel.kt     # 核心状态机：修炼/探索/经济/存档
            │   └── BattleLogic.kt       # 回合制战斗
            ├── save/SaveManager.kt      # JSON 存读档
            └── ui/
                ├── Common.kt            # 通用组件
                ├── GameApp.kt           # 入口 + 主菜单 + 建号
                ├── GameScreen.kt        # 主界面 + 修行 + 探索
                ├── Panels.kt            # 背包/炼制/坊市/洞府/宗门/角色
                ├── BattleScreen.kt      # 战斗界面
                └── theme/Theme.kt       # 水墨配色
```

---

## 三、环境要求

| 项目 | 版本 |
|---|---|
| Android Studio | Hedgehog (2023.1.1) 或更新 |
| JDK | 17（AS 自带即可） |
| Gradle | 8.7（wrapper 已配置，自动下载） |
| AGP | 8.5.2 |
| Kotlin | 1.9.24 |
| minSdk | 24（Android 7.0） |
| targetSdk / compileSdk | 34 |

---

## 四、编译方法

### 方式 A：Android Studio（推荐）

1. 把整个 `xiuxian-game` 文件夹复制到电脑上。
2. 打开 Android Studio → `File` → `Open`，选中 `xiuxian-game` 目录（**注意选包含 `settings.gradle.kts` 的那一层**）。
3. 首次打开会提示同步，点 `Trust Project` / `Sync Now`。
   Gradle 会自动下载 `gradle-8.7` 与全部依赖（需联网，约 3~8 分钟）。
4. 同步完成后：`Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`。
5. 产物路径：
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### 方式 B：命令行

```bash
cd xiuxian-game
chmod +x gradlew          # Windows 用 gradlew.bat，无需 chmod
./gradlew assembleDebug
```

产物同上。首次运行会自动下载 Gradle 发行版。

### 常见问题

- **`gradle-wrapper.jar` 缺失 / 校验失败**：删掉 `gradle/wrapper` 目录，在 Android Studio 里 `File → Sync Project with Gradle Files`，或在任意目录执行 `gradle wrapper --gradle-version 8.7` 后把生成的文件复制回来。
- **提示 JDK 版本不对**：`File → Settings → Build, Execution, Deployment → Build Tools → Gradle`，把 Gradle JDK 设为 17。
- **`Unsupported class file major version`**：同上，JDK 必须 17。
- **国内网络卡在下载依赖**：在 `settings.gradle.kts` 的两个 `repositories {}` 里，把 `google()` / `mavenCentral()` 前加上阿里云镜像：
  ```kotlin
  maven { url = uri("https://maven.aliyun.com/repository/google") }
  maven { url = uri("https://maven.aliyun.com/repository/central") }
  maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
  ```

---

## 五、安装到手机

1. 把 `app-debug.apk` 传到手机（微信文件传输 / 数据线 / ADB 均可）。
2. 手机上点开 APK，允许「安装未知来源应用」。
3. 安装后桌面会出现「凡人问道」。

ADB 方式：

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

App 不申请任何权限，不需要网络，飞行模式也能玩。

---

## 六、玩法提示

- **前期**：青牛山刷聚灵草和灵石，攒够材料炼疗伤丹，再打黑风林。
- **炼气十三层 → 筑基** 是一道大坎（基础成功率 55%）。先攒**筑基丹**（+40%）或**破境丹**（+25%），把心魔压到 0 再冲。
- **心魔**超过 60 会让修炼效率明显下降，静心丹（紫猴花 ×2 炼）要常备。
- **寿元**是真压力：每次探索、炼丹、扩建都在烧时间。突破境界才能续命。
- **法宝**在战斗中伤害倍率 1.8 倍，但每次消耗 15 灵力，别乱挥。
- 打不过就**跑**。逃跑失败只挨一下，总比丢一半灵石强。

---

## 七、存档位置

App 私有目录下：

```
/data/data/com.karios.xiuxian/files/saves/
├── slot0.json   # 自动存档
├── slot1.json
├── slot2.json
└── slot3.json
```

非 root 设备可通过「设置 → 应用 → 凡人问道 → 存储」清理数据来重置。

---

## 八、后续可扩展方向

已预留结构，方便继续加内容：

1. **新区域/事件**：往 `Regions.kt` 的 `ALL` 里加 `RegionDef` 即可，事件用 `SpecialEvent` 的 `effect` + `value` 描述，无需改引擎。
2. **新物品/功法**：`GameData.kt` 的 `Items.ALL`、`Techniques.kt` 的 `ALL` 追加条目。
3. **新妖兽**：`Enemies.kt` 加 `EnemyDef`。
4. 可加：宗门贡献兑换、法宝强化、多敌战斗、剧情主线、成就系统、音效。

---

## 九、声明

本作为原创世界观与剧情的同人风格练习项目，修仙体系术语属公有领域通用设定，未使用任何他人作品的角色、门派或文本。

---

## 十、已构建的 APK（两个版本）

仓库 `releases/` 目录下有两个可直接安装的成品：

| 文件 | 版本 | 大小 | 说明 |
|---|---|---|---|
| `凡人问道-原生版-v1.1.apk` | Kotlin + Compose | 8.7 MB | 正式版，由 GitHub Actions 从 `app/` 编译产出 |
| `凡人问道-WebView版-v1.1.apk` | Java + HTML5 | 33 KB | 轻量版，无需 Android SDK 也能本地构建 |

> **两者包名相同**（`com.karios.xiuxian`），签名不同，**不能共存**。
> 安装另一个之前请先卸载当前的；WebView 版的存档在卸载时会被一并清空。

- minSdk 24（Android 7.0）
- 已用 debug 证书签名，可直接安装（非商店分发用途）
- 安装：把 APK 传到手机 → 点开 → 允许「安装未知来源应用」

### 这一版是什么

因为构建环境里没有 Android SDK / Gradle，且 arm64 上没有官方 aapt2（只有逆向工具包内的 2.19 老版本），AGP 8.5 跑不起来，Compose 依赖的 AAR 资源合并也无法手工完成。

所以这一版是**等价的独立实现**：

```
apk-webview/
├── AndroidManifest.xml
├── src/com/karios/xiuxian/MainActivity.java   # WebView 壳 + 存档桥（约 90 行）
├── assets/
│   ├── index.html                             # 水墨深色 UI
│   └── game.js                                # 完整游戏逻辑（约 1200 行）
└── test.js                                    # Node 冒烟测试（800 步随机操作）
```

- 数值、境界、妖兽、事件、配方、公式与 Kotlin 版**完全一致**
- 存档走 `addJavascriptInterface` 桥接 SharedPreferences，另有一份 localStorage 兜底
- 已通过 Node 冒烟测试：模拟 800 次随机操作（修炼/突破/探索/战斗/炼制/买卖/任务），无异常，可正常推进到结丹期

### 主项目仍然是 Kotlin + Compose

`app/` 目录下的 Kotlin/Compose 工程是正式交付物，按本 README 第四节在电脑上编译即可得到原生版 APK。
WebView 版只是「在本机没有 Android SDK 的情况下，先让你能玩上」的应急产物。

### 重新构建 WebView 版（可选）

需要：JDK 17、`android.jar`（platform-35）、`r8.jar`、任意可用的 `aapt2`。

```bash
AAPT2 compile --dir res -o res.zip
AAPT2 link -o base.apk -I android.jar --manifest AndroidManifest.xml \
      --java gen --min-sdk-version 24 --target-sdk-version 35 -A assets res.zip
javac -encoding UTF-8 -source 8 -target 8 -bootclasspath android.jar \
      -d classes gen/com/karios/xiuxian/R.java src/com/karios/xiuxian/MainActivity.java
java -cp r8.jar com.android.tools.r8.D8 --lib android.jar --min-api 24 \
      --output dexout classes/com/karios/xiuxian/*.class
# 把 dexout/classes.dex 塞进 base.apk，再用 apksigner / jarsigner 签名
```

---

## 十一、v1.1 数值调整（对照原著）

### 参考的原著时间线

| 事件 | 韩立年龄 |
|---|---|
| 入七玄门，修炼长春功 | 10 岁 |
| 炼气三层 | 14 岁 |
| 炼气六层 | 16 岁 |
| 炼气九层 | 18 岁 |
| 炼气十一层 | 21 岁 |
| **筑基**（连服 8 颗筑基丹强破） | **25 岁** |
| **结丹** | **126 岁** |
| **元婴** | **217 岁** |
| 元婴中期 / 后期 | 252 / 360 岁 |
| **化神**（第一次冲击失败） | **约 540 岁** |

寿元：炼气 100–120 年、筑基 200–300 年、结丹约 500 年、元婴超千年。

### 改了什么

| 项目 | v1.0（旧） | v1.1（新） |
|---|---|---|
| 炼气一次静修 | 30 日 | **182 日（半年）** |
| 筑基一次静修 | 60 日 | **1826 日（5 年）** |
| 结丹一次静修 | 120 日 | **3652 日（10 年）** |
| 元婴一次静修 | 240 日 | **7305 日（20 年）** |
| 炼气寿元 | 130 | **120** |
| 筑基寿元 | 200 | **300** |
| 结丹寿元 | 300 | **600** |
| 元婴寿元 | 500 | **1500** |
| 大境界突破基础成功率 | 55% / 40% / 30%… | **20% / 14% / 12% / 10% / 8%…** |
| 小境界突破成功率 | 90% | **85%** |
| 灵根倍率 | 0.7 ~ 2.4 | **0.65 ~ 3.5**（差距拉大） |
| 突破失败 | 修为 −40%，心魔 +8 | 大境界 **修为 −60%、心魔 +5、下次突破 +12% 累积** |
| 静修 | 不降心魔 | **每次 −1 心魔**（打坐本身是静心） |
| 大境界丹药 | 只有筑基丹 +40% | 筑基丹 +35%、**结丹丹 +30%**、**元婴丹 +25%**、**化神丹 +20%** |

### 「失败累积」机制

大境界突破每失败一次，`failBonus` 永久 +12%，直到成功为止。

这是给凡人流的一点体面——不是每次重来都从零开始，而是**每一次失败都在垫高下一次**。

### 调整后的实测节奏（伪灵根 ×0.65、悟性 5，不用存档大法）

| 局 | 结局 | 死亡年龄 |
|---|---|---|
| 1 | 炼虚大圆满 | 4135 / 寿元 4000 |
| 2 | 结丹大圆满 | 605 / 寿元 600 |
| 3 | 结丹大圆满 | 607 / 寿元 600 |

炼气十三层稳定在 **45–48 岁**（韩立 21 岁，但他是四灵根 + 掌天瓶，我们是纯伪灵根），筑基 50–80 岁。

**方差大是故意的**——结丹那一关过不过得去，决定这一世能走多远。
