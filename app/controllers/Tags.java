/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import java.util.List;
import java.util.Map;
import models.Ayat;
import models.Tag;
import play.cache.Cache;
import play.mvc.Controller;

/**
 *
 * @author ali
 */
public class Tags extends Controller{

    public static void index(){
        List<Map> tags = Tag.getCloud();
        
        render(tags);
    }

    public static void viewAyats(String name,String slug){
        List<Ayat> ayats = Ayat.findTaggedWith(name);
        render(ayats,name);
    }

    public static void removeAyat(String tagName,long ayatID){
        Tag tag = Tag.find("byName", tagName).first();
        Ayat ayat = Ayat.findById(ayatID);
        ayat.tags.remove(tag);
        ayat.save();
        Cache.delete("sourat_ayat_"+ayat.sourat.number);
        renderJSON("{\"result\":\"ok\"}");
    }

}
