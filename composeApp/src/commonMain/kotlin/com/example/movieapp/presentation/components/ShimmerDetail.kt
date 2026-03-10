package com.example.movieapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Backdrop Shimmer
        ShimmerItem(Modifier.fillMaxWidth().height(300.dp))
        
        Spacer(Modifier.height(16.dp))
        
        // Poster & Title Shimmer
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            ShimmerItem(Modifier.width(130.dp).height(190.dp).clip(RoundedCornerShape(12.dp)))
            Spacer(Modifier.width(16.dp))
            Column {
                ShimmerItem(Modifier.width(200.dp).height(30.dp).clip(RoundedCornerShape(4.dp)))
                Spacer(Modifier.height(8.dp))
                ShimmerItem(Modifier.width(100.dp).height(20.dp).clip(RoundedCornerShape(4.dp)))
            }
        }
        
        Spacer(Modifier.height(32.dp))
        
        // Content Shimmer
        Column(Modifier.padding(horizontal = 20.dp)) {
            ShimmerItem(Modifier.width(100.dp).height(24.dp).clip(RoundedCornerShape(4.dp)))
            Spacer(Modifier.height(12.dp))
            repeat(4) {
                ShimmerItem(Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(2.dp)))
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
