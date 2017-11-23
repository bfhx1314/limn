package com.limn.tool.log;


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class LogDocument extends DefaultStyledDocument {
	private JTextComponent logTextArea = null;
	//显示行数
	private int lineMax = 200;

	public LogDocument(JTextComponent logTextArea) {
		this.logTextArea = logTextArea;
	}

	public LogDocument(JTextComponent logTextArea, int lineMax) {
		this.logTextArea = logTextArea;
		this.lineMax = lineMax;
	}

	public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException{

		String value = logTextArea.getText();
		int overrun = 0;
		if (value != null && value.indexOf('\n') >= 0
				&& value.split("\n").length >= lineMax) {
			overrun = value.indexOf('\n') + 1;
			super.remove(0, overrun);
		}
		logTextArea.setCaretPosition(offset - overrun);
		super.insertString(offset - overrun, s, attributeSet);
	}

}
