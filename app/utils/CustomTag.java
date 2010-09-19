/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import groovy.lang.Closure;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Ayat;
import models.Sourat;
import play.cache.Cache;
import play.mvc.Scope.Session;
import play.templates.FastTags;
import play.templates.JavaExtensions;
import play.templates.TagContext;
import play.templates.Template;
import play.templates.Template.ExecutableTemplate;
import play.templates.TemplateLoader;

/**
 *
 * @author ali
 */
@FastTags.Namespace(value = "")
public class CustomTag extends FastTags {

    public static void _souratAyat(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        long t1 = System.currentTimeMillis();
        Sourat sourat = (Sourat) args.get("sourat");
        String content = null;
        if (Cache.get("sourat_" + sourat.number) == null) {

            List<Ayat> ayats = Ayat.find("bySourat", args.get("sourat")).fetch();
            int maxAyat = ayats.size();

            Map args2 = new HashMap();
            args2.put("ayats", ayats);
            args2.put("sourat", args.get("sourat"));
            args2.put("maxAyat", maxAyat);
            args2.put("session", Session.current());
            Template t = TemplateLoader.load("Sourats/sourat.html");
            content = t.render(args2);
            Cache.add("sourat_" + sourat.number, content, "24h");
        } else {
            content = (String) Cache.get("sourat_" + sourat.number);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("time2 : "+(t2-t1));
        out.print(content);
    }
}
