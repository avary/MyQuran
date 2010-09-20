package notifiers;

import java.util.List;
import play.mvc.*;

import javax.mail.internet.*;

import models.*;
import play.i18n.Messages;

public class Notifier extends Mailer {

    public static boolean welcome(User user) throws Exception {
        setFrom(new InternetAddress("admin@al-imane.org", "Administrateur"));
        setReplyTo(new InternetAddress("admin@al-imane.org", ""));
        setSubject(Messages.get("mail.welcomeSubject", user.username));
        addRecipient(user.email, new InternetAddress("admin@al-imane.org", ""));
        return sendAndWait(user);
    }

    public static boolean lostPassword(User user) throws Exception {
        setFrom(new InternetAddress("admin@al-imane.org", "Administrateur"));
        setReplyTo(new InternetAddress("admin@al-imane.org", ""));
        setSubject(Messages.get("mail.lostPassword", user.username));
        addRecipient(user.email, new InternetAddress("admin@al-imane.org", ""));
        return sendAndWait(user);
    }

    public static boolean sendQuestion(String mail, String object, String subject) throws Exception {
        setFrom(new InternetAddress(mail, "Site Coran"));
        setReplyTo(new InternetAddress(mail, ""));
        setSubject(object);
        addRecipient(new InternetAddress("admin@al-imane.org", ""));
        return sendAndWait(subject);
    }

    public static boolean sendNewsLetter(User user,List<Proposal> translations,
            List<Proposal> comments, List<Proposal> tags,String tagURL) throws Exception {
        setFrom(new InternetAddress("admin@al-imane.org", "Site du Coran"));
        setReplyTo(new InternetAddress("admin@al-imane.org", ""));
        setSubject("Derniers ajouts");
        addRecipient(new InternetAddress(user.email, ""));
        return sendAndWait(user,translations, comments, tags,tagURL);
    }
}
