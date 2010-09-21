/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author inf04
 */
@With(InitControllers.class)
public class Users extends Controller {

    public static void showAccount() {
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        render(user);
    }

    public static void editAccount(@Required @Email String mail, @Required String password,
            String newPassword, String confirmPassword,boolean newsLetter,boolean newPost) {

        flash.clear();
        
        User user = User.find("byUsername", Secure.Security.connected()).first();

        if (validation.hasError("mail")) {
            flash.error("error");
            flash.now("errorEmail", "error.mail");
        }

        if (validation.hasError("password")) {
            flash.error("error");
            flash.now("errorPassword", "error.emptyPassword");
        } else {
            byte[] uniqueKey = password.getBytes();
            byte[] hash = null;
            try {
                hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuilder hashString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashString.append('0');
                    hashString.append(hex.charAt(hex.length() - 1));
                } else {
                    hashString.append(hex.substring(hex.length() - 2));
                }
            }

            if (!user.password.equals(hashString.toString())) {
                flash.error("error");
                flash.now("errorPassword", "error.badPassword");
            }

        }

        if (newPassword != null && !newPassword.isEmpty()) {
            if (confirmPassword == null || confirmPassword.isEmpty()) {
                flash.error("error");
                flash.now("errorPassword", "error.badConfirmPassword");
            } else {
                if (!newPassword.equals(confirmPassword)) {
                    flash.error("error");
                    flash.now("errorPassword", "error.badConfirmPassword");
                } else {
                    password = newPassword;
                }
            }
        }

        if (flash.get("error") == null) {
            user.email = mail;

            byte[] uniqueKey = password.getBytes();
            byte[] hash = null;
            try {
                hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
            StringBuilder hashString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashString.append('0');
                    hashString.append(hex.charAt(hex.length() - 1));
                } else {
                    hashString.append(hex.substring(hex.length() - 2));
                }
            }

            System.out.println(newPost);
            user.password = hashString.toString();
            user.newPost = newPost;
            user.newsletter = newsLetter;
            user.save();
            flash.success("info.userModified");
        }

        render("Users/showAccount.html", user);
    }
}
