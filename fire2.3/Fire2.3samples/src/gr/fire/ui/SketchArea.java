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

/**
 * 
 */
package gr.fire.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import gr.fire.core.Component;
import gr.fire.core.FireScreen;

/**
 * @author padeler
 *
 */
public class SketchArea extends Component 
{
	
	private Image area;
	private int POINT_BUF_LENGTH=18; // number of points in buffer
	private int[] pointBuf = new int[POINT_BUF_LENGTH*2]; 
	private int pointsPointer=0;
	
	private void add(int x,int y)
	{
		if(pointsPointer==POINT_BUF_LENGTH)
		{
			flush();
		}
		pointBuf[pointsPointer*2] = x;
		pointBuf[pointsPointer*2 +1 ] = y;
		pointsPointer++;
	}
	
	public void validate()
	{
		int[]ps = getPrefSize();
		if(ps==null) ps = getMinSize();
		if(width<=0 || height<=0)
		{
			width = ps[0];
			height = ps[1];
		}

		area = Image.createImage(width,height);
		clear();
		valid=true;		
	}
	
	public int[] getMinSize()
	{
		FireScreen fs = FireScreen.getScreen();
		return new int[]{fs.getWidth(),fs.getHeight()};
	}
	
	public void clear()
	{
		Graphics g = area.getGraphics();		
		g.setColor(backgroundColor);
		g.fillRect(0,0,width,height);
		pointsPointer=0;
		repaint();
	}
	
	public Image getSketch()
	{
		return area;
	}
	
	
	public void paint(Graphics g)
	{
		g.drawImage(area,0,0,Graphics.TOP|Graphics.LEFT);
	}
	
	public boolean isFocusable()
	{
		return true;
	}
	
	protected void keyReleased(int keyCode)
	{
		setSelected(!isSelected());
	}
	
	protected void pointerDragged(int x, int y)
	{
		pointerEvent(x,y);
	}
	
	protected void pointerReleased(int x, int y)
	{
		flush();
		pointerEvent(x,y);
	}
	
	protected void pointerPressed(int x, int y)
	{
		flush();
		pointerEvent(x,y);
	}
	
	private void flush()
	{	
		if(pointsPointer>=2) // at least two points
		{
			Graphics g = area.getGraphics();
			g.setColor(foregroundColor);
			for(int i=0;i<pointsPointer-2;i++)
			{
				g.drawLine(pointBuf[i*2],pointBuf[i*2+1],pointBuf[i*2+2],pointBuf[i*2+3]);
				g.drawLine(pointBuf[i*2]+1,pointBuf[i*2+1],pointBuf[i*2+2]+1,pointBuf[i*2+3]);
			}
			repaint();	
		}
		pointsPointer=0;
	}
	
	protected void pointerEvent(int x, int y)
	{
		if(isDublicate(x,y)==false)
		{
			add(x,y);
		}
	}
	
	private boolean isDublicate(int x,int y)
	{
		if(pointsPointer>0 && pointBuf[(pointsPointer-1)*2]==x && pointBuf[(pointsPointer-1)*2+1]==y)
		{
			return true;
		}
		return false;
	}
}
