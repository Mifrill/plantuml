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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.awt.geom.XPoint2D;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ConnectionVerticalDown extends AbstractConnection implements ConnectionTranslatable {

	private final XPoint2D p1;
	private final XPoint2D p2;
	private final Rainbow color;
	private final TextBlock textBlock;

	public ConnectionVerticalDown(Ftile ftile1, Ftile ftile2, XPoint2D p1, XPoint2D p2, Rainbow color,
			TextBlock textBlock) {
		super(ftile1, ftile2);
		if (color.size() == 0)
			throw new IllegalArgumentException();

		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
		this.textBlock = textBlock;
	}

	public void drawU(UGraphic ug) {
		ug.draw(getSimpleSnake());
	}

	public double getMaxX(StringBounder stringBounder) {
		return getSimpleSnake().getMaxX(stringBounder);
	}

	private Snake getSimpleSnake() {
		final ISkinParam skinParam = getFtile1().skinParam();
		final Snake snake = Snake.create(skinParam, color, skinParam.arrows().asToDown()).withLabel(textBlock,
				arrowHorizontalAlignment());
		snake.addPoint(p1);
		snake.addPoint(p2);
		return snake;
	}

	@Override
	public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
		final ISkinParam skinParam = getFtile1().skinParam();
		final Snake snake = Snake.create(skinParam, color, skinParam.arrows().asToDown()).withLabel(textBlock,
				arrowHorizontalAlignment());
		final XPoint2D mp1a = translate1.getTranslated(p1);
		final XPoint2D mp2b = translate2.getTranslated(p2);
		final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
		snake.addPoint(mp1a);
		snake.addPoint(mp1a.getX(), middle);
		snake.addPoint(mp2b.getX(), middle);
		snake.addPoint(mp2b);
		ug.draw(snake);

	}

}