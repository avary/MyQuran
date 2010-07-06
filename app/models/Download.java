/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import play.db.jpa.FileAttachment;
import play.db.jpa.Model;

/**
 *
 * @author inf04
 */
@Entity
public class Download extends Model{

    public String name;

    @Embedded
    public FileAttachment pdfFile;

    public boolean isActif;
    
}
