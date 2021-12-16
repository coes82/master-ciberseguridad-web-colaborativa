package controllers;


import helpers.HashUtils;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;

public class Secure extends Controller {

    public static void login(){
        render();
    }

    public static void logout(){
        session.remove("password");
        login();
    }

    public static void authenticate(String username, String password){
        User u = User.loadUser(username);
        if (u != null && u.getPassword().equals(HashUtils.getMd5(password))){
            session.put("username", username);
            session.put("password", password);
            Application.index();
        }else{
            flash.put("error", Messages.get("Public.login.error.credentials"));
            login();
        }
    }

    public static void DirectoryTraversalError(String relativePath){
        File file = new File(relativePath);
        if (file.isAbsolute())
        {
            throw new RuntimeException("Absolute path not allowed");
        }
        String pathCanonical;
        String pathAbsolute;
        try
        {
            pathCanonical = file.getCanonicalPath();
            pathAbsolute = file.getAbsolutePath();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Directory traversal error", e);
        }
        if (! pathCanonical.equals(pathAbsolute))
        {
            throw new RuntimeException("Directory traversal error");
        }
    }
}
