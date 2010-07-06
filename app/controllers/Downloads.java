/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.Download;
import play.mvc.Controller;

/**
 *
 * @author inf04
 */
public class Downloads extends Controller {

    public static void index() {
        render();
    }

    public static void allQuran() {
        Download download = Download.find("byIsActifAndName", true,"coranAll").first();
        renderBinary(download.pdfFile.get(), "coran.pdf");
    }

    public static void manzilQuran() {
        Download download = Download.find("byIsActifAndName", true,"quranManzil").first();
        renderBinary(download.pdfFile.get(), "coranManzil.zip");
    }

    public static void juzQuran() {
        Download download = Download.find("byIsActifAndName", true,"quranJuz").first();
        renderBinary(download.pdfFile.get(), "coranJuz.zip");
    }
}
