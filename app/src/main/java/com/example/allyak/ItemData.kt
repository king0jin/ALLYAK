package com.example.allyak


data class ItemData (
    var docId : String? = null,
    var UID: String? = null,
    var title: String? = null,
    var content: String? = null,
    var date: String? = null,
    var viewdate: String? = null,
    var likeCnt: Int = 0,
    var commentCnt: Int = 0,
    var comment: Map<String, Comments> = emptyMap()
)
data class Comments(
    var postId:String? = null,
    var docId: String? = null, // 댓글의 고유 ID
    var userId: String? = null, // 댓글 작성자의 고유 ID
    var content: String? = null,   // 댓글 내용
    var time: String? = null  // 댓글 작성 시간
)