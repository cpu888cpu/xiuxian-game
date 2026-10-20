# apk-webview — WebView 版（可直接构建的轻量实现）

这是「凡人问道」的等价独立实现：Java 壳 + WebView + HTML5。

因为主项目（Kotlin + Compose）需要完整的 Android SDK 与 Gradle 才能构建，
而这一版只需要 `aapt2`、`javac`、`d8`、`android.jar` 四样东西，
在 Termux / 服务器 / 甚至手机上都能直接打包。

## 文件说明

```
apk-webview/
├── AndroidManifest.xml
├── src/com/karios/xiuxian/MainActivity.java   # WebView 壳 + SharedPreferences 存档桥
├── assets/
│   ├── index.html                             # 水墨深色 UI（CSS 全部内联）
│   └── game.js                                # 完整游戏逻辑（约 1250 行）
├── res/
│   ├── values/strings.xml
│   └── drawable/ic_launcher.xml
└── test.js                                    # Node 冒烟测试（模拟 2 万步随机操作）
```

## 游戏逻辑

与 Kotlin 版**同一套数值**：境界表、灵根倍率、妖兽、事件、配方、伤害公式完全一致。

- 8 大境界（炼气 13 层 → 大乘），寿元 120 / 300 / 600 / 1500 / 2500...
- 大境界突破基础成功率 20% / 14% / 12% / 10% / 8%，失败累积 +12%
- 灵根 0.65（伪灵根）~ 3.5（变异灵根）
- 战斗：普攻 / 法术 / 法宝 / 符箓 / 丹药 / 逃跑
- 炼制：炼丹 10 种、炼器 5 种、制符 3 种
- 坊市、宗门差事、洞府灵田、心魔系统
- 存档：`addJavascriptInterface` → SharedPreferences，localStorage 兜底

## 冒烟测试

```bash
node test.js
```

会模拟一个伪灵根玩家推进 2 万步，检查：
- 修为 / 灵石 / 境界索引不会出现负数或 NaN
- 战斗、突破、炼制、买卖、任务全流程不抛异常
- 输出境界时间线（炼气十三层约 45–48 岁，筑基 50–80 岁）

## 手动构建

依赖：

- JDK 17（`javac`）
- `android.jar`（platform-35，可从 `platform-35_r02.zip` 的 `android-35/android.jar` 获取）
- `r8.jar`（`com.android.tools:r8:8.5.35`，用于 d8 转 dex）
- 任意可用的 `aapt2`（Android SDK build-tools 或逆向工具包内置的都行）

```bash
AAPT2=/path/to/aapt2
ANDROID_JAR=/path/to/android.jar
R8=/path/to/r8.jar

# 1. 编译资源
$AAPT2 compile --dir res -o res.zip

# 2. 链接资源 + 生成 R.java（-A 把 assets 打进去）
$AAPT2 link -o base.apk -I $ANDROID_JAR \
    --manifest AndroidManifest.xml --java gen \
    --min-sdk-version 24 --target-sdk-version 35 \
    -A assets res.zip

# 3. 编译 Java
javac -encoding UTF-8 -source 8 -target 8 -bootclasspath $ANDROID_JAR \
    -d classes gen/com/karios/xiuxian/R.java src/com/karios/xiuxian/MainActivity.java

# 4. 转 dex
java -cp $R8 com.android.tools.r8.D8 --lib $ANDROID_JAR \
    --min-api 24 --output dexout classes/com/karios/xiuxian/*.class

# 5. 把 classes.dex 塞进 base.apk（用 zip 工具或 python zipfile）

# 6. 签名（apksigner 或任意 debug 签名工具）
```

产物约 33 KB。

## 与 Kotlin 版的关系

Kotlin + Compose 版（`app/` 目录）是正式交付物，界面更原生、可扩展性更好。
这一版是「没有 Android SDK 也能出包」的应急方案，游戏内容等价。

两版存档格式不同（JSON 结构相似但字段名不同），不互通。
