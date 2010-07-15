/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Ayat;
import models.Chapter;
import models.User;
import play.cache.Cache;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author ali
 */
@With(value={Secure.class,InitControllers.class})
public class Chapters extends Controller {

    public static void selectAyat(Long ayatId) throws Throwable {
        if (Secure.Security.connected() == null) {
            Secure.login("");
        }

        Ayat ayat = Ayat.findById(ayatId);
        User user = User.find("byUsername", Secure.Security.connected()).first();

        Chapter chapter = Chapter.find("byTitleAndUser", "", user).first();

        if (chapter == null) {
            chapter = new Chapter();
            chapter.user = user;
        }

        if (chapter.ayats.contains(ayat)) {
            renderJSON("{\"result\":\"ko\"}");
        } else {
            chapter.ayats.add(ayat);
            chapter.save();
            renderJSON("{\"result\":\"ok\"}");
        }
    }

    public static void addAyatToChapter(long ayatID, long chapterID) {

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Chapter chapter = Chapter.findById(chapterID);

        if (chapter != null && chapter.user.equals(user)) {
            Ayat ayat = Ayat.findById(ayatID);
            if (chapter.ayats.contains(ayat)) {
                renderJSON("{\"result\":\"ko\"}");
            } else {
                Chapter defaultChapter = Chapter.find("byTitleAndUser", "", user).first();
                defaultChapter.ayats.remove(ayat);
                defaultChapter.save();

                chapter.ayats.add(ayat);
                chapter.save();
            }
        } else {
            renderJSON("{\"result\":\"error\"}");
        }

        renderJSON("{\"result\":\"ok\"}");
    }

    public static void index() {
        flash.clear();
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        long selectedAyat = Chapter.count("byTitleAndUser", "", user);

        List<Chapter> chapters = Chapter.find("user = ? and title != '' order by title", user).fetch();

        render(selectedAyat, chapters);
    }

    public static void newChapter(int type) {
        render(type);
    }

    public static void add(@Required String title) {
        flash.clear();
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        if (validation.hasError("title")) {
            flash.error("error");
            flash.put("emptyChapter", "error.emptyChapter");
        } else {
            Chapter chapter = Chapter.find("byUserAndTitle", user, title).first();
            if (chapter != null) {
                flash.error("error");
                flash.put("duplicateChapter", "error.duplicateChapter");
            }
        }

        if (flash.get("error") == null) {

            Chapter chapter = new Chapter();
            chapter.user = user;
            chapter.title = title;
            chapter.type = 2;
            chapter.save();

            flash.put("chapterAdded","info.chapterAdded");
        }

        flash.keep();

        newChapter(2);
    }

    public static void delete(Long id) {
        flash.clear();
        Chapter chapter = Chapter.findById(id);

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        if (chapter == null || !chapter.user.equals(user)) {
            flash.error("error");
            flash.put("errorDelete", "error.deleteChapter");
        } else {
            chapter.delete();
            flash.success("info.chapterDeleted");

        }

        long selectedAyat = Chapter.count("byTitleAndUser", "", user);

        List<Chapter> chapters = Chapter.find("user = ? and title != ''", user).fetch();

        render("Chapters/index.html", selectedAyat, chapters);
    }

    public static void edit(Long id, String title) {
        flash.clear();
        render(id, title);
    }

    public static void save(long id, @Required String title) {
        flash.clear();

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        if (validation.hasError("title")) {
            flash.error("error");
            flash.put("emptyChapter", "error.emptyEditChapter");
        } else {

            List<Chapter> chapters = Chapter.find("byTitleAndUser", title, user).fetch();

            if (chapters.size() > 1) {
                flash.error("error");
                flash.put("duplicateChapter", "error.duplicateChapter");
            } else if (chapters.size() == 1) {
                Chapter chapter = chapters.get(0);
                if (chapter.id != id) {
                    flash.error("error");
                    flash.put("duplicateChapter", "error.duplicateChapter");
                }
            }
        }

        if (flash.get("error") == null) {
            Chapter chapter = Chapter.findById(id);
            chapter.title = title;
            chapter.save();
            flash.success("info.chapterEdited");
        }

        render("Chapters/edit.html", id, title);
    }

    public static void viewSelectedAyat() {
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Chapter defaultChapter = Chapter.find("byTitleAndUser", "", user).first();
        List<Ayat> ayats = defaultChapter.ayats;

        List<Chapter> chapters = Chapter.find("user = ? and title != '' order by title", user).fetch();

        render(ayats, chapters,defaultChapter);
    }

    public static void viewChapterAyat(long chapterID,String title) {
        flash.clear();

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Chapter chapter = Chapter.findById(chapterID);

        if (chapter != null && chapter.user.equals(user)) {
            Collections.sort(chapter.ayats);
            render(chapter);
        } else {
            flash.error("error");
            flash.put("errorViewChapter", "error.viewChapter");

            long selectedAyat = Chapter.count("byTitleAndUser", "", user);
            List<Chapter> chapters = Chapter.find("user = ? and title != '' order by title", user).fetch();

            render("Chapters/index.html", selectedAyat, chapters);
        }
    }

    public static void removeChapterAyat(long ayatID, long chapterID) {
        flash.clear();

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Chapter chapter = Chapter.findById(chapterID);

        if (chapter != null && chapter.user.equals(user)) {
            Ayat ayat = Ayat.findById(ayatID);
            chapter.ayats.remove(ayat);
            chapter.save();
        } else {
            renderJSON("{\"result\":\"error\"}");
        }

        renderJSON("{\"result\":\"ok\"}");
    }
}
