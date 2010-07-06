/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import java.util.Date;
import models.Ayat;
import models.Proposal;
import models.User;
import models.forum.Forum;
import models.forum.Post;
import models.forum.Topic;
import play.cache.Cache;
import play.mvc.Controller;

/**
 *
 * @author inf04
 */
public class Proposals extends Controller{

    public static void newComment(Long ayatId,String comment){
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Ayat ayat = Ayat.findById(ayatId);

        Forum forum = Forum.find("byName", "Propositions de commentaires").first();
        Topic topic = new Topic();
        topic.author = user;
        topic.createAt = new Date();
        topic.updateAt = new Date();
        topic.forum = forum;
        topic.name = "[Commentaire] Sourate "+ayat.sourat.title+", verset "+ayat.number;
        topic.save();

        Proposal p = new Proposal();
        p.ayat = ayat;
        p.content = comment;
        p.finished = false;
        p.type = 1;
        p.user = user;
        p.save();

        forum.lastTopic = topic;
        forum.nbPost = forum.nbPost + 1;
        forum.nbTopic = forum.nbTopic + 1;
        forum.save();

        Post post = new Post();
        post.author = user;
        post.content = "<strong>Sourate "+ayat.sourat.title+", verset "+ayat.number+" : </strong><br/><br/>" +
                ""+ayat.content+"<br/><br/>" +
                "<strong>Proposition : </strong><br/><br/>"+comment;
        post.createAt = new Date();
        post.state = 0;
        post.topic = topic;
        post.title = "Sourate "+ayat.sourat.title+", verset "+ayat.number;
        post.save();

        topic.lastPost = post;
        topic.nbResponse = topic.nbResponse+1;
        topic.proposal = p;
        topic.save();

        renderJSON("{\"result\":\"ok\"}");
    }
    
    public static void newTranslation(Long ayatId,String translation){
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        Ayat ayat = Ayat.findById(ayatId);

        Forum forum = Forum.find("byName", "Propositions de traduction").first();
        Topic topic = new Topic();
        topic.author = user;
        topic.createAt = new Date();
        topic.updateAt = new Date();
        topic.forum = forum;
        topic.name = "[Traductions] Sourate "+ayat.sourat.title+", verset "+ayat.number;
        topic.save();

        Proposal p = new Proposal();
        p.ayat = ayat;
        p.content = translation;
        p.finished = false;
        p.type = 0;
        p.user = user;
        p.save();

        forum.lastTopic = topic;
        forum.nbPost = forum.nbPost + 1;
        forum.nbTopic = forum.nbTopic + 1;
        forum.save();

        Post post = new Post();
        post.author = user;
        post.content = "<strong>Sourate "+ayat.sourat.title+", verset "+ayat.number+" : </strong><br/><br/>" +
                ""+ayat.content+"<br/><br/>" +
                "<strong>Proposition : </strong><br/><br/>"+translation;
        post.createAt = new Date();
        post.state = 0;
        post.topic = topic;
        post.title = "Sourate "+ayat.sourat.title+", verset "+ayat.number;
        post.save();

        topic.lastPost = post;
        topic.nbResponse = topic.nbResponse+1;
        topic.proposal = p;
        topic.save();

        renderJSON("{\"result\":\"ok\"}");
    }

}
