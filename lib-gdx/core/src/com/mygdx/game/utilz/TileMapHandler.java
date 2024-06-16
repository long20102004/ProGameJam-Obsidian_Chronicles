package com.mygdx.game.utilz;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entity.*;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.object.AnimatedObject;

import java.util.Random;

import static com.mygdx.game.utilz.Constant.ANIMATED_TREE.*;
import static com.mygdx.game.utilz.Constant.PPM;

public class TileMapHandler {
    private TiledMap tiledMap;
    private TiledMap menuBack;
    private GameScreen gameScreen;
    private Random random = new Random();

    public TileMapHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("map/oldmap.tmx");
        parseMapObject(tiledMap.getLayers().get("Object Layer 1").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public OrthogonalTiledMapRenderer setUpMenu() {
//        menuBack = new TmxMapLoader().load("map/oldmap.tmx");
//        parseMapObject(menuBack.getLayers().get("Object Layer 1").getObjects());
        return null;
    }

    public static void createMenuBack() {

    }

    private Body createBody(Rectangle rectangle, boolean isStatic, short categoryBits) {
        return BodyHandler.createBody(
                rectangle.getX() + rectangle.getWidth() / 2,
                rectangle.getY() + rectangle.getHeight() / 2,
                rectangle.getWidth(),
                rectangle.getHeight(),
                isStatic,
                gameScreen.getWorld(), categoryBits
        );
    }

    private void parseMapObject(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof PolygonMapObject) {
                createStaticBody((PolygonMapObject) mapObject);
            }
            if (mapObject instanceof PolylineMapObject) {
                createStaticBodyForPolyline((PolylineMapObject) mapObject);
            }
            if (mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName = mapObject.getName();
                if (rectangleName == null) continue;

                if (rectangleName.equals("player")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_PLAYER);
                    gameScreen.setPlayer(
                            new SwordMan("Character/SwordHero.png", Constant.PLAYER.SWORD_HERO.WIDTH,
                                    Constant.PLAYER.SWORD_HERO.HEIGHT, Constant.PLAYER.SWORD_HERO.DEFAULT_WIDTH,
                                    Constant.PLAYER.SWORD_HERO.DEFAULT_HEIGHT, body, 27)
                    );
                }
                if (rectangleName.contains("ghoul")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(
                            new Ghoul("Enemies/Ghoul.png", Constant.GHOUL.WIDTH, Constant.GHOUL.HEIGHT,
                                    Constant.GHOUL.DEFAULT_WIDTH, Constant.GHOUL.DEFAULT_HEIGHT, body, 7)
                    );
                    if (rectangleName.contains("-light")) {
                        gameScreen.addRayHandler(body, true);
                    }
                }
                if (rectangleName.contains("spitter")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(
                            new Spitter("Enemies/Spitter.png", Constant.SPITTER.WIDTH, Constant.SPITTER.HEIGHT,
                                    Constant.SPITTER.DEFAULT_WIDTH, Constant.SPITTER.DEFAULT_HEIGHT, body, 5)
                    );
                }
                if (rectangleName.contains("sword-master")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(new SwordMaster("Enemies/SwordMaster.png", Constant.SWORD_MASTER.WIDTH, Constant.SWORD_MASTER.HEIGHT,
                            Constant.SWORD_MASTER.DEFAULT_WIDTH, Constant.SWORD_MASTER.DEFAULT_HEIGHT, body, 4));
                }
                if (rectangleName.contains("hoarder")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(new Hoarder("Enemies/Hoarder.png", Constant.HOARDER.WIDTH, Constant.HOARDER.HEIGHT,
                            Constant.HOARDER.DEFAULT_WIDTH, Constant.HOARDER.DEFAULT_HEIGHT, body, 13));
                }
                if (rectangleName.contains("cage-shocker")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(new CageShocker("Enemies/CageShocker.png", Constant.CAGE_SHOCKER.WIDTH, Constant.CAGE_SHOCKER.HEIGHT,
                            Constant.CAGE_SHOCKER.DEFAULT_WIDTH, Constant.CAGE_SHOCKER.DEFAULT_HEIGHT, body, 4));
                }
                if (rectangleName.contains("dark-warden")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(new DarkWarden("Enemies/DarkWarden.png", Constant.DARK_WARDEN.WIDTH, Constant.DARK_WARDEN.HEIGHT,
                            Constant.DARK_WARDEN.DEFAULT_WIDTH, Constant.DARK_WARDEN.DEFAULT_HEIGHT, body, 5));
                }
                if (rectangleName.contains("shielder")) {
                    Body body = createBody(rectangle, false, GameScreen.CATEGORY_MONSTER);
                    gameScreen.addEnemy(new Shielder("Enemies/Shielder.png", Constant.SHIELDER.WIDTH, Constant.SHIELDER.HEIGHT,
                            Constant.SHIELDER.DEFAULT_WIDTH, Constant.SHIELDER.DEFAULT_HEIGHT, body, 6));
                }
                if (rectangleName.contains("tree1")) {
                    Body body = createBody(rectangle, true, GameScreen.CATEGORY_OBJECTS);
                    gameScreen.addObject(new AnimatedObject("Objects/AnimatedTree1.png", 8, 4,
                            TREE1_WIDTH, TREE1_HEIGHT, TREE1_DEFAULT_WIDTH, TREE1_DEFAULT_HEIGHT, random.nextInt(1, 5), body));
                    if (rectangleName.contains("-light")) {
                        gameScreen.addRayHandler(body, true);
                    }
                }
                if (rectangleName.contains("bugs")) {
                    Body body = createBody(rectangle, true,GameScreen.CATEGORY_OBJECTS);
                    gameScreen.addObject(new AnimatedObject("Objects/greenBug.png", 13, 1,
                            Constant.GREEN_BUG.DEFAULT_WIDTH, Constant.GREEN_BUG.DEFAULT_HEIGHT, Constant.GREEN_BUG.DEFAULT_WIDTH, Constant.GREEN_BUG.DEFAULT_HEIGHT, 1, body));
                    if (rectangleName.contains("-light")) {
                        gameScreen.addRayHandler(body, true);
                    }
                }
                if (rectangleName.equals("light")) {
                    Body body = createBody(rectangle, true, GameScreen.CATEGORY_OBJECTS);
                    gameScreen.addRayHandler(body, true);
                }

//                if (rectangleName.equals("flower")){
//                    Body body = createBody(rectangle, true);
//                    gameScreen.addObject(new AnimatedObject("Objects/Flower.png", ));
//                }
            }
        }
    }

    private void createStaticBody(PolygonMapObject object) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(object);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    private Shape createPolylineShape(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
        }

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(worldVertices);
        return chainShape;
    }

    private void createStaticBodyForPolyline(PolylineMapObject polylineObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolylineShape(polylineObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
            worldVertices[i] = current;
        }
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(worldVertices);
        return polygonShape;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
