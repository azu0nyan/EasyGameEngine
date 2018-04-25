package ru.ege.engine.smartDO;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.util.HashSet;

public class BodyDO extends SmartDrawableObject implements HasBody {


    private Body body;

    private Color color = Color.BLACK;
    private Stroke stroke = new BasicStroke(2);
    private boolean fill = true;

    public BodyDO(Body body) {
        super();
        this.body = body;
    }

    public BodyDO(Vec2 position, float angle, BodyType type, Vec2 start, Vec2 end) {
        super();
        EdgeShape shape = new EdgeShape();
        shape.set(start, end);
        constructDefsAndInit(position, angle, type, shape);
    }

    public BodyDO(Vec2 position, float angle, BodyType type, float radius) {
        super();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        constructDefsAndInit(position, angle, type, shape);
    }

    public BodyDO(Vec2 position, float angle, BodyType type, float sx, float sy) {
        super();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sx, sy);
        constructDefsAndInit(position, angle, type, shape);
    }

    public BodyDO(Vec2 position, float angle, BodyType type, Vec2[] vertices, int count, boolean polygon) {
        super();
        Shape res = null;
        if (polygon) {
            PolygonShape shape = new PolygonShape();
            shape.set(vertices, count);
            res = shape;
        } else {
            ChainShape shape = new ChainShape();
            shape.createChain(vertices, count);
            res = shape;
        }
        constructDefsAndInit(position, angle, type, res);
    }

    public BodyDO(BodyDef bodyDef, FixtureDef fixtureDef) {
        super();
        init(bodyDef, fixtureDef);
    }

    private void constructDefsAndInit(Vec2 position, float angle, BodyType type, Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position = position.clone();
        bodyDef.angle = angle;
        bodyDef.type = type;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0.3f;
        fixtureDef.shape = shape;

        init(bodyDef, fixtureDef);
    }

    private void init(BodyDef bodyDef, FixtureDef fixtureDef) {
        body = EGEngine.i().getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);
    }

    @Override
    protected void update(double dt) {

    }

    @Override
    public void draw(Graphics2D g) {
        if (body == null) {
            return;
        }
        Fixture f = body.getFixtureList();

        while (f != null) {
            drawFixture(g, f);
            f = f.getNext();
        }
    }

    private void drawFixture(Graphics2D g, Fixture f) {
        Shape toDraw = f.getShape();
        switch (toDraw.m_type) {
            case EDGE:
                drawEdge(g, (EdgeShape) toDraw);
                break;
            case CHAIN:
                drawChain(g, (ChainShape) toDraw);
                break;
            case CIRCLE:
                drawCircle(g, (CircleShape) toDraw);
                break;
            case POLYGON:
                drawPolygon(g, (PolygonShape) toDraw);
                break;
        }
    }

    private void drawPolygon(Graphics2D g, PolygonShape toDraw) {
        int  xPoints [] = new int[toDraw.getVertexCount()];
        int  yPoints [] = new int[toDraw.getVertexCount()];
        for(int i = 0; i < toDraw.getVertexCount(); i++){
            Vector2D vecI = localToScreen(toDraw.getVertex(i));
            xPoints[i] = vecI.getXInt();
            yPoints[i] = vecI.getYInt();
        }
        g.setColor(color);
        g.setStroke(stroke);
        if(fill){
            g.fillPolygon(xPoints, yPoints, toDraw.getVertexCount());
        } else {
            g.drawPolygon(xPoints, yPoints, toDraw.getVertexCount());
        }


    }

    private void drawCircle(Graphics2D g, CircleShape toDraw) {
        double displayRad  = worldToScreen(toDraw.getRadius());
        g.setStroke(stroke);
        g.setColor(color);
        Vector2D where = worldToScreen(body.getPosition());
        if(fill){
            g.fillOval((int) (where.getX() - displayRad ), (int)(where.getY() - displayRad), (int)displayRad * 2, (int)displayRad * 2);
        } else {
            g.drawOval((int) (where.getX() - displayRad ), (int)(where.getY() - displayRad), (int)displayRad * 2, (int)displayRad * 2);
        }
    }

    private void drawChain(Graphics2D g, ChainShape toDraw) {
        for (int i = 0; i < toDraw.getChildCount(); i++) {
            EdgeShape edge = new EdgeShape();
            toDraw.getChildEdge(edge, i);
            drawEdge(g, edge);
        }
    }

    private void drawEdge(Graphics2D g, EdgeShape toDraw) {
        Vector2D start = localToScreen(toDraw.m_vertex1);
        Vector2D end = localToScreen(toDraw.m_vertex2);
        g.setStroke(stroke);
        g.setColor(color);
        g.drawLine(start.getXInt(), start.getYInt(), end.getXInt(), end.getYInt());
    }
    //Преобразования кооординат по которым рисуем
    public Vector2D localToScreen(Vec2 local) {
        Vec2 world = body.getWorldPoint(local);
        return worldToScreen(new Vector2D(world.x, world.y));
    }

    private Vector2D worldToScreen(Vector2D vector2D) {
        return Camera.worldToScreen(vector2D);
    }

    private double worldToScreen(float radius) {
        return Camera.worldToScreen(radius);
    }

    private Vector2D worldToScreen(Vec2 position) {
        return worldToScreen(new Vector2D(position.x, position.y));
    }
        /////////
    @Override
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }


}
