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

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class SentenceAndAnd implements Sentence {

	private final SentenceSimple sentence1;
	private final SentenceSimple sentence2;
	private final SentenceSimple sentence3;

	public SentenceAndAnd(SentenceSimple sentence1, SentenceSimple sentence2, SentenceSimple sentence3) {
		this.sentence1 = sentence1;
		this.sentence2 = sentence2;
		this.sentence3 = sentence3;
	}

	public IRegex toRegex() {
		return new RegexConcat(//
				RegexLeaf.start(), //
				sentence1.subjectii.toRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				sentence1.getVerbRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				sentence1.complementii.toRegex("1"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("and"), //
				RegexLeaf.spaceOneOrMore(), //
				sentence2.getVerbRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				sentence2.complementii.toRegex("2"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("and"), //
				RegexLeaf.spaceOneOrMore(), //
				sentence3.getVerbRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				sentence3.complementii.toRegex("3"), //
				RegexLeaf.end());
	}

	public final CommandExecutionResult execute(GanttDiagram project, RegexResult arg) {
		final Failable<? extends Object> subject = sentence1.subjectii.getMe(project, arg);
		if (subject.isFail()) {
			return CommandExecutionResult.error(subject.getError());
		}
		final Failable<? extends Object> complement1 = sentence1.complementii.getMe(project, arg, "1");
		if (complement1.isFail()) {
			return CommandExecutionResult.error(complement1.getError());
		}
		final CommandExecutionResult result1 = sentence1.execute(project, subject.get(), complement1.get());
		if (result1.isOk() == false) {
			return result1;
		}
		final Failable<? extends Object> complement2 = sentence2.complementii.getMe(project, arg, "2");
		if (complement2.isFail()) {
			return CommandExecutionResult.error(complement2.getError());
		}
		final CommandExecutionResult result2 = sentence2.execute(project, subject.get(), complement2.get());
		if (result2.isOk() == false) {
			return result2;
		}
		final Failable<? extends Object> complement3 = sentence3.complementii.getMe(project, arg, "3");
		if (complement3.isFail()) {
			return CommandExecutionResult.error(complement3.getError());
		}
		final CommandExecutionResult result3 = sentence3.execute(project, subject.get(), complement3.get());
		return result3;

	}

}
