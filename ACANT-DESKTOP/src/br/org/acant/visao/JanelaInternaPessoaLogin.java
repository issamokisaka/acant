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

public class JanelaInternaPessoaLogin extends JanelaInternaMae
									  implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel labelNome;
	private JTextField campoNome;

	private JLabel labelSenha;
	private JPasswordField campoSenha;

	private JButton bOK;
	private JButton bCancelar;

	public JanelaInternaPessoaLogin(JanelaPrincipal acant) {
		super("Entrar", acant);

		setBounds(50, 50, 260, 160);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEFT));

		labelNome = new JLabel("Nome do agricultor");
		add(labelNome);
		campoNome = new JTextField("", 20);
		campoNome.requestFocus();
		add(campoNome);
		
		labelSenha = new JLabel("Digite a senha:");
		add(labelSenha);
		campoSenha = new JPasswordField("",20);
		campoSenha.setEchoChar('*');
		add(campoSenha);

		bOK = new JButton("OK");
		bOK.setMnemonic('O');
		bOK.addActionListener(this);
		add(bOK);
		getRootPane().setDefaultButton(bOK);

		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic('C');
		bCancelar.addActionListener(this);
		add(bCancelar);

		final JanelaInternaMae logPess = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				campoNome.setText("");
				campoSenha.setText("");
				logPess.setVisible(false);
				
				logPess.acant.botaoCadastrar.setEnabled(true);
				logPess.acant.botaoLogar.setEnabled(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == bOK) {
			if (!campoNome.getText().trim().equals("")) {
				try {
					boolean existePessoa =
							Pessoa.consultarPessoa(campoNome.getText().trim());
					if (existePessoa) {

						Pessoa pessoa = Pessoa.obterPessoa(campoNome.getText().trim());

						String senhaDigitada = new String(campoSenha.getPassword());
						if (senhaDigitada.equals(pessoa.getSenha())) {
							campoNome.setText("");
							campoSenha.setText("");
							setVisible(false);

							getAcant().setPessoa(pessoa);
							JanelaInternaPlanoConta pessoaPlanoConta =
									new JanelaInternaPlanoConta(getAcant());
							getAcant().setPlanoConta(pessoaPlanoConta);
							pessoaPlanoConta.getAcant().getPainelCentral().add(pessoaPlanoConta);
							pessoaPlanoConta.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(this,
									"Senha incorreta.");
							campoSenha.requestFocus();
						}
					} else {
						JOptionPane.showMessageDialog(this,
								"Esta pessoa não foi encontrada na base");
						campoNome.requestFocus();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Nada foi digitado no campo nome");
				campoNome.requestFocus();
			}
		} else if (ae.getSource() == bCancelar) {
			campoNome.setText("");
			campoSenha.setText("");
			setVisible(false);
			
			acant.botaoCadastrar.setEnabled(true);
			acant.botaoLogar.setEnabled(true);
		}
	}
}
