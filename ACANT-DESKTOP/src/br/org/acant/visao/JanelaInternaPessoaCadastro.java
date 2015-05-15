package br.org.acant.visao;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import br.org.acant.modelo.Pessoa;

public class JanelaInternaPessoaCadastro extends JanelaInternaMae
										 implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel labelNome;
	private JTextField campoPessoa;

	private JLabel labelSenha01;
	private JPasswordField campoSenha01;
	private JLabel labelSenha02;
	private JPasswordField campoSenha02;

	private JButton bCadastrar;
	private JButton bCancelar;

	public JanelaInternaPessoaCadastro(JanelaPrincipal acant) {
		super("Inclusão de novo agricultor", acant);

		setBounds(50, 50, 270, 210);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEFT));

		labelNome = new JLabel("Nome");
		add(labelNome);
		campoPessoa = new JTextField("", 20);
		campoPessoa.requestFocus();
		add(campoPessoa);

		labelSenha01 = new JLabel("Digite a senha");
		add(labelSenha01);
		campoSenha01 = new JPasswordField("",20);
		campoSenha01.setEchoChar('*');
		add(campoSenha01);

		labelSenha02 = new JLabel("Redigite a senha");
		add(labelSenha02);
		campoSenha02 = new JPasswordField("",20);
		campoSenha02.setEchoChar('*');
		add(campoSenha02);

		bCadastrar = new JButton("Cadastrar");
		bCadastrar.setMnemonic('C');
		bCadastrar.addActionListener(this);
		add(bCadastrar);
		getRootPane().setDefaultButton(bCadastrar);

		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic('C');
		bCancelar.addActionListener(this);
		add(bCancelar);

		final JanelaInternaMae cadPess = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				campoPessoa.setText("");
				campoSenha01.setText("");
				campoSenha02.setText("");
				cadPess.setVisible(false);

				cadPess.acant.botaoCadastrar.setEnabled(true);
				cadPess.acant.botaoLogar.setEnabled(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == bCadastrar) {
			if (!campoPessoa.getText().trim().equals("")) {
				boolean retorno = true;
				try {
					retorno = Pessoa.consultarPessoa(campoPessoa.getText());

					if (!retorno) {
						if (getAcant().getPessoa() == null) {
							getAcant().setPessoa(new Pessoa(0, campoPessoa.getText(), null, null));
						} else {
							getAcant().getPessoa().setNome(campoPessoa.getText());
						}

						String senha01 = new String(campoSenha01.getPassword());
						String senha02 = new String(campoSenha02.getPassword());

						if (!senha01.trim().equals("") || !senha02.trim().equals("")) {
							if (!senha01.equals(senha02)) {
								JOptionPane.showInternalMessageDialog(this, "Senhas não são iguais",
										"Alerta!", JOptionPane.ERROR_MESSAGE);
								campoSenha01.requestFocus();
							} else {
								try {
									getAcant().getPessoa().setSenha(senha01);
									getAcant().getPessoa().setTipoPessoa(Pessoa.PROPRIETARIO);

									// Inclui a pessoa na base
									getAcant().getPessoa().incluir();
									
									// Obtem codigo, da base, da pessoa incluida no metodo acima
									getAcant().getPessoa().obterCodPessoa();

									getAcant().setPessoa(
											Pessoa.obterPessoa(
													getAcant().getPessoa().getNome()));

									JOptionPane.showInternalMessageDialog(this,
											"Agricultor incluído.");

									campoPessoa.setText("");
									campoSenha01.setText("");
									campoSenha02.setText("");
									setVisible(false);

									JanelaInternaPlanoConta pessoaPlanoConta =
											new JanelaInternaPlanoConta(getAcant());
									getAcant().setPlanoConta(pessoaPlanoConta);
									pessoaPlanoConta.getAcant().getPainelCentral().add(pessoaPlanoConta);
									pessoaPlanoConta.setVisible(true);
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
							}
						} else {
							JOptionPane.showInternalMessageDialog(this, "Nenhuma senha foi digitada",
									                              "Senhas", JOptionPane.ERROR_MESSAGE);
							campoSenha01.requestFocus();
						}

					} else {
						JOptionPane.showMessageDialog(this,
								"Esta pessoa já foi cadastrada.");
						campoPessoa.requestFocus();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Nada foi digitado no campo nome.");
				campoPessoa.setText("");
				campoPessoa.requestFocus();
			}
		} else if (ae.getSource() == bCancelar) {
			campoPessoa.setText("");
			campoSenha01.setText("");
			campoSenha02.setText("");
			setVisible(false);

			acant.botaoCadastrar.setEnabled(true);
			acant.botaoLogar.setEnabled(true);
		}
	}
}
