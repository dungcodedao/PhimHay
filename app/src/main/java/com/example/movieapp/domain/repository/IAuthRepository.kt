package com.example.movieapp.domain.repository

import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface cho xác thực người dùng sử dụng Supabase.
 */
interface IAuthRepository {
    fun isUserLoggedIn(): Flow<Boolean>
    fun getCurrentUserEmail(): String?
    fun getCurrentUserId(): String?
    fun login(email: String, password: String): Flow<Resource<Unit>>
    fun register(email: String, password: String): Flow<Resource<Unit>>
    suspend fun logout()
    suspend fun updateProfile(username: String?, avatarUrl: String?): Resource<Unit>
    suspend fun uploadAvatar(byteArray: ByteArray, fileName: String): Resource<String>
    fun getUsername(): String?
    fun getAvatarUrl(): String?
}
