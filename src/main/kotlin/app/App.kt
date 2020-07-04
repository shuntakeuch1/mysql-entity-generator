package app

import app.generator.EntityGenerator
import app.repository.MySQLRepository

class App {
}

// できたら optionでdata classかclassかを読み込む
// 終了コードを出す
fun main(args: Array<String>) {
    val tables = MySQLRepository().getTables()
    // 情報を元にfile生成する rootにファイル生成する
    EntityGenerator().execute(tables)
}
