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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.cucadiagram.DisplayPositioned;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.svek.DecorateEntityImage;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;

public class AnnotatedWorker {

	private final Annotated annotated;
	private final ISkinParam skinParam;
	private final StringBounder stringBounder;
	private final AnnotatedBuilder builder;

	public AnnotatedWorker(Annotated annotated, ISkinParam skinParam, StringBounder stringBounder,
			AnnotatedBuilder builder) {
		this.annotated = annotated;
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
		this.builder = builder;
	}

	public TextBlockBackcolored addAdd(TextBlock result) {
		result = builder.decoreWithFrame(result);
		result = addLegend(result);
		result = addTitle(result);
		result = addCaption(result);
		result = builder.addHeaderAndFooter(result);
		return (TextBlockBackcolored) result;
	}

	public TextBlock addLegend(TextBlock original) {
		final DisplayPositioned legend = annotated.getLegend();
		if (legend.isNull())
			return original;

		return DecorateEntityImage.add(original, builder.getLegend(), legend.getHorizontalAlignment(),
				legend.getVerticalAlignment());
	}

	public TextBlock addTitle(TextBlock original) {
		final DisplayPositioned title = (DisplayPositioned) annotated.getTitle();
		if (title.isNull())
			return original;

		return DecorateEntityImage.addTop(original, builder.getTitle(), HorizontalAlignment.CENTER);
	}

	private TextBlock addCaption(TextBlock original) {
		final DisplayPositioned caption = annotated.getCaption();
		if (caption.isNull())
			return original;

		return DecorateEntityImage.addBottom(original, builder.getCaption(), HorizontalAlignment.CENTER);
	}

}
