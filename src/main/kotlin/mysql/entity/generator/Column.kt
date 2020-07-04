package mysql.entity.generator

class Column(
    val field: String,
    val type: String,
    val nulFlag: String,
    val key: String,
    val defaultFlag: String?,
    val extra: String
)