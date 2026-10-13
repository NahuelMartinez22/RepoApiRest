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

                    // Lectura fila por fila (streaming): sin esto, el driver de MySQL carga la tabla
                    // entera en memoria, incluidos todos los archivos de clinical_files.
                    try (Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                        st.setFetchSize(Integer.MIN_VALUE);
                        try (ResultSet rs = st.executeQuery("SELECT * FROM `" + table + "`")) {

                            ResultSetMetaData rsmd = rs.getMetaData();
                            int cols = rsmd.getColumnCount();

                            out.println("-- ----------------------------");
                            out.println("-- Datos de tabla `" + table + "`");
                            out.println("-- ----------------------------");

                            while (rs.next()) {
                                out.print("INSERT INTO `");
                                out.print(table);
                                out.print("` VALUES (");
                                for (int i = 1; i <= cols; i++) {
                                    int type = rsmd.getColumnType(i);

                                    if (type == Types.BLOB
                                            || type == Types.LONGVARBINARY
                                            || type == Types.BINARY
                                            || type == Types.VARBINARY) {

                                        byte[] data = rs.getBytes(i);
                                        if (data == null) {
                                            out.print("NULL");
                                        } else {
                                            out.print("0x");
                                            writeHex(data, out);
                                        }

                                    } else if (type == Types.BIT
                                            || (rs.getObject(i) instanceof Boolean)
                                            || type == Types.TINYINT) {

                                        out.print(rs.getBoolean(i) ? "1" : "0");

                                    } else if (rs.getObject(i) instanceof Number) {
                                        out.print(rs.getObject(i));

                                    } else {
                                        String value = rs.getString(i);
                                        out.print(value == null ? "NULL" : "'" + escapeSqlString(value) + "'");
                                    }

                                    if (i < cols) out.print(", ");
                                }
                                out.println(");");
                            }
                            out.println();
                        }
                    }
                }
            }

            out.println("SET FOREIGN_KEY_CHECKS=1;");
        }
    }

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    // Escribe los bytes en hexadecimal por bloques, sin armar un String gigante en memoria
    static void writeHex(byte[] data, Writer out) throws IOException {
        char[] buffer = new char[8192];
        int pos = 0;
        for (byte b : data) {
            buffer[pos++] = HEX[(b >> 4) & 0x0F];
            buffer[pos++] = HEX[b & 0x0F];
            if (pos == buffer.length) {
                out.write(buffer, 0, pos);
                pos = 0;
            }
        }
        out.write(buffer, 0, pos);
    }

    // En MySQL la barra invertida también es carácter de escape dentro de un string
    static String escapeSqlString(String value) {
        return value.replace("\\", "\\\\").replace("'", "''");
    }
}
