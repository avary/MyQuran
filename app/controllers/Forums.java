/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import models.forum.Forum;
import models.forum.ForumCategory;
import models.forum.Post;
import models.forum.Topic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import play.cache.Cache;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.With;

/**
 *
 * @author inf04
 */
public class Forums extends Controller {

    public static void index() {
        List<ForumCategory> forumCategories = ForumCategory.find(""
                + "order by categoryOrder").fetch();
        render(forumCategories);
    }

    public static void listTopicUser(int page) {
        if (page < 1) {
            page = 1;
        }

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        List<models.forum.Topic> threads = models.forum.Topic.find("author = ? " +
                "and finished = false "
                    + "order by updateAt desc", user).fetch(page, 50);

        renderArgs.put("title", "zawaj.al-imane.org - Mes propositions");

        int nbPage = (int) (Math.ceil(threads.size() / 50));

        render(threads, nbPage, page);
    }

    public static void listThread(Long forumID, int page, String title) {
        if (page < 1) {
            page = 1;
        }

        Forum forum = Forum.findById(forumID);

        int nbPage = 0;
        if (forum.nbTopic > 0) {
            nbPage = (int) (Math.ceil(forum.nbTopic / 50));
        }

        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        List<models.forum.Topic> threads = null;
        if (user.isAdmin) {
            threads = models.forum.Topic.find("forum = ? and finished = false "
                    + "order by updateAt desc", forum).fetch(page, 50);
        } else {
            threads = models.forum.Topic.find("forum = ? and author = ? and finished = false "
                    + "order by updateAt desc", forum, user).fetch(page, 50);
        }

        renderArgs.put("title", "zawaj.al-imane.org - " + forum.name);
        render(forum, threads, nbPage, page);
    }

    public static void listPost(Long threadID, int page, String title) {
        flash.keep();
        if (page < 1) {
            page = 1;
        }

        Topic topic = Topic.findById(threadID);
        topic.nbDisplay++;
        int nbPage = 0;
        if (topic.nbResponse > 0) {
            nbPage = (int) (Math.ceil(topic.nbResponse / 10) + 1);
        }

        topic.save();
        List<Post> posts = Post.find("topic = ? "
                + "order by createAt", topic).fetch(page, 10);

        renderArgs.put("title", "zawaj.al-imane.org - " + topic.name);
        render(topic, posts, page, nbPage);
    }

    public static void newThread(Long forumID) {
        try {
            Secure.checkAccess();
            Forum forum = Forum.findById(forumID);
            render(forum);
        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createThread(@Required Long forumID, @Required String title,
            @Required String postContent) {
        try {
            Secure.checkAccess();
            Forum forum = Forum.findById(forumID);
            List<String> errorsMessage = new ArrayList<String>();
            Document doc = Jsoup.parse(postContent);

            if (validation.hasError("title")) {
                flash.error("error");
                errorsMessage.add("error.emptyTitle");
            }

            if (validation.hasError("postContent") || doc.body().text().isEmpty()) {
                flash.error("error");
                errorsMessage.add("error.emptyPostContent");
            }

            if (flash.get("error") != null) {
                render("Forums/newThread.html", forum, errorsMessage);
            }

            Policy policy = Policy.getInstance("Zawaj/lib/antisamy-myspace-1.3.xml");
            AntiSamy as = new AntiSamy();
            CleanResults cr = as.scan(postContent, policy);

            User user = (User) Cache.get("user_" + Secure.Security.connected());

            if (user == null) {
                user = User.find("byUsername", Secure.Security.connected()).first();
                Cache.set("user_" + user.username, user, "1h");
            }

            models.forum.Topic thread = new models.forum.Topic();
            thread.author = user;
            thread.createAt = new Date();
            thread.forum = forum;
            thread.name = title;
            thread.nbDisplay = -1;
            thread.nbResponse = 0;
            thread.updateAt = new Date();
            thread.save();


            forum.lastTopic = thread;
            forum.nbPost++;
            forum.nbTopic++;
            forum.save();


            Post post = new Post();
            post.author = user;
            post.content = cr.getCleanHTML();
            post.createAt = new Date();

            post.topic = thread;
            post.title = title;
            post.save();

            int page;
            if (thread.nbResponse <= 0) {
                page = 1;
            } else {
                page = (int) (Math.ceil(thread.nbResponse / 10) + 1);
            }
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("threadID", thread.id);
            args.put("page", page);
            String url = Router.getFullUrl("Forums.listPost", args);
            redirect(url + "#last");

        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void newPost(Long threadID) {
        try {
            Secure.checkAccess();
            models.forum.Topic thread = models.forum.Topic.findById(threadID);
            render(thread);
        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createPost(Long threadID, String title, @Required String postContent) {
        try {
            Secure.checkAccess();
            models.forum.Topic thread = models.forum.Topic.findById(threadID);
            List<String> errorsMessage = new ArrayList<String>();
            Document doc = Jsoup.parse(postContent);

            if (validation.hasError("postContent") || doc.body().text().isEmpty()) {
                flash.error("error");
                errorsMessage.add("error.emptyPostContent");
            }

            if (flash.get("error") != null) {
                render("Forums/newPost.html", thread, errorsMessage);
            }

            Policy policy = Policy.getInstance("Zawaj/lib/antisamy-myspace-1.3.xml");
            AntiSamy as = new AntiSamy();
            CleanResults cr = as.scan(postContent, policy);

            Forum forum = thread.forum;
            User user = (User) Cache.get("user_" + Secure.Security.connected());

            if (user == null) {
                user = User.find("byUsername", Secure.Security.connected()).first();
                Cache.set("user_" + user.username, user, "1h");
            }

            thread.nbDisplay++;
            thread.nbResponse++;
            thread.updateAt = new Date();
            thread.save();

            forum.lastTopic = thread;
            forum.nbPost++;
            forum.save();

            Post post = new Post();
            post.author = user;
            post.content = cr.getCleanHTML();
            post.createAt = new Date();

            post.topic = thread;
            post.title = title;
            post.save();

            thread.lastPost = post;
            thread.save();

            int page;
            if (thread.nbResponse <= 0) {
                page = 1;
            } else {
                page = (int) (Math.ceil(thread.nbResponse / 10) + 1);
            }
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("threadID", thread.id);
            args.put("page", page);
            String url = Router.getFullUrl("Forums.listPost", args);
            redirect(url + "#last");

        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void editPost(Long postID) {
        try {
            Secure.checkAccess();
            Post post = Post.findById(postID);
            User user = (User) Cache.get("user_" + Secure.Security.connected());

            if (user == null) {
                user = User.find("byUsername", Secure.Security.connected()).first();
                Cache.set("user_" + user.username, user, "1h");
            }

            if (user.equals(post.author) || user.isAdmin) {
                render(post);
            } else {
                flash.error("error.noEdit");
                flash.keep();
                listPost(post.topic.id, 1, post.topic.name);
            }

        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void newQuotedPost(Long postID) {
        try {
            Secure.checkAccess();
            Post post = Post.findById(postID);
            Policy policy = Policy.getInstance("Zawaj/lib/antisamy-custom.xml");
            AntiSamy as = new AntiSamy();
            CleanResults cr = as.scan(post.content, policy);

            post.content = "<blockquote>" + cr.getCleanHTML() + "</blockquote><p></p>";
            models.forum.Topic thread = post.topic;
            render("Forums/newPost.html", thread, post);
        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void savePost(Long postID, String title, @Required String postContent) {
        try {
            Secure.checkAccess();
            List<String> errorsMessage = new ArrayList<String>();
            Post post = Post.findById(postID);
            Document doc = Jsoup.parse(postContent);

            if (validation.hasError("postContent") || doc.body().text().isEmpty()) {
                flash.error("error");
                errorsMessage.add("error.emptyPostContent");
            }
            if (flash.get("error") != null) {
                render("Forums/editPost.html", post, errorsMessage);
            }

            Policy policy = Policy.getInstance("Zawaj/lib/antisamy-myspace-1.3.xml");
            AntiSamy as = new AntiSamy();
            CleanResults cr = as.scan(postContent, policy);

            post.title = title;
            post.content = cr.getCleanHTML();
            post.updateAt = new Date();
            User user = (User) Cache.get("user_" + Secure.Security.connected());

            if (user == null) {
                user = User.find("byUsername", Secure.Security.connected()).first();
                Cache.set("user_" + user.username, user, "1h");
            }

            post.save();

            int page;
            if (post.topic.nbResponse <= 0) {
                page = 1;
            } else {
                page = (int) (Math.ceil(post.topic.nbResponse / 10) + 1);
            }

            Map<String, Object> args = new HashMap<String, Object>();
            args.put("threadID", post.topic.id);
            args.put("page", page);
            String url = Router.getFullUrl("Forums.listPost", args);
            redirect(url + "#post" + post.id);

        } catch (Throwable ex) {
            Logger.getLogger(Forums.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deletePost(Long postID) {
        Post post = Post.findById(postID);
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        if (post == null || user == null) {
            index();
        }

        if (post.author.equals(user) || user.isAdmin) {
            post.state = 3;
            post.save();
        }

        int page;
        if (post.topic.nbResponse <= 0) {
            page = 1;
        } else {
            page = (int) (Math.ceil(post.topic.nbResponse / 10) + 1);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("threadID", post.topic.id);
        args.put("page", page);
        String url = Router.getFullUrl("Forums.listPost", args);
        redirect(url + "#post" + post.id);
    }

    public static void undeletePost(Long postID) {
        Post post = Post.findById(postID);
        User user = (User) Cache.get("user_" + Secure.Security.connected());

        if (user == null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            Cache.set("user_" + user.username, user, "1h");
        }

        if (post == null || user == null) {
            index();
        }

        if (user.isAdmin) {
            post.state = 1;
            post.save();
        }

        if (post.author.equals(user)) {
            post.state = 0;
            post.save();
        }

        int page;
        if (post.topic.nbResponse <= 0) {
            page = 1;
        } else {
            page = (int) (Math.ceil(post.topic.nbResponse / 10) + 1);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("threadID", post.topic.id);
        args.put("page", page);
        String url = Router.getFullUrl("Forums.listPost", args);
        redirect(url + "#post" + post.id);
    }
}
