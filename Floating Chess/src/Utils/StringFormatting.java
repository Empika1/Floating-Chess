//A class with static methods for formatting strings
package Utils;

public class StringFormatting {
    public static String msToTime(long ms) { //converts a number of milliseconds to hours, minutes, seconds, and tenths
        long msRemaining;
        long hours = ms / 3600000; //get the number of full hours in the time 
        msRemaining = ms % 3600000; //get the remainder
        long minutes = msRemaining / 60000; //get the number of full minutes in the time
        msRemaining %= 60000; //get the remainder
        long seconds = msRemaining / 1000; //get the number of full seconds in the time
        msRemaining %= 1000; //get the remainder
        long tenths = msRemaining / 100; //get the number of full tenths in the time

        String hoursString = hours + ""; //convert the number of hours to a string
        if (hoursString.length() == 1) //if theres only a single digit number of hours, pad it with a 0
            hoursString = "0" + hoursString;
        String minutesString = minutes + ""; //same but with minutes
        if (minutesString.length() == 1)
            minutesString = "0" + minutesString;
        String secondsString = seconds + ""; //same but with seconds
        if (secondsString.length() == 1)
            secondsString = "0" + secondsString;
        String tenthsString = tenths + ""; //same but with tenths, but without 0 padding

        return hoursString + ":" + minutesString + ":" + secondsString + "." + tenthsString; //return them concatenated together
    }
}