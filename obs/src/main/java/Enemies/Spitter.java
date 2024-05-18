package Enemies;

import Main.Game;
import UI.EnemyHealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Spitter extends Enemy {
    private final ArrayList<ProjectTile> projectTileList = new ArrayList<>();
    public static int damage = 2;

    public Spitter(int xPos, int yPos) {
        initClass(xPos, yPos);
        initEnemy(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        setRawImage(LoadSave.getImg(LoadSave.SPITTER));
        setAniSpeed(60);
        setAttackSight(Game.TILE_SIZE * 10);
        setSight(10 * attackSight);
        setXDrawOffset((int) (15 * Game.MODE));
        setYDrawOffset((int) (10 * Game.MODE));
        setImageHeight(5);
        setImageWidth(9);
        this.xPos = xPos;
        this.yPos = yPos;
        setHitboxWidth((int) (Constant.SPITTER.WIDTH / 2f));
        setHitBoxHeight((int) (Constant.SPITTER.DEFAULT_HEIGHT * 1.5f));
        setAttackBoxWidth((int) (Constant.SPITTER.WIDTH * 1.2f));
        setAttackBoxHeight((int) (Constant.SPITTER.HEIGHT * 1.5f));
        setTileWidth(Constant.SPITTER.DEFAULT_WIDTH);
        setTileHeight(Constant.SPITTER.DEFAULT_HEIGHT);
        setHealthBarWidth((int) (0.4 * Constant.SPITTER.WIDTH));
        setHealthBarHeight(Constant.SPITTER.WIDTH / 12);
        setSpeed(1f);
        setDrawWidth(Constant.SPITTER.WIDTH);
        setDrawHeight(Constant.SPITTER.HEIGHT);
        setAttackBoxChange(10 * Game.MODE);
        setDeadState(Constant.SPITTER.DEAD);
        setHitState(Constant.SPITTER.HIT);
        setAttackState(Constant.SPITTER.ATTACK);
        setDefaultState(Constant.SPITTER.WAKE);
        setCanShoot(true);
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        for (ProjectTile projectTile : projectTileList) {
            if (projectTile.isActive()) projectTile.draw(g, xLevelOffset, yLevelOffset);
        }
        super.draw(g,xLevelOffset,yLevelOffset);
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



    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case Constant.SPITTER.WAKE -> handleWakeState();
            case Constant.SPITTER.MOVING -> handleMovingState();
            case Constant.SPITTER.HIT -> handleHitState();
            case Constant.SPITTER.ATTACK -> handleAttackState(game);
            case Constant.SPITTER.DEAD -> handleDeadState();
        }
    }

    private void handleWakeState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.SPITTER.MOVING);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.SPITTER.ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.SPITTER.WAKE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.SPITTER.ATTACK);
    }

    private void handleHitState() {
        if (drawIndex == Constant.SPITTER.getType(Constant.SPITTER.HIT)) setState(Constant.SPITTER.WAKE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == Constant.SPITTER.getType(Constant.SPITTER.ATTACK) / 2) {
            projectTileList.add(new ProjectTile((int) hitbox.x, (int) hitbox.y, isRight));
            isAttacked = true;
        }
        if (drawIndex == Constant.SPITTER.getType(Constant.SPITTER.ATTACK) - 1) setState(Constant.SPITTER.WAKE);
    }

    private void handleDeadState() {
        if (drawIndex == Constant.SPITTER.getType(Constant.SPITTER.DEAD) - 1) isActive = false;
    }



    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex > Constant.SPITTER.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == Constant.SPITTER.DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

    static class ProjectTile {
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

