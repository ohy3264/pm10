package utill;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 10:41:42
 * @Explanation   : 설정 값
 * </pre>
 *
 */
public class Constant {

    public static String GUIDE_MSG = "";
    public static int ACTIONBAR_COLOR = 0;

	/* Request_flag */
	public static boolean REQUEST_FLAG = false;
	public static int REQUEST_NONE = 0;
	public static int REQUEST_NEW = 1;
	public static int REQUEST_STATE = REQUEST_NEW;
	public static boolean REQUEST_STATE_CHANGE = false;
	public static String CURRENT_LOCATION = null;
	
	/* BroadCastReceiver Filter */
	public static final String ACTION_EVENT 			= "com.widget.ACTION_EVENT";
	public static final String ACTION_CALL_ACTIVITY 	= "com.widget.ACTION_CALL_ACTIVITY";
	
	/* pm10 download img */
	public static int img_num = 40;

    public static String ANI_STATE_UP = "UP";
    public static String ANI_STATE_DOWN = "DOWN";
    public static String ANI_STATE = ANI_STATE_UP; // 하단메뉴 애니메이션 상태

    /* admob  on/off */
    public static boolean ADMOB = false;
	
}
