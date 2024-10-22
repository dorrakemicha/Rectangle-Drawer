package forms;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import forms.App.RectangleBounds;

/**
 * Cette classe calcule l'intersection entre deux rectangles.
 */
public class IntersectionCalculator {
    /**
     * Calcule et retourne l'aire d'intersection entre deux rectangles.
     *
     * @param rect1 Le premier rectangle.
     * @param rect2 Le deuxième rectangle.
     * @return L'aire d'intersection entre les deux rectangles.
     */
    public static Area calculateIntersection(RectangleBounds rect1, RectangleBounds rect2) {
        // Création des rectangles à partir des coordonnées des points de départ et d'arrivée de chaque rectangle
        Rectangle2D.Double r1 = new Rectangle2D.Double(
                Math.min(rect1.getStartPoint().x, rect1.getEndPoint().x),
                Math.min(rect1.getStartPoint().y, rect1.getEndPoint().y),
                rect1.getWidth(), rect1.getHeight());

        Rectangle2D.Double r2 = new Rectangle2D.Double(
                Math.min(rect2.getStartPoint().x, rect2.getEndPoint().x),
                Math.min(rect2.getStartPoint().y, rect2.getEndPoint().y),
                rect2.getWidth(), rect2.getHeight());

        // Création des zones de chaque rectangle
        Area area1 = new Area(r1);
        Area area2 = new Area(r2);

        // Intersection des zones
        area1.intersect(area2);

        // Retourne l'aire d'intersection
        return area1;
    }
}
