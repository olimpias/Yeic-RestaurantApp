package com.Yeic.GuiItem;

import com.Yeic.cse400.R;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyProgressDialog extends Dialog {
	private ImageView iv;
	private Context context;
	public MyProgressDialog(Context context) {
		super(context, R.style.TransparentProgressDialog);
		this.context=context;
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
    	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
    	getWindow().setAttributes(wlmp);
	setTitle(null);
	setCancelable(false);
	setOnCancelListener(null);
	LinearLayout layout = new LinearLayout(context);
	layout.setOrientation(LinearLayout.VERTICAL);
	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	iv = new ImageView(context);
	iv.setImageResource(R.drawable.ezimba);
	layout.addView(iv, params);
	addContentView(layout, params);
	}
	@Override
	public void show() {
		super.show();
		 Animation animation = AnimationUtils.loadAnimation(context, R.anim.clockwise);
		 iv.startAnimation(animation);
	}

}
