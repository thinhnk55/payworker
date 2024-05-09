package com.defi.util.sql.sqllite;

import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.HikariClient;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class SqlLiteManager {
    private static SqlLiteManager ins = null;

    private SqlLiteManager() {

    }

    public static SqlLiteManager instance() {
        if (ins == null) {
            ins = new SqlLiteManager();
        }
        return ins;
    }
    public void init(String configFile, String otherConfigFile) throws Exception {
        JsonObject config = GsonUtil.getJsonObject(configFile);
        String jdbcUrl = config.get("jdbcUrl").getAsString();
        sqlLiteClient = new HikariClient(jdbcUrl, otherConfigFile);
        bridge = new SQLJavaBridge(sqlLiteClient);
        createDatabase();
    }

    private void createDatabase() throws SQLException {
        Connection connection = null;
        try{
            connection = sqlLiteClient.getConnection();
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                DebugLogger.logger.info("The driver name is {}", meta.getDriverName());
            }
        }catch (Exception e){

        }finally {
            if (connection != null)
                connection.close();
        }
    }

    public HikariClient sqlLiteClient;
    public SQLJavaBridge bridge;
}
