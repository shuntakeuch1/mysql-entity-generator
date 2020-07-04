package mysql.entity.generator

import java.io.File
import java.io.PrintWriter
import java.sql.DriverManager

class App {
    companion object {
        const val jdbcURL = "jdbc:mysql://127.0.0.1/example"
        const val user = "root"
        const val password = "root"
        const val rootDirectory = "./"
        const val targetDirectory = "outputFile" // project root
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
                    nullFlag = resultSet.getString("Null"),
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
        val newDir = File(rootDirectory + targetDirectory)
        if (newDir.mkdir()) {
            println("make directory")
        }
        tables.forEach { it ->
            val className = it.name.capitalize()
            val filename = "$className.kt"
            val newFile = File("$targetDirectory/$filename")

            if (newFile.createNewFile()) {
                println("make $filename")
            }
            val pw = PrintWriter(newFile)
            var fieldVariablesString = ""
            val columnLastIndex = it.columns.lastIndex
            for ((index, column) in it.columns.withIndex()) {
                fieldVariablesString += "\n val ${column.field.toString()}: ${column.typeConverter()}"
                if (index != columnLastIndex) {
                    fieldVariablesString += ","
                }
            }
            pw.println("package $targetDirectory \n")
            pw.println("class $className ($fieldVariablesString \n)")
            pw.flush()
            pw.close()
        }
    }
}

// できたら optionでdata classかclassかを読み込む
// 終了コードを出す
fun main(args: Array<String>) {
    val tables = App().getTables()
    // 情報を元にfile生成する rootにファイル生成する
    App().makeEntity(tables)
}
