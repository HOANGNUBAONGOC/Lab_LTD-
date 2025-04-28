package com.example.marsphotos.ui

import com.example.marsphotos.model.MarsPhoto

sealed interface MarsUiState {
    object Loading : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : MarsUiState
}
