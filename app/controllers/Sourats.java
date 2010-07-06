/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import java.util.List;
import models.Sourat;
import models.User;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 *
 * @author ali
 */
@With(InitControllers.class)
public class Sourats extends Controller{


    public static void list(){
        renderArgs.put("souratList","coran.al-imane.org - "+Messages.get("title.souratList"));

        List<Sourat> sourats = null;
        if(Cache.get("sourats") == null){
            sourats = Sourat.findAll();
            Cache.set("sourats", sourats,"24h");
        }else{
            sourats = (List<Sourat>) Cache.get("sourats");
        }
        
        render(sourats);
    }

}
