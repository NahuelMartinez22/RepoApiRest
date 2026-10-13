package com.martinez.dentist.backup;

import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

class BackupServiceTest {

    @Test
    void escribeBytesEnHexadecimal() throws Exception {
        StringWriter out = new StringWriter();

        BackupService.writeHex(new byte[]{0x00, 0x0F, (byte) 0xAB, (byte) 0xFF}, out);

        assertThat(out.toString()).isEqualTo("000FABFF");
    }

    @Test
    void escribeArchivosGrandesCompletos() throws Exception {
        byte[] data = new byte[20_000];
        data[19_999] = 0x7F;
        StringWriter out = new StringWriter();

        BackupService.writeHex(data, out);

        assertThat(out.toString()).hasSize(40_000).endsWith("7F");
    }

    @Test
    void escapaComillasYBarrasInvertidas() {
        assertThat(BackupService.escapeSqlString("O'Brien")).isEqualTo("O''Brien");
        assertThat(BackupService.escapeSqlString("C:\\carpeta\\")).isEqualTo("C:\\\\carpeta\\\\");
        assertThat(BackupService.escapeSqlString("termina en \\'")).isEqualTo("termina en \\\\''");
    }
}
