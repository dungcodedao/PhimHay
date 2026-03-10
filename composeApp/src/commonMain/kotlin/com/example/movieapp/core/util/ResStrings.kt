package com.example.movieapp.core.util

/**
 * Một lớp tiện ích tạm thời để quản lý chuỗi ký tự trên toàn ứng dụng KMP.
 * Trong tương lai, nên chuyển sang sử dụng thư viện JetBrains Compose Resources.
 */
object ResStrings {
    const val app_name = "PhimHay"
    const val nav_home = "Trang chủ"
    const val nav_search = "Tìm kiếm"
    const val nav_favorite = "Yêu thích"
    const val nav_profile = "Cá nhân"
    const val watch_now = "Xem ngay"
    const val watch_history = "Lịch sử xem"
    const val overview_title = "Nội dung"
    const val cast_title = "Diễn viên"
    const val similar_movies = "Phim tương tự"
    const val rating_title = "Đánh giá người dùng"
    const val write_review = "Viết đánh giá của bạn..."
    const val submit_rating = "Gửi đánh giá"
    const val no_ratings = "Chưa có đánh giá nào"
    const val share_title = "Chia sẻ phim"
    const val no_internet = "Không có kết nối Internet"
    const val retry = "Thử lại"
    const val login_title = "Chào mừng trở lại"
    const val register_title = "Tạo tài khoản"
    const val email_label = "Email"
    const val password_label = "Mật khẩu"
    const val login_button = "Đăng nhập"
    const val register_button = "Đăng ký"
    
    fun runtime_min(minutes: Int) = "$minutes phút"
    fun review_count(count: Int) = "($count đánh giá)"
    fun share_message(title: String, id: Int) = "Xem phim \"$title\" trên PhimHay: movieapp://movie/$id"
}
