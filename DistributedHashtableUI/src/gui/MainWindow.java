package gui;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLayeredPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.event.ListSelectionListener;


import javax.swing.event.ListSelectionEvent;
import javax.swing.JTabbedPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.BevelBorder;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;


public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public MainWindow() {
		//emailSvc = new EmailServices();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
				
						JLayeredPane pnlConnection = new JLayeredPane();
						pnlConnection.setBackground(Color.GRAY);
						pnlConnection.setBounds(20, 72, 654, 329);
						contentPane.add(pnlConnection);
						pnlConnection.setLayout(null);
																
																		JPanel pnlConnectionSettings = new JPanel();
																		pnlConnectionSettings.setBounds(0, 0, 654, 329);
																		pnlConnection.add(pnlConnectionSettings);
																		pnlConnectionSettings.setAlignmentX(Component.LEFT_ALIGNMENT);
																								pnlConnectionSettings.setLayout(null);
																								
																								textField = new JTextField();
																								textField.setBounds(157, 37, 321, 20);
																								pnlConnectionSettings.add(textField);
																								textField.setColumns(10);
																								
																								JLabel lblServerUrl = new JLabel("Title");
																								lblServerUrl.setBounds(21, 40, 81, 14);
																								pnlConnectionSettings.add(lblServerUrl);
																								
																								JLabel label = new JLabel("Server URL");
																								label.setBounds(21, 75, 81, 14);
																								pnlConnectionSettings.add(label);
																								
																								textField_1 = new JTextField();
																								textField_1.setColumns(10);
																								textField_1.setBounds(157, 68, 321, 20);
																								pnlConnectionSettings.add(textField_1);
																pnlConnection.setVisible(false);
		
				JLayeredPane pnlMainBody = new JLayeredPane();
				pnlMainBody.setBounds(20, 72, 654, 329);
				contentPane.add(pnlMainBody);
						pnlMainBody.setLayout(new GridLayout(0, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.setBounds(0, 0, 654, 61);
		contentPane.add(tabbedPane);

		JPanel navMain = new JPanel();
		tabbedPane.addTab("Main", null, navMain, null);
		tabbedPane.setEnabledAt(0, true);
		
		JComboBox nodeSelector = new JComboBox();
		nodeSelector.setModel(new DefaultComboBoxModel(new String[] {"node1", "node2", "node3"}));
		navMain.add(nodeSelector);

		JButton btnNew = new JButton("New");
		navMain.add(btnNew);

		JButton btnSave = new JButton("Save");
		navMain.add(btnSave);

		JButton btnDelete = new JButton("Delete");
		navMain.add(btnDelete);

		JPanel navConnection = new JPanel();
		tabbedPane.addTab("Connection", null, navConnection, null);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"devBox", "Dans AWS Cluster"}));
		navConnection.add(comboBox_1);

		JButton btnConnect = new JButton("Connect");
		btnConnect.setVerticalAlignment(SwingConstants.TOP);
		btnConnect.setHorizontalAlignment(SwingConstants.LEFT);
		navConnection.add(btnConnect);

		JButton btnSave_1 = new JButton("Save");
		btnSave_1.setEnabled(false);
		btnSave_1.setVerticalAlignment(SwingConstants.TOP);
		btnSave_1.setHorizontalAlignment(SwingConstants.LEFT);
		navConnection.add(btnSave_1);
		
		JButton btnMakeDefault = new JButton("Make Default");
		btnMakeDefault.setVerticalAlignment(SwingConstants.TOP);
		btnMakeDefault.setHorizontalAlignment(SwingConstants.LEFT);
		navConnection.add(btnMakeDefault);
		

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();

				if (index == 0) {
					pnlMainBody.setVisible(true);
					pnlEmailBody.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlContacts.setVisible(false);
					pnlConnection.setVisible(false);
					pnlAccounts.setVisible(false);
				} else if (index == 1) {

					pnlEmailBody.setVisible(true);
					pnlMainBody.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlContacts.setVisible(false);
					pnlConnection.setVisible(false);
					pnlAccounts.setVisible(false);
					
					// TODO
					//IEmail email = getSelectedEmail();
					//txtEmailTextEditor.setText(email.getMessageBodyString());
					//txtTo.setText((String) email.getToAddresses().get(0));
					//txtSubject.setText(email.getSubject());
				}

				else if (index == 2) {
					pnlCalendarBody.setVisible(true);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
					pnlContacts.setVisible(false);
					pnlConnection.setVisible(false);
					pnlAccounts.setVisible(false);
				}

				else if (index == 3) {
					pnlContacts.setVisible(true);
					pnlCalendarBody.setVisible(false);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
					pnlConnection.setVisible(false);
					pnlAccounts.setVisible(false);
				}

				else if (index == 4) {
					pnlAccounts.setVisible(true);

					pnlContacts.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
					pnlConnection.setVisible(false);

				} else if (index == 5) {
					pnlConnection.setVisible(true);
					pnlAccounts.setVisible(false);

					pnlContacts.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
				}
			}
		});
		
		initializeEmailList();
		
	}

	public void initializeEmailList() {
		//IEmailManager emailMngr = emailSvc.getEmailManager();
		DefaultListModel<String> lm = new DefaultListModel<String>();
		int index = 0;
	}
}
