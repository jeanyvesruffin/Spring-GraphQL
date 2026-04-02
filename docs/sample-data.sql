-- Sample dataset for DB2ADMIN schema (Spring-GraphQL)
-- Adjust identity inserts if your DB disallows explicit IDs.
-- Instructions:
--  - To import with DB2 CLI: db2 -tvf docs/sample-data.sql
--  - If identity overrides are not allowed, remove ID columns from INSERTs.

-- Leagues (grouped)
INSERT INTO DB2ADMIN.LEAGUE (ID, NAME, CODE, COUNTRY, CREATED_AT) VALUES
  (1, 'Premier League', 'EPL', 'England', CURRENT TIMESTAMP),
  (2, 'La Liga', 'LL', 'Spain', CURRENT TIMESTAMP),
  (3, 'Bundesliga', 'BL', 'Germany', CURRENT TIMESTAMP),
  (4, 'Serie A', 'SA', 'Italy', CURRENT TIMESTAMP),
  (5, 'Ligue 1', 'L1', 'France', CURRENT TIMESTAMP),
  (6, 'MLS', 'MLS', 'USA', CURRENT TIMESTAMP),
  (7, 'Primeira Liga', 'PL', 'Portugal', CURRENT TIMESTAMP),
  (8, 'Eredivisie', 'ED', 'Netherlands', CURRENT TIMESTAMP),
  (9, 'Brasileiro Serie A', 'BRA', 'Brazil', CURRENT TIMESTAMP);
-- Teams (grouped)
INSERT INTO DB2ADMIN.TEAM (ID, LEAGUE_ID, NAME, SHORT_NAME, FOUNDED_YEAR, CITY, STADIUM, CREATED_AT) VALUES
  (1, 1, 'Manchester United', 'MUN', 1878, 'Manchester', 'Old Trafford', CURRENT TIMESTAMP),
  (2, 1, 'Liverpool', 'LIV', 1892, 'Liverpool', 'Anfield', CURRENT TIMESTAMP),
  (3, 2, 'Real Madrid', 'RMA', 1902, 'Madrid', 'Santiago Bernabeu', CURRENT TIMESTAMP),
  (4, 2, 'FC Barcelona', 'BAR', 1899, 'Barcelona', 'Camp Nou', CURRENT TIMESTAMP),
  (5, 3, 'Bayern Munich', 'BAY', 1900, 'Munich', 'Allianz Arena', CURRENT TIMESTAMP),
  (6, 3, 'Borussia Dortmund', 'BVB', 1909, 'Dortmund', 'Signal Iduna Park', CURRENT TIMESTAMP),
  (7, 4, 'Juventus', 'JUV', 1897, 'Turin', 'Allianz Stadium', CURRENT TIMESTAMP),
  (8, 4, 'AC Milan', 'ACM', 1899, 'Milan', 'San Siro', CURRENT TIMESTAMP),
  (9, 5, 'Paris Saint-Germain', 'PSG', 1970, 'Paris', 'Parc des Princes', CURRENT TIMESTAMP),
  (10, 5, 'Olympique Lyonnais', 'LYO', 1950, 'Lyon', 'Groupama Stadium', CURRENT TIMESTAMP),
  (11, 6, 'LA Galaxy', 'LAG', 1994, 'Los Angeles', 'Dignity Health Sports Park', CURRENT TIMESTAMP),
  (12, 6, 'New York City FC', 'NYCFC', 2013, 'New York', 'Yankee Stadium', CURRENT TIMESTAMP),
  (13, 7, 'FC Porto', 'POR', 1893, 'Porto', 'Estádio do Dragão', CURRENT TIMESTAMP),
  (14, 7, 'SL Benfica', 'BEN', 1904, 'Lisbon', 'Estádio da Luz', CURRENT TIMESTAMP),
  (15, 8, 'Ajax', 'AJA', 1900, 'Amsterdam', 'Johan Cruyff Arena', CURRENT TIMESTAMP),
  (16, 8, 'PSV Eindhoven', 'PSV', 1913, 'Eindhoven', 'Philips Stadion', CURRENT TIMESTAMP),
  (17, 9, 'Flamengo', 'FLA', 1895, 'Rio de Janeiro', 'Maracanã', CURRENT TIMESTAMP),
  (18, 9, 'Palmeiras', 'PAL', 1914, 'São Paulo', 'Allianz Parque', CURRENT TIMESTAMP);

-- Players (grouped)
INSERT INTO DB2ADMIN.PLAYER (ID, TEAM_ID, FIRST_NAME, LAST_NAME, BIRTHDATE, POSITION, NUMBER, NATIONALITY, CONTRACT_UNTIL, CREATED_AT) VALUES
  (1, 1, 'Marcus', 'Rashford', DATE('1997-10-31'), 'Forward', 10, 'England', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (2, 1, 'Bruno', 'Fernandes', DATE('1994-09-08'), 'Midfielder', 18, 'Portugal', DATE('2026-06-30'), CURRENT TIMESTAMP),
  (3, 2, 'Mohamed', 'Salah', DATE('1992-06-15'), 'Forward', 11, 'Egypt', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (4, 2, 'Virgil', 'van Dijk', DATE('1991-07-08'), 'Defender', 4, 'Netherlands', DATE('2024-06-30'), CURRENT TIMESTAMP),
  (5, 3, 'Karim', 'Benzema', DATE('1987-12-19'), 'Forward', 9, 'France', DATE('2023-06-30'), CURRENT TIMESTAMP),
  (6, 4, 'Sergio', 'Busquets', DATE('1988-07-16'), 'Midfielder', 5, 'Spain', DATE('2024-06-30'), CURRENT TIMESTAMP),
  (7, 5, 'Thomas', 'Muller', DATE('1989-09-13'), 'Forward', 25, 'Germany', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (8, 6, 'Erling', 'Haaland', DATE('2000-07-21'), 'Forward', 9, 'Norway', DATE('2028-06-30'), CURRENT TIMESTAMP),
  (9, 7, 'Paulo', 'Dybala', DATE('1993-11-15'), 'Forward', 21, 'Argentina', DATE('2026-06-30'), CURRENT TIMESTAMP),
  (10, 7, 'Federico', 'Chiesa', DATE('1997-10-25'), 'Winger', 22, 'Italy', DATE('2026-06-30'), CURRENT TIMESTAMP),
  (11, 8, 'Zlatan', 'Ibrahimovic', DATE('1981-10-03'), 'Forward', 11, 'Sweden', DATE('2023-06-30'), CURRENT TIMESTAMP),
  (12, 9, 'Kylian', 'Mbappé', DATE('1998-12-20'), 'Forward', 7, 'France', DATE('2027-06-30'), CURRENT TIMESTAMP),
  (13, 10, 'Memphis', 'Depay', DATE('1994-02-13'), 'Forward', 10, 'Netherlands', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (14, 11, 'Giovani', 'dos Santos', DATE('1990-05-11'), 'Midfielder', 8, 'Mexico', DATE('2024-06-30'), CURRENT TIMESTAMP),
  (15, 12, 'David', 'Villa', DATE('1981-12-03'), 'Forward', 7, 'Spain', DATE('2023-06-30'), CURRENT TIMESTAMP),
  (16, 13, 'Otávio', 'Marques', DATE('1995-02-02'), 'Midfielder', 8, 'Brazil', DATE('2026-06-30'), CURRENT TIMESTAMP),
  (17, 14, 'Rafa', 'Silva', DATE('1992-11-17'), 'Winger', 27, 'Portugal', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (18, 15, 'Dusan', 'Tadic', DATE('1988-11-20'), 'Midfielder', 10, 'Serbia', DATE('2024-06-30'), CURRENT TIMESTAMP),
  (19, 16, 'Donyell', 'Malen', DATE('1999-01-19'), 'Forward', 9, 'Netherlands', DATE('2026-06-30'), CURRENT TIMESTAMP),
  (20, 17, 'Gabriel', 'Barbosa', DATE('1996-08-30'), 'Forward', 9, 'Brazil', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (21, 18, 'Luiz', 'Adriano', DATE('1987-04-12'), 'Forward', 10, 'Brazil', DATE('2024-06-30'), CURRENT TIMESTAMP),
  (22, 11, 'Jonathan', 'dos Santos', DATE('1993-09-18'), 'Midfielder', 6, 'Mexico', DATE('2025-06-30'), CURRENT TIMESTAMP),
  (23, 12, 'Alexander', 'Ring', DATE('1991-08-26'), 'Midfielder', 16, 'Finland', DATE('2024-06-30'), CURRENT TIMESTAMP),
  (24, 13, 'Luis', 'Díaz', DATE('1997-01-13'), 'Forward', 7, 'Colombia', DATE('2026-06-30'), CURRENT TIMESTAMP);

-- Matches (grouped)
INSERT INTO DB2ADMIN.MATCHES (ID, LEAGUE_ID, SEASON, MATCH_DATE, HOME_TEAM_ID, AWAY_TEAM_ID, HOME_SCORE, AWAY_SCORE, STATUS, VENUE, CREATED_AT) VALUES
  (1, 1, '2024/2025', TIMESTAMP('2025-03-01 16:00:00'), 1, 2, 2, 1, 'FINISHED', 'Old Trafford', CURRENT TIMESTAMP),
  (2, 2, '2024/2025', TIMESTAMP('2025-03-02 20:00:00'), 3, 4, 3, 2, 'FINISHED', 'Santiago Bernabeu', CURRENT TIMESTAMP),
  (3, 3, '2024/2025', TIMESTAMP('2025-03-03 18:30:00'), 5, 6, 4, 0, 'FINISHED', 'Allianz Arena', CURRENT TIMESTAMP),
  (4, 1, '2024/2025', TIMESTAMP('2025-04-10 16:00:00'), 2, 1, 0, 0, 'SCHEDULED', 'Anfield', CURRENT TIMESTAMP),
  (5, 4, '2024/2025', TIMESTAMP('2025-03-05 18:00:00'), 7, 8, 1, 0, 'FINISHED', 'Allianz Stadium', CURRENT TIMESTAMP),
  (6, 4, '2024/2025', TIMESTAMP('2025-03-06 20:00:00'), 8, 7, 0, 2, 'FINISHED', 'San Siro', CURRENT TIMESTAMP),
  (7, 5, '2024/2025', TIMESTAMP('2025-03-07 19:45:00'), 9, 10, 2, 2, 'FINISHED', 'Parc des Princes', CURRENT TIMESTAMP),
  (8, 6, '2024/2025', TIMESTAMP('2025-03-08 20:00:00'), 11, 12, 3, 1, 'FINISHED', 'Dignity Health Sports Park', CURRENT TIMESTAMP),
  (9, 7, '2024/2025', TIMESTAMP('2025-03-09 18:30:00'), 13, 14, 1, 1, 'FINISHED', 'Estádio do Dragão', CURRENT TIMESTAMP),
  (10, 8, '2024/2025', TIMESTAMP('2025-03-10 18:30:00'), 15, 16, 0, 1, 'FINISHED', 'Johan Cruyff Arena', CURRENT TIMESTAMP),
  (11, 9, '2024/2025', TIMESTAMP('2025-03-11 20:00:00'), 17, 18, 2, 3, 'FINISHED', 'Maracanã', CURRENT TIMESTAMP),
  (12, 6, '2024/2025', TIMESTAMP('2025-04-01 20:00:00'), 12, 11, 1, 1, 'SCHEDULED', 'Yankee Stadium', CURRENT TIMESTAMP);

-- Player statistics (grouped)
INSERT INTO DB2ADMIN.PLAYER_STATISTIC (ID, MATCH_ID, PLAYER_ID, TEAM_ID, MINUTES_PLAYED, GOALS, ASSISTS, YELLOW_CARDS, RED_CARDS, SHOTS_ON_TARGET, CREATED_AT) VALUES
  (1, 1, 1, 1, 90, 1, 0, 0, 0, 3, CURRENT TIMESTAMP),
  (2, 1, 2, 1, 90, 1, 1, 0, 0, 2, CURRENT TIMESTAMP),
  (3, 1, 3, 2, 90, 0, 0, 1, 0, 1, CURRENT TIMESTAMP),
  (4, 2, 5, 3, 90, 2, 0, 0, 0, 4, CURRENT TIMESTAMP),
  (5, 2, 6, 4, 85, 0, 1, 1, 0, 1, CURRENT TIMESTAMP),
  (6, 3, 7, 5, 90, 1, 0, 0, 0, 2, CURRENT TIMESTAMP),
  (7, 3, 8, 6, 90, 2, 0, 0, 0, 5, CURRENT TIMESTAMP),
  (8, 5, 9, 7, 90, 1, 0, 0, 0, 2, CURRENT TIMESTAMP),
  (9, 5, 10, 7, 75, 0, 1, 1, 0, 1, CURRENT TIMESTAMP),
  (10, 6, 10, 7, 90, 2, 0, 0, 0, 4, CURRENT TIMESTAMP),
  (11, 7, 12, 9, 90, 1, 1, 0, 0, 3, CURRENT TIMESTAMP),
  (12, 7, 13, 10, 90, 1, 0, 1, 0, 2, CURRENT TIMESTAMP),
  (13, 8, 14, 11, 85, 1, 0, 0, 0, 3, CURRENT TIMESTAMP),
  (14, 8, 22, 11, 90, 0, 2, 0, 0, 1, CURRENT TIMESTAMP),
  (15, 9, 16, 13, 90, 1, 0, 0, 0, 2, CURRENT TIMESTAMP),
  (16, 9, 17, 14, 90, 0, 1, 1, 0, 1, CURRENT TIMESTAMP),
  (17, 10, 18, 15, 90, 1, 0, 0, 0, 2, CURRENT TIMESTAMP),
  (18, 10, 19, 16, 90, 0, 0, 0, 0, 1, CURRENT TIMESTAMP),
  (19, 11, 20, 17, 90, 2, 0, 0, 0, 5, CURRENT TIMESTAMP),
  (20, 11, 21, 18, 85, 1, 0, 0, 0, 2, CURRENT TIMESTAMP),
  (21, 12, 23, 12, 45, 0, 0, 0, 0, 0, CURRENT TIMESTAMP);

-- Achievements (grouped)
INSERT INTO DB2ADMIN.ACHIEVEMENT (ID, PLAYER_ID, TEAM_ID, TITLE, DESCRIPTION, ACHIEVED_AT, CREATED_AT) VALUES
  (1, 1, 1, 'Player of the Month', 'Outstanding performance in March', DATE('2025-03-31'), CURRENT TIMESTAMP),
  (2, 7, 5, 'Top Scorer', 'Scored most goals in the tournament', DATE('2024-12-01'), CURRENT TIMESTAMP),
  (3, 12, 9, 'Assist King', 'Most assists in a month', DATE('2025-03-31'), CURRENT TIMESTAMP),
  (4, 9, 7, 'Breakthrough Player', 'Young player of the season', DATE('2025-02-28'), CURRENT TIMESTAMP),
  (5, 14, 11, 'Captain Award', 'Outstanding leadership', DATE('2025-03-10'), CURRENT TIMESTAMP),
  (6, 20, 17, 'Golden Boot', 'Most goals in season', DATE('2025-05-15'), CURRENT TIMESTAMP);

-- Dashboards (grouped, dataset étendu)
INSERT INTO DB2ADMIN.DASHBOARD (ID, NAME, DESCRIPTION, LAST_UPDATED) VALUES
  (1, 'Overview', 'Main overview dashboard with key KPIs', TIMESTAMP('2025-03-15 10:00:00')),
  (2, 'Match Analytics', 'Per-match detailed analytics', TIMESTAMP('2025-03-15 11:00:00')),
  (3, 'Team Overview', 'Per-team KPIs', TIMESTAMP('2025-03-16 09:00:00')),
  (4, 'Season Summary', 'Season-level summary', TIMESTAMP('2025-03-16 09:30:00')),
  (5, 'International', 'International leagues overview', TIMESTAMP('2025-03-16 10:00:00')),
  (6, 'Simulations Extended', 'Additional simulation widgets', TIMESTAMP('2025-03-16 10:30:00')),
  (7, 'Custom Dashboard A', 'A custom test dashboard', TIMESTAMP('2025-03-17 09:00:00')),
  (8, 'Custom Dashboard B', 'Secondary custom dashboard', TIMESTAMP('2025-03-17 09:15:00')),
  (9, 'Team Insights', 'Metrics per team', TIMESTAMP('2025-03-17 09:30:00'));

-- Simulations (grouped)
INSERT INTO DB2ADMIN.SIMULATION (ID, NAME, STATUS, CONFIG, PROGRESS, CREATED_AT, STARTED_AT, ENDED_AT) VALUES
  (1, 'Season Simulation #1', 'COMPLETED', '{"teams":6,"iterations":1000}', 100.0, TIMESTAMP('2025-01-01 09:00:00'), TIMESTAMP('2025-01-01 09:01:00'), TIMESTAMP('2025-01-01 09:10:00')),
  (2, 'What-if: Home Advantage', 'RUNNING', '{"homeAdv":1.2,"iterations":500}', 45.0, TIMESTAMP('2025-03-10 12:00:00'), TIMESTAMP('2025-03-10 12:01:00'), NULL),
  (3, 'Season Simulation #2', 'COMPLETED', '{"teams":18,"iterations":2000}', 100.0, TIMESTAMP('2025-02-01 09:00:00'), TIMESTAMP('2025-02-01 09:01:00'), TIMESTAMP('2025-02-01 09:20:00')),
  (4, 'What-if: Injuries', 'COMPLETED', '{"injuryRate":0.05,"iterations":500}', 100.0, TIMESTAMP('2025-03-01 10:00:00'), TIMESTAMP('2025-03-01 10:01:00'), TIMESTAMP('2025-03-01 10:15:00')),
  (5, 'Continental Cup', 'RUNNING', '{"teams":8,"iterations":1000}', 60.5, TIMESTAMP('2025-03-12 08:00:00'), TIMESTAMP('2025-03-12 08:01:00'), NULL),
  (6, 'Youth League Sim', 'PENDING', '{"teams":10,"iterations":500}', 0.0, TIMESTAMP('2025-03-20 08:00:00'), NULL, NULL);

-- Simulation results (grouped)
INSERT INTO DB2ADMIN.SIMULATION_RESULT (ID, SIMULATION_ID, METRIC, VALUE, TIMESTAMP) VALUES
  (1, 1, 'avg_goals_per_match', 2.75, TIMESTAMP('2025-01-01 09:11:00')),
  (2, 1, 'win_prob_team_1', 0.42, TIMESTAMP('2025-01-01 09:11:00')),
  (3, 2, 'avg_home_win_prob', 0.58, TIMESTAMP('2025-03-10 12:45:00')),
  (4, 3, 'avg_goals', 3.10, TIMESTAMP('2025-02-01 09:21:00')),
  (5, 3, 'win_prob_top3', 0.72, TIMESTAMP('2025-02-01 09:21:00')),
  (6, 4, 'injury_rate', 0.047, TIMESTAMP('2025-03-01 10:16:00')),
  (7, 5, 'avg_points', 1.80, TIMESTAMP('2025-03-12 09:01:00')),
  (8, 5, 'avg_goals', 2.90, TIMESTAMP('2025-03-12 09:01:00')),
  (9, 6, 'youth_top_scorer_prob', 0.12, TIMESTAMP('2025-03-20 09:00:00'));

-- Widgets (grouped, includes new widgets for dashboards)
INSERT INTO DB2ADMIN.WIDGET (ID, DASHBOARD_ID, TYPE, TITLE, CONFIG, LINKED_SIMULATION_ID) VALUES
  (1, 1, 'CHART', 'Goals per Match', '{"metric":"avg_goals_per_match"}', 1),
  (2, 1, 'TABLE', 'Top Scorers', '{"limit":10}', NULL),
  (3, 2, 'KPI', 'Simulation Progress', '{"metric":"progress"}', 2),
  (4, 3, 'TABLE', 'League Standings', '{"limit":20}', 3),
  (5, 3, 'CHART', 'Home/Away Goals', '{"metric":"home_away_goals"}', 3),
  (6, 4, 'TABLE', 'Injury Report', '{"limit":50}', 4),
  (7, 5, 'CHART', 'Continental Progress', '{"metric":"avg_points"}', 5),
  (8, 6, 'KPI', 'Youth Progress', '{"metric":"progress"}', 6),
  (9, 2, 'CHART', 'International Comparison', '{"metric":"avg_goals"}', 3),
  (10, 7, 'CHART', 'Custom KPI', '{"metric":"kpi_custom"}', NULL),
  (11, 8, 'TABLE', 'Custom Table', '{"limit":5}', NULL),
  (12, 9, 'CHART', 'Team Metrics', '{"metric":"team_metrics"}', NULL);

COMMIT;
