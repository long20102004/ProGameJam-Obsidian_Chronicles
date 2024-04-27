package Player;

import Main.Game;

import java.awt.*;

public interface PlayerMethods {
    void updateWallSlide();

    void updateXPos(float speed);

    void updateInAir();

    void setAction();

    void updateAttackBox();

    void updateProperties();

    void draw(Graphics g, float xLevelOffset, float yLevelOffset);

    void update(Game game);

    void updateHealthAndPower(int changeHealth, int changePower, int knockBack);

    void setState(int state);
}
