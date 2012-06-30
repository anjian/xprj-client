/**
 * Tiny.cn.uc.ui.ex.CommandEx.java, 2010-11-29
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import javax.microedition.lcdui.Image;

import cn.uc.T;
import cn.uc.build.Config;
import cn.uc.tiny.Resource;
import cn.uc.util.BitUtils;
import cn.uc.util.debug.Assert;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class CommandEx {

	/** Empty group, used for defensive programming. */
	public static final CommandEx[] EMPTY_GROUP = new CommandEx[0];

	// built-in pre-defined command id
	public static final int CMD_ID_BACKWARD = -1;
	public static final int CMD_ID_FOREWARD = -2;
	public static final int CMD_ID_RETURN = -3;
	public static final int CMD_ID_CANCEL = -4;
	public static final int CMD_ID_CONFIRM = -5;
	public static final int CMD_ID_START = -6;
	public static final int CMD_ID_SWITCH = -7;
	public static final int CMD_ID_SELECT = -8;

	// built-in pre-defined command group id
	public static final int CMD_ID_GROUP_MENU = -100;

	public static final int CMD_ID_USER_COMMAND_BEGIN = 0;

	// built-in pre-defined commands
	public static final CommandEx CMD_BACKWARD = new CommandEx(CMD_ID_BACKWARD,
		Resource.getText(T.CMD_BACKWARD), null);
	public static final CommandEx CMD_FOREWARD = new CommandEx(CMD_ID_FOREWARD,
		Resource.getText(T.CMD_FORWARD), null);
	public static final CommandEx CMD_RETURN = new CommandEx(CMD_ID_RETURN,
		Resource.getText(T.CMD_RETURN), null);
	public static final CommandEx CMD_CANCEL = new CommandEx(CMD_ID_CANCEL,
		Resource.getText(T.CMD_CANCEL), null);
	public static final CommandEx CMD_CONFIRM = new CommandEx(CMD_ID_CONFIRM,
		Resource.getText(T.CMD_CONFIRM), null);
	public static final CommandEx CMD_START = new CommandEx(CMD_ID_START,
		Resource.getText(T.CMD_START), null);
	public static final CommandEx CMD_SWITCH = new CommandEx(CMD_ID_SWITCH,
		Resource.getText(T.CMD_SWITCH), null);
	public static final CommandEx CMD_SELECT = new CommandEx(CMD_ID_SELECT,
		Resource.getText(T.CMD_SELECT), null);
	public static final CommandEx CMD_GROUP_MENU = new CommandEx(
		CMD_ID_GROUP_MENU, Resource.getText(T.CMD_MENU), null).asGroup();

	/**
	 * {BitSet} Flag of {@link CommandEx}
	 */
	static final class Flag {

		public static final int IS_GROUP = 0;
		public static final int IS_ENABLED = 1;
		public static final int IS_VISIBLE = 2;
		public static final int IS_SELECTED = 3;
	}

	/** Id of command */
	private final int id;

	/** Label of command */
	private String label;

	/** Attribute and state flags of command */
	private int flags;

	/** Icon of command, can show in menu or menubar */
	private Image icon;

	/**
	 * Create a user command by a text id.
	 * 
	 * @param aTxtId the id of text resource, it is unique.
	 * @return user command
	 */
	public static CommandEx create(int aTxtId) {

		return create(aTxtId, Resource.getText(aTxtId), null);
	}

	/**
	 * Create a user command.
	 * 
	 * <p>
	 * <strong>Usually the specified command id is unique, at least in a
	 * specified context, and the value need to larger than or equals to 0, and
	 * less than Short.MAX_VALUE.</strong>
	 * </p>
	 * 
	 * @param aId the specified command id, must be unique
	 * @param aLabel the label string of command
	 * @return user command
	 */
	public static CommandEx create(int aId, String aLabel) {

		return create(aId, aLabel, null);
	}

	/**
	 * Create a user command.
	 * 
	 * <p>
	 * <strong>Usually the specified command id is unique, at least in a
	 * specified context, and the value need to larger than or equals to 0, and
	 * less than Short.MAX_VALUE.</strong>
	 * </p>
	 * 
	 * @param aId the specified command id
	 * @param aLabel the label string of command
	 * @param aIcon the icon of command, null if the command do not have an icon
	 * @return user command
	 */
	public static CommandEx create(int aId, String aLabel, Image aIcon) {

		Assert.assertInRangeV1(CMD_ID_USER_COMMAND_BEGIN, aId, Short.MAX_VALUE,
			Assert.ARG);

		return new CommandEx(aId, aLabel, aIcon);
	}

	/**
	 * Create a user group of commands by a text id.
	 * 
	 * @param aTxtId the id of text resource, it is unique.
	 * @return user group of commands
	 */
	public static CommandEx createGroup(int aTxtId) {

		return createGroup(aTxtId, Resource.getText(aTxtId), null);
	}

	/**
	 * Create a user group of commands.
	 * 
	 * <p>
	 * <strong>Usually the specified group id is unique, at least in a specified
	 * context, and the value need to larger than or equals to 0, and less than
	 * Short.MAX_VALUE.</strong>
	 * </p>
	 * 
	 * @param aId the specified group id
	 * @param aLabel the label string of group
	 * @return user group of commands
	 */
	public static CommandEx createGroup(int aId, String aLabel) {

		return createGroup(aId, aLabel, null);
	}

	/**
	 * Create a user group of commands.
	 * 
	 * <p>
	 * <strong>Usually the specified group id is unique, at least in a specified
	 * context, and the value need to larger than or equals to 0, and less than
	 * Short.MAX_VALUE.</strong>
	 * </p>
	 * 
	 * @param aId the specified group id, must be unique
	 * @param aLabel the label string of group
	 * @param aIcon the icon of group, null if the group do not have an icon
	 * @return user group of commands
	 */
	public static CommandEx createGroup(int aId, String aLabel, Image aIcon) {

		Assert.assertInRangeV1(CMD_ID_USER_COMMAND_BEGIN, aId, Short.MAX_VALUE,
			Assert.ARG);

		CommandEx group = new CommandEx(aId, aLabel, aIcon);
		return group.asGroup();
	}

	private CommandEx(int aId, String aLabel, Image aIcon) {

		id = aId;
		label = aLabel;
		icon = aIcon;
	}

	private CommandEx asGroup() {

		flags = BitUtils.set(flags, Flag.IS_GROUP, true);
		return this;
	}

	public boolean isGroup() {

		return BitUtils.get(flags, Flag.IS_GROUP);
	}

	/**
	 * Get the id of command.
	 * 
	 * @return id of command
	 */
	public int getId() {

		return id;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {

		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {

		return label;
	}

	public void setIcon(Image aIcon) {

		icon = aIcon;
	}

	public Image getIcon() {

		return icon;
	}

	public void setEnabled(boolean aEnabled) {

		flags = BitUtils.set(flags, Flag.IS_ENABLED, aEnabled);
	}

	public boolean isEnabled() {

		return BitUtils.get(flags, Flag.IS_ENABLED);
	}

	public void setVisible(boolean aVisible) {

		flags = BitUtils.set(flags, Flag.IS_VISIBLE, aVisible);
	}

	public boolean isVisible() {

		return BitUtils.get(flags, Flag.IS_VISIBLE);
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("CommandEx [id=");
		sBuffer.append(id);
		sBuffer.append(", label=");
		sBuffer.append(label);
		sBuffer.append("]");
		return sBuffer.toString();
	}

	/****************************************************************/
	/*                                                              */
	/* Shortcut Manager Begin */
	/*                                                              */
	/****************************************************************/
	public static final int INVALID_SHORTCUT_INDEX = -1;

	private static final int SC_MAX_NUM = 40;
	private static final int SC_TABLE_ENTRY_LEN = 5;
	private static final int SC_TABLE_MAX_LEN = SC_MAX_NUM * SC_TABLE_ENTRY_LEN;

	private static final int SC_1ST_KEY_OFF = 0;
	private static final int SC_2ND_KEY_OFF = 1;
	private static final int SC_CMD_ID_OFF = 2;
	private static final int SC_TXT_ID_OFF = 3;
	private static final int SC_STATES_OFF = 4;

	private static final short SC_ENABLED = 1;
	private static final short SC_DISABLED = 2;

	private static final short[] gScTable = new short[SC_TABLE_MAX_LEN];
	private static int gScCursor = 0;

	public static final int addShortcut(int a1stKey, int a2ndKey, int aCmdId,
		int aTxtId) {

		Assert.assertInRangeV1(Short.MIN_VALUE, a1stKey, Short.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV1(Short.MIN_VALUE, a2ndKey, Short.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV1(Short.MIN_VALUE, aCmdId, Short.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV1(Short.MIN_VALUE, aTxtId, Short.MAX_VALUE,
			Assert.ARG);

		Assert.assertFalse(isShortcutConfilt(a1stKey, a2ndKey, aCmdId),
			Assert.ARG);

		Assert.assertLessThan(gScCursor, SC_TABLE_MAX_LEN, Assert.STATE);

		gScTable[gScCursor++] = (short) a1stKey;
		gScTable[gScCursor++] = (short) a2ndKey;
		gScTable[gScCursor++] = (short) aCmdId;
		gScTable[gScCursor++] = (short) aTxtId;
		gScTable[gScCursor++] = SC_ENABLED;

		return getShortcutCount() - 1;
	}

	public static final void changeShortcutKey(int aIdx, int aNew1stKey,
		int aNew2ndKey) {

		int old1stKey = getShortcut1stKeycode(aIdx);
		int old2ndKey = getShortcut2ndKeycode(aIdx);

		if (old1stKey == aNew1stKey && old2ndKey == aNew2ndKey)
			return;

		gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_1ST_KEY_OFF] = (short) aNew1stKey;
		gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_2ND_KEY_OFF] = (short) aNew2ndKey;

		int affectIdx = findShortcut(aNew1stKey, aNew2ndKey);

		if (affectIdx != INVALID_SHORTCUT_INDEX) {

			gScTable[affectIdx * SC_TABLE_ENTRY_LEN + SC_1ST_KEY_OFF] = (short) old1stKey;
			gScTable[affectIdx * SC_TABLE_ENTRY_LEN + SC_2ND_KEY_OFF] = (short) old2ndKey;
		}
	}

	public static final void setShortcutEnabled(int aIdx, boolean aEnabled) {

		gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_STATES_OFF] = aEnabled ? SC_ENABLED
			: SC_DISABLED;
	}

	public static final int getShortcutCount() {

		return gScCursor / SC_TABLE_ENTRY_LEN;
	}

	public static final int findShortcut(int a1stKey, int a2ndKey) {

		for (int i = 0; i < gScCursor; i += SC_TABLE_ENTRY_LEN) {

			if (gScTable[i + SC_1ST_KEY_OFF] == a1stKey
				&& gScTable[i + SC_2ND_KEY_OFF] == a2ndKey)
				return i / SC_TABLE_ENTRY_LEN;
		}

		return INVALID_SHORTCUT_INDEX;
	}

	public static final int findShortcut(int aCmdId) {

		for (int i = 0; i < gScCursor; i += SC_TABLE_ENTRY_LEN) {

			if (gScTable[i + SC_CMD_ID_OFF] == aCmdId
				&& gScTable[i + SC_1ST_KEY_OFF] != 0)
				return i / SC_TABLE_ENTRY_LEN;
		}

		return INVALID_SHORTCUT_INDEX;
	}

	public static final int getShortcutCountBy1stKey(int a1stKey) {

		int count = 0;

		for (int i = 0; i < gScCursor; i += SC_TABLE_ENTRY_LEN) {

			if (gScTable[i + SC_1ST_KEY_OFF] == a1stKey) {

				++count;
			}
		}

		return count;
	}

	public static final int[] getShortcutsBy1stKey(int a1stKey) {

		int[] scArray = new int[getShortcutCountBy1stKey(a1stKey)];

		for (int i = 0, j = 0; i < gScCursor; i += SC_TABLE_ENTRY_LEN) {

			if (gScTable[i + SC_1ST_KEY_OFF] == a1stKey) {

				scArray[j++] = i / SC_TABLE_ENTRY_LEN;
			}
		}

		// return scArray;
		return sortShortcut(scArray);
	}

	public static final int getShortcut1stKeycode(int aIdx) {

		return gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_1ST_KEY_OFF];
	}

	public static final int getShortcut2ndKeycode(int aIdx) {

		return gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_2ND_KEY_OFF];
	}

	public static final int getShortcutCommandId(int aIdx) {

		return gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_CMD_ID_OFF];
	}

	public static final String getShortcutText(int aIdx) {

		return Resource.getText(gScTable[aIdx * SC_TABLE_ENTRY_LEN
			+ SC_TXT_ID_OFF]);
	}

	public static final boolean isShortcutEnabled(int aIdx) {

		return gScTable[aIdx * SC_TABLE_ENTRY_LEN + SC_STATES_OFF] == SC_ENABLED;
	}

	private static boolean isShortcutConfilt(int a1stKey, int a2ndKey,
		int aCmdId) {

		if (findShortcut(aCmdId) != INVALID_SHORTCUT_INDEX)
			return true;

		if (a1stKey == 0 && a2ndKey == 0)
			return false;

		return findShortcut(a1stKey, a2ndKey) != INVALID_SHORTCUT_INDEX
			|| findShortcut(a1stKey, 0) != INVALID_SHORTCUT_INDEX;
	}

	private static int[] sortShortcut(int[] aShortcuts) {

		for (int i = 0, temp = 0; i < aShortcuts.length - 1; ++i) {

			for (int j = i + 1; j < aShortcuts.length; ++j) {

				if (compareShortcut2ndKey(getShortcut2ndKeycode(aShortcuts[i]),
					getShortcut2ndKeycode(aShortcuts[j]))) {

					temp = aShortcuts[i];
					aShortcuts[i] = aShortcuts[j];
					aShortcuts[j] = temp;
				}
			}
		}
		return aShortcuts;
	}

	private static boolean compareShortcut2ndKey(int a2ndKey1, int a2ndKey2) {

		if (CanvasEx.isNumberKey(a2ndKey1) && CanvasEx.isNumberKey(a2ndKey2))
			return a2ndKey1 > a2ndKey2;
		else
			return a2ndKey1 < a2ndKey2;
	}
}
