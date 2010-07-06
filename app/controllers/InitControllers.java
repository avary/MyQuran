/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import models.User;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;

/**
 *
 * @author inf04
 */
public class InitControllers extends Controller{

    @Before
    public static void init(){
        User user = (User) Cache.get("user_" + Secure.Security.connected());
        if (user == null && Secure.Security.connected() != null) {
            user = User.find("byUsername", Secure.Security.connected()).first();
            if (user != null) {
                Cache.set("user_" + user.username, user, "1h");
            }
        }

        if (user != null) {
            session.put("proposal", user.proposal);
            session.put("notification", user.notification ? 1 : 0);
        }
    }

}
