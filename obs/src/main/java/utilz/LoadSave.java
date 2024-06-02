package utilz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static java.util.Arrays.sort;

public class LoadSave {
    public static final String SWORD_WOMAN_ATTACK = "Character/JoannaP1.png";
    public static final String SWORD_WOMAN_BUFFS = "Character/JoannaP2.png";
    public static final String SWORD_WOMAN_DEFAULT = "Character/JoannaP3.png";
    public static final String SWORD_WOMAN_HEALING = "Character/JoannaP4.png";
    public static final String SWORD_WOMAN_EXTENSION = "Character/JoannaP5.png";
    public static final String SWORD_HERO = "Character/SwordHero.png";
    public static final String GUNSLINGER = "Character/Gunslinger.png";
    public static final String CURSOR = "UI/cursor.png";
    public static final String GHOUL = "Enemies/Ghoul.png";
    public static final String SPITTER = "Enemies/Spitter.png";
    public static final String SPITTER_BULLET = "Weapon/RedBullet.png";
    public static final String SUMMONER = "Objects/Summoner.png";
    public static final String HIVE = "Enemies/Hive.png";
    public static final String DAGGER = "Enemies/Dagger.png";
    public static final String GUARDIAN = "Enemies/Guardian.png";
    public static final String SHIELDER = "Enemies/Shielder.png";
    public static final String SWORD_MASTER = "Enemies/SwordMaster.png";
    public static final String FLOWER = "Objects/Flower.png";
    public static final String LIGHT_BUG = "Objects/LightBug.png";
    public static final String JUMP_BASE = "Objects/JumpBase.png";
    public static final String BUGS = "Objects/Bugs.png";
    public static final String TREE_TYPE_1 = "Objects/AnimatedTree1.png";
    public static final String TREE_TYPE_2 = "Objects/AnimatedTree2.png";
    public static final String URM_BUTTONS = "UI/Buttons.png";
    public static final String KEYBOARD_BUTTON1 = "UI/Keyboard1.png";
    public static final String KEYBOARD_BUTTON2 = "UI/Keyboard2.png";
    public static final String MENU_SCREEN = "background/MenuBack.png";
    public static final String YELLOW_LIGHT = "Objects/light.png";
    public static final String WHITE_LIGHT = "Objects/whiteLight.png";
    public static final String HEALTH_BAR = "UI/HealthBar.png";
    public static final String POWER_BAR = "UI/PowerBar.png";
    public static final String PAUSE_BACKGROUND = "Objects/PauseBack.png";
    public static final String ITEMS = "UI/Items.png";
    public static final String MERCHANT = "Objects/Merchant1.png";
    public static final String DOOR = "Objects/door.png";
    public static final String BLOOD_PORTAL = "Objects/Blood Portal.png";
    public static final String WEAPONS = "Shop/weapon.png";
    public static final String POTIONS = "Shop/potions.png";
    public static final String HOARDER = "Enemies/Hoarder.png";
    public static final String GUN = "Weapon/Guns.png";
    public static final String BULLET = "Weapon/Bullet.png";
    public static final String AIM_CURSOR = "Weapon/Aim Cursors.png";
    public static final String DIALOGUE = "UI/Dialogue.png";
    public static final String SHOP_BACKGROUND = "Shop/ShopBackground.png";
    public static final String PRODUCT_FRAME = "Shop/ProductFrame.png";
    public static final String HIGHLIGHTER = "Shop/Highlighter.png";
    public static final String GREEN_BUG = "Objects/greenBug.png";
    public static final String SKILLS = "Shop/skills.png";
    public static final String OLD_MAN = "NPCs/OldMan.png";
    public static final String COIN = "Objects/coin.png";
    public static final String SWORD_HERO_ICON = "Character/SwordHeroIcon.png";
    public static final String SWORD_WOMAN_ICON = "Character/JoannaIcon.png";
    public static final String TRAP_RED = "Objects/trap-red.png";
    public static final String TRAP_BLUE = "Objects/trap-blue.png";

    public static BufferedImage getImg(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] getAllLvlsTileMap() {
        int i = 1;
        ArrayList<BufferedImage> list = new ArrayList<>();
        InputStream input = LoadSave.class.getResourceAsStream("/lvlsMap/TileMap" + i + ".png");
        while (input != null) {
            input = LoadSave.class.getResourceAsStream("/lvlsMap/TileMap" + i + ".png");
            if (input == null) break;
            try {
                BufferedImage img = ImageIO.read(input);
                list.add(img);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
        return list.toArray(new BufferedImage[0]);
    }

    public static BufferedImage[] getAllLvlsTileSet() {
        int i = 1;
        ArrayList<BufferedImage> list = new ArrayList<>();
        InputStream input = LoadSave.class.getResourceAsStream("/lvlsTileSet/TileSet" + i + ".png");
        while (input != null) {
            input = LoadSave.class.getResourceAsStream("/lvlsTileSet/TileSet" + i + ".png");
            if (input == null) break;
            try {
                BufferedImage img = ImageIO.read(input);
                list.add(img);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
        return list.toArray(new BufferedImage[0]);
    }

    public static BufferedImage[] getAllBackgrounds() {
        int i = 1;
        ArrayList<BufferedImage> list = new ArrayList<>();
        InputStream input = LoadSave.class.getResourceAsStream("/background/background" + i + ".png");
        while (input != null) {
            input = LoadSave.class.getResourceAsStream("/background/background" + i + ".png");
            if (input == null) break;
            try {
                BufferedImage img = ImageIO.read(input);
                list.add(img);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
        return list.toArray(new BufferedImage[0]);
    }

    public static Font numberFont(int size) {
        Font numberFont = new Component() {
        }.getFont();
        InputStream inputStream = LoadSave.class.getResourceAsStream("/Font/PixelFont.ttf");
        try {
            numberFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            numberFont = numberFont.deriveFont(Font.PLAIN, size);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
        return numberFont;
    }

    public static Font wordFont() {
        Font wordFont = new Component() {
        }.getFont();
                InputStream inputStream = LoadSave.class.getResourceAsStream("/Font/PixelWord.ttf");
        try {
            wordFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            wordFont = wordFont.deriveFont(Font.PLAIN, 15);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
        return wordFont;
    }
}
