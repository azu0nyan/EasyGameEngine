package ru.ege.examples.stuff;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import ru.ege.engine.EGEngine;
import ru.ege.engine.smartDO.BodyDO;
import ru.ege.engine.smartDO.Camera;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EGEngine.i().startDrawingThread();

        DebugObjects.addDebugObjects(EGEngine.i());
        EGEngine.i().enableCamera();
        Camera.enableControls();
        EGEngine.i().getWorld().setGravity(new Vec2(0, 100));

        //Линии
        new BodyDO(new Vec2(900, 1000), 0, BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        new BodyDO(new Vec2(900, 50), 0, BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        new BodyDO(new Vec2(50, 1000), (float) (Math.PI / 2), BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        new BodyDO(new Vec2(1500, 50), (float) (Math.PI / 2), BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        //Круг
        BodyDO circleDO = new BodyDO(new Vec2(900, 700), 0, BodyType.DYNAMIC, 100);
        circleDO.setColor(new Color(189, 8, 255));

        //Прямоугольник
        BodyDO rectDO =new BodyDO(new Vec2(500, 200), (float)Math.PI / 8.0f, BodyType.STATIC, 100.0f, 100.0f);
        rectDO.setStroke( new BasicStroke(10));
        rectDO.setColor(Color.RED);
        rectDO.setFill(false);



        Vec2 chain [] = {
                new Vec2(-100, -100),
                new Vec2(-100, -50),
                new Vec2(-50, -30),
                new Vec2(-50, -10),
                new Vec2(25, 0),
                new Vec2(0, 20),
                new Vec2(50, 100)};
        BodyDO chainDO = new BodyDO(new Vec2(500, 700), 0, BodyType.STATIC, chain, chain.length, false);
        chainDO.setStroke(new BasicStroke(3));
        chainDO.setColor(Color.DARK_GRAY);

        Vec2 poly [] = {
                new Vec2(-100, -100),
                new Vec2(-100, 50),
                new Vec2(-75, 70),
                new Vec2(50, 10),
                new Vec2(-30, 80) };
        BodyDO polyDO = new BodyDO(new Vec2(300, 700), 0, BodyType.STATIC, poly, poly.length, true);
        polyDO.setColor(Color.BLUE);


        for(int i = 0 ; i < 10; i ++){
            for(int j = i ; j < 10; j ++){
                BodyDO bodyDo = new BodyDO(new Vec2( 200 + 20 * i, 300 + 20 * j), (float)((Math.random() - 0.5) * 20 + Math.PI / 180.0f), BodyType.DYNAMIC, 10.0f , 10.0f);
                bodyDo.setColor(new Color( 25 * i , 25 * j, 100));
            }
        }
    }
}
