package controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Chapter;
import models.User;
import notifiers.Notifier;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.*;

@With(InitControllers.class)
public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha().setBackground("#eeeeee");
        String code = captcha.getText().toLowerCase();
        Cache.set(id, code, "5mn");
        renderBinary(captcha);
    }

    public static void contact() {
        flash.clear();
        String randomID = Codec.UUID();
        render(randomID);
    }

    public static void contacUS(@Required @Email String mail, @Required String object, String subject, @Required String randomID,
            @Required String captcha) {
        if (!Secure.Security.isConnected()) {
            if (validation.hasError("captcha")) {
                flash.error("error");
                flash.put("errorCaptcha", "error.emptyCaptcha");
            } else {
                if (!captcha.equals(Cache.get(randomID))) {
                    flash.error("error");
                    flash.put("errorBadCaptcha", "error.badCaptcha");
                }
            }
        }

        if (validation.hasError("mail")) {
            flash.error("error");
            flash.put("errorEmail", "error.mail");
        }

        if (validation.hasError("object")) {
            flash.error("error");
            flash.put("errorObject", "error.emptyObject");
        }

        if (flash.get("error") == null) {

            try {
                Notifier.sendQuestion(mail,object,subject);
                flash.success("info.sendMessage");
            } catch (Exception ex) {
                Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        render("Application/contact.html", randomID);
    }
}
