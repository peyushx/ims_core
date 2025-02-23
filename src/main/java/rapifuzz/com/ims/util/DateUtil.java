package rapifuzz.com.ims.util;

import java.time.LocalDate;

public class DateUtil {

    public static int getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear();
    }
}
