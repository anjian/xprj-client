package india.xxoo.ui;

import cn.uc.tiny.Component;
import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.GraphicsEx;

public class InputBar extends Component {
	private String iInputText = "ÇëÊäÈëÎÄ×Ö";
	private StringBuffer iURLBuffer = new StringBuffer();
	int iInputOffset = 0;
	private int timer;
	int iKeyCodeLast = 0;

	short[] iKeyCount = new short[1];

	byte iInputState = 0;
	public static final byte INPUT_STATE_COMMON = 0;
	public static final byte INPUT_STATE_WAIT_CONFIRM = 1;
	public static final byte INPUT_STATE_URL_MATCH = 2;
	private static final byte INPUT_MODE_LETTER = 0;
	private static final byte INPUT_MODE_NUMBER = 1;
	private static byte gInputMode = 0;

	private static final char[][] iKeyMap = { { ' ', '0' },
			{ '.', '/', ':', '_', '?', '=', '-', '%', '&', '1' },
			{ 'a', 'b', 'c', '2' }, { 'd', 'e', 'f', '3' },
			{ 'g', 'h', 'i', '4' }, { 'j', 'k', 'l', '5' },
			{ 'm', 'n', 'o', '6' }, { 'p', 'q', 'r', 's', '7' },
			{ 't', 'u', 'v', '8' }, { 'w', 'x', 'y', 'z', '9' } };

	long lastTime = 0L;
	byte keyState = 0;
	byte iKeyResponseHandleType = -1;

	public static boolean isLetterMode() {
		return gInputMode == 0;
	}

	public static boolean isNumberMode() {
		return gInputMode == 1;
	}

	private boolean isDelKey(int aKeyCode) {
		return (aKeyCode == -8) || (aKeyCode == 127);
	}

	private void delCharProcess() {
		if ((this.iInputState == 0) || (this.iInputState == 1)) {
			if (this.iInputOffset - 1 >= 0) {
				this.iURLBuffer.deleteCharAt(this.iInputOffset - 1);
			}
			this.iInputOffset -= 1;

			if (this.iInputOffset < 0)
				this.iInputOffset = 0;
		}
	}

	public void handleKeyEvent(int keyRepressKeyCode, int keyCodeType) {
		boolean sIsKeyChanged = this.iKeyCodeLast != keyRepressKeyCode;
		this.lastTime = System.currentTimeMillis();
		int sKeyCode2 = 0;
		this.iKeyCodeLast = (sKeyCode2 = keyRepressKeyCode);
		if (handleCharacterInputKey(sKeyCode2, keyCodeType, this.iKeyCount,
				sIsKeyChanged))
			repaint(10);
	}

	private boolean handleCharacterInputKey(int sKeyCode, int keyCodeType,
			short[] sKeyCount, boolean sIsKeyChanged) {
		boolean isCharacter = false;

		if (isDelKey(sKeyCode)) {
			delCharProcess();
			return true;
		}

		if ((char) sKeyCode == '#') {
			gInputMode = (byte) (isLetterMode() ? 1 : 0);
			return true;
		}

		if ((isNumberMode()) && ((sKeyCode < 48) || (sKeyCode >= 58))) {
			return true;
		}

		if ((sKeyCode < 48) || (sKeyCode >= 58)) {
		} else {
			if ((keyCodeType == 1) || (isCharacter)) {
				if (sIsKeyChanged)
					sKeyCount[0] = 0;
				int tmp140_139 = 0;
				short[] tmp140_138 = sKeyCount;
				short tmp142_141 = tmp140_138[tmp140_139];
				tmp140_138[tmp140_139] = (short) (tmp142_141 + 1);
				char sNewChar = (isCharacter) || (isNumberMode()) ? (char) sKeyCode
						: iKeyMap[(sKeyCode - 48)][(tmp142_141 % iKeyMap[(sKeyCode - 48)].length)];
				int tmp162_161 = 0;
				short[] tmp162_160 = sKeyCount;
				tmp162_160[tmp162_161] = (short) (tmp162_160[tmp162_161] & 0x7F);
				if ((this.iInputState == 1) && (!sIsKeyChanged)
						&& (!isCharacter)) {
					this.iURLBuffer.setCharAt(this.iInputOffset - 1, sNewChar);
				} else {
					this.iURLBuffer.insert(this.iInputOffset, sNewChar);
					this.iInputOffset += 1;
				}
				this.iInputState = (byte) ((isCharacter) || (isNumberMode()) ? 0 : 1);
			} else if ((keyCodeType == 2) && (this.iInputState == 1)) {
				char sNewChar = isNumberMode() ? (char) sKeyCode
						: iKeyMap[(sKeyCode - 48)][(iKeyMap[(sKeyCode - 48)].length - 1)];
				char sCurrentChar = this.iURLBuffer
						.charAt(this.iInputOffset - 1);

				this.iURLBuffer.setCharAt(this.iInputOffset - 1, sNewChar);

				this.iInputState = (byte) (isNumberMode() ? 0 : 1);
			}

			return true;
		}
		return false;
	}

	public InputBar() {
		setAttr(16, true);
	}

	public String getClazz() {
		return "InputBar";
	}

	protected void initializeComponent() {
		this.timer = CanvasEx.findAvailableTimer();
	}

	protected void deinitializeComponent() {
		CanvasEx.closeTimer(this.timer);
	}

	public void timerEvent(Event aTimerEv) {
		if ((System.currentTimeMillis() - this.lastTime > 60L)
				&& (this.iInputState == 1) && (this.keyState != 2)) {
			this.iInputState = 0;
		}
		repaint(10);
	}

	public void repaint(int aReason) {
		if (aReason == 10) {
			this.iInputText = this.iURLBuffer.toString();
		}

		super.repaint(aReason);
	}

	protected void paintContent(GraphicsEx aG) {
		aG.drawString(this.iInputText, this.x + 5, this.y + 5, 20);
	}

	protected void paintBackground(GraphicsEx aG) {
		if (hasFocus())
			aG.setBrush(Brush.createColorBrush(-5383962));
		else {
			aG.setBrush(Brush.createColorBrush(-7876885));
		}
		aG.fillRectEx(this.x, this.y, this.width, this.height);
	}

	public void keyPressed(Event aKeyEv) {
		this.keyState = 1;
		handleKeyEvent(aKeyEv.getKeyCode(), 1);
		CanvasEx.startTimer(this.timer, this, 0);
	}

	public void keyReleased(Event aKeyEv) {
		this.keyState = 0;
	}

	public void pointerLongPressed(Event aPtEv) {
		super.pointerLongPressed(aPtEv);
	}

	public void pointerPressed(Event aPtEv) {
		super.pointerPressed(aPtEv);
	}

	public void pointerReleased(Event aPtEv) {
		super.pointerReleased(aPtEv);
	}

	public void keyRepeated(Event aKeyEv) {
		this.keyState = 1;
	}

	public String getText() {
		return this.iInputText;
	}

	public void clear() {
		this.iInputText = "";
		this.iURLBuffer.setLength(0);
		this.iInputOffset = 0;
	}
}