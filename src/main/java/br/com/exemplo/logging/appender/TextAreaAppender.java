package br.com.exemplo.logging.appender;

import javax.swing.JTextArea;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaAppender extends AppenderSkeleton {

	private static final String DEFAULT_PATTERN = "%d{ABSOLUTE} %5p %c{1}:%L - %m%n";
	private JTextArea jTextArea;

	public TextAreaAppender(JTextArea jTextArea) {
		if (jTextArea == null) {
			throw new IllegalArgumentException("jTextArea não pode ser nulo.");
		}

		this.jTextArea = jTextArea;
	}

	public void close() {
		// Nada a fazer nesse método
	}

	public boolean requiresLayout() {
		return true;
	}

	/*
	 * Optei por uma implementação onde o layout é opcional, se ninguém informar
	 * um layout, é criado um layout default.
	 */
	@Override
	public Layout getLayout() {
		if (layout == null) {
			layout = new PatternLayout(DEFAULT_PATTERN);
		}

		return super.getLayout();
	}

	@Override
	protected void append(LoggingEvent event) {
		String formatted = getLayout().format(event);

		jTextArea.append(formatted);

		// Se a mensagem possuí informações sobre a exceção que causou o erro
		if (event.getThrowableInformation() != null) {
			jTextArea.append(ExceptionUtils.getFullStackTrace(event
					.getThrowableInformation().getThrowable()));
		}
	}

}