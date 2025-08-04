package com.martinez.dentist.backup;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

@Service
public class BackupService {

    private final DataSource dataSource;

    public BackupService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void writeSqlDump(OutputStream os) throws SQLException, IOException {
        try (
                Connection conn = dataSource.getConnection();
                OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                PrintWriter out = new PrintWriter(osw, true)
        ) {
            String catalog = conn.getCatalog();
            DatabaseMetaData meta = conn.getMetaData();

            out.println("CREATE DATABASE IF NOT EXISTS `" + catalog + "` " +
                    "DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
            out.println("USE `" + catalog + "`;");
            out.println("SET NAMES utf8mb4;");
            out.println();

            out.println("SET FOREIGN_KEY_CHECKS=0;");
            out.println();

            try (ResultSet tables = meta.getTables(catalog, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String table = tables.getString("TABLE_NAME");

                    out.println("-- ----------------------------");
                    out.println("-- Elimina la tabla `" + table + "` si existe");
                    out.println("-- ----------------------------");
                    out.println("DROP TABLE IF EXISTS `" + table + "`;");
                    out.println();

                    try (Statement st = conn.createStatement();
                         ResultSet cr = st.executeQuery("SHOW CREATE TABLE `" + table + "`")) {
                        if (cr.next()) {
                            String orig = cr.getString(2);
                            String ddl = orig
                                    .replaceFirst(
                                            "CREATE TABLE `" + table + "`",
                                            "CREATE TABLE IF NOT EXISTS `" + table + "`"
                                    )
                                    .replaceAll(
                                            "DEFAULT CHARSET=[^;\\s]+",
                                            "DEFAULT CHARSET=utf8mb4"
                                    )
                                    + ";";

                            out.println("-- ----------------------------");
                            out.println("-- Estructura de tabla `" + table + "`");
                            out.println("-- ----------------------------");
                            out.println(ddl);
                            out.println();
                        }
                    }

                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery("SELECT * FROM `" + table + "`")) {

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int cols = rsmd.getColumnCount();

                        out.println("-- ----------------------------");
                        out.println("-- Datos de tabla `" + table + "`");
                        out.println("-- ----------------------------");

                        while (rs.next()) {
                            StringBuilder sb = new StringBuilder("INSERT INTO `")
                                    .append(table)
                                    .append("` VALUES (");
                            for (int i = 1; i <= cols; i++) {
                                int type = rsmd.getColumnType(i);

                                if (type == Types.BLOB
                                        || type == Types.LONGVARBINARY
                                        || type == Types.BINARY
                                        || type == Types.VARBINARY) {

                                    byte[] data = rs.getBytes(i);
                                    if (data == null) {
                                        sb.append("NULL");
                                    } else {
                                        StringBuilder hex = new StringBuilder();
                                        for (byte b : data) {
                                            hex.append(String.format("%02X", b));
                                        }
                                        sb.append("0x").append(hex);
                                    }

                                } else if (type == Types.BIT
                                        || (rs.getObject(i) instanceof Boolean)
                                        || type == Types.TINYINT) {

                                    sb.append(rs.getBoolean(i) ? "1" : "0");

                                } else if (rs.getObject(i) instanceof Number) {
                                    sb.append(rs.getObject(i));

                                } else {
                                    String s = rs.getString(i);
                                    if (s == null) {
                                        sb.append("NULL");
                                    } else {
                                        sb.append("'")
                                                .append(s.replace("'", "''"))
                                                .append("'");
                                    }
                                }

                                if (i < cols) sb.append(", ");
                            }
                            sb.append(");");
                            out.println(sb);
                        }
                        out.println();
                    }
                }
            }

            out.println("SET FOREIGN_KEY_CHECKS=1;");
        }
    }
}
