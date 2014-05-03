package se.jacob.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextFieldLimiter extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int limit;
	private boolean numerical;

	public TextFieldLimiter(int limit, boolean onlyNumerical) {
		this.limit = limit;
		this.numerical = onlyNumerical;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null) return;
		
		Character ch = str.charAt(str.length()-1);
		if (numerical && !Character.isDigit(ch)) {
			return;
		}
		
		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
