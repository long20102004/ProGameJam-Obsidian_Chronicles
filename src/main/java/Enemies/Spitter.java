package Enemies;

import Main.Game;
import UI.EnemyHealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;

import static utilz.Constant.SPITTER.*;

public class Spitter extends Enemy {
    private final ArrayList<ProjectTile> projectTileList = new ArrayList<>();
    public static int damage = 2;

    public Spitter(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 60;
        attackSight = TILE_SIZE * 10;
        sight = 10 * attackSight;
        xDrawOffset = (int) (15 * MODE);
        yDrawOffset = (int) (10 * MODE);

        canShoot = true;
        animation = new BufferedImage[5][9];
        revAnimation = new BufferedImage[5][9];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, WIDTH / 2f, DEFAULT_HEIGHT * 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.SPITTER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        speed = 1f;
        healthBar = new EnemyHealthBar(this, (int) (0.4 * WIDTH), WIDTH / 15);
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        for (ProjectTile projectTile : projectTileList) {
            if (projectTile.isActive()) projectTile.draw(g, xLevelOffset, yLevelOffset);
        }
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        g.setColor(Color.RED);
        healthBar.draw(g, xLevelOffset, yLevelOffset);
//            g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
    }

    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        for (ProjectTile projectTile : projectTileList) {
            if (projectTile.isActive()) {
                if (projectTile.border.intersects(game.getPlayer().getHitbox())) {
                    enemyAttack(game, damage, 0);
                    projectTile.setActive(false);
                    continue;
                }
                projectTile.update();
            }
        } healthBar.update();
    }


    public void updateHealth(int damage) {
        currentHealth += damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            setState(DEAD);
        }
    }


    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case WAKE -> handleWakeState();
            case MOVING -> handleMovingState();
            case HIT -> handleHitState();
            case ATTACK -> handleAttackState(game);
            case DEAD -> handleDeadState();
        }
    }

    private void handleWakeState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(MOVING);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(WAKE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleHitState() {
        if (drawIndex == getType(HIT)) setState(WAKE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == getType(ATTACK) / 2) {
            projectTileList.add(new ProjectTile((int) hitbox.x, (int) hitbox.y, isRight));
            isAttacked = true;
        }
        if (drawIndex == getType(ATTACK) - 1) setState(WAKE);
    }

    private void handleDeadState() {
        if (drawIndex == getType(DEAD) - 1) isActive = false;
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
            if (drawIndex > getType(state)) {
                drawIndex = 0;
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
    public void resetAll() {
        super.resetAll();
        isActive = true;
        state = WAKE;
        currentHealth = maxHealth = 100;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, WIDTH / 2f, DEFAULT_HEIGHT * 1.5f);
    }

    @Override
    public void hurt(int damage) {
        setState(HIT);
        updateHealth(-damage);
    }

    class ProjectTile {
        private BufferedImage[][] animation;
        private int status = 0;
        private int MOVING = 0;
        private final int BLOW_1 = 1;
        private final int BLOW_2 = 2;
        private int drawIndex = 0, aniTick = 0, aniSpeed = 40;
        private int xPos, yPos;
        private boolean isActive = true;
        private Rectangle2D.Float border;
        private float speed = 2;

        public ProjectTile(int xPos, int yPos, boolean isRight) {
            this.xPos = xPos;
            this.yPos = yPos;
            if (!isRight) speed *= -1;
            initClass();
        }

        private void initClass() {
            border = new Rectangle2D.Float(xPos, yPos + 5, 16, 16);
            BufferedImage tmp = LoadSave.getImg(LoadSave.SPITTER_BULLET);
            animation = new BufferedImage[3][7];
            for (int i = 0; i < animation.length; i++) {
                for (int j = 0; j < animation[0].length; j++) {
                    animation[i][j] = tmp.getSubimage(j * 16, i * 16, 16, 16);
                }
            }
        }

        public void draw(Graphics g, float xLevelOffset, float yLevelOffset) {
            g.drawImage(animation[status][drawIndex], (int) ((int) border.x - xLevelOffset), (int) ((int) border.y - yLevelOffset), (int) border.width * 2, (int) border.height * 2, null);
            g.setColor(Color.RED);
//            g.drawRect((int) (border.x - xLevelOffset), (int) (border.y - yLevelOffset), (int) border.width, (int) border.height);
        }

        public void update() {
            updateAniTick();
            updateDir();
        }


        private void updateDir() {
            border.x += speed;
        }

        private void updateAniTick() {
            aniTick++;
            if (aniTick > aniSpeed) {
                drawIndex++;
                aniTick = 0;
                if (drawIndex >= getType(status)) drawIndex = 0;
            }
        }

        private int getType(int type) {
            if (type == MOVING) return 4;
            return 7;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }
}

