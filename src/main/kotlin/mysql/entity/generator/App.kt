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
    fun databaseConnection() {
        Class.forName("com.mysql.jdbc.Driver")
        val conn = DriverManager.getConnection(jdbcURL, user, password)
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery("SHOW DATABASES;")
        while (resultSet.next()) {
            val database = resultSet.getString("Database")
            println(database)
        }
        resultSet.close()
        statement.close()
        conn.close()
    }
}

// できたら optiondでdata classかclassかを読み込む
// table情報とカラムを読み込む
// 情報を元にfile生成する rootにファイル生成する
// 終了コードを出す
fun main(args: Array<String>) {
    App().databaseConnection()
}
