package model;

import java.io.Serializable;

public class IdGeneration implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String EVENT_PREFIX = "EVE";
    private static String SALE_PREFIX = "SAL";
    private static String JOB_PREFIX = "JOB";
    private static int eventNo = 0;
    private static int saleNo = 0;
    private static int jobNo = 0;
    private static String eventid;
    private static String saleid;
    private static String jobid;


    public static void checkId(String Id) {
        if (Id.contains("EVE")) {
            String[] eId = Id.split("EVE");
            eventNo = Integer.parseInt(eId[1]);
        } else if (Id.contains("SAL")) {
            String[] eId = Id.split("SAL");
            saleNo = Integer.parseInt(eId[1]);
        }else if (Id.contains("JOB")) {
            String[] eId = Id.split("JOB");
            jobNo = Integer.parseInt(eId[1]);
        }
    }


    public static String generateId(String postName) {

        if (postName.equalsIgnoreCase("Event")) {
            eventNo++;
            eventid = EVENT_PREFIX + String.format("%03d", eventNo);
            return eventid;
        } else if (postName.equalsIgnoreCase("Sale")) {
            saleNo++;
            saleid = SALE_PREFIX + String.format("%03d", saleNo);
            return saleid;
        } else {
            jobNo++;
            jobid = JOB_PREFIX + String.format("%03d", jobNo);
            return jobid;
        }


    }
}
