package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Lớp BaseViewModel cơ bản để quản lý các xử lý chung nếu cần.
 */
abstract class BaseViewModel : ViewModel() {
    // Có thể thêm các helper function xử lý UI State ở đây
}
