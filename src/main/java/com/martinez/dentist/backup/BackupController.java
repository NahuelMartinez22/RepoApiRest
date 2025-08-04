package com.martinez.dentist.backup;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/user")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/backup")
    public void downloadBackup(HttpServletResponse resp) throws Exception {
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String filename = "backup-" + ts + ".sql";

        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        resp.setContentType("text/plain; charset=UTF-8");

        backupService.writeSqlDump(resp.getOutputStream());
    }
}
