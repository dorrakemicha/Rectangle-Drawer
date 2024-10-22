package forms;

import forms.App.RectangleBounds;

/**
 * Représente une paire de rectangles.
 */
public class RectanglePair {
    private RectangleBounds rect1;
    private RectangleBounds rect2;
    private int dx; // Décalage dans la direction x
    private int dy; // Décalage dans la direction y

    /**
     * Initialise une paire de rectangles avec deux rectangles donnés.
     *
     * @param rect1 Le premier rectangle.
     * @param rect2 Le deuxième rectangle.
     */
    public RectanglePair(RectangleBounds rect1, RectangleBounds rect2) {
        this.rect1 = rect1;
        this.rect2 = rect2;
        this.dx = 0; // Initialise les décalages à zéro
        this.dy = 0;
    }

    /**
     * Obtient le premier rectangle de la paire.
     *
     * @return Le premier rectangle.
     */
    public RectangleBounds getRect1() {
        return rect1;
    }

    /**
     * Obtient le deuxième rectangle de la paire.
     *
     * @return Le deuxième rectangle.
     */
    public RectangleBounds getRect2() {
        return rect2;
    }

    /**
     * Définit le décalage dans les directions x et y.
     *
     * @param dx Le décalage dans la direction x.
     * @param dy Le décalage dans la direction y.
     */
    public void setOffset(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Déplace les deux rectangles de la paire selon le décalage spécifié.
     *
     * @param dx Le décalage dans la direction x.
     * @param dy Le décalage dans la direction y.
     */
    public void move(int dx, int dy) {
        rect1.move(dx, dy);
        rect2.move(dx + this.dx, dy + this.dy); // Utilise les variables d'instance dx et dy ici
    }

    /**
     * Calcule l'union des deux rectangles et retourne une nouvelle paire de rectangles.
     *
     * @param rect1 Le premier rectangle.
     * @param rect2 Le deuxième rectangle.
     * @return Une paire de rectangles représentant l'union des deux rectangles donnés.
     */
    public static RectanglePair calculateUnion(RectangleBounds rect1, RectangleBounds rect2) {
        // Calcule les coordonnées du rectangle d'union
        int minX = Math.min(rect1.getStartPoint().x, rect2.getStartPoint().x);
        int minY = Math.min(rect1.getStartPoint().y, rect2.getStartPoint().y);
        int maxX = Math.max(rect1.getEndPoint().x, rect2.getEndPoint().x);
        int maxY = Math.max(rect1.getEndPoint().y, rect2.getEndPoint().y);

        // Crée un nouveau rectangle représentant l'union
        RectangleBounds union = new RectangleBounds(new java.awt.Point(minX, minY));
        union.setEndPoint(new java.awt.Point(maxX, maxY));
        union.setColor(rect1.getColor()); // Choisissez une couleur à partir de l'un des rectangles

        // Calcule le décalage entre les deux rectangles
        int offsetX = rect1.getStartPoint().x - rect2.getStartPoint().x;
        int offsetY = rect1.getStartPoint().y - rect2.getStartPoint().y;

        // Crée une nouvelle paire de rectangles avec les décalages calculés
        RectanglePair pair = new RectanglePair(rect1, rect2);
        pair.setOffset(offsetX, offsetY);

        return pair;
    }
}



/*public class Difference {
    public static Area Difference(RectangleBounds rect1, RectangleBounds rect2) {
        Rectangle2D.Double r1 = new Rectangle2D.Double(
                Math.min(rect1.getStartPoint().x, rect1.getEndPoint().x),
                Math.min(rect1.getStartPoint().y, rect1.getEndPoint().y),
                rect1.getWidth(), rect1.getHeight());

        Rectangle2D.Double r2 = new Rectangle2D.Double(
                Math.min(rect2.getStartPoint().x, rect2.getEndPoint().x),
                Math.min(rect2.getStartPoint().y, rect2.getEndPoint().y),
                rect2.getWidth(), rect2.getHeight());

        Area area1 = new Area(r1);
        Area area2 = new Area(r2);

        // Calculer l'intersection
        Area intersection = new Area(area1);
        intersection.intersect(area2);

        // Soustraire l'intersection de la première zone
        area1.subtract(intersection);

        return area1;
    }
}
*/
