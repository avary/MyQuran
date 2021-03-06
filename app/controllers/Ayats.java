/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Ayat;
import models.Sourat;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import play.mvc.With;
import play.templates.JavaExtensions;
import play.templates.Template;
import play.templates.TemplateLoader;
import search.SearchModule;

/**
 *
 * @author inf04
 */
@With(InitControllers.class)
public class Ayats extends Controller {

    public static void list(String title, int souratNumber) {
        long t1 = System.currentTimeMillis();
        Sourat sourat = Sourat.find("byNumber", souratNumber).first();
        renderArgs.put("title", "coran.al-imane.org - Sourate " + sourat.number + " - " + sourat.title);

        String content = null;
        if (Cache.get("sourat_" + souratNumber) == null) {
            List<Ayat> ayats = Ayat.find("bySourat", sourat).fetch();
            int maxAyat = ayats.size();

            Map args = new HashMap();
            args.put("ayats", ayats);
            args.put("sourat", sourat);
            args.put("maxAyat", maxAyat);
            args.put("session", Session.current());
            Template t = TemplateLoader.load("Sourats/sourat.html");
            content = t.render(args);
            Cache.add("sourat_" + sourat.number, content, "24h");
        } else {
            content = (String) Cache.get("sourat_" + souratNumber);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("time : "+(t2-t1));

        render(content,sourat);
    }

    

    public static void search(String q, int page) {

        List<Long> result = SearchModule.search("content:" + q, Ayat.class).all().fetchIds();
        //Collections.sort(result);

        List<Ayat> ayats = new ArrayList<Ayat>();

        if (page <= 0) {
            page = 1;
        }

        int pageSize = 200;
        int nbAyat = result.size();

        int start = (page - 1) * pageSize;
        int end = start + pageSize;
        int nbPage = (int) Math.ceil(result.size() / 200.0);

        if (start >= result.size()) {
            render(ayats, q);
        }

        if (end >= result.size()) {
            end = result.size();
        }

        List<Long> searchPage = result.subList(start, end);
        for (Long id : searchPage) {
            System.out.println("ayat id : " + id);
            ayats.add((Ayat) Ayat.findById(id));
        }

        for (Ayat ayat : ayats) {
            System.out.println("#ayat id : " + ayat.id);
        }

        Collections.sort(ayats);

        render(ayats, q, page, nbPage, nbAyat);
    }

    @Check("admin")
    public static void edit(String content, Long translationID, Long ayatID) throws Throwable {
        Secure.checkAccess();
        flash.clear();
        /*
        User user = User.find("byUsername", Secure.Security.connected()).first();

        Translation t = Translation.findById(translationID);
        t.accepted = true;
        t.newMessage = true;
        t.user.newMessage = true;
        t.user.translation--;
        t.user.save();
        Cache.set("user_" + t.user.username, t.user, "1h");

        t.save();

        Message message = new Message();
        message.message = Messages.get("info.acceptedTranslation");
        message.newMessage = true;
        message.translation = t;
        message.user = user;
        message.messageTime = new Date();
        message.save();

        Ayat ayat = Ayat.findById(ayatID);
        ayat.content = content;
        ayat.save();

        Cache.delete("sourat_ayat_"+ayat.sourat.number);

        user.translation--;
        user.save();
        Cache.set("user_" + user.username, user, "1h");
         */
        flash.success("info.editAyatSuccess");

        //render("Translations/view.html", t);
    }

    @Check("admin")
    public static void editAyat(Long ayatID) throws Throwable {
        Secure.checkAccess();
        flash.clear();
        Ayat ayat = Ayat.findById(ayatID);

        render(ayat);
    }

    @Check("admin")
    public static void saveAyat(Long ayatID, String content) throws Throwable {
        Secure.checkAccess();
        flash.clear();
        Ayat ayat = Ayat.findById(ayatID);

        ayat.content = content;
        ayat.save();

        Cache.delete("sourat_ayat_" + ayat.sourat.number);

        flash.success("info.editAyatSuccess");

        render("Ayats/editAyat.html", ayat);
    }
}
