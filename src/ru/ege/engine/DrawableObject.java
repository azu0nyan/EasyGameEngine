package ru.ege.engine;

import java.awt.*;

/**
 * Интерфейс для объектов которые нужно отображать в окне
 */
public interface DrawableObject {
    /**
     * Вызывается каждый кадр, для каждого объекта, здесь надо обновлять состояние объекта и рисовать его.
     * @param g экземпляр класса Graphics2D, с помощью него надо рисовать объект
     * @param dt время в секундах с прошедшего кадра
     */
    void drawAndUpdate(Graphics2D g, double dt);

}
