/*
 * Fire is a fast, themable UI toolkit and xHTML/CSS renderer for mobile application 
 * and game development. It is an eye-candy alternative to the standard midp2 UI 
 * components and unlike them it produces a superior UI result on all mobile devices!
 *  
 * Copyright (C) 2006,2007,2008,2009,2010 Pashalis Padeleris (padeler at users.sourceforge.net)
 * 
 * This file is part of Fire.
 *
 * Fire is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fire is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fire.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package gr.fire.test;

import gr.fire.core.CommandListener;
import gr.fire.core.Component;
import gr.fire.core.FireScreen;
import gr.fire.ui.FireTheme;
import gr.fire.ui.SketchArea;
import gr.fire.util.Log;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * @author padeler
 *
 */
public class SketchTest extends MIDlet implements CommandListener
{
	
	// Some commands exit, exits the application
	private gr.fire.browser.util.Command exit = new gr.fire.browser.util.Command("Exit",Command.EXIT,1);
	private gr.fire.browser.util.Command clear = new gr.fire.browser.util.Command("Clear",Command.CANCEL,1);
	
	private SketchArea sketch;

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp()
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException
	{
		// initialize the firescreen using the Display 
		FireScreen screen = FireScreen.getScreen(Display.getDisplay(this));
		screen.setFullScreenMode(true); // use the fireScreen in fullscreen mode.	 
		//screen.setOrientation(FireScreen.LANDSCAPELEFT);
		try
		{ // load a theme. This is not necessery, but it improves the visual result a lot.
			FireScreen.setTheme(new FireTheme("file://theme.properties"));
		} catch (IOException e)
		{
			Log.logError("Failed to load theme.",e);
		}
		
		sketch = new SketchArea();
		sketch.setPrefSize(screen.getWidth(),screen.getHeight());
		sketch.setBackgroundColor(0x00000000);
		sketch.setForegroundColor(0xFFFFFFFF);
		sketch.setLeftSoftKeyCommand(exit);
		sketch.setRightSoftKeyCommand(clear);
		sketch.setCommandListener(this);
		screen.setCurrent(sketch);
	}

	/* (non-Javadoc)
	 * @see gr.fire.core.CommandListener#commandAction(javax.microedition.lcdui.Command, gr.fire.core.Component)
	 */
	public void commandAction(Command cmd, Component c)
	{
		if(cmd==clear)
		{
			sketch.clear();
			return;
		}
		
		if(cmd==exit)
		{
			notifyDestroyed();
		}
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command arg0, Displayable arg1)
	{
	}

}
