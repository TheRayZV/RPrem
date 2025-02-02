package org.rprem.utils;

public class TimeUtils {

    public static long parseTime(String time) {
        if (time.equalsIgnoreCase("lifetime")) {
            return Long.MAX_VALUE;
        }

        try {
            char unit = time.charAt(time.length() - 1);
            long value = Long.parseLong(time.substring(0, time.length() - 1));

            switch (unit) {
                case 'h': return value * 3600 * 1000;
                case 'd': return value * 24 * 3600 * 1000;
                case 'w': return value * 7 * 24 * 3600 * 1000;
                case 'm': return value * 30 * 24 * 3600 * 1000;
                case 'y': return value * 365 * 24 * 3600 * 1000;
                default: return -1;
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public static String formatTime(long millis) {
        if (millis == Long.MAX_VALUE) {
            return "Навсегда";
        }

        long days = millis / (24 * 3600 * 1000);
        if (days > 0) {
            return days + " д.";
        }

        long hours = millis / (3600 * 1000);
        if (hours > 0) {
            return hours + " ч.";
        }

        long minutes = millis / (60 * 1000);
        if (minutes > 0) {
            return minutes + " мин.";
        }

        return "Меньше минуты";
    }
}