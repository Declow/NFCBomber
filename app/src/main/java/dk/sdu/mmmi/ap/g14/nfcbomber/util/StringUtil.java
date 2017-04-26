package dk.sdu.mmmi.ap.g14.nfcbomber.util;

/**
 * Created by declow on 4/26/17.
 */

public class StringUtil {

    public String formatTime(float time) {
        float secs = (long)(time/1000);
        float mins = (long)((time/1000)/60);

        /* Convert seconds to string */
        secs = secs % 60;
        String seconds = Integer.toString(Math.round(secs));
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }


        /* Convert minutes to string */
        mins = mins % 60;
        String minutes = Integer.toString(Math.round(mins));
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

        /* Convert milliseconds */
        String milliseconds = Integer.toString((int)(time/10) % 100);

        if(milliseconds.length() == 1) {
            milliseconds = "0"+milliseconds;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(minutes);
        sb.append(':');
        sb.append(seconds);
        sb.append(':');
        sb.append(milliseconds);

        return sb.toString();
    }
}