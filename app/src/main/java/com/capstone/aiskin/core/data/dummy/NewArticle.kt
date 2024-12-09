package com.capstone.aiskin.core.data.dummy

data class ArticleItem(
    val imageUrl: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val content: String
)


public fun getDummyNewArticles(): List<ArticleItem> {
    return listOf(
        ArticleItem(
            "https://via.placeholder.com/150",
            "How to Care for Your Skin",
            "Learn the best tips to keep your skin healthy and glowing.",
            "22 Nov 2024",
            "Keeping your skin healthy involves proper hydration, sunscreen application, and using skincare products suitable for your skin type. It’s also important to avoid harsh ingredients and maintain a balanced diet rich in vitamins and antioxidants."
        ),
        ArticleItem(
            "https://via.placeholder.com/150",
            "Understanding Skin Conditions",
            "An overview of common skin conditions and their treatments.",
            "21 Nov 2024",
            "Common skin conditions like acne, eczema, and psoriasis can affect anyone. Understanding the triggers and finding appropriate treatments like topical creams, oral medications, and lifestyle changes can significantly improve your skin health."
        ),
        ArticleItem(
            "https://via.placeholder.com/150",
            "Top Skincare Myths Debunked",
            "Discover the truth about skincare myths you may have believed.",
            "20 Nov 2024",
            "Many skincare myths exist, such as believing that oily skin doesn’t need moisturizer. In reality, all skin types benefit from hydration. Debunking these myths helps us make better skincare choices and promotes healthy skin habits."
        )
    )
}