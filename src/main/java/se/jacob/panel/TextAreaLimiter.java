package se.jacob.panel;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextAreaLimiter extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int limit;
	private boolean numerical;
	private final int MAX_LINES;

	/**
	 * 
	 * @param limit max number of lines
	 * @param maxLines must be >=1
	 * @param onlyNumerical true to only allow numerical input
	 * @throws IllegalArgumentException
	 */
	public TextAreaLimiter(int limit, int maxLines, boolean onlyNumerical) throws IllegalArgumentException {
		this.limit = limit;
		this.numerical = onlyNumerical;
		if (maxLines < 1) {
			throw new IllegalArgumentException("maxLines must be >=1");
		}
		this.MAX_LINES = maxLines;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null) return;
		
		Character ch = str.charAt(str.length()-1);
		if (numerical && !Character.isDigit(ch)) {
			return;
		}
		
		int linebreaks = 0;
		for (char z : getText(0, getLength()).toCharArray()) {
			if (z == '\n')
				linebreaks++;
		}
		if (linebreaks >= MAX_LINES)
			return;
		
		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
