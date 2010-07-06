/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

/**
 *
 * @author ali
 */
@Entity(name="sourat")
public class Sourat extends Model{

    public String title;
    public int number;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sourat other = (Sourat) obj;
        if (this.number != other.number) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.number;
        return hash;
    }

    
}
