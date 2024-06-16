package com.mygdx.game.utilz;

import com.badlogic.gdx.Gdx;

public class Constant {
    public static final float MODE = 2.3f;
    public static final float PPM = 32f;
    public static final float TILE_SIZE = 16 / PPM;
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public static class PAUSE_SCREEN {
        public static final int DEFAULT_HEIGHT = 492;
        public static final int DEFAULT_WIDTH = 225;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
    }

    public static class URM_BUTTONS {
        public static final int DEFAULT_HEIGHT = 33;
        public static final int DEFAULT_WIDTH = 93;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int CONTINUE = 0;
        public static final int NEW_GAME = 1;
        public static final int SAVE = 2;
        public static final int QUIT = 3;
        public static final int BACK = 4;
        public static final int START = 5;
        public static final int DELETE = 6;
        public static final int EXIT = 7;
        public static final int CREDITS = 8;
        public static final int MAIN_MENU = 9;
        public static final int RESUME = 10;
        public static final int OPTIONS = 11;
        public static final int SETTINGS = 12;
        public static final int RESTART = 13;
    }

    public static class PLAYER {
        public static class SWORD_HERO {
            public static final int DEFAULT_WIDTH = 90;
            public static final int DEFAULT_HEIGHT = 37;
            public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.5f);
            public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
            public static final int STATIC_IDLE = 0;
            public static final int IDLE = 1;
            public static final int WALK = 2;
            public static final int RUN = 3;
            public static final int RUN_FAST = 4;
            public static final int ROLL = 5;
            public static final int ROLL_ATTACK = 6;
            public static final int SLASH_1 = 7;
            public static final int SLASH_2 = 8;
            public static final int SLAM_ATTACK = 9;
            public static final int SPIN_ATTACK = 10;
            public static final int BLOCK = 11;
            public static final int DASH = 12;
            public static final int JUMP = 13;
            public static final int TRANSITION_FROM_JUMP_TO_FALL = 14;
            public static final int FALL = 15;
            public static final int JUMP_TELEPORT = 16;
            public static final int APPEAR_FROM_TELEPORT = 17;
            public static final int FALL_ATTACK = 18;
            public static final int LAND_CROUCH = 19;
            public static final int LEDGE_GRAB = 20;
            public static final int STATIC_WALL_HOLD = 21;
            public static final int TRANSITION_FROM_STATIC_WALL_HOLD_TO_WALL_SLIDE = 22;
            public static final int WALL_SLIDE = 23;
            public static final int WALL_SLIDE_STOP = 24;
            public static final int HIT = 25;
            public static final int DEATH = 26;

            public static int getType(int type) {
                switch (type) {
                    case STATIC_IDLE:
                        return 1;
                    case IDLE:
                        return 9;
                    case WALK:
                        return 8;
                    case RUN:
                        return 8;
                    case RUN_FAST:
                        return 8;
                    case ROLL:
                        return 8;
                    case ROLL_ATTACK:
                        return 10;
                    case SLASH_1:
                        return 7;
                    case SLASH_2:
                        return 5;
                    case SLAM_ATTACK:
                        return 5;
                    case SPIN_ATTACK:
                        return 6;
                    case BLOCK:
                        return 6;
                    case DASH:
                        return 7;
                    case JUMP:
                        return 3;
                    case TRANSITION_FROM_JUMP_TO_FALL:
                        return 4;
                    case FALL:
                        return 3;
                    case JUMP_TELEPORT:
                        return 3;
                    case APPEAR_FROM_TELEPORT:
                        return 6;
                    case FALL_ATTACK:
                        return 9;
                    case LAND_CROUCH:
                        return 4;
                    case LEDGE_GRAB:
                        return 5;
                    case STATIC_WALL_HOLD:
                        return 1;
                    case TRANSITION_FROM_STATIC_WALL_HOLD_TO_WALL_SLIDE:
                        return 2;
                    case WALL_SLIDE:
                        return 3;
                    case WALL_SLIDE_STOP:
                        return 3;
                    case HIT:
                        return 2;
                    case DEATH:
                        return 6;
                }
                return 1;
            }
        }

        public static class GUNSLINGER {
            public static final int DEFAULT_HEIGHT = 48;
            public static final int DEFAULT_WIDTH = 48;
            public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.1f);
            public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.1f);
            public static final int IDLE = 0;
            public static final int RUNNING = 1;
            public static final int CHARGE = 2;
            public static final int SHOOT_1 = 3;
            public static final int SHOOT_2 = 4;
            public static final int LEDGE_GRAB = 5;
            public static final int JUMP = 6;
            public static final int TRANSITION = 7;
            public static final int FALL = 8;
            public static final int LAND = 9;
            public static final int ROLL = 10;
            public static final int HIT = 11;
            public static final int DEAD = 12;

            public static int getType(int type) {
                switch (type) {
                    case IDLE:
                        return 1;
                    case RUNNING:
                        return 8;
                    case CHARGE:
                        return 5;
                    case SHOOT_1:
                        return 3;
                    case SHOOT_2:
                        return 4;
                    case LEDGE_GRAB:
                        return 6;
                    case JUMP:
                        return 1;
                    case TRANSITION:
                        return 1;
                    case FALL:
                        return 1;
                    case LAND:
                        return 3;
                    case ROLL:
                        return 9;
                    case HIT:
                        return 2;
                    case DEAD:
                        return 4;
                }
                return 1;
            }
        }

        public static class SWORD_WOMAN {
            public static final int DEFAULT_HEIGHT = 66;
            public static final int DEFAULT_WIDTH = 98;
            public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.5f);
            public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.5f);

            public static class ATTACKS {
                public static final int DEFAULT_WIDTH = 144;
                public static final int DEFAULT_HEIGHT = 80;
                public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.5f);
                public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.5f);
                public static final int FRONT_ATTACK = 0;
                public static final int LIGHT_ATTACK = 1;
                public static final int UP_LIGHT_ATTACK = 2;
                public static final int AIR_ATTACK = 3;
                public static final int HEAVY_AIR_ATTACK = 4;

                public static int getType(int type) {
                    switch (type) {
                        case FRONT_ATTACK:
                            return 18;
                        case LIGHT_ATTACK:
                            return 12;
                        case UP_LIGHT_ATTACK:
                            return 9;
                        case AIR_ATTACK:
                            return 17;
                        case HEAVY_AIR_ATTACK:
                            return 24;
                    }
                    return 1;
                }
            }

            public static class BUFFS {
                public static final int DEFAULT_WIDTH = 286;
                public static final int DEFAULT_HEIGHT = 114;
                public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.5f);
                public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.5f);
                public static final int CAST_BUFF_1 = 0;
                public static final int CAST_BUFF_2 = 1;
                public static final int CAST_BUFF_3 = 2;
                public static final int CAST_SHIELD_BUFF_1 = 3;
                public static final int CAST_SHIELD_BUFF_2 = 4;
                public static final int CAST_SHIELD_BUFF_3 = 5;
                public static final int LIGHT_CUT_1 = 6;
                public static final int LIGHT_CUT_2 = 7;
                public static final int LIGHT_CUT_3 = 8;
                public static final int HEAL_1 = 9;
                public static final int HEAL_2 = 10;
                public static final int HEAL_3 = 11;
                public static final int HOLY_SLASH_1 = 12;
                public static final int HOLY_SLASH_2 = 13;
                public static final int HOLY_SLASH_3 = 14;
                public static final int GREAT_HEAL_1 = 15;
                public static final int GREAT_HEAL_2 = 16;
                public static final int GREAT_HEAL_3 = 17;
                public static final int GREAT_HEAL_4 = 18;

                public static int getType(int attackType) {
                    switch (attackType) {
                        case CAST_BUFF_1:
                            return 3;
                        case CAST_BUFF_2:
                            return 4;
                        case CAST_BUFF_3:
                            return 14;
                        case CAST_SHIELD_BUFF_1:
                            return 3;
                        case CAST_SHIELD_BUFF_2:
                            return 3;
                        case CAST_SHIELD_BUFF_3:
                            return 10;
                        case LIGHT_CUT_1:
                            return 10;
                        case LIGHT_CUT_2:
                            return 8;
                        case LIGHT_CUT_3:
                            return 10;
                        case HEAL_1:
                            return 7;
                        case HEAL_2:
                            return 8;
                        case HEAL_3:
                            return 10;
                        case HOLY_SLASH_1:
                            return 5;
                        case HOLY_SLASH_2:
                            return 7;
                        case HOLY_SLASH_3:
                            return 16;
                        case GREAT_HEAL_1:
                            return 5;
                        case GREAT_HEAL_2:
                            return 8;
                        case GREAT_HEAL_3:
                            return 16;
                        case GREAT_HEAL_4:
                            return 5;
                    }
                    return 1;
                }
            }

            public static class DEFAULT_ACTIONS {
                public static final int DEFAULT_WIDTH = 66;
                public static final int DEFAULT_HEIGHT = 98;
                public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.5f);
                public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.5f);
                public static final int IDLE = 0;
                public static final int WALK = 1;
                public static final int RUN = 2;
                public static final int JUMP_AND_FALL = 3;
                public static final int SPRINT_JUMP = 4;
                public static final int ROLL = 5;
                public static final int DASH = 6;
                public static final int BLOCK = 7;
                public static final int SILDE = 8;

                public static final int getType(int type) {
                    switch (type) {
                        case IDLE:
                            return 6;
                        case WALK:
                            return 24;
                        case RUN:
                            return 11; // CHUA FUll
                        case JUMP_AND_FALL:
                            return 16;
                        case SPRINT_JUMP:
                            return 25;
                        case ROLL:
                            return 15;
                        case DASH:
                            return 12;
                        case BLOCK:
                            return 6;
                        case SILDE:
                            return 11;
                    }
                    return 1;
                }
            }

            public static class HEALING_AND_GRAB {
                public static final int DEFAULT_WIDTH = 79;
                public static final int DEFAULT_HEIGHT = 96;
                public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.5f);
                public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.5f);
                public static final int HURT = 0;
                public static final int DEATH = 1;
                public static final int GRAB = 2;
                public static final int LAND_HARD = 3;
                public static final int REST = 4;

                public static int getType(int type) {
                    switch (type) {
                        case HURT:
                            return 6;
                        case DEATH:
                            return 25;
                        case GRAB:
                            return 14;
                        case LAND_HARD:
                            return 12;
                        case REST:
                            return 24;
                    }
                    return 1;
                }
            }

            public static class EXTENSION_ACTIONS {
                public static final int DEFAULT_WIDTH = 128;
                public static final int DEFAULT_HEIGHT = 128;
                public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE / 1.5f);
                public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE / 1.5f);
                public static final int COUNTER = 0;
                public static final int LADDER = 1;
                public static final int SPRINT = 2;
                public static final int BACK_DASH = 3;
                public static final int KNOCK_BACK = 4;
                public static final int CROUCH = 5;

                public static int getType(int type) {
                    switch (type) {
                        case COUNTER:
                            return 24;
                        case LADDER:
                            return 30;
                        case SPRINT:
                            return 15;
                        case BACK_DASH:
                            return 13;
                        case KNOCK_BACK:
                            return 25;
                        case CROUCH:
                            return 15;
                    }
                    return 1;
                }
            }
        }
    }

    public static class SWORD_MASTER {
        public static final int DEFAULT_WIDTH = 95;
        public static final int DEFAULT_HEIGHT = 49;
        public static final int WIDTH = (int) (95 * 1.5f);
        public static final int HEIGHT = (int) (49 * MODE);
        public static final int IDLE = 0;
        public static final int MOVING = 1;
        public static final int ATTACK = 2;
        public static final int DEAD = 3;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 11;
                case MOVING:
                    return 7;
                case ATTACK:
                    return 23;
                case DEAD:
                    return 22;
            }
            return 1;
        }
    }

    public static class GHOUL {
        public static final int DEFAULT_WIDTH = 62;
        public static final int DEFAULT_HEIGHT = 33;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 2f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WAKE = 0;
        public static final int MOVING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;
        public static final int REVIVE = 5;

        public static int getType(int type) {
            switch (type) {
                case WAKE:
                    return 4;
                case MOVING:
                    return 8;
                case ATTACK:
                    return 6;
                case HIT:
                    return 3;
                case DEAD:
                    return 5;
                case REVIVE:
                    return 10;
            }
            return 1;
        }
    }

    public static class HIVE {
        public static final int DEFAULT_WIDTH = 78;
        public static final int DEFAULT_HEIGHT = 43;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE * 1.3);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE * 1.3);
        public static final int IDLE = 0;
        public static final int PRODUCE = 1;
        public static final int HIT = 2;
        public static final int DEAD = 3;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 1;
                case PRODUCE:
                    return 9;
                case HIT:
                    return 3;
                case DEAD:
                    return 8;
            }
            return 1;
        }
    }

    //    80x144
    public static class DAGGER {
        public static final int DEFAULT_HEIGHT = 80;
        public static final int DEFAULT_WIDTH = 144;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int IDLE = 0;
        public static final int AIR_ATTACK = 1;
        public static final int ATTACK = 2;
        public static final int RUN = 3;
        public static final int RUN_FAST = 4;
        public static final int SHOOT = 5;
        public static final int HIT = 6;
        public static final int DEATH = 7;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 4;
                case AIR_ATTACK:
                    return 10;
                case ATTACK:
                    return 9;
                case RUN:
                    return 7;
                case RUN_FAST:
                    return 5;
                case SHOOT:
                    return 6;
                case HIT:
                    return 1;
                case DEATH:
                    return 10;
            }
            return 1;
        }
    }

    public static class FLOWER {
        public static final int DEFAULT_SIZE = 32;
        public static final int SIZE = (int) (DEFAULT_SIZE * MODE);
        public static final int GLOW = 5;
    }

    public static class LIGHT_BUG {
        public static final int DEFAULT_HEIGHT = 37;
        public static final int DEFAULT_WIDTH = 32;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int TURN = 9;
    }

    public static class TRAP {
        public static final int DEFAULT_HEIGHT = 64;
        public static final int DEFAULT_WIDTH = 16;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
    }

    public static class JUMP_BASE {
        public static final int DEFAULT_HEIGHT = 41;
        public static final int DEFAULT_WIDTH = 28;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int IDLE = 0;
        public static final int WARP = 1;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 8;
                case WARP:
                    return 8;
            }
            return 1;
        }
    }

    public static class BUG {
        public static final int DEFAULT_WIDTH = 111;
        public static final int DEFAULT_HEIGHT = 54;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int LIGHT = 15;
    }

    public static class GREEN_BUG {
        public static final int DEFAULT_WIDTH = 96;
        public static final int DEFAULT_HEIGHT = 64;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.8f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * 1.8f);
        public static final int LIGHT = 13;
    }

    public static class ANIMATED_TREE {
        public static final int TREE1_DEFAULT_HEIGHT = 128;
        public static final int TREE1_DEFAULT_WIDTH = 112;
        public static final int TREE1_HEIGHT = (int) (TREE1_DEFAULT_HEIGHT * MODE);
        public static final int TREE1_WIDTH = (int) (TREE1_DEFAULT_WIDTH * 1.2f);
        public static final int TREE2_DEFAULT_HEIGHT = 96;
        public static final int TREE2_DEFAULT_WIDTH = 128;
        public static final int TREE2_HEIGHT = (int) (TREE2_DEFAULT_HEIGHT * MODE);
        public static final int TREE2_WIDTH = (int) (TREE2_DEFAULT_WIDTH * MODE);
        public static final int BLOOD = 0;
        public static final int ANCIENT1 = 1;
        public static final int ANCIENT2 = 2;
        public static final int GROWN = 3;

        public static int getType(int type) {
            switch (type) {
                case BLOOD:
                    return 7;
                case ANCIENT1:
                    return 13;
                case ANCIENT2:
                    return 7;
                case GROWN:
                    return 15;
            }
            return 1;
        }
    }

    public static class HEALTH_BAR {
        public static final int DEFAULT_WIDTH = 48;
        public static final int DEFAULT_HEIGHT = 12;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE * 1.5);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
    }

    public static class POWER_BAR {
        public static final int DEFAULT_WIDTH = 35;
        public static final int DEFAULT_HEIGHT = 16;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE * 1.5);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int POWER = 10;
    }

    public static class LIGHT {
        public static final int DEFAULT_HEIGHT = 400;
        public static final int DEFAULT_WIDTH = 400;
        public static final int LIGHT_LOOP = 9;
    }

    public static class ITEMS {
        public static final int DEFAULT_HEIGHT = 16;
        public static final int DEFAULT_WIDTH = 16;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int COINS1 = 0;
        public static final int COINS2 = 1;
        public static final int BANDAGE = 9;

        public static int getType(int type) {
            switch (type) {
                case COINS1:
                case COINS2:
                    return 3;
                case BANDAGE:
                    return 9;
            }
            return 1;
        }
    }

    public static class MERCHANT {
        public static final int DEFAULT_WIDTH = 108;
        public static final int DEFAULT_HEIGHT = 39;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE * 1.2f);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE * 1.2f);
        public static final int TURN_LIGHT = 20;
    }

    public static class CHAT {
        public static final int FRAME_DEFAULT_WIDTH = 336;
        public static final int FRAME_DEFAULT_HEIGHT = 81;
    }

    public static class SUMMONER {
        public static final int DEFAULT_HEIGHT = 44;
        public static final int DEFAULT_WIDTH = 46;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int IDLE = 0;
        public static final int MOVE = 1;
        public static final int SUMMON = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 5;
                case MOVE:
                    return 5;
                case SUMMON:
                    return 8;
                case HIT:
                    return 3;
                case DEAD:
                    return 7;
            }
            return 1;
        }
    }

    public static class SPITTER {
        public static final int DEFAULT_WIDTH = 57;
        public static final int DEFAULT_HEIGHT = 39;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.6f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WAKE = 0;
        public static final int MOVING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static int getType(int type) {
            switch (type) {
                case WAKE:
                    return 3;
                case MOVING:
                    return 6;
                case ATTACK:
                    return 5;
                case HIT:
                    return 3;
                case DEAD:
                    return 5;
            }
            return 1;
        }
    }

    public static class SHIELDER {
        public static final int DEFAULT_WIDTH = 129;
        public static final int DEFAULT_HEIGHT = 46;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.5f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int IDLE = 0;
        public static final int WALK = 1;
        public static final int ATTACK = 2;
        public static final int SHOT = 3;
        public static final int HIT = 4;
        public static final int DEAD = 5;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 1;
                case WALK:
                    return 6;
                case ATTACK:
                    return 17;
                case SHOT:
                    return 15;
                case HIT:
                    return 2;
                case DEAD:
                    return 17;
            }
            return 1;
        }
    }

    public static class GUARDIAN {
        public static final int DEFAULT_WIDTH = 166;
        public static final int DEFAULT_HEIGHT = 96;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int IDLE = 0;
        public static final int SHIELD = 1;
        public static final int LASER = 2;
        public static final int AOE = 3;
        public static final int TELEPORT = 4;
        public static final int APPEAR = 5;
        public static final int DEAD = 6;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 24;
                case SHIELD:
                    return 24;
                case LASER:
                    return 24;
                case AOE:
                    return 24;
                case TELEPORT:
                    return 12;
                case APPEAR:
                    return 8;
                case DEAD:
                    return 12;
            }
            return 1;
        }
    }

    public static class DOOR {
        public static final int DEFAULT_HEIGHT = 48;
        public static final int DEFAULT_WIDTH = 41;
        public static final int TURN = 15;
        public static final int IDLE = 0;
    }

    public static class PORTAL {
        public static final int DEFAULT_WIDTH = 189;
        public static final int DEFAULT_HEIGHT = 90;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.6f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * 1.6f);
        public static final int IDLE = 0;
        public static final int READY = 1;
        public static final int TELEPORT = 2;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 24;
                case READY:
                    return 17;
                case TELEPORT:
                    return 8;
            }
            return 1;
        }
    }

    public static class PRODUCT {
        public static final int DEFAULT_WIDTH = 32;
        public static final int DEFAULT_HEIGHT = 32;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.5f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * 1.6f);
    }
    public static class DARK_WARDEN {
        public static final int DEFAULT_WIDTH = 110;
        public static final int DEFAULT_HEIGHT = 42;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 2f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int IDLE = 0;
        public static final int MOVING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 24;
                case MOVING:
                    return 12;
                case ATTACK:
                    return 16;
                case HIT:
                    return 2;
                case DEAD:
                    return 12;
            }
            return 1;
        }
    }

    public static class CAGE_SHOCKER {
        public static final int DEFAULT_WIDTH = 97;
        public static final int DEFAULT_HEIGHT = 32;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.5f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int IDLE = 0;
        public static final int MOVING = 1;
        public static final int ATTACK = 2;
        public static final int DEAD = 3;

        public static int getType(int type) {
            switch (type) {
                case IDLE:
                    return 12;
                case MOVING:
                    return 12;
                case ATTACK:
                    return 15;
                case DEAD:
                    return 11;
            }
            return 1;
        }
    }

    public static class HOARDER {
        public static final int DEFAULT_WIDTH = 222;
        public static final int DEFAULT_HEIGHT = 119;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * 1.7f);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int IDLE_LOW = 0;
        public static final int STATIONARY_ATTACK = 1;
        public static final int READY_TO_MOVE = 2;
        public static final int MOVE = 3;
        public static final int PREPARE_ATTACK = 4;
        public static final int MOVE_ATTACK = 5;
        public static final int STOP_MOVING_AFTER_ATTACK = 6;
        public static final int STOP_MOVING = 7;
        public static final int VANISH = 8;
        public static final int APPEAR = 9;
        public static final int RETURN_TO_IDLE = 10;
        public static final int HIT = 11;
        public static final int DEAD = 12;

        public static int getType(int type) {
            switch (type) {
                case IDLE_LOW:
                    return 9;
                case STATIONARY_ATTACK:
                    return 9;
                case READY_TO_MOVE:
                    return 2;
                case MOVE:
                    return 7;
                case PREPARE_ATTACK:
                    return 8;
                case MOVE_ATTACK:
                    return 6;
                case STOP_MOVING_AFTER_ATTACK:
                    return 4;
                case STOP_MOVING:
                    return 4;
                case VANISH:
                    return 4;
                case APPEAR:
                    return 9;
                case RETURN_TO_IDLE:
                    return 13;
                case HIT:
                    return 2;
                case DEAD:
                    return 36;
            }
            return 1;
        }
    }

    public static class GUN {
        public static final int DEFAULT_HEIGHT = 32;
        public static final int DEFAULT_WIDTH = 32;
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int IDLE_GUN_1 = 0;
        public static final int SHOOT_GUN_1 = 1;
        public static final int IDLE_GUN_2 = 2;
        public static final int CHARGE_GUN_2 = 3;
        public static final int SHOOT_GUN_2 = 4;

        public static final int getType(int type) {
            switch (type) {
                case IDLE_GUN_1:
                    return 1;
                case SHOOT_GUN_1:
                    return 4;
                case IDLE_GUN_2:
                    return 1;
                case CHARGE_GUN_2:
                    return 4;
                case SHOOT_GUN_2:
                    return 3;
            }
            return 1;
        }
    }

    public static class FX {
        public static class HIT {
            public static final int HIT1 = 0;
            public static final int HIT2 = 1;
            public static final int HIT3 = 2;
            public static final int HIT4 = 3;
            public static final int SPARK1 = 4;
            public static final int SPARK2 = 5;
            public static final int ELECTRIC1 = 6;
            public static final int ELECTRIC2 = 7;
            public static final int ELECTRIC3 = 8;
            public static final int ELECTRIC4 = 9;
            public static final int BLOOD1 = 10;
            public static final int BLOOD2 = 11;
            public static final int BLOOD3 = 12;
            public static final int POISON1 = 13;
            public static final int POISON2 = 14;
            public static final int POISON3 = 15;
            public static final int STORM = 16;
            public static final int RED_CLAW = 17;
            public static final int DEFAULT_WIDTH = 82;
            public static final int DEFAULT_HEIGHT = 65;
            public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
            public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);

            public static int getType(int type) {
                switch (type) {
                    case HIT1:
                        return 4;
                    case HIT2:
                        return 8;
                    case HIT3:
                        return 3;
                    case HIT4:
                        return 8;
                    case SPARK1:
                        return 4;
                    case SPARK2:
                        return 5;
                    case ELECTRIC1:
                        return 6;
                    case ELECTRIC2:
                        return 8;
                    case ELECTRIC3:
                        return 4;
                    case ELECTRIC4:
                        return 9;
                    case BLOOD1:
                        return 4;
                    case BLOOD2:
                        return 3;
                    case BLOOD3:
                        return 8;
                    case POISON1:
                        return 9;
                    case POISON2:
                        return 13;
                    case POISON3:
                        return 14;
                    case STORM:
                        return 9;
                    case RED_CLAW:
                        return 10;
                }
                return 1;
            }
        }
    }

    public static class NPC {
        public static final int DEFAULT_WIDTH = 48;
        public static final int DEFAULT_HEIGHT = 32;
        public static final int WIDTH = (int) (DEFAULT_WIDTH * MODE);
        public static final int HEIGHT = (int) (DEFAULT_HEIGHT * MODE);
    }

}
