/**
 * Tiny.cn.uc.demo.SplashDemo.java, 2011-4-9
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.Resource;
import cn.uc.tiny.Window;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.Platform;
import cn.uc.util.debug.Log;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class SplashDemo extends MIDlet implements EventHandler {

	private boolean startup = false;
	private Window splash = null;
	private int progress = 0;
	private volatile boolean init = true;

	/**
	 * 
	 */
	public SplashDemo() {

	}

	/** {@inheritDoc} */
	protected void startApp() throws MIDletStateChangeException {

		if (startup) {
			CanvasEx.restoreCanvas();
			return;
		}

		startup = true;

		//Log.addTagFilter(CanvasEx.P_TAG);
		Log.addTagFilter(CanvasEx.E_TAG);
		Log.addTagFilter(CanvasEx.T_TAG);
		Log.addTagFilter(Component.P_TAG);
		Log.addTagFilter(Component.S_TAG);
		Log.addTagFilter(GraphicsEx.TAG);
		Log.addTagFilter(Animation.TAG);
		Log.addTagFilter(Motion.TAG);

		CanvasEx.initCanvas(this);
		FontEx.initializeUsedFonts(FontEx.SUPPORT_NORMAL_FONTS);
		FontEx.setDefaultFont(FontEx.getFont(FontEx.STYLE_PLAIN,
			FontEx.SIZE_MEDIUM));

		showSplash();
		return;
	}

	/** {@inheritDoc} */
	protected void pauseApp() {

	}

	/** {@inheritDoc} */
	protected void destroyApp(boolean aParamBoolean)
		throws MIDletStateChangeException {

	}

	void showSplash() {

		splash = Window.createSplash(
			Resource.getImage(Resource.LOGO_UCWEB_STARTUP, false),
			Resource.getImage(Resource.LOGO_UCWEB_VERSION, false),
			Resource.getImage(Resource.ICON_PROGRESS_ON, false),
			Resource.getImage(Resource.ICON_PROGRESS_OFF, false));

		Menu splashMenu = Menu.buildWindowMenu(null, CommandEx.CMD_CANCEL);
		splashMenu.setOwner(this);
		splash.setSplashMenu(splashMenu);
		splash.setTitle("Splash");
		splash.setId("Splash");
		splash.show();

		Runnable initRun = new Runnable() {

			public void run() {

				while (init) {
					String info = null;
					switch (progress) {

					case 0:
						info = "初始化网络...";
						break;

					case 1:
						info = "链接服务器...";
						break;

					case 2:
						info = "上传机器型号数据到服务器...";
						break;

					case 3:
						info = "等待服务器适配...";
						break;

					case 4:
						info = "从服务器下载初始化数据...";
						break;

					default:
						info = "软件初始化...";
						break;
					}

					CanvasEx.postProgressEvent(splash, progress++, info);
					Platform.sleepThread(1000);
				}
			}
		};

		new Thread(initRun).start();
	}

	/** {@inheritDoc} */
	public boolean event(Event aEv) {

		if (aEv.isCommandAction()
			&& aEv.getCommandId() == CommandEx.CMD_ID_CANCEL) {

			init = false;
			splash = null;

			CanvasEx.exit();
			return true;
		}

		return false;
	}

	/** {@inheritDoc} */
	public void keyEvent(Event aKeyEv) {
	}

	/** {@inheritDoc} */
	public void pointerEvent(Event aPtEv) {
	}

	/** {@inheritDoc} */
	public void actionEvent(Event aActEv) {
	}

	/** {@inheritDoc} */
	public void timerEvent(Event aTimerEv) {
	}

	/** {@inheritDoc} */
	public void progressEvent(Event aProgressEv) {
	}

	/** {@inheritDoc} */
	public void errorEvent(Event aErrorEv) {
	}

	/** {@inheritDoc} */
	public void sizeChanged(int aNewWidth, int aNewHeight) {
	}

	/** {@inheritDoc} */
	public void orentationChanged(int aNewOrentation) {
	}

	/** {@inheritDoc} */
	public void showNotify() {
	}

	/** {@inheritDoc} */
	public void hideNotify() {
	}

	/** {@inheritDoc} */
	public void focusGained(Event aCauseEv) {
	}

	/** {@inheritDoc} */
	public void focusLost(Event aCauseEv) {
	}

	/** {@inheritDoc} */
	public void onEventError(Throwable aErr) {
	}
}
