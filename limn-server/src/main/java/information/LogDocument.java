package information;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class LogDocument extends PlainDocument {
	private JTextComponent logText = null;
	// 日志行数控制
	private int lineMax = 200;

	public LogDocument(JTextComponent logText) {
		this.logText = logText;
	}

	public LogDocument(JTextComponent logText, int lineMax) {
		this.logText = logText;
		this.lineMax = lineMax;
	}

	public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException{

		String value = logText.getText();
		int overrun = 0;
		if (value != null && value.indexOf('\n') >= 0
				&& value.split("\n").length >= lineMax) {
			overrun = value.indexOf('\n') + 1;
			super.remove(0, overrun);
		}
		logText.setCaretPosition(offset - overrun);
		super.insertString(offset - overrun, s, attributeSet);
	}

}
