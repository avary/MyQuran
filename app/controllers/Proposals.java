/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Ayat;
import models.Comment;
import models.Proposal;
import models.Tag;
import models.User;
import models.forum.Forum;
import models.forum.Post;
import models.forum.Topic;
import notifiers.Notifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;

/**
 *
 * @author inf04
 */
public class Proposals extends Controller {

    public static void newTag(long ayatID, @Required String names) {
        if (Validation.hasErrors()) {
            renderJSON("{\"result\":\"noTag\"}");
        }

        Ayat ayat = Ayat.findById(ayatID);
        if (ayat == null) {
            renderJSON("{\"result\":\"noAyat\"}");
        }

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        String existingTag = "";
        String tag = "";
        StringTokenizer st = new StringTokenizer(names, ",");
        while (st.hasMoreTokens()) {
            tag = st.nextToken();
            if (ayat.tags.contains(new Tag(tag))) {
                existingTag += existingTag.length() > 0 ? ", " : "";
                existingTag += tag;
            }
        }

        if (existingTag.length() > 0) {
            renderJSON("{\"result\":\"existingTag\",\"tags\":\"" + existingTag + "\"}");
        }

        Proposal p = new Proposal();
        p.ayat = ayat;
        p.state = 0;
        p.type = 2;
        p.user = user;
        p.content=names;
        p.save();

        Forum forum = Forum.find("byName", "Propositions de chapitres").first();
        Topic topic = new Topic();
        topic.author = user;
        topic.createAt = new Date();
        topic.updateAt = new Date();
        topic.forum = forum;
        topic.name = "[Tag] Ajout d'un verset : " + names;
        topic.proposal = p;
        topic.save();

        forum.lastTopic = topic;
        forum.nbPost = forum.nbPost + 1;
        forum.nbTopic = forum.nbTopic + 1;
        forum.save();

        Post post = new Post();
        post.author = user;
        post.content = "<strong>Proposition d'ajout du verset dans les tags suivants : "
                + "</strong>" + names + "<br/><br/>"
                + "Sourate " + ayat.sourat.number + ", verset " + ayat.number + " : "
                + "<br/><br/><strong>" + ayat.content + "</strong>";
        post.createAt = new Date();
        post.state = 0;
        post.topic = topic;
        post.title = "";
        post.save();

        topic.lastPost = post;
        topic.nbResponse = topic.nbResponse + 1;
        topic.proposal = p;
        topic.save();

        List<User> users = User.find("isAdmin = true").fetch();
        for (User u : users) {
            try {
                u.notification = true;
                u.save();
                Notifier.sendNewProposal(u, topic);
                Cache.set("user_" + u.username, u, "1h");
            } catch (Exception ex) {
                Logger.getLogger(Proposals.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        renderJSON("{\"result\":\"ok\"}");
    }

    public static void newComment(Long ayatId, String comment) {
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
        topic.name = "[Commentaire] Sourate " + ayat.sourat.title + ", verset " + ayat.number;
        topic.save();

        Proposal p = new Proposal();
        p.ayat = ayat;
        p.content = comment;
        p.state = 0;
        p.type = 1;
        p.user = user;
        p.save();

        forum.lastTopic = topic;
        forum.nbPost = forum.nbPost + 1;
        forum.nbTopic = forum.nbTopic + 1;
        forum.save();

        Post post = new Post();
        post.author = user;
        post.content = "<strong>Sourate " + ayat.sourat.title + ", verset " + ayat.number + " : </strong><br/><br/>"
                + "" + ayat.content + "<br/><br/>"
                + "<strong>Proposition : </strong><br/><br/>" + comment;
        post.createAt = new Date();
        post.state = 0;
        post.topic = topic;
        post.title = "Sourate " + ayat.sourat.title + ", verset " + ayat.number;
        post.save();

        topic.lastPost = post;
        topic.nbResponse = topic.nbResponse + 1;
        topic.proposal = p;
        topic.save();

        List<User> users = User.find("isAdmin = true").fetch();
        for (User u : users) {
            try {
                u.notification = true;
                u.save();
                Notifier.sendNewProposal(u, topic);
                Cache.set("user_" + u.username, u, "1h");
            } catch (Exception ex) {
                Logger.getLogger(Proposals.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        renderJSON("{\"result\":\"ok\"}");
    }

    public static void newTranslation(Long ayatId, String translation) {
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
        topic.name = "[Traductions] Sourate " + ayat.sourat.title + ", verset " + ayat.number;
        topic.save();

        Proposal p = new Proposal();
        p.ayat = ayat;
        p.content = translation;
        p.state = 0;
        p.type = 0;
        p.user = user;
        p.save();

        forum.lastTopic = topic;
        forum.nbPost = forum.nbPost + 1;
        forum.nbTopic = forum.nbTopic + 1;
        forum.save();

        Post post = new Post();
        post.author = user;
        post.content = "<strong>Sourate " + ayat.sourat.title + ", verset " + ayat.number + " : </strong><br/><br/>"
                + "" + ayat.content + "<br/><br/>"
                + "<strong>Proposition : </strong><br/><br/>" + translation;
        post.createAt = new Date();
        post.state = 0;
        post.topic = topic;
        post.title = "Sourate " + ayat.sourat.title + ", verset " + ayat.number;
        post.save();

        topic.lastPost = post;
        topic.nbResponse = topic.nbResponse + 1;
        topic.proposal = p;
        topic.save();

        List<User> users = User.find("isAdmin = true").fetch();
        for (User u : users) {
            try {
                u.notification = true;
                u.save();
                Notifier.sendNewProposal(u, topic);
                Cache.set("user_" + u.username, u, "1h");
            } catch (Exception ex) {
                Logger.getLogger(Proposals.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        renderJSON("{\"result\":\"ok\"}");
    }

    public static void validate(Long postID) {
        Post post = Post.findById(postID);
        boolean accepted = true;

        if (post.topic.proposal.type == 0) {
            
            render("Proposals/validateTransalation.html", post, accepted);
        }
        if (post.topic.proposal.type == 1) {
            
            Comment c = Comment.find("ayat = ? and user is null ", post.topic.proposal.ayat).first();
            render("Proposals/validateComment.html", post, accepted, c);
        }
        if (post.topic.proposal.type == 2) {
            
            render("Proposals/validateTags.html", post, accepted);
        }

    }

    public static void validateTags(Long postID, @Required String tags) {
        Post post = Post.findById(postID);

        if (Validation.hasErrors()) {
            flash.put("error", "error.noTag");
            flash.keep();
            validate(postID);
        }

        StringTokenizer st = new StringTokenizer(tags, ",");
        while (st.hasMoreTokens()) {
            post.topic.proposal.ayat.tagItWith(st.nextToken());
        }
        post.topic.proposal.ayat.save();
        Cache.delete("sourat_"+post.topic.proposal.ayat.sourat.number);
        
        post.topic.finished = true;
        post.topic.updateAt = new Date();
        post.topic.proposal.state = 1;
        post.topic.proposal.proposalFinish = new Date();

        post.topic.save();
        post.topic.proposal.save();

        flash.success("tag.validated");

        render(post);
    }

    public static void validateTransalation(Long postID, String content) {
        if (content == null || content.trim().isEmpty()) {
            flash.put("transalationError", "transalation.noContent");
            flash.keep();
            validate(postID);
        }

        Post post = Post.findById(postID);
        post.topic.finished = true;
        post.topic.updateAt = new Date();
        post.topic.proposal.state = 1;
        post.topic.proposal.proposalFinish = new Date();
        post.topic.proposal.ayat.content = content.trim();

        post.topic.save();
        post.topic.proposal.save();
        post.topic.proposal.ayat.save();

        Cache.delete("sourat_ayat_" + post.topic.proposal.ayat.sourat.number);

        flash.success("transalation.added");
        render(post);
    }

    public static void validateComment(Long postID, String comment) {
        try {
            Document doc = Jsoup.parse(comment);

            if (comment == null || comment.trim().isEmpty() || doc.body().text().trim().isEmpty()) {
                flash.put("commentError", "comment.noContent");
                flash.keep();
                validate(postID);
            }

            Policy policy = Policy.getInstance("MyQuran/lib/antisamy-myspace-1.3.xml");
            AntiSamy as = new AntiSamy();
            CleanResults cr = as.scan(comment, policy);

            Post post = Post.findById(postID);

            post.topic.finished = true;
            post.topic.updateAt = new Date();
            post.topic.proposal.proposalFinish = new Date();
            post.topic.proposal.state = 1;
            post.topic.proposal.ayat.comment = true;

            Comment c = Comment.find("ayat = ? and user is null ", post.topic.proposal.ayat).first();
            if (c == null) {
                c = new Comment();
                c.ayat = post.topic.proposal.ayat;
                c.sourat = post.topic.proposal.ayat.sourat;
            }
            c.content = cr.getCleanHTML();
            c.save();

            post.topic.save();
            post.topic.proposal.save();
            post.topic.proposal.ayat.save();

            Cache.delete("sourat_ayat_" + post.topic.proposal.ayat.sourat.number);

            flash.success("comment.added");
            render(post, c);
        } catch (Throwable ex) {
            Logger.getLogger(Proposals.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reject(Long postID) {
        Post post = Post.findById(postID);

        if (post.topic.proposal.type == 0) {
            post.topic.finished = true;
            post.topic.proposal.state = 2;
            post.topic.updateAt = new Date();

            post.topic.save();
            post.topic.proposal.save();

            flash.success("transalation.rejectedTransalation");
            render("Proposals/validateTransalation.html", post);
        }
        if (post.topic.proposal.type == 1) {
            Comment c = Comment.find("ayat = ? and user is null ", post.topic.proposal.ayat).first();
            post.topic.finished = true;
            post.topic.proposal.state = 2;
            post.topic.updateAt = new Date();

            post.topic.save();
            post.topic.proposal.save();

            flash.success("comment.rejectedComment");
            render("Proposals/validateComment.html", post, c);
        }
        if (post.topic.proposal.type == 2) {
            post.topic.finished = true;
            post.topic.proposal.state = 2;
            post.topic.updateAt = new Date();

            post.topic.save();
            post.topic.proposal.save();

            if (post.topic.proposal.ayat == null) {
                flash.success("chapter.rejectedChapter");
            } else {
                flash.success("chapter.rejectedChapterAyat");
            }

            render("Proposals/validateChapter.html", post);
        }
    }
}
