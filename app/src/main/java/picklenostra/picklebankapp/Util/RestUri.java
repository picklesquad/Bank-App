package picklenostra.picklebankapp.Util;

/**
 * Created by andrikurniawan.id@gmail.com on 5/22/2016.
 */
public class RestUri {

    private static final String BASE_URI_API = "http://104.155.206.184:8080/api/bank/";

    public static class login{
        public static final String LOGIN = BASE_URI_API + "login";
        public static final String GCM_UPDATE = BASE_URI_API + "gcmRegister";
    }

    public static class nasabah{
        private static final String NASABAH = BASE_URI_API + "nasabah/";

        public static final String NASABAH_ALL = NASABAH + "getAll";
        public static final String NASABAH_DETAIL = NASABAH + "%1$s";
    }

    public static class notifikasi{
        public static final String NOTIFICATION = BASE_URI_API + "notification";
    }

    public static class transaction{
        public static final String TRANSACTION = BASE_URI_API + "transaction";

        public static final String TRANSACTION_NEW = TRANSACTION + "/addNew";
    }

    public static class withdraw{
        public static final String WITHDRAW = BASE_URI_API + "withdraw";

        public static final String WITHDRAW_DETAIL = WITHDRAW + "/%1$s";
        private static final String UPDATE_WITHDRAW = WITHDRAW + "/updateStatus/";

        public static final String WITHDRAW_ACCEPT = UPDATE_WITHDRAW + "accept";
        public static final String WITHDRAW_REJECT = UPDATE_WITHDRAW + "reject";
        public static final String WITHDRAW_COMPLETE = UPDATE_WITHDRAW + "complete";
    }
}
