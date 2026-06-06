package cr.ac.una.kingdomfantasy.util;

import cr.ac.una.kingdomfantasy.model.CrossbowDesign;
import cr.ac.una.kingdomfantasy.model.MejoraDto;
import cr.ac.una.kingdomfantasy.model.PartidaDto;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.model.PlayerProgress;
import cr.ac.una.kingdomfantasy.model.UpgradeProfile;
import cr.ac.una.kingdomfantasy.model.UpgradeType;
import cr.ac.una.kingdomfantasy.service.MejoraService;
import cr.ac.una.kingdomfantasy.service.PartidaService;
import cr.ac.una.kingdomfantasy.service.PlayerService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlayerRegistry {

    public static final String PLAYERS_KEY = "playerProfiles";
    public static final String CURRENT_PLAYER_KEY = "currentPlayerProfile";
    public static final String TOTAL_POINTS_KEY = "playerTotalPoints";
    public static final String GOLD_KEY = "playerGold";
    public static final String CURRENT_LEVEL_KEY = "currentLevel";
    public static final String CROSSBOW_DESIGN_KEY = "crossbowDesign";
    public static final String UPGRADE_PROFILE_KEY = "upgradeProfile";

    private static final String PLAYER_DTO_KEY = "Player";
    private static final String PARTIDA_DTO_KEY = "Partida";
    private static final String MEJORA_DTO_KEY = "Mejora";
    private static final String REVIEW_MODE_KEY = "reviewModeEnabled";

    private static final PlayerService PLAYER_SERVICE = new PlayerService();
    private static final PartidaService PARTIDA_SERVICE = new PartidaService();
    private static final MejoraService MEJORA_SERVICE = new MejoraService();

    private PlayerRegistry() {
    }

    @SuppressWarnings("unchecked")
    public static List<PlayerProgress> getPlayers() {
        Object value = AppContext.getInstance().get(PLAYERS_KEY);
        if (value instanceof List<?>) {
            return (List<PlayerProgress>) value;
        }
        List<PlayerProgress> players = new ArrayList<>();
        AppContext.getInstance().set(PLAYERS_KEY, players);
        return players;
    }

    public static List<PlayerProgress> getRankingPlayers() {
        syncCurrentFromContext();
        List<PlayerProgress> players = new ArrayList<>(getPlayers());
        players.sort((first, second) -> {
            int levelCompare = Integer.compare(second.getCurrentLevel(), first.getCurrentLevel());
            if (levelCompare != 0) {
                return levelCompare;
            }
            int pointsCompare = Integer.compare(second.getTotalPoints(), first.getTotalPoints());
            if (pointsCompare != 0) {
                return pointsCompare;
            }
            return first.getName().compareToIgnoreCase(second.getName());
        });
        return players;
    }

    public static PlayerProgress findByName(String name) {
        String safeName = normalizeName(name);
        for (PlayerProgress player : getPlayers()) {
            if (player.getName().equalsIgnoreCase(safeName)) {
                return player;
            }
        }
        return null;
    }

    public static PlayerProgress createPlayer(String name, CrossbowDesign design) {
        String safeName = normalizeName(name);
        PlayerProgress existing = findByName(safeName);
        if (existing != null) {
            return existing;
        }
        PlayerProgress player = new PlayerProgress(safeName);
        player.setCrossbowDesign(design);
        getPlayers().add(player);
        return player;
    }

    public static PlayerProgress getCurrentPlayer() {
        Object value = AppContext.getInstance().get(CURRENT_PLAYER_KEY);
        return value instanceof PlayerProgress ? (PlayerProgress) value : null;
    }

    public static PlayerProgress getOrCreateCurrentPlayer() {
        PlayerDto playerDto = currentPlayerDto();
        if (playerDto != null) {
            PlayerProgress current = getCurrentPlayer();
            if (current == null || !current.getName().equalsIgnoreCase(playerDto.getNombre())) {
                current = playerProgressFromDatabase(playerDto);
                rememberPlayer(current);
            } else {
                syncCurrentFromDatabase(current, playerDto);
            }
            activateInContext(current);
            return current;
        }

        PlayerProgress current = getCurrentPlayer();
        if (current != null) {
            return current;
        }
        PlayerProgress guest = findByName("Invitado");
        if (guest == null) {
            guest = createPlayer("Invitado", CrossbowDesign.GREEN);
        }
        activate(guest);
        return guest;
    }

    public static void activate(PlayerProgress player) {
        if (player == null) {
            return;
        }
        rememberPlayer(player);
        activateInContext(player);
    }

    public static void syncCurrentFromContext() {
        PlayerProgress current = getOrCreateCurrentPlayer();
        current.setGold(readInt(GOLD_KEY, current.getGold()));
        current.setCurrentLevel(readInt(CURRENT_LEVEL_KEY, current.getCurrentLevel()));
        current.setTotalPoints(readInt(TOTAL_POINTS_KEY, current.getTotalPoints()));

        Object design = AppContext.getInstance().get(CROSSBOW_DESIGN_KEY);
        if (design instanceof CrossbowDesign) {
            current.setCrossbowDesign((CrossbowDesign) design);
        }
        Object profile = AppContext.getInstance().get(UPGRADE_PROFILE_KEY);
        if (profile instanceof UpgradeProfile) {
            current.setUpgradeProfile((UpgradeProfile) profile);
        }
        persistCurrent(current);
    }

    public static void setGold(int gold) {
        PlayerProgress current = getOrCreateCurrentPlayer();
        int safeGold = Math.max(0, gold);
        current.setGold(safeGold);
        AppContext.getInstance().set(GOLD_KEY, safeGold);
        PartidaDto partida = currentPartidaDto();
        if (partida != null && !isReviewModeEnabled()) {
            partida.setPuntosActuales((long) safeGold);
            partida.setFechaGuardado(LocalDate.now());
            savePartida(partida);
        }
    }

    public static void setCurrentLevel(int level) {
        PlayerProgress current = getOrCreateCurrentPlayer();
        int safeLevel = Math.max(1, Math.min(100, level));
        current.setCurrentLevel(safeLevel);
        AppContext.getInstance().set(CURRENT_LEVEL_KEY, safeLevel);
        PartidaDto partida = currentPartidaDto();
        if (partida != null && !isReviewModeEnabled()) {
            partida.setNivelActual(safeLevel);
            partida.setFechaGuardado(LocalDate.now());
            savePartida(partida);
        }
    }

    public static void setCrossbowDesign(CrossbowDesign design) {
        PlayerProgress current = getOrCreateCurrentPlayer();
        CrossbowDesign safeDesign = design == null ? CrossbowDesign.GREEN : design;
        current.setCrossbowDesign(safeDesign);
        AppContext.getInstance().set(CROSSBOW_DESIGN_KEY, safeDesign);
        PlayerDto playerDto = currentPlayerDto();
        if (playerDto != null && !isReviewModeEnabled()) {
            playerDto.setIdBallesta(crossbowId(safeDesign));
            savePlayer(playerDto);
        }
    }

    public static void setUpgradeProfile(UpgradeProfile profile) {
        PlayerProgress current = getOrCreateCurrentPlayer();
        UpgradeProfile safeProfile = profile == null ? new UpgradeProfile() : profile;
        current.setUpgradeProfile(safeProfile);
        AppContext.getInstance().set(UPGRADE_PROFILE_KEY, safeProfile);
        MejoraDto mejora = currentMejoraDto();
        if (mejora != null && !isReviewModeEnabled()) {
            copyProfileToDto(safeProfile, mejora);
            saveMejora(mejora);
        }
    }

    public static void addHistoricalPoints(int points) {
        PlayerProgress current = getOrCreateCurrentPlayer();
        current.addPoints(points);
        AppContext.getInstance().set(TOTAL_POINTS_KEY, current.getTotalPoints());
        PlayerDto playerDto = currentPlayerDto();
        if (playerDto != null && !isReviewModeEnabled()) {
            playerDto.setPuntosTotales((long) current.getTotalPoints());
            savePlayer(playerDto);
        }
    }

    public static int getHistoricalPoints() {
        PlayerProgress current = getCurrentPlayer();
        if (current != null) {
            return current.getTotalPoints();
        }
        return readInt(TOTAL_POINTS_KEY, 0);
    }

    public static int rankOf(PlayerProgress player) {
        if (player == null) {
            return -1;
        }
        List<PlayerProgress> ranking = getRankingPlayers();
        return ranking.indexOf(player) + 1;
    }

    public static List<PlayerProgress> readOnlyPlayers() {
        return Collections.unmodifiableList(getPlayers());
    }

    private static PlayerProgress playerProgressFromDatabase(PlayerDto playerDto) {
        PartidaDto partida = ensurePartida(playerDto);
        MejoraDto mejora = ensureMejora(partida);

        PlayerProgress progress = new PlayerProgress(playerDto.getNombre());
        progress.setTotalPoints(toInt(playerDto.getPuntosTotales(), 0));
        progress.setGold(toInt(partida == null ? null : partida.getPuntosActuales(), 0));
        progress.setCurrentLevel(toInt(partida == null ? null : partida.getNivelActual(), 1));
        progress.setCrossbowDesign(designFromId(playerDto.getIdBallesta()));
        progress.setUpgradeProfile(profileFromDto(mejora));
        return progress;
    }

    private static void syncCurrentFromDatabase(PlayerProgress current, PlayerDto playerDto) {
        PartidaDto partida = ensurePartida(playerDto);
        MejoraDto mejora = ensureMejora(partida);
        current.setTotalPoints(toInt(playerDto.getPuntosTotales(), current.getTotalPoints()));
        current.setGold(toInt(partida == null ? null : partida.getPuntosActuales(), current.getGold()));
        current.setCurrentLevel(toInt(partida == null ? null : partida.getNivelActual(), current.getCurrentLevel()));
        current.setCrossbowDesign(designFromId(playerDto.getIdBallesta()));
        current.setUpgradeProfile(profileFromDto(mejora));
    }

    private static void activateInContext(PlayerProgress player) {
        AppContext.getInstance().set(CURRENT_PLAYER_KEY, player);
        AppContext.getInstance().set(GOLD_KEY, player.getGold());
        AppContext.getInstance().set(CURRENT_LEVEL_KEY, player.getCurrentLevel());
        AppContext.getInstance().set(CROSSBOW_DESIGN_KEY, player.getCrossbowDesign());
        AppContext.getInstance().set(UPGRADE_PROFILE_KEY, player.getUpgradeProfile());
        AppContext.getInstance().set(TOTAL_POINTS_KEY, player.getTotalPoints());
    }

    private static void persistCurrent(PlayerProgress current) {
        if (current == null || isReviewModeEnabled()) {
            return;
        }
        PlayerDto playerDto = currentPlayerDto();
        PartidaDto partida = currentPartidaDto();
        MejoraDto mejora = currentMejoraDto();
        if (playerDto != null) {
            playerDto.setPuntosTotales((long) current.getTotalPoints());
            playerDto.setIdBallesta(crossbowId(current.getCrossbowDesign()));
            savePlayer(playerDto);
        }
        if (partida != null) {
            partida.setPuntosActuales((long) current.getGold());
            partida.setNivelActual(current.getCurrentLevel());
            partida.setFechaGuardado(LocalDate.now());
            savePartida(partida);
        }
        if (mejora != null) {
            copyProfileToDto(current.getUpgradeProfile(), mejora);
            saveMejora(mejora);
        }
    }

    private static PartidaDto ensurePartida(PlayerDto playerDto) {
        PartidaDto partida = currentPartidaDto();
        if (partida != null || playerDto == null || playerDto.getId() == null) {
            return partida;
        }
        Respuesta respuesta = PARTIDA_SERVICE.getPartida(playerDto.getId());
        if (respuesta.getEstado()) {
            partida = (PartidaDto) respuesta.getResultado("Partida");
            AppContext.getInstance().set(PARTIDA_DTO_KEY, partida);
        }
        return partida;
    }

    private static MejoraDto ensureMejora(PartidaDto partida) {
        MejoraDto mejora = currentMejoraDto();
        if (mejora != null || partida == null || partida.getIdmej() == null || partida.getIdmej().getId() == null) {
            return mejora;
        }
        Respuesta respuesta = MEJORA_SERVICE.getMejora(partida.getIdmej().getId());
        if (respuesta.getEstado()) {
            mejora = (MejoraDto) respuesta.getResultado("Mejora");
            AppContext.getInstance().set(MEJORA_DTO_KEY, mejora);
        }
        return mejora;
    }

    private static PlayerDto currentPlayerDto() {
        Object value = AppContext.getInstance().get(PLAYER_DTO_KEY);
        return value instanceof PlayerDto ? (PlayerDto) value : null;
    }

    private static PartidaDto currentPartidaDto() {
        Object value = AppContext.getInstance().get(PARTIDA_DTO_KEY);
        return value instanceof PartidaDto ? (PartidaDto) value : null;
    }

    private static MejoraDto currentMejoraDto() {
        Object value = AppContext.getInstance().get(MEJORA_DTO_KEY);
        return value instanceof MejoraDto ? (MejoraDto) value : null;
    }

    private static UpgradeProfile profileFromDto(MejoraDto dto) {
        UpgradeProfile profile = new UpgradeProfile();
        if (dto == null) {
            return profile;
        }
        profile.setLevel(UpgradeType.CROSSBOW_DAMAGE, toInt(dto.getNivelDanoBallesta(), 1));
        profile.setLevel(UpgradeType.CROSSBOW_SPEED, toInt(dto.getNivelVelocidadBallesta(), 1));
        profile.setLevel(UpgradeType.CASTLE_HEALTH, toInt(dto.getNivelCastillo(), 1));
        profile.setLevel(UpgradeType.ELIXIR_CAPACITY, toInt(dto.getNivelElixir(), 1));
        profile.setLevel(UpgradeType.METEOR_DAMAGE, toInt(dto.getNivelEfectoMeteoro(), 1));
        profile.setLevel(UpgradeType.METEOR_RADIUS, toInt(dto.getNivelRangoMeteoro(), 1));
        profile.setLevel(UpgradeType.ICE_DURATION, toInt(dto.getNivelEfectoHielo(), 1));
        profile.setLevel(UpgradeType.ICE_RADIUS, toInt(dto.getNivelRangoHielo(), 1));
        return profile;
    }

    private static void copyProfileToDto(UpgradeProfile profile, MejoraDto dto) {
        if (profile == null || dto == null) {
            return;
        }
        dto.setNivelDanoBallesta(profile.getLevel(UpgradeType.CROSSBOW_DAMAGE));
        dto.setNivelVelocidadBallesta(profile.getLevel(UpgradeType.CROSSBOW_SPEED));
        dto.setNivelCastillo(profile.getLevel(UpgradeType.CASTLE_HEALTH));
        dto.setNivelElixir(profile.getLevel(UpgradeType.ELIXIR_CAPACITY));
        dto.setNivelEfectoMeteoro(profile.getLevel(UpgradeType.METEOR_DAMAGE));
        dto.setNivelRangoMeteoro(profile.getLevel(UpgradeType.METEOR_RADIUS));
        dto.setNivelEfectoHielo(profile.getLevel(UpgradeType.ICE_DURATION));
        dto.setNivelRangoHielo(profile.getLevel(UpgradeType.ICE_RADIUS));
    }

    private static void savePlayer(PlayerDto playerDto) {
        Respuesta respuesta = PLAYER_SERVICE.guardarPlayer(playerDto);
        if (respuesta.getEstado()) {
            AppContext.getInstance().set(PLAYER_DTO_KEY, respuesta.getResultado("Jugador"));
        }
    }

    private static void savePartida(PartidaDto partida) {
        Respuesta respuesta = PARTIDA_SERVICE.guardarPartida(partida);
        if (respuesta.getEstado()) {
            AppContext.getInstance().set(PARTIDA_DTO_KEY, respuesta.getResultado("Partida"));
        }
    }

    private static void saveMejora(MejoraDto mejora) {
        Respuesta respuesta = MEJORA_SERVICE.guardarMejora(mejora);
        if (respuesta.getEstado()) {
            AppContext.getInstance().set(MEJORA_DTO_KEY, respuesta.getResultado("Mejora"));
        }
    }

    private static CrossbowDesign designFromId(Integer idBallesta) {
        return idBallesta != null && idBallesta == 2 ? CrossbowDesign.PURPLE : CrossbowDesign.GREEN;
    }

    private static int crossbowId(CrossbowDesign design) {
        return design == CrossbowDesign.PURPLE ? 2 : 1;
    }

    private static void rememberPlayer(PlayerProgress player) {
        if (player == null || findByName(player.getName()) != null) {
            return;
        }
        getPlayers().add(player);
    }

    private static int readInt(String key, int fallback) {
        Object value = AppContext.getInstance().get(key);
        if (value instanceof Number) {
            return Math.max(0, ((Number) value).intValue());
        }
        return fallback;
    }

    private static int toInt(Number value, int fallback) {
        return value == null ? fallback : value.intValue();
    }

    private static String normalizeName(String name) {
        String safeName = name == null ? "" : name.trim();
        return safeName.isBlank() ? "Invitado" : safeName;
    }

    private static boolean isReviewModeEnabled() {
        Object value = AppContext.getInstance().get(REVIEW_MODE_KEY);
        return value instanceof Boolean && (Boolean) value;
    }
}
