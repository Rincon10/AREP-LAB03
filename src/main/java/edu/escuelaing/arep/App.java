package edu.escuelaing.arep;

import edu.escuelaing.arep.services.IHasher;
import edu.escuelaing.arep.services.impl.Hasher;

import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {
    private static String helloPath = "/hello";
    private static String defaultPath = "/";
    private static IHasher hasher = new Hasher();
    private static ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();

    private static void setGetControllers() {
        get(defaultPath, (req, res) -> {
            res.redirect("/index.html");
            res.status(200);
            return null;
        });

        get(helloPath, (req, res) -> {
            res.status(200);
            return "Hello, world from spark.";
        });
    }

    private static void generateUsers() {
        users.put("Rincon10", hasher.hash("123456"));
        users.put("test", hasher.hash("test"));
    }

    /**
     * Main method, that start our Spark application
     *
     * @param args
     */
    public static void main(String[] args) {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath,truststorePassword);
        secure("", "", "", "");

        //Setting the portNumber
        port(getPort());

        //Fake DataBase
        generateUsers();

        // Set the file location
        staticFileLocation("/public");

        secure(getKeyStore(), "password", null, null);

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


        //before("/security/*", (req, res) -> isUserLoggedIn(req));

        // This only if we wanna have the front in resources
//        get(defaultPath, (req, res) -> {
//            res.redirect("/index.html");
//            return "";
//        });

        setGetControllers();

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
