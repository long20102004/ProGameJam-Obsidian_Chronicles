package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.HIVE.*;

public class Hive extends Enemy{
    private BufferedImage[][] animation;
    private BufferedImage[][] revAnimation;
    private ArrayList<Spitter> temp = new ArrayList<>();
    private int drawIndex = 0;
    private int aniTick = 0;
    private int aniSpeed = 800;
    private final int attackSight = TILE_SIZE * 50;
    private final int xDrawOffset = (int) (25 * MODE);
    private final int yDrawOffset = (int) (20 * MODE);
    public static int damage = 0;

    public Hive(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        currentHealth = maxHealth = 400;
        animation = new BufferedImage[4][9];
        revAnimation = new BufferedImage[4][9];
        this.xPos = xPos;
        this.yPos = yPos;
        isFly = true;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.HIVE);
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
        switch (state) {
            case IDLE -> handleIdleState();
            case HIT -> handleHitState(game);
            case PRODUCE -> handleSummonState(game);
            case DEAD -> handleDeadState();
        }
    }

    private void handleIdleState(){
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(PRODUCE);
    }
    private void handleHitState(Game game){
        if (drawIndex == getType(HIT) - 1) {
            setState(IDLE);
        }
    }
    private void handleSummonState(Game game){
        if (drawIndex == 0){
            isAttacked = false;
        }
        if (!isAttacked && drawIndex == getType(PRODUCE) / 2){
            summonSpitter(game);
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

    private void summonSpitter(Game game){
        game.getEnemyManager().extraSpitter.add(new Spitter((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height)));
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
        aniSpeed = 400;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
    }
    @Override
    public void hurt(int damage){
        setState(HIT);
        updateHealth(-damage);
    }
}
