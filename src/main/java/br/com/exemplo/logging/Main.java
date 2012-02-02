package br.com.exemplo.logging;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.Logger;

import br.com.exemplo.logging.appender.TextAreaAppender;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();

		main.init();
	}

	private JPanel panelButton;
	private JTextArea textLogArea;

	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Custom JTextArea Appender");
		setPreferredSize(new Dimension(600, 600));

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout());
		panelButton.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		textLogArea = new JTextArea();
		textLogArea.setEditable(false);
		/*
		 * Faz com que o JTextArea atualize o scroll de forma automática
		 */
		DefaultCaret caret = (DefaultCaret) textLogArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane(textLogArea);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(panelButton, BorderLayout.SOUTH);

		initLogger();

		initButtons();

		pack();

		setVisible(true);
	}

	private void initLogger() {
		TextAreaAppender appender = new TextAreaAppender(textLogArea);

		Logger.getRootLogger().addAppender(appender);

		logger.info("Log inicializado");
	}

	private void initButtons() {
		JButton execute = new JButton("Execute");
		panelButton.add(execute);
		execute.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				executeAction();

			}
		});

		JButton clearLog = new JButton("Limpar Log");
		panelButton.add(clearLog);

		clearLog.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				textLogArea.setText("");
			}
		});
	}
	
	
	/*
	 * É importante que o método seja executado dentro de uma thread para que a GUI 
	 * seja atualizada adequadamente, senão, a tela só será renderizada no final do 
	 * processo 
	 */
	public void executeAction() {
		Runnable runnable = new Runnable() {

			public void run() {
				for (int i = 0; i < 100; i++) {
					logger.info("Running " + i);
					
					try {
						//Para simular algum processamento 
						Thread.sleep(20);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
				}

				try {
					throw new RuntimeException(
							"Erro forçado para exemplificar a captura do stacktrace");
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		};

		Thread thread = new Thread(runnable);

		thread.start();
	}

}
