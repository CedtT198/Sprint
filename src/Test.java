import com.google.gson.Gson;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        System.out.println("Début du programme..."); // Pour vérifier que le programme démarre

        HashMap<String, Object> map = new HashMap<>();
        map.put("clé1", "valeur1");
        map.put("clé2", 42);
        map.put("clé3", true);

        // Convertir le HashMap en JSON avec Gson
        Gson gson = new Gson();
        String json = gson.toJson(map);

        // Afficher le JSON
        System.out.println("JSON généré : " + json);
    }
}
