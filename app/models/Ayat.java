/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;
import search.Field;
import search.Indexed;

/**
 *
 * @author ali
 */
@Indexed
@Entity(name = "ayat")
public class Ayat extends Model implements Comparable<Ayat> {

    public String sa;
    @ManyToOne
    public Sourat sourat;
    public int number;
    @Field
    @Lob
    public String content;
    @Lob
    public String arabic;
    public boolean comment;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ayat other = (Ayat) obj;
        if ((this.sa == null) ? (other.sa != null) : !this.sa.equals(other.sa)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.sa != null ? this.sa.hashCode() : 0);
        return hash;
    }

    public int compareTo(Ayat o) {
        if (this.sourat.number == o.sourat.number) {
            if (this.number < o.number) {
                return -1;
            }

            if (this.number > o.number) {
                return 1;
            }
        }else{
            if (this.sourat.number < o.sourat.number) {
                return -1;
            }

            if (this.sourat.number > o.sourat.number) {
                return 1;
            }
        }


        return 0;
    }
}
