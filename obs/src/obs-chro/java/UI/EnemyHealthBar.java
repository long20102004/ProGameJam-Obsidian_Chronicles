package UI;

import Enemies.Enemy;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyHealthBar {
    private BufferedImage border;
    private final Enemy enemy;
    private final int maxHealth;
    private int currentHealth;
    private float xPos, yPos;
    private final int constWidth;
    private final int constHeight;
    private int width;

    public EnemyHealthBar(Enemy enemy, int constWidth, int constHeight) {
        initHealthBar();
        this.enemy = enemy;
        this.xPos = enemy.getHitbox().x;
        this.yPos = enemy.getHitbox().y - constHeight * 2;
        this.constWidth = constWidth;
        this.constHeight = constHeight;
        this.maxHealth = enemy.getMaxHealth();
        this.currentHealth = maxHealth;
}

    private void initHealthBar() {
        border = LoadSave.getImg(LoadSave.HEALTH_BAR);
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        if (!enemy.isActive()) return;
        g.drawImage(border, (int) xPos - xLevelOffset, (int) yPos - yLevelOffset, constWidth, constHeight, null);
        g.setColor(Color.RED);
        g.fillRect((int) (xPos + 6 - xLevelOffset), (int) (yPos + 2 - yLevelOffset), width - 16, constHeight - 6);
    }
    public void update(){
        updateHealthPos();
        updateHealthLeft();
    }

    private void updateHealthLeft() {
        width = (int) (constWidth * (1.0 * currentHealth / maxHealth));
        currentHealth = enemy.getCurrentHealth();
    }

    private void updateHealthPos() {
        xPos = enemy.getHitbox().x;
        yPos = enemy.getHitbox().y - constHeight * 2;
    }
}
