package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.SUMMONER.*;
import Level.Level;

public class Summoner extends Enemy{
    public Summoner(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 80;
        attackSight = TILE_SIZE * 50;
        xDrawOffset = (int) (25 * MODE);
        yDrawOffset = (int) (10 * MODE);
        currentHealth = maxHealth = 400;
        animation = new BufferedImage[5][9];
        revAnimation = new BufferedImage[5][9];
        this.xPos = xPos;
        this.yPos = yPos;
        isFly = true;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.SUMMONER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
    }

    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
    }
    public void updateHealth(int damage) {
        currentHealth += damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            setState(DEAD);
        }
    }
    private void updateAttackBox() {
        if (isRight){
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        }
        else{
            attackBox.x = hitbox.x - hitbox.width - 10 * MODE;
            attackBox.y = hitbox.y;
        }
    }
    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case IDLE -> handleIdleState();
            case HIT -> handleHitState(game);
            case SUMMON -> handleSummonState(game);
            case DEAD -> handleDeadState();
        }
    }

    private void handleIdleState(){
        aniSpeed = 40;
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(SUMMON);
    }
    private void handleHitState(Game game){
        aniSpeed = 20;
        if (!isAttacked && drawIndex == getType(SUMMON) / 2){
            summonGhoul(game.getLevelManager().getLevel());
            isAttacked = true;
            setState(IDLE);
        }
        if (drawIndex == getType(HIT) - 2) {
            setState(IDLE);
        }
    }
    private void handleSummonState(Game game){
        if (drawIndex == 0){
            isAttacked = false;
        }
        if (!isAttacked && drawIndex == getType(SUMMON) / 2){
            summonGhoul(game.getLevelManager().getLevel());
            isAttacked = true;
            setState(IDLE);
        }
    }
    private void handleDeadState(){
        if (drawIndex == getType(DEAD) - 1) {
            isActive = false;
        }
    }
    private void updateDir(Game game) {
        if (game.getPlayer().getHitbox().x < hitbox.x) {
            isLeft = true;
            isRight = false;
        } else {
            isRight = true;
            isLeft = false;
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= getType(state)) {
                drawIndex = 0;
            }
        }
    }

    private void summonGhoul(Level level){
        for (Enemy enemy : level.enemies){
            if (enemy instanceof Ghoul ghoul) {
                if (ghoul.hitbox.y > this.hitbox.y) {
                    if (!ghoul.isActive) {
                        ghoul.revive();
                        break;
                    }
                }
            }
        }
    }
    @Override
    public void setState(int state) {
        if (this.state == DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll(){
        super.resetAll();
        isActive = true;
        state = IDLE;
        currentHealth = maxHealth = 400;
        drawIndex = 0;
        aniSpeed = 40;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
    }
    @Override
    public void hurt(int damage){
        setState(HIT);
        updateHealth(-damage);
    }
    @Override
    public void move() {
        float xspeed = speed;
        if (isLeft) xspeed *= -1;
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x + xspeed, hitbox.y)) {
            hitbox.x += xspeed;
        }
    }
}
