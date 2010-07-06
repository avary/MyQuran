/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ext;

import play.templates.JavaExtensions;

/**
 *
 * @author ali
 */
public class StringUtils extends JavaExtensions{

    public static String maxString(String string,int max){
        if(string.length() <= max){
            return string;
        }

        String s = string.substring(0,max)+"...";
        return s;
    }

}
