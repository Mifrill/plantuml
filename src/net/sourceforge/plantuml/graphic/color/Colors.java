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
package net.sourceforge.plantuml.graphic.color;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamColors;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Colors {

	private final Map<ColorType, HColor> map = new EnumMap<ColorType, HColor>(ColorType.class);
	private LinkStyle lineStyle = null;
	private Boolean shadowing = null;

	@Override
	public String toString() {
		return map.toString() + " " + lineStyle;
	}

	public static Colors empty() {
		return new Colors();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	private Colors copy() {
		final Colors result = new Colors();
		result.map.putAll(this.map);
		result.lineStyle = this.lineStyle;
		return result;
	}

	private Colors() {
	}

	public Colors(String data, HColorSet set, ColorType mainType) throws NoSuchColorException {
		data = StringUtils.goLowerCase(data);

		for (final StringTokenizer st = new StringTokenizer(data, "#;"); st.hasMoreTokens();) {
			final String s = st.nextToken();
			final int x = s.indexOf(':');
			if (x == -1) {
				if (s.contains(".") == false)
					map.put(mainType, set.getColor(s));

			} else {
				final String name = s.substring(0, x);
				final String value = s.substring(x + 1);
				if (name.equalsIgnoreCase("shadowing")) {
					this.shadowing = value.equalsIgnoreCase("true");
				} else {
					final ColorType key = ColorType.getType(name);
					final HColor color = set.getColor(value);
					map.put(key, color);
				}
			}
		}
		if (data.contains("line.dashed"))
			lineStyle = LinkStyle.DASHED();
		else if (data.contains("line.dotted"))
			lineStyle = LinkStyle.DOTTED();
		else if (data.contains("line.bold"))
			lineStyle = LinkStyle.BOLD();

	}

	public HColor getColor(ColorType key) {
		return map.get(Objects.requireNonNull(key));
	}

	public HColor getColor(ColorType key1, ColorType key2) {
		final HColor result = getColor(key1);
		if (result != null)
			return result;

		return getColor(key2);
	}

	public UStroke getSpecificLineStroke() {
		if (lineStyle == null)
			return null;

		return lineStyle.getStroke3();
	}

	public Colors add(ColorType type, HColor color) {
		if (color == null)
			return this;

		final Colors result = copy();
		result.map.put(type, color);
		return result;
	}

	private Colors add(ColorType colorType, Colors other) {
		final Colors result = copy();
		result.map.putAll(other.map);
		if (other.lineStyle != null)
			result.lineStyle = other.lineStyle;

		return result;
	}

	public final LinkStyle getLineStyle() {
		return lineStyle;
	}

	public ISkinParam mute(ISkinParam skinParam) {
		return new SkinParamColors(skinParam, this);
	}

	public Colors addLegacyStroke(String s) {
		final Colors result = copy();
		result.lineStyle = LinkStyle.fromString1(StringUtils.goUpperCase(Objects.requireNonNull(s)));
		return result;

	}

	public static UGraphic applyStroke(UGraphic ug, Colors colors) {
		if (colors == null) 
			return ug;
		
		if (colors.lineStyle == null) 
			return ug;
		
		return ug.apply(colors.lineStyle.getStroke3());
	}

	public Colors applyStereotype(Stereotype stereotype, ISkinParam skinParam, ColorParam param)
			throws NoSuchColorException {
		Objects.requireNonNull(stereotype);
		final ColorType colorType = Objects.requireNonNull(Objects.requireNonNull(param).getColorType());
		if (getColor(colorType) != null)
			return this;

		final Colors colors = skinParam.getColors(param, stereotype);
		return add(colorType, colors);
	}

	public Colors applyStereotypeForNote(Stereotype stereotype, ISkinParam skinParam, ColorParam... params)
			throws NoSuchColorException {
		Objects.requireNonNull(stereotype);
		Colors result = this;
		for (ColorParam param : Objects.requireNonNull(params))
			result = result.applyStereotype(stereotype, skinParam, param);

		result.shadowing = skinParam.shadowingForNote(stereotype);
		return result;
	}

	public Boolean getShadowing() {
		return shadowing;
	}

	public UStroke muteStroke(UStroke stroke) {
		if (lineStyle == null)
			return stroke;

		return lineStyle.muteStroke(stroke);
	}

}
