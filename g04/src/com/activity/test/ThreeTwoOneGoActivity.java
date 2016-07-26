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
 * ��������һ��GO����
 * 
 * @author �ٴ���
 * 
 */
public class ThreeTwoOneGoActivity extends Activity {

	/** ������־��ʶ */
	private final String TAG = "ThreeTwoOneGoActivity";
	/** һ��3\2\1\go����ʾ���� */
	private PopupWindow mPopupWindow;
	/** PopupWindow�ϵĿؼ������������3 2 1 ���� */
	private TextView tv_popup;
	/** һ��Button��ť */
	private Button button_go;
	/** ��С���󶯻� */
	private Animation scaleSmallToBigAnimation;
	/** �ɴ�С���� */
	private Animation scaleBigToSmallAnimation;
	/** ���е��޶��� */
	private Animation alphaAnimation;
	/* =================Control=================== */
	/** ����ʱÿ����ʼ������1��2��3����С����Ķ����������� */
	private MyAnimationListener startAnimationListener = new MyAnimationListener() {

		@Override
		public void onAnimationEnd(Animation arg0) {
			String tvStr = tv_popup.getText().toString();
			if (tvStr.equals("3")) {
				tv_popup.startAnimation(alphaAnimation);// ִ�н�����ʧ����
			} else if (tvStr.equals("2")) {
				tv_popup.startAnimation(alphaAnimation);// ִ�н�����ʧ����
			} else if (tvStr.equals("1")) {
				tv_popup.startAnimation(alphaAnimation);// ִ�н�����ʧ����
			} else if (tvStr.equals("GO!")) {
				mPopupWindow.dismiss();// �رնԻ���
				// tv_popup.startAnimation(alphaAnimation);// ִ�н�����ʧ����
				/* ��ת����λҳ */
				startActivity(new Intent(ThreeTwoOneGoActivity.this, MainActivity_gps.class));
			}
		}

	};
	/** ����ʱÿ������������1��2��3�����е��޵Ľ��䶯���������� */
	private MyAnimationListener endAnimationListener = new MyAnimationListener() {

		@Override
		public void onAnimationEnd(Animation arg0) {
			String tvStr = tv_popup.getText().toString();
			if (tvStr.equals("3")) {
				tv_popup.setText("2");
				tv_popup.startAnimation(scaleSmallToBigAnimation);// ��С���󶯻�
			} else if (tvStr.equals("2")) {
				tv_popup.setText("1");
				tv_popup.startAnimation(scaleSmallToBigAnimation);// ��С���󶯻�
			} else if (tvStr.equals("1")) {
				tv_popup.setText("GO!");
				tv_popup.startAnimation(scaleBigToSmallAnimation);// ��С���󶯻�
			} else if (tvStr.equals("GO!")) {
				
			}
		}

	};

	/** ������ʼ��ť���� */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			arg0.setVisibility(View.GONE);
			Log.i(TAG, "==��������");
			LayoutInflater layoutInflater = LayoutInflater.from(ThreeTwoOneGoActivity.this);
			View popupWindow = layoutInflater.inflate(R.layout.popup_window, null);
			tv_popup = (TextView) popupWindow.findViewById(R.id.tv_popup);
			tv_popup.setText("3");
			// ����һ��PopupWindow
			// ����1��contentView ָ��PopupWindow������
			// ����2��width ָ��PopupWindow��width
			// ����3��height ָ��PopupWindow��height
			mPopupWindow = new PopupWindow(popupWindow, 400, 400);
			mPopupWindow.showAsDropDown(button_go);// ���Button��ʾ
			// mPopupWindow.showAtLocation(ThreeTwoOneGoActivity.this.getWindow().getDecorView(),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // ����layout��PopupWindow����ʾ��λ��
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

	/* ================�ӹ���================ */
	/**
	 * ��ʼ����ز���
	 */
	private void initView() {
		/* �����沼�� */
		setContentView(R.layout.activity_go);
		/* ��ʼ��ť��ʼ�� */
		button_go = (Button) findViewById(R.id.button_go);
		button_go.setOnClickListener(mOnClickListener);
	}

	/**
	 * ��ʼ���������
	 */
	private void initData() {
		/* ��С�������Ŷ�����ʼ�� */
		scaleSmallToBigAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
		scaleSmallToBigAnimation.setDuration(500);
		scaleSmallToBigAnimation.setAnimationListener(startAnimationListener);
		/* �ɴ�С���Ŷ�����ʼ�� */
		scaleBigToSmallAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f);
		scaleBigToSmallAnimation.setDuration(500);
		scaleBigToSmallAnimation.setAnimationListener(startAnimationListener);
		/* ���е��޶��� */
		alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
		alphaAnimation.setDuration(500);// ���ö���ʱ��
		alphaAnimation.setAnimationListener(endAnimationListener);
	}

	/* ============�ڲ���================ */
	/**
	 * Ϊ��ʡ����
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
