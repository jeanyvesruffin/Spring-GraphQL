package ruffinjy.spring_graphql.config;

import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class IdentityFixConfig {

    @Bean
    CommandLineRunner restartIdentityCounters(DataSource dataSource) {
        return args -> {
            String[] tables = {
                "SIMULATION", "SIMULATION_RESULT", "WIDGET", "DASHBOARD",
                "LEAGUE", "TEAM", "PLAYER", "MATCHES",
                "PLAYER_STATISTIC", "ACHIEVEMENT"
            };
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                for (String table : tables) {
                    try {
                        var rs = stmt.executeQuery(
                            "SELECT COALESCE(MAX(ID), 0) FROM DB2ADMIN." + table);
                        rs.next();
                        long maxId = rs.getLong(1);
                        rs.close();
                        if (maxId > 0) {
                            stmt.execute("ALTER TABLE DB2ADMIN." + table
                                + " ALTER COLUMN ID RESTART WITH " + (maxId + 1));
                        }
                    } catch (Exception e) {
                        // table may not exist yet — ignore
                    }
                }
            }
        };
    }
}
