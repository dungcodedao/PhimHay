package com.example.movieapp.data.repository

import com.example.movieapp.domain.repository.IAuthRepository
import com.example.movieapp.domain.util.Resource
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thực thi xác thực người dùng bằng Supabase Auth.
 */
@Singleton
class AuthRepositorySupabaseImpl @Inject constructor(
    private val supabaseAuth: Auth,
    private val storage: io.github.jan.supabase.storage.Storage
) : IAuthRepository {

    override fun isUserLoggedIn(): Flow<Boolean> {
        return supabaseAuth.sessionStatus.map { status ->
            status is SessionStatus.Authenticated
        }
    }

    override fun getCurrentUserEmail(): String? = supabaseAuth.currentUserOrNull()?.email

    override fun getCurrentUserId(): String? = supabaseAuth.currentUserOrNull()?.id

    override fun login(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            supabaseAuth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Đăng nhập thất bại"))
        }
    }

    override fun register(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            supabaseAuth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Đăng ký thất bại"))
        }
    }

    override suspend fun logout() {
        supabaseAuth.signOut()
    }

    override suspend fun updateProfile(username: String?, avatarUrl: String?): Resource<Unit> {
        return try {
            supabaseAuth.updateUser {
                data = buildJsonObject {
                    username?.let { put("username", it) }
                    avatarUrl?.let { put("avatar_url", it) }
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Cập nhật profile thất bại")
        }
    }

    override suspend fun uploadAvatar(byteArray: ByteArray, fileName: String): Resource<String> {
        return try {
            val bucket = storage["avatars"]
            val path = "${getCurrentUserId()}/$fileName"
            // Trong bản 3.0.0, sử dụng lambda config đơn giản
            bucket.upload(path, byteArray) {
                this.upsert = true
            }
            val url = bucket.publicUrl(path)
            Resource.Success(url)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Tải ảnh lên thất bại")
        }
    }

    override fun getUsername(): String? = supabaseAuth.currentUserOrNull()?.userMetadata?.get("username")?.toString()?.removeSurrounding("\"")

    override fun getAvatarUrl(): String? = supabaseAuth.currentUserOrNull()?.userMetadata?.get("avatar_url")?.toString()?.removeSurrounding("\"")
}
