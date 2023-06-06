package Utils;

public class StringFormatting {
    public static String msToTime(long ms) {
        long msRemaining;
        long hours = ms / 3600000;
        msRemaining = ms % 3600000;
        long minutes = msRemaining / 60000;
        msRemaining %= 60000;
        long seconds = msRemaining / 1000;
        msRemaining %= 1000;
        long tenths = msRemaining / 100;
        msRemaining %= 100;

        String hoursString = hours + "";
        if (hoursString.length() == 1)
            hoursString = "0" + hoursString;
        String minutesString = minutes + "";
        if (minutesString.length() == 1)
            minutesString = "0" + minutesString;
        String secondsString = seconds + "";
        if (secondsString.length() == 1)
            secondsString = "0" + secondsString;
        String tenthsString = tenths + "";

        return hoursString + ":" + minutesString + ":" + secondsString + "." + tenthsString;
    }
}