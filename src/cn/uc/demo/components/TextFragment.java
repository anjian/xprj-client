/**
 * Tiny.cn.uc.demo.components.TextFragment.java, 2011-4-13
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import cn.uc.tiny.Component;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.TextLayout;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class TextFragment extends Component {

	private TextLayout layout;
	private int textColor = Color.BLUEVIOLET;

	public TextFragment() {

		super();

		setAttr(Attr.HAS_BACKGROUND, false);
	}

	/** {@inheritDoc} */
	public String getClazz() {

		return "TextFrag";
	}

	/** {@inheritDoc} */
	public void setTextLayout(TextLayout aLayout) {

		layout = aLayout;
	}

	public void setTextColor(int aTextColor) {

		textColor = aTextColor;
	}

	/** {@inheritDoc} */
	public void layout() {

		if (layout != null) {
			setSize(layout.getBoundsWidth(), layout.getBoundsHeight());
			repaint(Reason.UPDATE);
		}
	}

	/** {@inheritDoc} */
	protected void paintContent(GraphicsEx aG) {
		
		if (layout != null) {
			
			aG.save(GraphicsEx.SAVE_COLOR | GraphicsEx.SAVE_TRANSLATION);
			aG.translate(x, y);
			aG.setColor(textColor);
			layout.draw(aG, width, height);
			aG.restore();
		}
	}
}
