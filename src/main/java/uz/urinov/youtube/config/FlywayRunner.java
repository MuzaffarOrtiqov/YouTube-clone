package uz.urinov.youtube.config;


import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayRunner implements CommandLineRunner {
    private final DataSource dataSource;
    public FlywayRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public void run(String... args) throws Exception {
        Flyway
                .configure()
                .baselineOnMigrate(true)
                // version default 1 boladi agar 1chi versiondan migrate qilishni xoxlasangiz 1.0 dan 0.0 yozishi talab qilinadi.
                .baselineVersion("0.0")
                .dataSource(dataSource)
                .load()
                .migrate();
    }
}
