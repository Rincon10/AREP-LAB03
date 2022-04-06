package edu.escuelaing.arep.services;

import com.google.gson.JsonObject;

/**
 * @author Iván Camilo Rincón Saavedra
 * @version 1.0 4/6/2022
 * @project SecureApp
 */
public interface ITransformer {
    JsonObject toJson(int code,  String message ,String serverMessage );
}
