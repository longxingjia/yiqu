package com.dwnld.notify;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

import com.byron.framework.R;


/**
 * This class is singleton and manage all notifications of application
 * 
 */
public final class NotificationHelper {
	/**
	 * Notification IDs
	 */
	public static final int NOTIFICATION_ID_APP_DWNLD = 101;
	public static final int NOTIFICATION_ID_UPGRADE = 102;
	
	private static NotificationHelper instance;

	private Context context;
	private NotificationManager nm;

	// Download
	// private volatile boolean isAppDwnldRunning = false;
	private Notification dwnldNotification;
	private RemoteViews dwnldContentView;
	// private String dwnldLabel = "";

	// Upgrade
	private volatile boolean isUpgradeRunning = false;
	private Notification upgradeNotification;
	private RemoteViews upgradeContentView;

	private NotificationHelper(Context context) {
		this.context = context;
		nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static NotificationHelper getInstance(Context context) {
		// There is no need to control thread safe here
		if (instance == null) {
			instance = new NotificationHelper(context);
		}
		return instance;
	}

	/**
	 * Set up app download notification with customised layout
	 */
	public void setupDwnldNotification(final String label, int id) {
		// Initialize Notification
		int icon = android.R.drawable.stat_sys_download;
		String tickerText = "开始下载";//context.getString(R.string.ah_dwnld_ticker);
		long when = System.currentTimeMillis();
		dwnldNotification = new Notification(icon, tickerText, when);
		dwnldNotification.flags |= Notification.FLAG_ONGOING_EVENT;

		// ContentView
		dwnldContentView = new RemoteViews(context.getPackageName(), R.layout.ah_notification_dwnld);
		dwnldContentView.setTextViewText(R.id.txtLabel, label);
		dwnldContentView.setProgressBar(R.id.dwnldProgressBar, 100, 0, false);
		dwnldNotification.contentView = dwnldContentView;

		// ContentIntent
//		Intent notificationIntent = new Intent(context, ManagerActivity.class); 
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		notificationIntent.putExtra(Const.KEY_ACTIVITY_TARGET, Const.ACTIVITY_TARGET_DWNLDING);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//		dwnldNotification.contentIntent = contentIntent;

		nm.notify(id, dwnldNotification);

		// isAppDwnldRunning = true;
	}

	/**
	 * @param label
	 * @param progress
	 */
	public void updateDwnldNotification(final String label, final int progress, int id) {
		// if(!isAppDwnldRunning) {
		// throw new
		// SystemException("Please invoke setupDwnldNotification() first.");
		// }
		dwnldContentView.setTextViewText(R.id.txtLabel, label);
		dwnldContentView.setProgressBar(R.id.dwnldProgressBar, 100, progress, false);
		if (progress >= 100) {
			dwnldNotification.flags = Notification.FLAG_AUTO_CANCEL;
		}
		nm.notify(id, dwnldNotification);
		dwnldNotification.flags |= Notification.FLAG_ONGOING_EVENT;
	}

	/**
	 * Cancel download notification
	 */
	public void cancelDwnldNotification(int id) {
		nm.cancel(id);
		// isAppDwnldRunning = false;
	}

	/**
	 * Get current label for download notification
	 * 
	 * @return
	 */
	// public String getDwnldLabel() {
	// return dwnldLabel;
	// }

	// public boolean isAppDwnldNotificationShowing() {
	// return isAppDwnldRunning;
	// }

	/**
	 * Set up upgrade notification with customised layout
	 */
	public void setupUpgradeNotification() {
		// Initialize Notification
		int icon = android.R.drawable.stat_sys_download;
		String tickerText = "开始下载";//context.getString(R.string.ah_dwnld_ticker);
		long when = System.currentTimeMillis();
		upgradeNotification = new Notification(icon, tickerText, when);
		upgradeNotification.flags |= Notification.FLAG_ONGOING_EVENT;

		// ContentView
		upgradeContentView = new RemoteViews(context.getPackageName(), R.layout.ah_notification_dwnld);
		upgradeContentView.setTextViewText(R.id.txtLabel, "下载中…");//context.getString(R.string.ah_dwnlding));
		upgradeContentView.setProgressBar(R.id.dwnldProgressBar, 100, 0, false);
		upgradeNotification.contentView = upgradeContentView;

		// ContentIntent
		// Intent notificationIntent = new Intent(context,
		// WelcomeActivity.class);
		// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// notificationIntent.putExtra(Const.KEY_IS_UPGRADING, true);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, null, 0);
		upgradeNotification.contentIntent = contentIntent;

		nm.notify(NOTIFICATION_ID_UPGRADE, upgradeNotification);

		isUpgradeRunning = true;
	}

	/**
	 * @param label
	 * @param progress
	 */
	public void updateUpgradeNotification(final String label, final int progress) {
		if (!isUpgradeRunning) {
//			throw new SystemException("Please invoke setupUpgradeNotification() first.");
		}

		if (label != null) {
			upgradeContentView.setTextViewText(R.id.txtLabel, label);
		}
		upgradeContentView.setProgressBar(R.id.dwnldProgressBar, 100, progress, false);
		if (progress >= 100) {
			upgradeNotification.flags = Notification.FLAG_AUTO_CANCEL;
		}
		nm.notify(NOTIFICATION_ID_UPGRADE, upgradeNotification);
	}

	/**
	 * Cancel download notification
	 */
	public void cancelUpgradeNotification() {
		nm.cancel(NOTIFICATION_ID_UPGRADE);
		isUpgradeRunning = false;
	}

	public boolean isUpgradeNotificationShowing() {
		return isUpgradeRunning;
	}

}
