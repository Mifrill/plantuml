/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.flowdiagram;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.golem.Tile;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.URectangle;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ActivityBox extends AbstractTextBlock {

	private static final int CORNER = 25;
	private static final int MARGIN = 10;

	private final Tile tile;
	private final String id;
	private final String label;
	private final TextBlock tb;

	public ActivityBox(Tile tile, String id, String label) {
		this.tile = tile;
		this.id = id;
		this.label = label;
		final UFont font = UFont.serif(14);
		final FontConfiguration fc = FontConfiguration.blackBlueTrue(font);
		tb = Display.create(label).create(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	public Tile getTile() {
		return tile;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void drawU(UGraphic ug) {
		final XDimension2D dimTotal = calculateDimension(ug.getStringBounder());
		// final Dimension2D dimDesc = tb.calculateDimension(ug.getStringBounder());

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal).rounded(CORNER);
		ug = ug.apply(HColors.MY_RED);
		ug = ug.apply(HColors.MY_YELLOW.bg());
		ug.apply(new UStroke(1.5)).draw(rect);

		tb.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = tb.calculateDimension(stringBounder);

		return dim.delta((2 * MARGIN), (2 * MARGIN));
	}

}
