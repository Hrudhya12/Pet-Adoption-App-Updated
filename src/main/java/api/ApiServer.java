package api;

import static spark.Spark.*;
import com.google.gson.Gson;
import dao.PetDAO;
import model.Pet;

public class ApiServer {

    private static Gson gson = new Gson();
    private static PetDAO petDAO = new PetDAO();

    public static void start() {

        port(8080);

        get("/pets", (req, res) -> {
            res.type("application/json");
            return gson.toJson(petDAO.getAllPets());
        });

        post("/adopt/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Pet p = petDAO.getPetById(id);

            if (p == null) {
                res.status(404);
                return "Pet not found";
            }

            p.adopt("Jerry");
            petDAO.updatePet(p);

            return "OK";
        });

        get("/adopted", (req, res) -> {
            res.type("application/json");
            return gson.toJson(petDAO.getAdoptedPets("Jerry"));
        });
    }
}
