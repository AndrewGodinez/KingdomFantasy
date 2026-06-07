package cr.ac.una.kingdomfantasy.util;

import java.net.URL;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class MusicManager {

    public enum MusicTrack {
        MAIN_MENU,
        GAME,
        IMPROVEMENTS
    }

    public enum SoundEffect {
        CROSSBOW_SHOT,
        PROJECTILE_HIT,
        MONSTER_DEFEATED,
        NEW_WAVE,
        METEOR_CAST,
        ICE_CAST,
        SPELL_IMPACT,
        HERO_ATTACK,
        VICTORY,
        DEFEAT,
        GAME_COMPLETE,
        BUTTON_CLICK
    }

    private static final Logger LOGGER = Logger.getLogger(MusicManager.class.getName());
    private static final MusicManager INSTANCE = new MusicManager();

    private final Map<MusicTrack, String> trackSources = new EnumMap<>(MusicTrack.class);
    private final Map<SoundEffect, String> effectSources = new EnumMap<>(SoundEffect.class);
    private final Set<MediaPlayer> activeEffectPlayers = new HashSet<>();

    private volatile MediaPlayer currentTrackPlayer;
    private MusicTrack currentTrack;
    private double musicVolume = 0.45;
    private double effectVolume = 0.65;
    private volatile boolean muted;
    private volatile boolean shutdownCalled = false;

    private MusicManager() {
        configureDefaultSources();
    }

    public static MusicManager getInstance() {
        return INSTANCE;
    }

    private void configureDefaultSources() {
        registerTrack(MusicTrack.MAIN_MENU,    "/cr/ac/una/kingdomfantasy/resource/audio/Menu.wav");
        registerTrack(MusicTrack.GAME,         "/cr/ac/una/kingdomfantasy/resource/audio/PlayTime.wav");
        registerTrack(MusicTrack.IMPROVEMENTS, "/cr/ac/una/kingdomfantasy/resource/audio/AcercaDe.wav");
        registerEffect(SoundEffect.CROSSBOW_SHOT, "/cr/ac/una/kingdomfantasy/resource/audio/CrossbowShot.wav");
        registerEffect(SoundEffect.PROJECTILE_HIT, "/cr/ac/una/kingdomfantasy/resource/audio/ProjectileHit.wav");
        registerEffect(SoundEffect.MONSTER_DEFEATED, "/cr/ac/una/kingdomfantasy/resource/audio/MonsterDefeated.wav");
        registerEffect(SoundEffect.NEW_WAVE, "/cr/ac/una/kingdomfantasy/resource/audio/NewWave.wav");
        registerEffect(SoundEffect.METEOR_CAST, "/cr/ac/una/kingdomfantasy/resource/audio/MeteorCast.wav");
        registerEffect(SoundEffect.ICE_CAST, "/cr/ac/una/kingdomfantasy/resource/audio/IceCast.wav");
        registerEffect(SoundEffect.SPELL_IMPACT, "/cr/ac/una/kingdomfantasy/resource/audio/SpellImpact.wav");
        registerEffect(SoundEffect.HERO_ATTACK, "/cr/ac/una/kingdomfantasy/resource/audio/HeroAttack.wav");
        registerEffect(SoundEffect.VICTORY, "/cr/ac/una/kingdomfantasy/resource/audio/LevelWin.wav");
        registerEffect(SoundEffect.DEFEAT, "/cr/ac/una/kingdomfantasy/resource/audio/LevelLose.wav");
        registerEffect(SoundEffect.GAME_COMPLETE, "/cr/ac/una/kingdomfantasy/resource/audio/GameWin.wav");
        registerEffect(SoundEffect.BUTTON_CLICK, "/cr/ac/una/kingdomfantasy/resource/audio/ButtonClick.wav");
    }

    public void registerTrack(MusicTrack track, String source) {
        if (track != null) {
            trackSources.put(track, source == null ? "" : source.trim());
        }
    }

    public void registerEffect(SoundEffect effect, String source) {
        if (effect != null) {
            effectSources.put(effect, source == null ? "" : source.trim());
        }
    }

    public void playTrack(MusicTrack track) {
        if (shutdownCalled || track == null) {
            stopTrack();
            return;
        }
        if (track == currentTrack && currentTrackPlayer != null) {
            if (muted) {
                currentTrackPlayer.pause();
            } else if (currentTrackPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                currentTrackPlayer.play();
            }
            return;
        }
        stopTrack();
        String mediaUrl = resolveResourceUrl(trackSources.get(track));
        if (mediaUrl == null) {
            LOGGER.log(Level.WARNING, "Audio track resource not found: {0}", track);
            return;
        }
        try {
            MediaPlayer player = new MediaPlayer(new Media(mediaUrl));
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(muted ? 0 : musicVolume);
            currentTrack = track;
            currentTrackPlayer = player;
            player.setOnError(() ->
                LOGGER.log(Level.WARNING, "MediaPlayer error on track: " + track));
            if (!muted) {
                player.play();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "El sonido no puede ser cargado " + track, ex);
            currentTrack = null;
            currentTrackPlayer = null;
        }
    }

    public void stopTrack() {
        MediaPlayer player = currentTrackPlayer;
        currentTrackPlayer = null;
        currentTrack = null;
        if (player != null) {
            try { player.setVolume(0); } catch (Exception ignored) {}
            try { player.stop();       } catch (Exception ignored) {}
            try { player.dispose();    } catch (Exception ignored) {}
        }
    }

    public void pauseTrack() {
        if (currentTrackPlayer != null) {
            currentTrackPlayer.pause();
        }
    }

    public void resumeTrack() {
        if (currentTrackPlayer != null && !muted) {
            currentTrackPlayer.play();
        }
    }

    public void playEffect(SoundEffect effect) {
        if (shutdownCalled || effect == null || muted) {
            return;
        }
        String src = effectSources.get(effect);
        String mediaUrl = resolveResourceUrl(src);
        if (mediaUrl == null) {
            return;
        }
        try {
            MediaPlayer player = new MediaPlayer(new Media(mediaUrl));
            player.setVolume(effectVolume);
            activeEffectPlayers.add(player);
            player.setOnEndOfMedia(() -> disposeEffectPlayer(player));
            player.setOnError(()       -> disposeEffectPlayer(player));
            player.play();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "El efecto de sonido no puede ser transmitido: " + effect, ex);
        }
    }

    public void setMusicVolume(double volume) {
        musicVolume = clamp(volume);
        if (currentTrackPlayer != null && !muted) {
            currentTrackPlayer.setVolume(musicVolume);
        }
    }

    public void setEffectVolume(double volume) {
        effectVolume = clamp(volume);
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (currentTrackPlayer != null) {
            if (muted) {
                currentTrackPlayer.pause();
            } else {
                currentTrackPlayer.setVolume(musicVolume);
                currentTrackPlayer.play();
            }
        }
        if (muted) {
            stopEffects();
        }
    }

    public boolean isMuted() { return muted; }

    public MusicTrack getCurrentTrack() { return currentTrack; }

    public synchronized void shutdown() {
        if (shutdownCalled) return;
        shutdownCalled = true;
        muted = true;
        stopTrack();
        stopEffects();
    }

    private void stopEffects() {
        for (MediaPlayer player : new HashSet<>(activeEffectPlayers)) {
            disposeEffectPlayer(player);
        }
        activeEffectPlayers.clear();
    }

    private void disposeEffectPlayer(MediaPlayer player) {
        if (player == null) return;
        activeEffectPlayers.remove(player);
        try { player.setVolume(0); } catch (Exception ignored) {}
        try { player.stop();       } catch (Exception ignored) {}
        try { player.dispose();    } catch (Exception ignored) {}
    }

    private String resolveResourceUrl(String source) {
        if (source == null || source.isBlank()) return null;
        URL url = MusicManager.class.getResource(source);
        if (url != null) return url.toExternalForm();
        LOGGER.log(Level.SEVERE, "Audio no encontrado ", source);
        return null;
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }
}