package edu.escuelaing.arep.services.impl;

import com.google.gson.JsonObject;
import edu.escuelaing.arep.services.ITransformer;

/**
 * @author Iván Camilo Rincón Saavedra
 * @version 1.0 4/6/2022
 * @project SecureApp
 */
public class Transformer implements ITransformer {

    @Override
    public JsonObject toJson(int code, String message, String serverMessage) {
        JsonObject json = new JsonObject();
        json.addProperty("status", code);
        json.addProperty("result", message);
        json.addProperty("serverResponse", serverMessage);

        return json;
    }
}
