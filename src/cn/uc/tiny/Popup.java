/**
 * Tiny.cn.uc.ui.Popup.java, 2010-12-24
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.TextLayout;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.BitUtils;
import cn.uc.util.Platform;
import cn.uc.util.StringUtils;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class Popup extends Component {

	public static final String NAME = "Popup";
	public static final String TOAST = "Toast";
	public static final String MENU = "Menu";
	public static final String DIALOG = "Dialog";

	public static final String ERR_DIALOG = "ErrorDlg";
	public static final String WARNING_DIALOG = "WarningDlg";
	public static final String INFO_DIALOG = "InfoDlg";
	public static final String EXIT_DIALOG = "ExitDlg";

	/****************************************************************/
	/*                                                              */
	/* Toast Relative Build/Show Parameters Begin */
	/*                                                              */
	/****************************************************************/
	// layout relative parameters
	public static int gToastLeftMargin = 5;
	public static int gToastTopMargin = 5;
	public static int gToastRightMargin = 5;
	public static int gToastBottomMargin = 5;
	public static int gToastLayoutSpacing = 3;
	public static int gToastTextOptions = TextLayout.PREDEFINED_TEXT_OPTIONS_ALIGN_CENTER;

	// animation relative parameters
	public static Motion gToastMotion = Motion.createFriction(6000, 20, true);
	public static int gToastStayDuration = 3000;
	public static int gToastInOutDuration = 300;
	public static int gToastStartAnchor = GraphicsEx.TOP | GraphicsEx.OUT_BOX;
	public static int gToastStartX = 0;
	public static int gToastStartY = 0;
	public static int gToastEndAnchor = GraphicsEx.CENTER | GraphicsEx.IN_BOX;
	public static int gToastEndX = 0;
	public static int gToastEndY = 0;
	/****************************************************************/
	/*                                                              */
	/* Toast Relative Build/Show Parameters End */
	/*                                                              */
	/****************************************************************/

	/****************************************************************/
	/*                                                              */
	/* Dialog Relative Build/Show Parameters Begin */
	/*                                                              */
	/****************************************************************/
	// layout relative parameters
	public static int gDialogLeftMargin = 5;
	public static int gDialogTopMargin = 25;
	public static int gDialogRightMargin = 5;
	public static int gDialogBottomMargin = 5;
	public static int gDialogLineSpacing = 3;
	public static int gDialogTextOptions = TextLayout.PREDEFINED_TEXT_OPTIONS_ALIGN_LEFT_TOP;

	// animation relative parameters
	public static Motion gDialogMotion = gToastMotion;
	public static int gDialogStayDuration = 0;
	public static int gDialogInOutDuration = 300;
	public static int gDialogStartAnchor = GraphicsEx.HCENTER
		| GraphicsEx.BOTTOM | GraphicsEx.OUT_BOX;
	public static int gDialogStartX = 0;
	public static int gDialogStartY = 0;
	public static int gDialogEndAnchor = GraphicsEx.HCENTER | GraphicsEx.BOTTOM
		| GraphicsEx.IN_BOX;
	public static int gDialogEndX = 0;
	public static int gDialogEndY = 0;
	/****************************************************************/
	/*                                                              */
	/* Dialog Relative Build/Show Parameters End */
	/*                                                              */
	/****************************************************************/

	private TextLayout txtLayout;
	private String title;

	private final Motion motion;

	private int motionState;
	private int duration;
	private int stayTime;

	private int anchor;
	private int start;
	private int end;

	private int margins;
	private int spacing;
	private int txtOptions;

	/** The menu of Popup. */
	protected Menu menu;

	/** The font of Popup. */
	protected FontEx font = FontEx.getDefaultFont();

	public static Popup buildToast(String aText) {

		Popup toast = new Popup(TOAST, gToastMotion, gToastStayDuration,
			gToastInOutDuration, gToastStartAnchor, gToastEndAnchor,
			gToastStartX, gToastStartY, gToastEndX, gToastEndY,
			gToastLeftMargin, gToastTopMargin, gToastRightMargin,
			gToastBottomMargin, gToastLayoutSpacing, gToastTextOptions);

		toast.setAttr(Attr.FLOAT, true);// float
		toast.setAttr(Attr.TOAST, true);// toast
		toast.setAttr(Attr.TRANSPARENT_FOR_POINTER_EVENT, true);
		toast.setText(aText);

		return toast;
	}

	public static Popup buildSimpleDialog(String aId, String aTitle,
		String aText, CommandEx aLeft, CommandEx aRight) {

		Popup dialog = buildDialog(aId, aTitle,
			Menu.buildWindowMenu(aLeft, aRight));
		dialog.setText(aText);
		return dialog;
	}

	public static Popup buildSimpleDialog(String aId, String aTitle,
		String aText, Menu aMenu) {

		Popup dialog = buildDialog(aId, aTitle, aMenu);
		dialog.setText(aText);
		return dialog;
	}

	public static Popup buildComplexDialog(String aId, String aTitle,
		Component aCentralCmp, Menu aMenu) {

		Popup dialog = buildDialog(aId, aTitle, aMenu);
		dialog.addComponent(aCentralCmp);
		return dialog;
	}

	private static Popup buildDialog(String aId, String aTitle, Menu aMenu) {

		Popup dialog = new Popup(aId, gDialogMotion, gDialogStayDuration,
			gDialogInOutDuration, gDialogStartAnchor, gDialogEndAnchor,
			gDialogStartX, gDialogStartY, gDialogEndX, gDialogEndY,
			gDialogLeftMargin, gDialogTopMargin, gDialogRightMargin,
			gDialogBottomMargin, gDialogLineSpacing, gDialogTextOptions);

		dialog.setAttr(Attr.MODAL, true);// modal
		dialog.setAttr(Attr.DIALOG, true);// dialog
		dialog.setTitle(aTitle);

		dialog.menu = aMenu;
		if (!dialog.menu.hasOwner()) {
			dialog.menu.setOwner(dialog);
		}

		return dialog;
	}

	/**
	 * Create a Popup with an ID and a Motion.
	 * 
	 * @param aId id of the popup
	 * @param aMotion in/out animations motion of the popup, if it is
	 *            <code>null</code>, means the Popup do not have in/out
	 *            animations.
	 */
	protected Popup(String aId, Motion aMotion) {

		setId(aId);

		motion = aMotion != null ? aMotion.clone() : null;

		// set not opaque
		// setAttr(Attr.OPAQUE, false);

		// motion state
		setMotionState(Motion.END);
		// popable
		setAttr(Attr.POPABLE, true);
		// need calculate preferred size
		setShouldCalcPreferredSize(true);
	}

	protected Popup(String aId, Motion aMotion, int aStayDuration,
		int aInOutDuration, int aStartAnchor, int aEndAnchor, int aStartX,
		int aStartY, int aEndX, int aEndY, int aLeftMagin, int aTopMargin,
		int aRightMargin, int aBottomMargin, int aLayoutSpacing,
		int aLayoutHints) {

		this(aId, aMotion);

		setDuration(aInOutDuration, aStayDuration);

		setStartEndAnchor(aStartAnchor, aEndAnchor);
		setStartOffset(aStartX, aStartY);
		setEndOffset(aEndX, aEndY);

		setMargins(aLeftMagin, aTopMargin, aRightMargin, aBottomMargin);

		spacing = aLayoutSpacing;
		txtOptions = aLayoutHints;
	}

	/** {@inheritDoc} */
	public String getName() {

		return NAME;
	}

	/** {@inheritDoc} */
	public String getClazz() {

		if (hasAttr(Attr.TOAST)) {
			return TOAST;
		} else if (hasAttr(Attr.DIALOG)) {
			return DIALOG;
		} else if (hasAttr(Attr.MENU)) {
			return MENU;
		}

		return super.getClazz();
	}

	/** {@inheritDoc} */
	public void setText(String aText) {

		if (txtLayout == null) {

			txtLayout = new TextLayout();
		}

		txtLayout.setText(aText);
	}

	/** {@inheritDoc} */
	public String getText() {

		return txtLayout != null ? txtLayout.getText() : StringUtils.EMPTY;
	}

	/** {@inheritDoc} */
	public void setTitle(String aTitle) {

		title = aTitle;
	}

	/** {@inheritDoc} */
	public String getTitle() {

		return title;
	}

	/** {@inheritDoc} */
	public Menu getWindowMenu() {

		return menu;
	}

	/**
	 * Show the Popup with an IN animation.
	 * 
	 * @return true when the Popup can be shown, otherwise return false
	 */
	public boolean show() {

		// check window
		Window win = CanvasEx.getCurrWindow();
		if (win == null) {
			return false;
		}

		font = getFont();
		// append
		win.appendPopup(this);
		// start motion animation
		if (motion != null) {
			setMotionState(Motion.IN);
			motion.start();
			startAnimation();
		} else {
			stop();
		}

		return true;
	}

	/**
	 * Stop the Popup, put it into STAY state, and do not move anymore.
	 */
	public void stop() {

		setMotionState(Motion.STAY);
		stopAnimation();
	}

	/**
	 * Pop out the Popup with an Out animation.
	 */
	public void popout() {

		if (motion != null && getMotionState() == Motion.STAY) {

			setMotionState(Motion.OUT);
			motion.start();
			startAnimation();
		} else {

			close();
		}
	}

	/**
	 * Close the Popup immediately without out animation.
	 */
	public void close() {

		setMotionState(Motion.END);
		stopAnimation();

		// remove
		Window win = getWindow();
		if (win != null) {
			win.removePopup(this);
		}
	}

	/**
	 * Is the Popup on shown, i.e. it is in IN or STAY state.
	 * 
	 * @return true if the Popup is on shown.
	 */
	public boolean isShown() {

		return getMotionState() == Motion.STAY || getMotionState() == Motion.IN;
	}

	public boolean animate() {

		long currTime = Platform.currentTimeMillis();
		int motionState = getMotionState();

		switch (motionState) {

		case Motion.IN:
		case Motion.OUT:

			int newX = motion.getValueX(motionState, currTime);
			int newY = motion.getValueY(motionState, currTime);
			setPosition(newX, newY);

			if (motion.isFinished(currTime)) {

				if (motionState == Motion.IN) {
					// IN turn to STAY
					setMotionState(Motion.STAY);
					stayTime = Platform.currentTimeAfterStart();
				} else {
					// OUT turn to END
					setMotionState(Motion.END);
				}
			}
			break;

		case Motion.STAY:

			if (getStayDuration() <= 0) {
				// when the duration is less than or equals to zero,
				// it means to stop the popup
				stop();
			} else {
				// STAY turn to OUT, when stay duration over
				int eclipse = Platform.eclipse(currTime, stayTime);
				if (eclipse > getStayDuration()) {
					popout();
				}
			}
			break;

		case Motion.END:

			// END, close it
			close();
			break;
		}

		// always return false because the position changed already cause
		// parent repaint, do not need to repaint itself again, otherwise
		// will be duplicated
		return false;
	}

	/** {@inheritDoc} */
	public void pointerReleased(Event aPtEv) {

		super.pointerReleased(aPtEv);

		if (aPtEv.isAccepted()) {
			return;
		}

		// popout dialog when it has been clicked
		if (hasAttr(Attr.DIALOG)) {
			popout();
		}
	}

	/** {@inheritDoc} */
	public void actionEvent(Event aActEv) {

		if (aActEv.isCommandAction()) {
			
			// out
			popout();

			// repost to window
			CanvasEx.postCommandActionEvent(null, aActEv.getCommandId());
		}
	}

	protected final void setMotionState(int aMotionState) {

		motionState = aMotionState;
	}

	protected final int getMotionState() {

		return motionState;
	}

	protected final void setDuration(int aMotionInOut, int aStay) {

		duration = BitUtils.toInt(aMotionInOut, aStay);
	}

	protected final int getMotionInOutDuration() {

		return BitUtils.low(duration);
	}

	protected final int getStayDuration() {

		return BitUtils.high(duration);
	}

	protected final void setStartEndAnchor(int aStartAnchor, int aEndAnchor) {

		anchor = BitUtils.toInt(aStartAnchor, aEndAnchor);

	}

	protected final void setStartOffset(int aStartX, int aStartY) {

		start = BitUtils.toInt(aStartX, aStartY);
	}

	protected final void setEndOffset(int aEndX, int aEndY) {

		end = BitUtils.toInt(aEndX, aEndY);
	}

	protected final int getStartAnchor() {

		return BitUtils.low(anchor);
	}

	protected final int getEndAnchor() {

		return BitUtils.high(anchor);
	}

	protected final int getStartX() {

		return BitUtils.low(start);
	}

	protected final int getStartY() {

		return BitUtils.high(start);
	}

	protected final int getEndX() {

		return BitUtils.low(end);
	}

	protected final int getEndY() {

		return BitUtils.high(end);
	}

	protected final void setMargins(int aLeft, int aTop, int aRight, int aBottom) {

		margins = BitUtils.toInt(aLeft, aTop, aRight, aBottom);
	}

	protected final int getLeftMargin() {

		return BitUtils.byte1(margins);
	}

	protected final int getTopMargin() {

		return BitUtils.byte2(margins);
	}

	protected final int getRightMargin() {

		return BitUtils.byte3(margins);
	}

	protected final int getBottomMargin() {

		return BitUtils.byte4(margins);
	}

	protected final int getLineSpacing() {

		return spacing;

	}

	protected final int getTextOptions() {

		return txtOptions;
	}

	protected void initializeComponent() {

		// initialize layout, start/end position and motion
		int preferredW = getPreferredWidth();
		int preferredH = getPreferredHeight();

		int mgLeft = getLeftMargin();
		int mgTop = getTopMargin();
		int mgRight = getRightMargin();
		int mgBottom = getBottomMargin();

		if (txtLayout != null) {

			if (font != txtLayout.getFont()) {
				txtLayout.setFont(font);
			}

			if (txtLayout.getState() == TextLayout.STATE_LAYOUT_END
				&& txtLayout.getBoundsWidth() == preferredW) {
				return;// already layout and no changes happen
			}

			txtLayout.setTextOptions(getTextOptions());
			txtLayout.layout(preferredW - mgLeft - mgRight, 0);

			if (hasAttr(Attr.TOAST)) {

				// toast's width/height depends on its text layout
				preferredW = txtLayout.getBoundsWidth() + mgLeft + mgRight;
				preferredH = txtLayout.getBoundsHeight() + mgTop + mgBottom;
				setPreferredSizeInternal(preferredW, preferredH);
			}
		}

		Rectangle box = getBoxRegion();

		int srcX = GraphicsEx.getBoxX(preferredW, box.x, box.width,
			getStartX(), getStartAnchor());
		int srcY = GraphicsEx.getBoxY(preferredH, box.y, box.height,
			getStartY(), getStartAnchor());

		int dstX = GraphicsEx.getBoxX(preferredW, box.x, box.width, getEndX(),
			getEndAnchor());
		int dstY = GraphicsEx.getBoxY(preferredH, box.y, box.height, getEndY(),
			getEndAnchor());

		if (motion != null) {

			motion.setSource(srcX, srcY);
			motion.setDestination(dstX, dstY);
			motion.setDuration(getMotionInOutDuration());
			setBounds(new Rectangle(srcX, srcY, preferredW, preferredH));
		} else {
			// do not have motion, set to destination directly
			setBounds(new Rectangle(dstX, dstY, preferredW, preferredH));
		}

		if (getComponentCount() > 0) {

			Component embedded = getFirstComponent();
			if (hasAttr(Attr.DIALOG)) {
				// set dialog children's bounds
				embedded.setBounds(new Rectangle(mgLeft, mgTop, preferredW
					- mgLeft - mgRight, preferredH - mgTop - mgBottom));
			} else {
				embedded.setBounds(new Rectangle(0, 0, preferredW, preferredH));
			}
			embedded.layout();
		}
	}

	protected void calcPreferredSize() {

		int preferredW = 0;
		int preferredH = 0;

		int displayW = CanvasEx.getDisplayWidth();
		int displayH = CanvasEx.getDisplayHeight();

		if (hasAttr(Attr.TOAST)) {

			preferredW = CanvasEx.getDisplayWidth() / 2;
			preferredH = CanvasEx.getDisplayHeight() / 3;
		} else if (hasAttr(Attr.DIALOG)) {

			preferredW = CanvasEx.getDisplayWidth();
			preferredH = Math.max(displayW, displayH) * 2 / 5;
		}

		setPreferredSizeInternal(preferredW, preferredH);
	}

	protected Rectangle getBoxRegion() {

		Window win = CanvasEx.getCurrWindow();
		if (win != null) {
			return win.getPopupArea();
		}

		return CanvasEx.getDisplayBounds();
	}

	protected void paintContent(GraphicsEx aG) {

		int mgLeft = getLeftMargin();
		int mgTop = getTopMargin();
		int mgRight = getRightMargin();
		int mgBottom = getBottomMargin();

		int color = Color.SNOW;

		// draw child component or text layout
		if (getComponentCount() > 0) {
			super.paintContent(aG);
		} else if (txtLayout != null && txtLayout.getLineCount() > 0) {

			aG.save(GraphicsEx.SAVE_COLOR | GraphicsEx.SAVE_TRANSLATION);
			aG.setColor(color);
			aG.translate(x + mgLeft, y + mgTop);

			int boxW = width - mgLeft - mgRight;
			int boxH = height - mgTop - mgBottom;

			txtLayout.draw(aG, boxW, boxH);
			aG.restore();
		}

		// draw title
		if (StringUtils.isNotEmpty(title)) {

			aG.save(GraphicsEx.SAVE_COLOR | GraphicsEx.SAVE_FONT);
			aG.setColor(color);
			aG.setFont(font);

			aG.drawBoxedString(title, mgLeft, 0, x, y, width, mgTop,
				GraphicsEx.LEFT | GraphicsEx.VCENTER);

			aG.restore();
		}
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		Brush brush = Brush.createColorBrush(Color.EIGHTN_TRANSPARENT,
			Color.ORANGE);
		setAttr(Attr.OPAQUE, brush.isOpaque());
		aG.setBrush(brush);
		aG.fillRectEx(x, y, width, height);
	}

	// protected void paintBackground(GraphicsEx aG) {
	//
	// aG.setBrush(this.bgBrush);
	// aG.fillRectEx(this.getBounds());
	//
	// //int color = Brush.getGrayColor(this.bgBrush.getColor());
	// // aG.setColor(SVGColor.DEEPPINK);
	// // aG.drawRect(this.getX(), this.getY(), this.getWidth() - 1, this
	// // .getHeight() - 1);
	// // //color = Brush.getDarkerColor(color, 150);
	// // //aG.setColor(color);
	// // aG.drawRect(this.getX() + 1, this.getY() + 1, this.getWidth() - 3,
	// this
	// // .getHeight() - 3);
	// }
}
