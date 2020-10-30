package model;

import java.io.Serializable;

public class ValidateDate implements Serializable {
    private static final long serialVersionUID = 1L;
    public static boolean validateDate(String date) {
        if (!date.contains("/")) {
            return false;
        }
        String dateV[] = date.split("/");
        int day = Integer.parseInt(dateV[0]);
        int month = Integer.parseInt(dateV[1]);
        boolean dateAccept = false;
        boolean isleap = ((Integer.parseInt(dateV[2]) % 4 == 0) && (Integer.parseInt(dateV[2]) % 100 != 0)
                || (Integer.parseInt(dateV[2]) % 400 == 0));
        // checking if it is a leap year or not to accept the date for month FEB
        if (isleap) {
            if (Integer.parseInt(dateV[1]) == 02) {
                if (Integer.parseInt(dateV[0]) <= 29) {
                    dateAccept = true;
                }
            }
        } else if (Integer.parseInt(dateV[1]) == 02) {
            if (Integer.parseInt(dateV[0]) <= 28) {
                dateAccept = true;
            }
        }
        // to check if the days are entered properly
        if ((Integer.parseInt(dateV[1]) == 1 || Integer.parseInt(dateV[1]) == 3 || Integer.parseInt(dateV[1]) == 5
                || Integer.parseInt(dateV[1]) == 7 || Integer.parseInt(dateV[1]) == 8
                || Integer.parseInt(dateV[1]) == 10 || Integer.parseInt(dateV[1]) == 12)
                && Integer.parseInt(dateV[1]) != 02) {
            if (Integer.parseInt(dateV[0]) <= 31) {
                dateAccept = true;
            }
        } else if ((Integer.parseInt(dateV[0]) <= 30) && Integer.parseInt(dateV[1]) != 02) {
            dateAccept = true;

        }
        if (day > 31 || month > 12) {
            dateAccept = false;
        }
        return dateAccept;
    }
}
