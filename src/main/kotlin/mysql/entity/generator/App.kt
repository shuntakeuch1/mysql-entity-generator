package mysql.entity.generator

import java.sql.DriverManager

class App {
    companion object {
        const val jdbcURL = "jdbc:mysql://127.0.0.1/example"
        const val user = "root"
        const val password = "root"
    }

    /**
     * DB Access
     */
    fun getTables(): List<Table> {
        Class.forName("com.mysql.jdbc.Driver")
        val conn = DriverManager.getConnection(jdbcURL, user, password)
        val statement = conn.createStatement()
        var resultSet = statement.executeQuery("show tables;")
        var tablesName = arrayOf<String>()

        while (resultSet.next()) {
            val table = resultSet.getString("Tables_in_example")
            tablesName += table
        }
        val tables = tablesName.map {
            resultSet = statement.executeQuery("show columns from $it;")
            var columns = arrayOf<Column>()
            while (resultSet.next()) {
                val column = Column(
                    field = resultSet.getString("Field"),
                    type = resultSet.getString("Type"),
                    nulFlag = resultSet.getString("Null"),
                    key = resultSet.getString("Key"),
                    defaultFlag = resultSet.getString("Default"),
                    extra = resultSet.getString("Extra")
                )
                columns += column
            }
            Table(
                name = it,
                columns = columns
            )
        }
        resultSet.close()
        statement.close()
        conn.close()

        return tables
    }

    fun makeEntity(tables: List<Table>) {

    }
}

// できたら optionでdata classかclassかを読み込む
// 終了コードを出す
fun main(args: Array<String>) {
    val tables = App().getTables()
    // 情報を元にfile生成する rootにファイル生成する
    App().makeEntity(tables)
}
