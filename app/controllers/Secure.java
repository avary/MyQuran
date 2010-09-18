package controllers;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import notifiers.Notifier;
import play.Play;
import play.cache.Cache;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.utils.*;
import utils.LoginLimitManager;

public class Secure extends Controller {

    @Before(unless = {"login", "authenticate", "logout", "registration", "register",
        "confirmUser", "checkUsername", "lostPassword", "newPassword", "changePassword",
        "recoverPassword"})
    public static void checkAccess() throws Throwable {
        // Authent
        if (!session.contains("username")) {
            flash.put("url", request.method == "GET" ? request.url : "/"); // seems a good default
            flash.error("error.userNotConnected");
            login("");
        }
        // Checks
        Check check = getActionAnnotation(Check.class);
        if (check != null) {
            check(check);
        }
        check = getControllerInheritedAnnotation(Check.class);
        if (check != null) {
            check(check);
        }
    }

    private static void check(Check check) throws Throwable {
        boolean hasProfile = false;
        for (String profile : check.value()) {
            hasProfile |= (Boolean) Security.invoke("check", profile);
            if (!hasProfile) {
                Security.invoke("onCheckFailed", profile);
            }
        }
    }

    // ~~~ Login
    public static void login(String login) throws Throwable {
        Http.Cookie remember = request.cookies.get("rememberme");
        if (remember != null && remember.value.indexOf("-") > 0) {
            String sign = remember.value.substring(0, remember.value.indexOf("-"));
            String username = remember.value.substring(remember.value.indexOf("-") + 1);
            if (Crypto.sign(username).equals(sign)) {
                session.put("username", username);
                redirectToOriginalURL();
            }
        }
        flash.keep("url");

        if (login == null || login.isEmpty()) {
            render();
        }

        if (LoginLimitManager.getInstance().hasHitConnectionLimit(login, request.remoteAddress)) {
            render("Secure/connectionLimit.html");
        }

        render();
    }

    public static void registration() {
        String randomID = Codec.UUID();
        render(randomID);
    }

    public static void checkUsername(String username) {
        User user = User.find("byUsername", username).first();
        if (user == null) {
            renderJSON("{\"result\":\"ok\"}");
        } else {
            renderJSON("{\"result\":\"ko\"}");
        }
    }

    public static void lostPassword() {
        String randomID = Codec.UUID();
        render(randomID);
    }

    public static void recoverPassword(@Required @Email String mail, @Required String randomID,
            @Required String captcha) {

        if (validation.hasError("captcha")) {
            flash.error("error");
            flash.put("errorCaptcha", "error.emptyCaptcha");
        } else {
            if (!captcha.equals(Cache.get(randomID))) {
                flash.error("error");
                flash.put("errorBadCaptcha", "error.badCaptcha");
            }
        }

        User user = null;
        if (validation.hasError("mail")) {
            flash.error("error");
            flash.put("errorEmail", "error.mail");
        } else {
            user = User.find("byEmail", mail).first();
            if (user == null) {
                flash.error("error");
                flash.put("errorEmail", "error.badMail");
            }
        }

        if (flash.get("error") == null) {

            try {
                Notifier.lostPassword(user);
                flash.success("info.recoverPassword");
            } catch (Exception ex) {
                Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        render("Secure/lostPassword.html", randomID);

    }

    public static void newPassword(String confirm) {
        User user = User.find("byConfirm", confirm).first();

        if (user == null) {
            flash.error("error");
            flash.put("errorConfirm", "secure.badConfirmPassword");
        }

        render(user);
    }

    public static void changePassword(String confirm, @Required String password, @Required String confirmPassword) {
        if (validation.hasError("password")) {
            flash.error("error");
            flash.put("errorPassword", "error.emptyPassword");
        } else {
            if (confirmPassword != null && !password.equals(confirmPassword)) {
                flash.error("error");
                flash.put("errorPassword", "error.badConfirmPassword");
            }
        }

        User user = User.find("byConfirm", confirm).first();

        if (user == null) {
            newPassword(confirm);
        }

        if (flash.get("error") == null) {
            try {
                confirm = "" + System.currentTimeMillis() + "" + user.username.hashCode();

                byte[] uniqueKey = password.getBytes();
                byte[] hash = null;
                hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
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

                user.password = hashString.toString();
                user.confirm = confirm;
                user.save();
                flash.success("info.changePassword");
                render("Secure/login.html");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        render("Secure/newPassword.html", user);
    }

    public static void register(@Required String username, @Required @Email String mail, @Required String password, @Required String confirmPassword, @Required String randomID,
            @Required String captcha) {
        if (validation.hasError("captcha")) {
            flash.error("error");
            flash.put("errorCaptcha", "error.emptyCaptcha");
        } else {
            if (!captcha.toLowerCase().equals(Cache.get(randomID))) {
                flash.error("error");
                flash.put("errorBadCaptcha", "error.badCaptcha");
            }
        }

        if (validation.hasError("username")) {
            flash.error("error");
            flash.put("errorUsername", "error.emptyUsername");
        } else {
            if (username.length() < 3) {
                flash.error("error");
                flash.put("errorUsername", "error.shortUsername");
            } else {
                User user = User.find("byUsername", username).first();
                if (user != null) {
                    flash.error("error");
                    flash.put("errorUsername", "error.badUsername");
                }
            }
        }

        if (validation.hasError("mail")) {
            flash.error("error");
            flash.put("errorEmail", "error.mail");
        } else {
            User user = User.find("byEmail", mail).first();
            if (user != null) {
                flash.error("error");
                flash.put("errorEmail", "error.mailAlreadyUsed");
            }
        }

        if (validation.hasError("password")) {
            flash.error("error");
            flash.put("errorPassword", "error.emptyPassword");
        } else {
            if (confirmPassword != null && !password.equals(confirmPassword)) {
                flash.error("error");
                flash.put("errorPassword", "error.badConfirmPassword");
            }
        }

        if (flash.get("error") == null) {

            byte[] uniqueKey = password.getBytes();
            byte[] hash = null;
            try {
                hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
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

            String confirm = "" + System.currentTimeMillis() + "" + username.hashCode();
            User user = new User(username, mail, hashString.toString(), confirm);
            user.isAdmin = false;
            user.notification = false;
            user.proposal = 0;
            user.save();
            try {
                Notifier.welcome(user);
                flash.success("info.userAdded");
            } catch (Exception ex) {
                Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        render("Secure/registration.html", randomID);
    }

    public static void confirmUser(String confirm) {
        User user = User.find("byConfirm", confirm).first();
        if (user == null) {
            flash.error("secure.badConfirm");
        } else {
            user.enabled = true;
            user.confirm = "" + System.currentTimeMillis() + "" + user.username.hashCode();
            user.save();
        }

        render(user);
    }

    public static void authenticate(@Required String username, String password, boolean remember) throws Throwable {
        // Check tokens
        if (validation.hasErrors() || !(Boolean) Security.invoke("authentify", username, password)) {
            flash.keep("url");
            flash.error("secure.error");
            params.flash();
            login(username);
        }
        User user = User.find("byUsername", username).first();
        if (!user.enabled) {
            flash.keep("url");
            flash.error("secure.userDisabled");
            params.flash();
            login(username);
        }
        // Mark user as connected
        session.put("username", username);
        if(user.lastVisit == null){
            session.put("lastVisit", -1);
        }else{
            session.put("lastVisit", user.lastVisit.getTime());
        }

        user.lastVisit = new Date();
        user.save();

        // Remember if needed
        if (remember) {
            response.setCookie("rememberme", Crypto.sign(username) + "-" + username, "30d");
        }
        // Redirect to the original URL (or /)
        redirectToOriginalURL();
    }

    public static void logout() throws Throwable {
        session.clear();
        response.setCookie("rememberme", "", 0);
        Security.invoke("onDisconnected");
        flash.success("secure.logout");
        login("");
    }

    // ~~~ Utils
    static void redirectToOriginalURL() throws Throwable {
        Security.invoke("onAuthenticated");
        String url = flash.get("url");
        if (url == null) {
            url = "/";
        }
        redirect(url);
    }

    public static class Security extends Controller {

        static boolean authentify(String username, String password) {
            if (LoginLimitManager.getInstance().hasHitConnectionLimit(username, request.remoteAddress)) {
                return false;
            }

            User user = User.connect(username, password);
            if (user == null) {
                LoginLimitManager.getInstance().recordFailedAttempt(username, request.remoteAddress);
                return false;
            }

            LoginLimitManager.getInstance().recordSuccessfulAttempt(username, request.remoteAddress);
            return true;
        }

        static boolean check(String profile) {
            User user = User.find("byUsername", connected()).<User>first();

            if (profile.equals("admin")) {
                return user.isAdmin;
            } else {
                return !user.isAdmin;
            }

        }

        static String connected() {
            return session.get("username");
        }

        static boolean isConnected() {
            return session.contains("username");
        }

        static void onAuthenticated() {
            Application.index();
        }

        static void onDisconnected() {
        }

        static void onCheckFailed(String profile) {
            forbidden();
        }

        private static Object invoke(String m, Object... args) throws Throwable {
            Class security = null;
            List<Class> classes = Play.classloader.getAssignableClasses(Security.class);
            if (classes.size() == 0) {
                security = Security.class;
            } else {
                security = classes.get(0);
            }
            try {
                return Java.invokeStaticOrParent(security, m, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
