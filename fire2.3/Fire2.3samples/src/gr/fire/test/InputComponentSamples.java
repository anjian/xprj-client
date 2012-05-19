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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Stack;

import gr.fire.core.CommandListener;
import gr.fire.core.Component;
import gr.fire.core.Container;
import gr.fire.core.FireScreen;
import gr.fire.core.GridLayout;
import gr.fire.core.KeyListener;
import gr.fire.core.Panel;
import gr.fire.ui.FireTheme;
import gr.fire.ui.InputComponent;
import gr.fire.ui.TextArea;
import gr.fire.util.Log;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class InputComponentSamples extends MIDlet implements CommandListener
{
	private InputComponent numField; 
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		FireScreen.getScreen().destroy();
	}

	protected void pauseApp()
	{
	}

	protected void startApp() throws MIDletStateChangeException
	{
		// initialize the firescreen using the Display
		FireScreen screen = FireScreen.getScreen(Display.getDisplay(this));
		screen.setFullScreenMode(true);
		try
		{
			FireScreen.setTheme(new FireTheme("file://theme.properties"));
		} catch (IOException e)
		{
			Log.logError("Failed to load theme.",e);
		}
		
		Container cnt = new Container(new GridLayout(10,1));
		
		numField = new InputComponent(InputComponent.TEXT);
		numField.setPrefSize(screen.getWidth(),20);   
		numField.setRows(3);
		numField.setCommandListener(this);
		numField.setCommand(new Command("Input",Command.OK,1));
		
		cnt.add(numField);
		
		screen.setCurrent(new gr.fire.core.Panel(cnt,Panel.HORIZONTAL_SCROLLBAR|Panel.VERTICAL_SCROLLBAR,true));
	}
	public void commandAction(Command cmd, Component c)
	{
		if(c==numField) // alternatevly you can check for the command (cmd) here 
		{
			TextArea ta = new TextArea(numField);
			FireScreen.getScreen().setCurrent(ta);
			return;				
		}
	}
	public void commandAction(Command arg0, Displayable arg1)
	{
	}
}
