package forms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Cette classe charge un dessin à partir d'un fichier.
 */
public class Loader {
    /**
     * Charge le dessin à partir d'un fichier.
     *
     * @param filename Le nom du fichier à charger.
     * @return Une liste de formes représentant le dessin chargé.
     * @throws IOException            Si une erreur d'entrée/sortie survient lors de la lecture du fichier.
     * @throws ClassNotFoundException Si la classe des objets lus du fichier n'est pas trouvée.
     */
    @SuppressWarnings("unchecked")
    public static List<App.RectangleBounds> loadDrawing(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<App.RectangleBounds>) ois.readObject();
        }
    }
}

