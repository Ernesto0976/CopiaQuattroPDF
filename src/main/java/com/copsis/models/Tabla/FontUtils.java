package com.copsis.models.Tabla;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


public class FontUtils {
	//private final static Logger LOGGER = LoggerFactory.getLogger(FontUtils.class);

		private static final class FontMetrics {
			private final float ascent;

			private final float descent;

			private final float height;

			public FontMetrics(final float height, final float ascent, final float descent) {
				this.height = height;
				this.ascent = ascent;
				this.descent = descent;
			}
		}

		/**
		 * <p>
		 * {@link HashMap} for caching {@link FontMetrics} for designated
		 * {@link PDFont} because {@link FontUtils#getHeight(PDFont, float)} is
		 * expensive to calculate and the results are only approximate.
		 */
		private static final Map<String, FontMetrics> fontMetrics = new HashMap<>();

		private FontUtils() {
		}

		/**
		 * <p>
		 * Loads the {@link PDType0Font} to be embedded in the specified
		 * {@link PDDocument}.
		 * </p>
		 * 
		 * @param document
		 *            {@link PDDocument} where fonts will be loaded
		 * @param fontPath
		 *            font path which will be loaded
		 * @return The read {@link PDType0Font}
		 * @throws IOException
		 *             If reading the font file fails
		 */
		public static final PDType0Font loadFont(PDDocument document, String fontPath) throws IOException {
			return PDType0Font.load(document, FontUtils.class.getClassLoader().getResourceAsStream(fontPath));
		}

		/**
		 * <p>
		 * Retrieving {@link String} width depending on current font size. The width
		 * of the string in 1/1000 units of text space.
		 * </p>
		 * 
		 * @param font
		 *            The font of text whose width will be retrieved
		 * @param text
		 *            The text whose width will be retrieved
		 * @param fontSize
		 *            The font size of text whose width will be retrieved
		 * @return text width
		 */
		public static float getStringWidth(final PDFont font, final String text, final float fontSize) {
			try {
				return font.getStringWidth(text) / 1000 * fontSize;
			} catch (final IOException e) {
				// turn into runtime exception
				throw new IllegalStateException("Unable to determine text width", e);
			}
		}

		/**
		 * <p>
		 * Calculate the font ascent distance.
		 * </p>
		 * 
		 * @param font
		 *            The font from which calculation will be applied
		 * @param fontSize
		 *            The font size from which calculation will be applied
		 * @return Positive font ascent distance
		 */
		public static float getAscent(final PDFont font, final float fontSize) {
			final String fontName = font.getName();
			if (!fontMetrics.containsKey(fontName)) {
				try {
					createFontMetrics(font);
				} catch (final IOException e) {
					//LOGGER.info("Getting font ascent distance ...");
					return font.getFontDescriptor().getAscent() / 1000 * fontSize;
				}
			}

			return fontMetrics.get(fontName).ascent * fontSize;
		}

		/**
		 * <p>
		 * Calculate the font descent distance.
		 * </p>
		 * 
		 * @param font
		 *            The font from which calculation will be applied
		 * @param fontSize
		 *            The font size from which calculation will be applied
		 * @return Negative font descent distance
		 */
		public static float getDescent(final PDFont font, final float fontSize) {
			final String fontName = font.getName();
			if (!fontMetrics.containsKey(fontName)) {
				try {
					createFontMetrics(font);
				} catch (final IOException e) {
					//LOGGER.info("Getting font descent distance ...");
					return font.getFontDescriptor().getDescent() / 1000 * fontSize;
				}
			}

			return fontMetrics.get(fontName).descent * fontSize;
		}

		/**
		 * <p>
		 * Calculate the font height.
		 * </p>
		 * 
		 * @param font
		 *            {@link PDFont} from which the height will be calculated.
		 * @param fontSize
		 *            font size for current {@link PDFont}.
		 * @return {@link PDFont}'s height
		 */
		public static float getHeight(final PDFont font, final float fontSize) {
			final String fontName = font.getName();
			if (!fontMetrics.containsKey(fontName)) {
				try {
					createFontMetrics(font);
				} catch (final IOException e) {
					//LOGGER.info("Getting font height ...");
					return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize * 0.865f;
				}
			}

			return fontMetrics.get(fontName).height * fontSize;
		}

		/**
		 * <p>
		 * Create basic {@link FontMetrics} for current font.
		 * <p>
		 * 
		 * @param font
		 *            The font from which calculation will be applied
		 * @throws IOException
		 *             If reading the font file fails
		 */
		private static void createFontMetrics(final PDFont font) throws IOException {
			final float base = font.getFontDescriptor().getXHeight() / 1000;
			final float ascent = font.getFontDescriptor().getAscent() / 1000 - base;
			final float descent = font.getFontDescriptor().getDescent() / 1000;
			fontMetrics.put(font.getName(), new FontMetrics(base + ascent - descent, ascent, descent));
		}
}
