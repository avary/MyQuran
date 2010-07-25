/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.List;
import models.Ayat;
import models.Comment;
import models.Sourat;
import models.User;
import play.cache.Cache;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author inf04
 */
@With(value={Secure.class,InitControllers.class})
public class Comments extends Controller {

    public static void list(int page) {

        if (page < 1) {
            page = 1;
        }

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        List<Comment> comments = Comment.find("user = ? order by ayat.id asc", user).from((page - 1) * 200).fetch(200);
        int nbPage = (int) Math.ceil(Comment.count() / 200.0);

        List<Sourat> sourats = Comment.find("select distinct (c.sourat) from models.Comment c "
                + "where c.user = ? order by c.sourat.number", user).fetch();

        Long id = -1L;

        render(page, nbPage, comments, sourats, id);
    }

    public static void listPublic(int page) {

        if (page < 1) {
            page = 1;
        }


        List<Comment> comments = Comment.find("user is null order by ayat.id asc").from((page - 1) * 200).fetch(200);
        int nbPage = (int) Math.ceil(Comment.count() / 200.0);

        List<Sourat> sourats = Comment.find("select distinct (c.sourat) from models.Comment c "
                + "where c.user is null order by c.sourat.number").fetch();

        Long id = -1L;

        render(page, nbPage, comments, sourats, id);
    }

    public static void listBySourat(Long id) {
        if (id == -1) {
            list(1);
        }

        Sourat sourat = Sourat.findById(id);
        if (sourat == null) {
            list(1);
        }

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }


        List<Comment> comments = Comment.find("user = ? and sourat = ? order by ayat.id", user, sourat).fetch();

        List<Sourat> sourats = Comment.find("select distinct (c.sourat) from models.Comment c "
                + "where c.user = ? order by c.sourat.number", user).fetch();

        render("Comments/list.html", 0, 0, comments, sourats, id);
    }

    public static void load(Long ayatId) throws Throwable {

        if (Secure.Security.connected() == null) {
            Secure.login("");
        }

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Ayat ayat = Ayat.findById(ayatId);
        Comment comment = Comment.find("byUserAndAyat", user, ayat).first();
        System.out.println("### comment = "+comment);
        if (comment == null) {
            comment = new Comment();
        }
        renderJSON(comment);

    }

    public static void loadPublic(Long ayatID) throws Throwable {
        Ayat ayat = Ayat.findById(ayatID);
        Comment comment = Comment.find("ayat = ? and user is null ", ayat).first();
        System.out.println("### public comment = "+comment);
        if (comment == null) {
            comment = new Comment();
        }
        renderJSON(comment);
    }

    public static void save(Long ayatId, String content) {
        flash.clear();

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Ayat ayat = Ayat.findById(ayatId);
        Comment comment = Comment.find("byUserAndAyat", user, ayat).first();
        if (comment == null) {
            comment = new Comment();
            comment.user = user;
            comment.ayat = ayat;
            comment.sourat = ayat.sourat;
        }

        comment.content = content;
        comment.save();
        renderJSON("{\"result\":\"ok\"}");
    }
}
