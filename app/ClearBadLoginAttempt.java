
import play.jobs.Every;
import play.jobs.Job;
import utils.LoginLimitManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author inf04
 */
@Every("15mn")
public class ClearBadLoginAttempt extends Job {

    @Override
    public void doJob() {
        LoginLimitManager.getInstance().clearBadAttempt();
    }
}
