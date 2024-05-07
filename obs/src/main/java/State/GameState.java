package State;

import org.springframework.stereotype.Component;

@Component
public enum GameState {
    PLAYING, MENU, PAUSE, SETTING;
    public static GameState gameState = MENU;
}
