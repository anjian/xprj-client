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

import gr.fire.browser.Browser;
import gr.fire.browser.util.Command;
import gr.fire.browser.util.Page;
import gr.fire.browser.util.PageListener;
import gr.fire.core.BoxLayout;
import gr.fire.core.CommandListener;
import gr.fire.core.Component;
import gr.fire.core.Container;
import gr.fire.core.FireScreen;
import gr.fire.core.KeyListener;
import gr.fire.core.Panel;
import gr.fire.core.SplashScreen;
import gr.fire.ui.Alert;
import gr.fire.ui.FireTheme;
import gr.fire.ui.InputComponent;
import gr.fire.ui.SpriteAnimation;
import gr.fire.ui.TextComponent;
import gr.fire.util.Lang;
import gr.fire.util.Log;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


/**
 * This application demonstrates the use of the Browser to access remote and local web pages.
 * You can also find code samples on:
 * <ul>
 * <li>the use of Alerts and the FireScreen.showAlert() method.</li>
 * <li>How to create and display custom animations.</li>
 * <li>How to use the Console</li>
 * <li>How to show a popup menu</li>
 * <li>How to show a splash screen</li>
 * <li>How to use the gr.fire.browser.util.Command class to load pages</li> 
 * </ul>
 * 
 *  
 * @see SpriteAnimation
 * @see SplashScreen
 * @see Console
 * 
 * @author padeler
 *
 */
public class BrowserTest extends MIDlet implements CommandListener, PageListener,KeyListener
{
	private Console console; 
	private Command consoleCmd = new Command(Lang.get("Console"),Command.OK,1); 
	private Command menuCmd = new Command(Lang.get("Menu"),Command.OK,1);
	private Command mainCmd = new Command(Lang.get("Main"),Command.OK,1);
	
	private Command exitCmd = new Command(Lang.get("Exit"),Command.OK,1);
	private Command closeMenuCmd = new Command(Lang.get("Close"),Command.OK,1);
	private Command cancelCmd = new Command(Lang.get("Cancel"),Command.OK,1);
	
	private Browser b;
	private FireScreen fireScreen;
	
	

	protected void pauseApp()
	{
	}

	protected void startApp() throws MIDletStateChangeException
	{
		try{
			// First create a splash screen.
			// Displaying a slash screen apart from been a good practice to notify the user 
			// that the application is loading, also helps to avoid an annoing bug on some phones 
			// that cannot switch to fullscreen canvas if there not a canvas already on the screen
			Display disp = Display.getDisplay(this);

			SplashScreen splash = new SplashScreen();
			splash.setBgColor(0x00FFFFFF);
			splash.setFgColor(0x00395f79);
			try
			{
				Image logo = Image.createImage(this.getClass().getResourceAsStream("/icon.png"));
				splash.setLogo(logo);
			} catch (Exception e)
			{ // failed to set image as logo, set text.
				splash.setTitle("Fire Browser");
			}
			disp.setCurrent(splash);
			
			// now continue with the application initialization.
			fireScreen = FireScreen.getScreen(Display.getDisplay(this));
			fireScreen.setFullScreenMode(true);	
			
			// set the theme. The theme can be stored either locally or remotely accesible via http
			FireScreen.setTheme(new FireTheme("file://theme.properties"));
			
			b = new Browser();
			b.getHttpClient().loadCookies("testingcookies"); // load previously stored cookies. This way we can keep session info across application invocations. see the javadoc for more info.
			b.setListener(this); // i want all form and link events to come to this class
			b.setPageListener(this); // i want to be notified when a request to loadPageAsync completes. The default method that handles link and form events in the browser calls the loadPageAsync.
			
			showMainMenu(); // load the main menu.
			
			// ok so far we displayed the main menu. Now lets do something more fancy.
			
			// create an animation to run on the Screen.
			// For this examples I will use the SpriteAnimation class, created to demonstrate how to create custom animations.
			// Check the SpriteAnimation javadoc and in source comments for more details
			// The SpriteAnimation loads a png sprite and is actually a wrapper for the Sprite class inside FireScreen.
			SpriteAnimation anim = new SpriteAnimation(new Sprite(Image.createImage(this.getClass().getResourceAsStream("/sheep-anim.png")),40,29));
			anim.setPosition(fireScreen.getWidth()-140,fireScreen.getHeight()-29-30);
			anim.setAutoMoveData(-4,0,10,10,200,200,false,true);
			anim.setAutoMove(true);
			fireScreen.addComponent(anim,-1); // ZINDEX -1 will make FireScreen display the animation below the panel. 
			// This will cause the animation to apear behing the html page. You can change this value to move it above or below a component.
			// By default all components set to the FireScreen using the setCurrent method get ZINDEX=0.
			// NOTE: Adding a component on the same ZINDEX as another will remove the oldest one from the FireScreen.
			
			// Ok Display an alert. FireScreen.showAlert is a utility method for displaying alerts fast and easy.  
			fireScreen.showAlert(Lang.get("Welcome to the BrowserTest. Demonstrating the capabilities of the Fire2.3 XHTML basic component."),Alert.TYPE_INFO,Alert.USER_SELECTED_OK,null,null);

			Log.logInfo("Phones supported keyRepeated events: "+fireScreen.hasRepeatEvents());
			
			// Use a console in this sample. 
			console = new Console(Display.getDisplay(this),this,mainCmd,200); // create the instance of the Console which implements Logger 
			Log.addLogDestination(console); // add the logger to the Log destinations.
			// now each time a method from Log (logInfo, logWarn, logError, logDebug) is called the 
			// logger will also append the output to the console
			//Log.showDebug=true;
			Log.logDebug("Console initialized."); // This will only be displayed if Log.showDebug==true. 
			
			
		}catch(Throwable e)
		{
			Log.logError("Application Failed to start",e);
		}
	}
	
	private void showMainMenu()
	{
		try{
			Page p = b.loadPage("file://index.html",HttpConnection.GET,null,null); // this will load the first screen.
			Container c = p.getPageContainer(); // the page is rendered in a container
			// create a panel for the container
			Panel mainPanel = new Panel(c,Panel.VERTICAL_SCROLLBAR|Panel.HORIZONTAL_SCROLLBAR,true);
			mainPanel.setCommandListener(this);
			mainPanel.setRightSoftKeyCommand(consoleCmd);
			mainPanel.setLeftSoftKeyCommand(menuCmd);
			mainPanel.setKeyListener(this);
			fireScreen.setCurrent(mainPanel);
		}catch(Exception e) // failed to load main menu.
		{
			// show console
			fireScreen.setCurrent(console);
			// log the event.
			Log.logError("Failed to load main page.",e);
		}
	}
	
	/**
	 * This method creates a Panel that will apear as a popup menu for the application. 
	 */
	private void showPopupMenu()
	{
		Font f = FireScreen.getTheme().getFontProperty("font");
		Container menuCnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));

		// first calculate a nice size for the popup menu.
		int menuW = f.stringWidth("Longest Item"); // max allowed popup width 
		
		
		// Notice the Lang.get() calls. Lang class will return the correct language for the given screen. 
		// The language bundle can be set using the Lang.setBundle method. Check the Lang javadoc for more.
		TextComponent cmp = new TextComponent(Lang.get("Console"),menuW,3);
		cmp.setCommand(consoleCmd);
		cmp.setHightlightMode(TextComponent.HIGHLIGHT_MODE_FULL); // set the highlight effect to look like a list item rather than a link 
		cmp.setCommandListener(this); 
		cmp.setFont(f);
		menuCnt.add(cmp);

		cmp = new TextComponent(Lang.get("Start page"),menuW,3);
		cmp.setCommand(mainCmd);
		cmp.setCommandListener(this);
		cmp.setHightlightMode(TextComponent.HIGHLIGHT_MODE_FULL);
		cmp.setFont(f);
		menuCnt.add(cmp);

		cmp = new TextComponent(Lang.get("Other page"),menuW,3);
		// this menu item will load the other.html file. The browser will handle it as a link command if the URL field is set with a url.
		// NOTE: Using such a command to move from one page to the other with the default browser behaivior will not close the popup.
		// The page will be loaded o ZINDEX 0. If we want to close the popup we can either use commands like the consoleCmd above or 
		// we can overide the browser specific behaivior on the listener and check for open popups before loading the page.
		cmp.setCommand(new Command("This is a url link",Command.OK,1,"file://other.html"));
		cmp.setHightlightMode(TextComponent.HIGHLIGHT_MODE_FULL);
		cmp.setFont(f);
		cmp.setCommandListener(this);
		menuCnt.add(cmp);

		cmp = new TextComponent(Lang.get("Help"),menuW,3);
		cmp.setCommand(new Command("Another url link",Command.OK,1,"file://help.html"));
		cmp.setHightlightMode(TextComponent.HIGHLIGHT_MODE_FULL);
		cmp.setFont(f);
		cmp.setCommandListener(this);
		menuCnt.add(cmp);


		TextComponent exitCmp = new TextComponent(Lang.get("Exit"),menuW,3);
		exitCmp.setFont(f);
		exitCmp.setHightlightMode(TextComponent.HIGHLIGHT_MODE_FULL);
		exitCmp.setCommand(exitCmd);
		exitCmp.setCommandListener(this);
		menuCnt.add(exitCmp);

		int height = fireScreen.getHeight();
		int menuH = ((f.getHeight() +2 ) * menuCnt.countComponents());
		if(menuH>(height*3)/4) menuH=(3*height)/4; // max allowed popup height


		// ok the container is prepared and filled with memu items now create a Panel for it. That way the menu will also have scrollbars.
		// if you dont need scrollbars, you can just use the Container.
		Panel popup = new Panel(menuCnt, Panel.VERTICAL_SCROLLBAR, false);
		popup.setLeftSoftKeyCommand(closeMenuCmd); // when the popup is open, the right softkey will close it.
		popup.setCommandListener(this);
		popup.setShowBackground(true); // display the theme specific background behind the container. 
		popup.setBorder(true); 
		popup.setPrefSize(menuW,menuH); 
		// locate the Panel on the screen
		// add the component to the firescreen on zinex 2. Since the main panel is on zindex 0 this will add the panel above the main panel.
		fireScreen.showPopupOnLeftSoftkey(popup,2);
		// The above is equivalent to :
		// fireScreen.showPopupOnComponent(popup,fireScreen.getComponent(FireScreen.LEFT_SOFTKEY_ZINDEX), 2);
	}
	
	
	public void commandAction(javax.microedition.lcdui.Command cmd, Component c)
	{
		if(cmd==exitCmd)
		{
			fireScreen.removeComponent(2); 
			notifyDestroyed();
			return;
		}
		if(cmd==mainCmd)
		{
			fireScreen.removeComponent(2);
			showMainMenu();
			return;
		}
		if(cmd==consoleCmd)
		{
			fireScreen.removeComponent(2); 
			fireScreen.setCurrent(console);
			return;
		}
		if(cmd==closeMenuCmd)
		{
			fireScreen.removeComponent(2); 
			return;
		}
		if(cmd==menuCmd)
		{
			showPopupMenu();
			return;
		}
		if(cmd==cancelCmd)
		{
			b.cancel();
			return;
		}
		if (c instanceof InputComponent && ((InputComponent) c).getType() == InputComponent.SUBMIT)
		{
			((gr.fire.browser.util.Command)cmd).getForm().submit((InputComponent) c);
			return;
		}
		
		fireScreen.getCurrent().setRightSoftKeyCommand(cancelCmd);
		fireScreen.getCurrent().setLeftSoftKeyCommand(null);
		
		b.commandAction(cmd,c);
	}

	public void commandAction(javax.microedition.lcdui.Command arg0, Displayable arg1)
	{
	}

	/**
	 * This method is called by the Browser when a request made with loadPageAsync completes. 
	 */
	public void pageLoadCompleted(String url, String method, Hashtable requestParams, Page page)
	{
		// Use the of the log class
		Log.logInfo("Loading of URL["+url+"] completed.");
		fireScreen.getCurrent().setLeftSoftKeyCommand(menuCmd);
		fireScreen.getCurrent().setRightSoftKeyCommand(consoleCmd);

		b.pageLoadCompleted(url,method,requestParams,page);
	}

	/**
	 * This method is called by the Browser when a request using loadPageAsync failes with an Exception (or Error)
	 * 
	 */
	public void pageLoadFailed(String url, String method, Hashtable requestParams, Throwable error)
	{
		// Use the of the log class
		Log.logError("Loading of URL["+url+"] failed with error.",error);
		fireScreen.getCurrent().setLeftSoftKeyCommand(menuCmd);
		fireScreen.getCurrent().setRightSoftKeyCommand(consoleCmd);
		b.pageLoadFailed(url,method,requestParams,error);
	}
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		try{
			// The HttpClient will store the Cookies loaded while communicating with Http server 
			// in memory. The developer can choose to persist this cookies using the HttpClient.storeCookies() method.
			// this method will create a record-store with the given name and serialize the cookies in it.
			// Cookies stored using this method can be later loaded using the HttpClient.loadCookies() method. 
			b.getHttpClient().storeCookies("testingcookies");
			FireScreen.getScreen().destroy(); // notify firescreen that the application will close. 
			// This will also stop the animation thread.
		}catch(IOException e){
			Log.logError("Failed to store cookies",e);
		}
	}

	public void keyPressed(int code, Component src)
	{
	}

	public void keyReleased(int code, Component src)
	{
		if(code==FireScreen.KEY_STAR)
		{ // switch orientation when the star key is pressed
			int orientation = fireScreen.getOrientation();
			switch(orientation)
			{
			case FireScreen.NORMAL:
				fireScreen.setOrientation(FireScreen.LANDSCAPELEFT);
				break;
			case FireScreen.LANDSCAPELEFT:
				fireScreen.setOrientation(FireScreen.LANDSCAPERIGHT);
				break;
			case FireScreen.LANDSCAPERIGHT:
				fireScreen.setOrientation(FireScreen.NORMAL);
				break;				
			}
		}
	}

	public void keyRepeated(int code, Component src)
	{	
	}

	public void pageLoadProgress(String url,String message, byte state, int percent)
	{
		b.pageLoadProgress(url,message,state,percent); // use the default implementation for gauge handling.
		
	}
	
}
