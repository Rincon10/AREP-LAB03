package edu.escuelaing.arep.server;

/**
 * @author Iván Camilo Rincón Saavedra
 * @version 1.0 4/6/2022
 * @project SecureApp
 */

import edu.escuelaing.arep.utils.Password;

import static spark.Spark.*;

public class HelloServer {
    public static void main(String args[]) {
        port(getPort());

        secure(getKeyStore(), Password.keyStorePassword, null, null);

        get("/hello", (req, res) -> "Hello From Hello Server");
    }


    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5050; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    static String getKeyStore() {
        if (System.getenv("KEYSTORE") != null) {
            return System.getenv("KEYSTORE");
        }
        return "keystores/ecikeystore.p12"; //returns default keystore if keystore isn't set (i.e. on localhost)
    }
}
