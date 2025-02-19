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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public abstract class FtileDecorate extends AbstractTextBlock implements Ftile {

	final private Ftile ftile;

	public FtileDecorate(final Ftile ftile) {
		this.ftile = ftile;
	}

	@Override
	public String toString() {
		return "" + getClass() + " " + ftile;
	}

	@Override
	public LinkRendering getOutLinkRendering() {
		return ftile.getOutLinkRendering();
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return ftile.getInLinkRendering();
	}

	@Override
	public void drawU(UGraphic ug) {
		ftile.drawU(ug);
	}

	@Override
	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		return ftile.calculateDimension(stringBounder);
	}

	@Override
	public Collection<Connection> getInnerConnections() {
		return ftile.getInnerConnections();
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		return ftile.getSwimlanes();
	}

	@Override
	public Swimlane getSwimlaneIn() {
		return ftile.getSwimlaneIn();
	}

	@Override
	public Swimlane getSwimlaneOut() {
		return ftile.getSwimlaneOut();
	}

	@Override
	public ISkinParam skinParam() {
		return ftile.skinParam();
	}

	@Override
	public UStroke getThickness(Style style) {
		return ftile.getThickness(style);
	}

	protected final Ftile getFtileDelegated() {
		return ftile;
	}

	@Override
	public List<WeldingPoint> getWeldingPoints() {
		return ftile.getWeldingPoints();
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == ftile)
			return new UTranslate();

		return ftile.getTranslateFor(child, stringBounder);
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		if (this == ftile)
			throw new IllegalStateException();

		return Collections.singleton(ftile);
	}

	@Override
	public HorizontalAlignment arrowHorizontalAlignment() {
		return ftile.arrowHorizontalAlignment();
	}

}
