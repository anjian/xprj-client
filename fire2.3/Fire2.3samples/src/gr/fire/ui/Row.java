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

package gr.fire.ui;

import gr.fire.core.BoxLayout;
import gr.fire.core.CommandListener;
import gr.fire.core.Container;
import gr.fire.core.FireScreen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

/**
 * 
 * This class is demonstrating the use of the basic UI components of fire2.0 in order to 
 * create a class similar in functionallity to the Fire1.2 Row class.
 *  
 * @author padeler
 *
 */
public class Row extends Container
{
	private ImageComponent imgCmp=null;
	private TextComponent txtCmp=null;
	
	
	public Row(Image img,String txt,boolean imgFirst,int width,int layout)
	{
		super(new BoxLayout(BoxLayout.X_AXIS));
		//super(new GridLayout(1,2));
		int w = width;
		int height = 0;
		if(img!=null)
		{
			imgCmp = new ImageComponent(img,"");
			imgCmp.setLayout(layout);
			imgCmp.validate();
			height=img.getHeight();
			w -=img.getWidth();
		}
		if(txt!=null)
		{
			txtCmp = new TextComponent(txt,w);
			txtCmp.setLayout(layout);
			txtCmp.validate();
			int th = txtCmp.getContentHeight();
			if(th>height) height=th;
		}
		
		if(imgFirst)
		{
			if(imgCmp!=null)
				add(imgCmp);
			if(txtCmp!=null)
				add(txtCmp);
		}
		else
		{
			if(txtCmp!=null)
				add(txtCmp);
			if(imgCmp!=null)
				add(imgCmp);
		}
		setPrefSize(width,height);
	}
	
	/**
	 * Creates a Row with an image followed by text
	 * 
	 * @param img
	 * @param txt
	 */
	public Row(Image img,String txt)
	{
		this(img,txt,true,FireScreen.getScreen().getWidth(),FireScreen.TOP|FireScreen.LEFT);
	}
	
	/**
	 * Creates a Row with text followed by an image
	 * 
	 * @param img
	 * @param txt
	 */	
	public Row(String txt, Image img)
	{
		this(img,txt,false,FireScreen.getScreen().getWidth(),FireScreen.TOP|FireScreen.LEFT);
	}
	
	public Row(Image img)
	{
		this(img,null,false,FireScreen.getScreen().getWidth(),FireScreen.TOP|FireScreen.LEFT);
	}
	
	public Row(String txt)
	{
		this(null,txt,false,FireScreen.getScreen().getWidth(),FireScreen.TOP|FireScreen.LEFT);
	}	
	
	
	public void setCommand(Command c)
	{
		if(txtCmp!=null)
		{
			txtCmp.setCommand(c);
		}
		else if(imgCmp!=null)
		{
			imgCmp.setCommand(c);
		}
	}
	
	
	public void setCommandListener(CommandListener listener)
	{
		if(txtCmp!=null)
		{
			txtCmp.setCommandListener(listener);
		}
		else if(imgCmp!=null)
		{
			imgCmp.setCommandListener(listener);
		}
	}
}
