package utill;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 10:42:19
 * @Explanation   : 폰트 변경
 * </pre>
 *
 */
public class HYFont {
	private Typeface mTypeface;

	public HYFont(Context context) {
		// TODO Auto-generated constructor stub
		mTypeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/SangSangTitle.ttf");
        /*mTypeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/NanumGothic.ttf");*/
	}

	public void setGlobalFont(ViewGroup root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View child = root.getChildAt(i);
			if (child instanceof TextView)
				((TextView) child).setTypeface(mTypeface);
			else if (child instanceof ViewGroup)
				setGlobalFont((ViewGroup) child);
		}
	}
}
