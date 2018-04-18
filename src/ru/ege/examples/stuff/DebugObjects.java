package ru.ege.examples.stuff;

import ru.ege.engine.EGEngine;
import ru.ege.engine.DrawableObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

/**
 * Вывод всякой полезной для отладки информации
 */
public class DebugObjects {

    public static void addDebugObjects(EGEngine EGEngine) {
        CurrentTime currentTime = new CurrentTime();
        currentTime.rowNumber = 1;
        EGEngine.addDrawableObject(currentTime);
        FpsCounter fpsCounter = new FpsCounter();
        fpsCounter.rowNumber = 2;
        EGEngine.addDrawableObject(fpsCounter);
        ObjectCounter objectCounter = new ObjectCounter();
        objectCounter.rowNumber = 3;
        EGEngine.addDrawableObject(objectCounter);
        ThreadCounter threadCounter = new ThreadCounter();
        threadCounter.rowNumber = 4;
        EGEngine.addDrawableObject(threadCounter);

    }

}

class CurrentTime implements DrawableObject {
    int fontSize = 15;
    int rowHeight = 20;
    int rowNumber = 1;
    int dx = 30;
    int dy = 30;

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setColor(Color.RED);
        g.setFont(new Font(null, 0, fontSize));
        Calendar c = Calendar.getInstance();
        String time = "Время %d:%d:%d.%d";
        g.drawString(String.format(time, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND)), dx, dy + rowNumber * rowHeight);
    }
}

class FpsCounter implements DrawableObject {
    int fontSize = 15;
    int rowHeight = 20;
    int rowNumber = 1;
    int dx = 30;
    int dy = 30;

    int framesCount = 0;
    java.util.List<Long> lastFrames = new ArrayList<>();

    public void drawAndUpdate(Graphics2D g, double dt) {
        framesCount++;
        long currentTime = System.currentTimeMillis();
        lastFrames.add(currentTime);
        lastFrames = lastFrames.stream().filter(e -> currentTime - e < 1000).collect(Collectors.toList());
        g.setColor(Color.RED);
        g.setFont(new Font(null, 0, fontSize));
        g.drawString("Со старта прошло:" + EGEngine.instance().getTimeFromStartMillis() + " мс. всего кадров:" + framesCount + " fps:" + lastFrames.size(), dx, dy + rowNumber * rowHeight);
    }
}

class ObjectCounter implements DrawableObject {
    int fontSize = 15;
    int rowHeight = 20;
    int rowNumber = 1;
    int dx = 30;
    int dy = 30;

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setColor(Color.RED);
        g.setFont(new Font(null, 0, fontSize));
        g.drawString("Всего drawableObjects:" + EGEngine.instance().getDrawableObjects(DrawableObject.class).size(), dx, dy + rowNumber * rowHeight);
    }
}

class ThreadCounter implements DrawableObject {
    int fontSize = 15;
    int rowHeight = 20;
    int rowNumber = 1;
    int dx = 30;
    int dy = 30;

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setColor(Color.RED);
        g.setFont(new Font(null, 0, fontSize));
        int threadCount = Thread.activeCount();
        g.drawString("Всего потоков запущенно:" + threadCount, dx, dy + rowNumber * rowHeight);
    }
}