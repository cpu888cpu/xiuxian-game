package com.karios.xiuxian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.karios.xiuxian.ui.GameApp
import com.karios.xiuxian.ui.theme.InkBlack
import com.karios.xiuxian.ui.theme.XiuXianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XiuXianTheme {
                Surface(color = InkBlack, modifier = Modifier.fillMaxSize()) {
                    GameApp()
                }
            }
        }
    }
}