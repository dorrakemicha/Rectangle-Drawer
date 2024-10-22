package forms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Classe utilitaire pour sauvegarder une liste de rectangles.
 */
public class Saver {
    /**
     * Sauvegarde la liste de rectangles dans un fichier spécifié.
     *
     * @param rectangles La liste de rectangles à sauvegarder.
     * @param filename   Le nom du fichier où sauvegarder les rectangles.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la sauvegarde.
     */
    public static void saveDrawing(List<App.RectangleBounds> rectangles, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(rectangles);
        }
    }
}

