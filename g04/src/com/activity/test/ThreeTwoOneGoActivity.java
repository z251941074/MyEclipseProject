package com.activity.test;

import com.activity.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 三、二、一、GO界面
 * 
 * @author 仲丛旭
 * 
 */
public class ThreeTwoOneGoActivity extends Activity {

	/** 测试日志标识 */
	private final String TAG = "ThreeTwoOneGoActivity";
	/** 一个3\2\1\go的显示窗口 */
	private PopupWindow mPopupWindow;
	/** PopupWindow上的控件，用来具体的3 2 1 内容 */
	private TextView tv_popup;
	/** 一个Button按钮 */
	private Button button_go;
	/** 由小到大动画 */
	private Animation scaleSmallToBigAnimation;
	/** 由大到小动画 */
	private Animation scaleBigToSmallAnimation;
	/** 由有到无动画 */
	private Animation alphaAnimation;
	/* =================Control=================== */
	/** 倒计时每个开始动画：1、2、3是由小到大的动画结束监听 */
	private MyAnimationListener startAnimationListener = new MyAnimationListener() {

		@Override
		public void onAnimationEnd(Animation arg0) {
			String tvStr = tv_popup.getText().toString();
			if (tvStr.equals("3")) {
				tv_popup.startAnimation(alphaAnimation);// 执行渐渐消失动画
			} else if (tvStr.equals("2")) {
				tv_popup.startAnimation(alphaAnimation);// 执行渐渐消失动画
			} else if (tvStr.equals("1")) {
				tv_popup.startAnimation(alphaAnimation);// 执行渐渐消失动画
			} else if (tvStr.equals("GO!")) {
				mPopupWindow.dismiss();// 关闭对话框
				// tv_popup.startAnimation(alphaAnimation);// 执行渐渐消失动画
				/* 跳转到定位页 */
				startActivity(new Intent(ThreeTwoOneGoActivity.this, MainActivity_gps.class));
			}
		}

	};
	/** 倒计时每个结束动画：1、2、3是由有到无的渐变动画结束监听 */
	private MyAnimationListener endAnimationListener = new MyAnimationListener() {

		@Override
		public void onAnimationEnd(Animation arg0) {
			String tvStr = tv_popup.getText().toString();
			if (tvStr.equals("3")) {
				tv_popup.setText("2");
				tv_popup.startAnimation(scaleSmallToBigAnimation);// 由小到大动画
			} else if (tvStr.equals("2")) {
				tv_popup.setText("1");
				tv_popup.startAnimation(scaleSmallToBigAnimation);// 由小到大动画
			} else if (tvStr.equals("1")) {
				tv_popup.setText("GO!");
				tv_popup.startAnimation(scaleBigToSmallAnimation);// 由小到大动画
			} else if (tvStr.equals("GO!")) {
				
			}
		}

	};

	/** 单击开始按钮监听 */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			arg0.setVisibility(View.GONE);
			Log.i(TAG, "==单击操作");
			LayoutInflater layoutInflater = LayoutInflater.from(ThreeTwoOneGoActivity.this);
			View popupWindow = layoutInflater.inflate(R.layout.popup_window, null);
			tv_popup = (TextView) popupWindow.findViewById(R.id.tv_popup);
			tv_popup.setText("3");
			// 创建一个PopupWindow
			// 参数1：contentView 指定PopupWindow的内容
			// 参数2：width 指定PopupWindow的width
			// 参数3：height 指定PopupWindow的height
			mPopupWindow = new PopupWindow(popupWindow, 400, 400);
			mPopupWindow.showAsDropDown(button_go);// 相对Button显示
			// mPopupWindow.showAtLocation(ThreeTwoOneGoActivity.this.getWindow().getDecorView(),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			tv_popup.startAnimation(scaleSmallToBigAnimation);
		}

	};

	/* =================Lifecycle================= */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

	/* ================子过程================ */
	/**
	 * 初始化相关布局
	 */
	private void initView() {
		/* 主界面布局 */
		setContentView(R.layout.activity_go);
		/* 开始按钮初始化 */
		button_go = (Button) findViewById(R.id.button_go);
		button_go.setOnClickListener(mOnClickListener);
	}

	/**
	 * 初始化相关数据
	 */
	private void initData() {
		/* 由小到大缩放动画初始化 */
		scaleSmallToBigAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
		scaleSmallToBigAnimation.setDuration(500);
		scaleSmallToBigAnimation.setAnimationListener(startAnimationListener);
		/* 由大到小缩放动画初始化 */
		scaleBigToSmallAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f);
		scaleBigToSmallAnimation.setDuration(500);
		scaleBigToSmallAnimation.setAnimationListener(startAnimationListener);
		/* 由有到无动画 */
		alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
		alphaAnimation.setDuration(500);// 设置动画时间
		alphaAnimation.setAnimationListener(endAnimationListener);
	}

	/* ============内部类================ */
	/**
	 * 为了省代码
	 * 
	 * @author sf
	 * 
	 */
	abstract class MyAnimationListener implements AnimationListener {

		@Override
		public void onAnimationStart(Animation arg0) {

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}
	}
}
