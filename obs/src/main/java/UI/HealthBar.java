package UI;

import Main.Game;
import Player.Player;
import utilz.Constant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


public class HealthBar {
    private BufferedImage healthBar;
    private BufferedImage[] powerBar;
    private final Player player;
    private int maxHealth;
    private int currentHealth;
    private int maxPower;
    private int currentPower;
    private int powerIndex = 9;
    private final int healthXPos = 50;
    private final int healthYPos = 50;
    private final int powerXPos = 60;
    private final int powerYPos = 70;
    private int previousPower;
    private int healthWidth, healthHeight, powerWidth, powerHeight;
    private int aniTick = 0, aniSpeed = 40;
    public HealthBar(Player player, int maxHealth, int maxPower){
        this.currentHealth = this.maxHealth = maxHealth;
        this.currentPower = this.maxPower = previousPower = maxPower;
        this.player = player;
        initClass();
    }
    private void initClass() {
        healthBar = LoadSave.getImg(LoadSave.HEALTH_BAR);
        powerBar = new BufferedImage[10];
        BufferedImage tmp = LoadSave.getImg(LoadSave.POWER_BAR);
        for (int i = 0; i < Constant.POWER_BAR.POWER; i++){
            powerBar[i] = tmp.getSubimage(i * Constant.POWER_BAR.DEFAULT_WIDTH, 0, Constant.POWER_BAR.DEFAULT_WIDTH, Constant.HEALTH_BAR.DEFAULT_HEIGHT);
        }
        healthWidth = (int) (Constant.HEALTH_BAR.WIDTH - 12 * Game.MODE);
        healthHeight = (int) (Constant.HEALTH_BAR.HEIGHT - 6 * Game.MODE);
        powerWidth = Constant.POWER_BAR.WIDTH;
        powerHeight = Constant.POWER_BAR.HEIGHT;
    }
    public void draw(Graphics g){
        g.drawImage(healthBar, healthXPos, healthYPos, Constant.HEALTH_BAR.WIDTH, Constant.HEALTH_BAR.HEIGHT, null);
        g.setColor(Color.RED);
        g.fillRect(healthXPos + 10, healthYPos + 5, healthWidth, healthHeight);
        g.drawImage(powerBar[powerIndex], powerXPos, powerYPos, powerWidth, powerHeight, null);
    }


    public void update(){
        currentHealth = player.getCurrentHealth();
        maxHealth = player.getMaxHealth();
        currentPower = player.getCurrentPower();
        maxPower = player.getMaxPower();
        healthWidth = (int) ((Constant.HEALTH_BAR.WIDTH - 12 * Game.MODE) * (1.0 * currentHealth / maxHealth));
        updateAniTick();
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            if (currentPower == previousPower){
                return;
            }
            else if (currentPower > previousPower){
                powerIndex++;
                previousPower += maxPower / 10;
            }
            else{
                powerIndex--;
                previousPower -= maxPower / 10;
            }
            if (powerIndex < 0) powerIndex = 0;
            if (powerIndex > 9) powerIndex = 9;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
}
