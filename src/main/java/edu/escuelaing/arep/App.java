package edu.escuelaing.arep;

import com.google.gson.Gson;
import edu.escuelaing.arep.dto.UserDto;
import edu.escuelaing.arep.services.IHasher;
import edu.escuelaing.arep.services.ITransformer;
import edu.escuelaing.arep.services.impl.Hasher;
import edu.escuelaing.arep.services.impl.Transformer;
import edu.escuelaing.arep.utils.Password;
import edu.escuelaing.arep.utils.SecureURLReader;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {
    private static String helloPath = "/hello";
    private static String defaultPath = "/";
    private static IHasher hasher = new Hasher();
    private static ITransformer transformer = new Transformer();
    private static ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();

    private static void setControllers() {
        get(defaultPath, (req, res) -> {
            res.redirect("/index.html");
            res.status(200);
            return null;
        });

        get(helloPath, (req, res) -> {
            res.status(200);
            return "Hello, world from spark.";
        });

        post("/login", (req, res) -> {
            res.type("application/json");

            if (req.body() != null) {
                ArrayList<String> responses = login(req);
                return transformer.toJson(200,responses.get(0),responses.get(1));
            }
            return transformer.toJson(400,"Bad Request","Not logged");
        });

        get("/security/helloService", (req, res) -> onHelloService(res));
    }

    private static String onHelloService(Response res){
        try {
            return "<h1>" + SecureURLReader.readURL("https://localhost:5050/hello") + "</h1>";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        res.status(500);
        return "<h1>Internal server error</h1>";
    }

    private static ArrayList<String> login(Request req){
        UserDto user = (new Gson()).fromJson(req.body(), UserDto.class);

        // Validate that the username and password are in the request and are valid
        String username = user.getEmail();
        String password = user.getPassword();

        boolean isUsernamePresent = users.containsKey(username);
        ArrayList<String> responses = new ArrayList<String>();

        if(isUsernamePresent){
            boolean isPasswordCorrect = users.get(username).equals(hasher.hash(password));
            if(isPasswordCorrect){
                req.session(true);
                req.session().attribute("isLoggedIn", true);
                responses.add("Login successful!");;
                responses.add(getHelloServiceResponse(req));
                return responses;
            }

            responses.add("Wrong password");
            responses.add(getHelloServiceResponse(req));
            return responses;
        }

        responses.add("User doesn't exist");
        responses.add(getHelloServiceResponse(req));
        return responses;
    }

    private static String getHelloServiceResponse(Request req){
        req.session();
        try{
            if(req.session().attribute("isLoggedIn")){;
                return SecureURLReader.readURL("https://localhost:5050/hello");
            }
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Not");
            return "Not Logged in!";
        }
        return null;

    }

    private static void generateUsers() {
        users.put("Rincon10", hasher.hash("123456"));
        users.put("test", hasher.hash("test"));
    }

    /**
     * Verifies if the user is logged in
     */
    private static boolean isUserLoggedIn(Request req) {
        // $
        System.out.println("is User Logged in");

        req.session(true);

        if (req.session().isNew()) {
            req.session().attribute("isLoggedIn", false);
        }

        if (req.session().attribute("isLoggedIn")){
            return true;
        }

        halt(401, "<h1>No está autorizado para estar aquí</h1>");
        return false;
    }

    /**
     * Main method, that start our Spark application
     *
     * @param args
     */
    public static void main(String[] args) {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath,truststorePassword);
        secure(getKeyStore(), Password.keyStorePassword, null, null);

        //Setting the portNumber
        port(getPort());

        // Connect securely to the server
        SecureURLReader.connectSecurely();

        //Fake DataBase
        generateUsers();

        // Set the file location
        staticFileLocation("/public");

        //After-filters are evaluated after each request, and can read the request and read/modify the response:
        // Allow CORS
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


        before("/security/*", (req, res) -> isUserLoggedIn(req));

        // This only if we wanna have the front in resources
//        get(defaultPath, (req, res) -> {
//            res.redirect("/index.html");
//            return "";
//        });

        setControllers();

    }


    /***
     * Method that returns the port number to use in our App
     * @return int, port number
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; //returns default port if heroku-port isn't se  (i.e. on localhost)
    }

    static String getKeyStore() {
        if (System.getenv("KEYSTORE") != null) {
            return System.getenv("KEYSTORE");
        }
        return "keystores/ecikeystore.p12"; //returns default keystore if heroku-keystore isn't se  (i.e. on localhost)
    }

    static String getTrustStore() {
        if (System.getenv("TRUSTSTORE") != null) {
            return System.getenv("TRUSTSTORE");
        }
        return "keystores/myTrustStore"; //returns default keystore if heroku-keystore isn't se  (i.e. on localhost)
    }
}
