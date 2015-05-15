package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import br.org.acant.modelo.Pessoa;
import br.org.acant.util.Calculadora;

public class JanelaPrincipal extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JToolBar barraFerramentas;

	protected JButton botaoCadastrar;
	protected JButton botaoLogar;

	private JButton botaoImprimir;
	private JButton botaoCalc;
	private JButton botaoBkp;

	private JButton botaoSair;

	private JDesktopPane painelCentral;

	private JPanel barraStatus;
	private JLabel labelBarraStatus;

	private JanelaInternaPessoaCadastro cadPess;
	private JanelaInternaPessoaLogin logPess;
	private JanelaInternaPlanoConta planoConta;

	private Calculadora calc;

	private Pessoa pessoa;

	public Calculadora getCalc() {
		return calc;
	}
	public void setCalc(Calculadora calc) {
		this.calc = calc;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public JLabel getLabelBarraStatus() {
		return labelBarraStatus;
	}
	public void setLabelBarraStatus(JLabel labelBarraStatus) {
		this.labelBarraStatus = labelBarraStatus;
	}

	public JDesktopPane getPainelCentral() {
		return painelCentral;
	}
	public void setPainelCentral(JDesktopPane painelCentral) {
		this.painelCentral = painelCentral;
	}

	public JanelaInternaPessoaCadastro getCadPess() {
		return cadPess;
	}

	public void setCadPess(JanelaInternaPessoaCadastro cadPess) {
		this.cadPess = cadPess;
	}

	public JanelaInternaPessoaLogin getLogPess() {
		return logPess;
	}

	public void setLogPess(JanelaInternaPessoaLogin logPess) {
		this.logPess = logPess;
	}

	public JanelaInternaPlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(JanelaInternaPlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	/**
	 * 
	 */
	public JanelaPrincipal() {
		setTitle("Acant");
		setBounds(300, 140, 900, 700);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		barraFerramentas = new JToolBar();
		barraFerramentas.setFloatable(false);

		botaoCadastrar = new JButton("Incluir");
		botaoCadastrar.setSize(70, 70);
		botaoCadastrar.setHorizontalTextPosition(SwingConstants.CENTER);
		botaoCadastrar.setVerticalTextPosition(SwingConstants.BOTTOM);
		botaoCadastrar.setVerticalAlignment(SwingConstants.TOP);
		botaoCadastrar.setToolTipText("Cadastrar novo agricultor");
		botaoCadastrar.setMnemonic('I');
		botaoCadastrar.setIcon(new ImageIcon(getClass().getResource("/img/pessoa.png")));
		botaoCadastrar.addActionListener(this);
		barraFerramentas.add(botaoCadastrar);

		botaoLogar = new JButton("Entrar");
		botaoLogar.setSize(70, 70);
		botaoLogar.setHorizontalTextPosition(SwingConstants.CENTER);
		botaoLogar.setVerticalTextPosition(SwingConstants.BOTTOM);
		botaoLogar.setVerticalAlignment(SwingConstants.TOP);
		botaoLogar.setToolTipText("Acessar contas do agricultor");
		botaoLogar.setMnemonic('E');
		botaoLogar.setIcon(new ImageIcon(getClass().getResource("/img/login.png")));
		botaoLogar.addActionListener(this);
		barraFerramentas.add(botaoLogar);

		barraFerramentas.addSeparator();

//		botaoImprimir = new JButton("Imprimir");
//		botaoImprimir.setSize(70, 70);
//		botaoImprimir.setHorizontalTextPosition(SwingConstants.CENTER);
//		botaoImprimir.setVerticalTextPosition(SwingConstants.BOTTOM);
//		botaoImprimir.setVerticalAlignment(SwingConstants.TOP);
//		botaoImprimir.setToolTipText("Relatórios");
//		botaoImprimir.setMnemonic('p');
//		botaoImprimir.setIcon(new ImageIcon(getClass().getResource("/img/impressora.png")));
//		botaoImprimir.addActionListener(this);
//		barraFerramentas.add(botaoImprimir);

		botaoCalc = new JButton("Calculadora");
		botaoCalc.setSize(70, 70);
		botaoCalc.setHorizontalTextPosition(SwingConstants.CENTER);
		botaoCalc.setVerticalTextPosition(SwingConstants.BOTTOM);
		botaoCalc.setVerticalAlignment(SwingConstants.TOP);
		botaoCalc.setToolTipText("Calculadora");
		botaoCalc.setMnemonic('C');
		botaoCalc.setIcon(new ImageIcon(getClass().getResource("/img/calculadora.png")));
		botaoCalc.addActionListener(this);
		barraFerramentas.add(botaoCalc);

//		botaoBkp = new JButton("Cópia");
//		botaoBkp.setSize(70, 70);
//		botaoBkp.setHorizontalTextPosition(SwingConstants.CENTER);
//		botaoBkp.setVerticalTextPosition(SwingConstants.BOTTOM);
//		botaoBkp.setVerticalAlignment(SwingConstants.TOP);
//		botaoBkp.setToolTipText("Cópia de segurança");
//		botaoBkp.setMnemonic('ó');
//		botaoBkp.setIcon(new ImageIcon(getClass().getResource("/img/backup.png")));
//		botaoBkp.addActionListener(this);
//		barraFerramentas.add(botaoBkp);

		barraFerramentas.addSeparator();

		botaoSair = new JButton("Sair");
		botaoSair.setSize(70, 70);
		botaoSair.setHorizontalTextPosition(SwingConstants.CENTER);
		botaoSair.setVerticalTextPosition(SwingConstants.BOTTOM);
		botaoSair.setVerticalAlignment(SwingConstants.TOP);
		botaoSair.setToolTipText("Fechar programa");
		botaoSair.setMnemonic('S');
		botaoSair.setIcon(new ImageIcon(getClass().getResource("/img/sair.png")));
		botaoSair.addActionListener(this);
		barraFerramentas.add(botaoSair);

		add(barraFerramentas, BorderLayout.NORTH);

		painelCentral = new JDesktopPane() {
			private static final long serialVersionUID = 1L;

			Image img = obterImagem("/img/acant.png");
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				int x = ( this.getWidth() - img.getWidth(null)) / 2;  
			    int y = ( this.getHeight() - img.getHeight(null)) / 2;  
			    g.drawImage (img, x, y, this);
			    
			}
		};
		painelCentral.setBackground(Color.WHITE);
		add(painelCentral, BorderLayout.CENTER);

		barraStatus = new JPanel();
		barraStatus.setLayout(new FlowLayout(FlowLayout.RIGHT));
		labelBarraStatus = new JLabel(" ");
		barraStatus.add(labelBarraStatus);
		add(barraStatus, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sairPrograma();
			}
		});
	}

	/**
	 * 
	 */
	private void sairPrograma() {
		int retornoDialog = JOptionPane.showConfirmDialog(this,
				"Deseja realmente fechar este programa?",
				"Acant", JOptionPane.YES_NO_OPTION);
		if (retornoDialog == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == botaoSair) {
			sairPrograma();
		} else if (ae.getSource() == botaoCadastrar) {
			botaoCadastrar.setEnabled(false);
			botaoLogar.setEnabled(false);
			
			if (logPess != null) {
				logPess.setVisible(false);
			}
			if (planoConta != null) {
				planoConta.setVisible(false);
				labelBarraStatus.setText("Acant");
			}

			if (cadPess == null) {
				cadPess = new JanelaInternaPessoaCadastro(this);
				painelCentral.add(cadPess);
			}
			cadPess.setVisible(true);
		} else if (ae.getSource() == botaoLogar) {
			botaoCadastrar.setEnabled(false);
			botaoLogar.setEnabled(false);
			
			if (cadPess != null) {
				cadPess.setVisible(false);
			}
			if (planoConta != null) {
				planoConta.setVisible(false);
				labelBarraStatus.setText("Acant");
			}

			if (logPess == null) {
				logPess = new JanelaInternaPessoaLogin(this);
				painelCentral.add(logPess);
			}
			logPess.setVisible(true);
		} else if (ae.getSource() == botaoCalc) {
			botaoCalc.setEnabled(false);
			
			calc = new Calculadora(this);
			painelCentral.add(calc);
			calc.setVisible(true);
		}
	}

	/**
	 * @param imagem
	 * @return
	 */
	private Image obterImagem(String imagem) {
		Image img = null;
		try {
			img = ImageIO.read(this.getClass().getResource(imagem));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
