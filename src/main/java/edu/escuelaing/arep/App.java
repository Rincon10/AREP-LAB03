package edu.escuelaing.arep;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {
    private static String helloPath = "/hello";

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
        //staticFileLocation("/public");

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
        // This only if we wanna have the front in resources
//        get(defaultPath, (req, res) -> {
//            res.redirect("/index.html");
//            return "";
//        });
        get(helloPath, (req, res) -> "Hello, world from spark.");

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
