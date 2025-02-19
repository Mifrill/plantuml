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
package net.sourceforge.plantuml.sudoku;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.klimt.URectangle;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.svg.LengthAdjust;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.ugraphic.tikz.UGraphicTikz;

public class GraphicsSudoku {

	private final ISudoku sudoku;
	private final UFont numberFont = UFont.sansSerif(20).bold();
	private final UFont font = UFont.sansSerif(11);

	public GraphicsSudoku(ISudoku sudoku) {
		this.sudoku = sudoku;
	}

	// ::comment when CORE
	public ImageData writeImageEps(OutputStream os) throws IOException {
		final UGraphicEps ug = new UGraphicEps(HColors.WHITE, ColorMapper.IDENTITY,
				FileFormat.EPS_TEXT.getDefaultStringBounder(), EpsStrategy.WITH_MACRO_AND_TEXT);
		drawInternal(ug);
		os.write(ug.getEPSCode().getBytes());
		return ImageDataSimple.ok();
	}

	public ImageData writeImageLatex(OutputStream os, FileFormat fileFormat) throws IOException {
		final UGraphicTikz ug = new UGraphicTikz(HColors.WHITE, ColorMapper.IDENTITY,
				FileFormat.LATEX.getDefaultStringBounder(), 1, fileFormat == FileFormat.LATEX);
		drawInternal(ug);
		ug.writeToStream(os, null, -1); // dpi param is not used
		return ImageDataSimple.ok();
	}
	// ::done

	public ImageData writeImageSvg(OutputStream os) throws IOException {
		final UGraphicSvg ug = new UGraphicSvg(HColors.WHITE, true, new XDimension2D(0, 0), ColorMapper.IDENTITY, false,
				1.0, null, null, 0, "none", FileFormat.SVG.getDefaultStringBounder(), LengthAdjust.defaultValue(),
				false);
		drawInternal(ug);
		ug.writeToStream(os, null, -1); // dpi param is not used
		return ImageDataSimple.ok();
	}

	public ImageData writeImagePng(OutputStream os) throws IOException {
		final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();
		final EmptyImageBuilder builder = new EmptyImageBuilder(null, sudoWidth, sudoHeight + textTotalHeight,
				Color.WHITE, stringBounder);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g3d = builder.getGraphics2D();

		final UGraphic ug = new UGraphicG2d(HColors.WHITE, ColorMapper.IDENTITY, stringBounder, g3d, 1.0);

		drawInternal(ug);
		g3d.dispose();
		PngIO.write(im, ColorMapper.IDENTITY, os, null, 96);
		return new ImageDataSimple(im.getWidth(), im.getHeight());
	}

	final private int xOffset = 5;
	final private int yOffset = 5;

	final private int cellWidth = 30;
	final private int cellHeight = 32;

	final private int numberxOffset = 10;
	final private int numberyOffset = 5;

	final private int textTotalHeight = 50;

	final private int boldWidth = 3;
	final private int sudoHeight = 9 * cellHeight + 2 * yOffset + boldWidth;
	final private int sudoWidth = 9 * cellWidth + 2 * xOffset + boldWidth;

	private void drawInternal(UGraphic ug) {
		ug = ug.apply(new UTranslate(xOffset, yOffset));

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				final int num = sudoku.getGiven(x, y);
				if (num > 0) {
					final TextBlock text = Display.create("" + num).create(FontConfiguration.blackBlueTrue(numberFont),
							HorizontalAlignment.CENTER, new SpriteContainerEmpty());
					text.drawU(ug
							.apply(new UTranslate((numberxOffset + x * cellWidth), (numberyOffset + y * cellHeight))));
				}
			}
		}

		ug = ug.apply(HColors.BLACK.bg()).apply(HColors.none());
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			ug.apply(UTranslate.dy(i * cellHeight)).draw(new URectangle(9 * cellWidth + boldWidth, w));
		}
		for (int i = 0; i < 10; i++) {
			final boolean bold = i % boldWidth == 0;
			final int w = bold ? boldWidth : 1;
			ug.apply(UTranslate.dx(i * cellWidth)).draw(new URectangle(w, 9 * cellHeight + boldWidth));
		}

		ug = ug.apply(UTranslate.dy(sudoHeight));
		final List<String> texts = new ArrayList<>();
		texts.add("http://plantuml.com");
		texts.add("Seed " + Long.toString(sudoku.getSeed(), 36));
		texts.add("Difficulty " + sudoku.getRatting());
		final TextBlock textBlock = Display.create(texts).create(FontConfiguration.blackBlueTrue(font),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		textBlock.drawU(ug);
	}

}
