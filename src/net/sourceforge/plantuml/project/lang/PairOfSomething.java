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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class PairOfSomething implements Something {

	private final Something complement1;
	private final Something complement2;

	public PairOfSomething(Something complement1, Something complement2) {
		this.complement1 = complement1;
		this.complement2 = complement2;
	}

	public Failable<? extends Object> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		final Failable<? extends Object> r1 = complement1.getMe(system, arg, "A" + suffix);
		final Failable<? extends Object> r2 = complement2.getMe(system, arg, "B" + suffix);
		if (r1.isFail()) {
			return r1;
		}
		if (r2.isFail()) {
			return r2;
		}
		final Object[] result = new Object[] { r1.get(), r2.get() };
		return Failable.ok(result);
	}

	public IRegex toRegex(String suffix) {
		final IRegex pattern1 = complement1.toRegex("A" + suffix);
		final IRegex pattern2 = complement2.toRegex("B" + suffix);
		return new RegexConcat(pattern1, new RegexLeaf("[%s]+"), pattern2);
	}

}
