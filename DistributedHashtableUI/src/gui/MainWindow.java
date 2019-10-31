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
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;


public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtTo;
	private JTextField txtCC;
	private JTextField txtSubject;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private JList listEmails;

	JTextArea txtEmailMessage;

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

		JLayeredPane pnlSettings = new JLayeredPane();
		pnlSettings.setBounds(20, 72, 654, 329);
		contentPane.add(pnlSettings);
		pnlSettings.setLayout(null);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "480x360", "720x480", "1280x720" }));
		comboBox.setBounds(10, 38, 108, 20);
		pnlSettings.add(comboBox);

		JButton btnSignOff = new JButton("Sign Off");
		btnSignOff.setBounds(10, 295, 89, 23);
		pnlSettings.add(btnSignOff);

		JLabel lblNewLabel_2 = new JLabel("display");
		lblNewLabel_2.setBounds(10, 11, 75, 14);
		pnlSettings.add(lblNewLabel_2);

		JButton btnIgnoreList = new JButton("Ignore List");
		btnIgnoreList.setBounds(526, 295, 118, 23);
		pnlSettings.add(btnIgnoreList);

		JCheckBox chckbxAutoLogin = new JCheckBox("Auto Login");
		chckbxAutoLogin.setBounds(547, 37, 97, 23);
		pnlSettings.add(chckbxAutoLogin);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.setBounds(0, 0, 654, 61);
		contentPane.add(tabbedPane);

		JPanel navMain = new JPanel();
		tabbedPane.addTab("Main", null, navMain, null);

		textField = new JTextField();
		textField.setText("Search email...");
		textField.setColumns(20);
		navMain.add(textField);

		JButton btnOpen = new JButton("Open");
		navMain.add(btnOpen);

		JButton button_1 = new JButton("Mark as Read");
		navMain.add(button_1);

		JButton button_2 = new JButton("Delete");
		navMain.add(button_2);

		JLabel lblSort = new JLabel("Sort:");
		navMain.add(lblSort);

		JComboBox cmbSort = new JComboBox();
		navMain.add(cmbSort);
		cmbSort.setModel(new DefaultComboBoxModel(new String[] { "Date", "Alphabetically", "Read/Unread" }));

		JPanel navEmail = new JPanel();
		tabbedPane.addTab("Email", null, navEmail, null);
		navEmail.setLayout(null);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 649, 33);
		navEmail.add(layeredPane);
		layeredPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton button_3 = new JButton("Send");
		layeredPane.add(button_3);

		JButton button_4 = new JButton("Save Draft");
		button_4.setEnabled(false);
		layeredPane.add(button_4);

		JButton button_5 = new JButton("Print");
		button_5.setEnabled(false);
		layeredPane.add(button_5);

		JButton button_6 = new JButton("Insert");
		layeredPane.add(button_6);

		JButton button_7 = new JButton("Cancel");
		layeredPane.add(button_7);

		JButton button_9 = new JButton("Export");
		layeredPane.add(button_9);
		button_9.setEnabled(false);

		JButton button_10 = new JButton("Move");
		layeredPane.add(button_10);
		button_10.setEnabled(false);

		JButton button_11 = new JButton("Delete");
		layeredPane.add(button_11);

		JButton btnDir = new JButton("Directory");
		layeredPane.add(btnDir);

		JPanel navCalendar = new JPanel();
		tabbedPane.addTab("Calendar", null, navCalendar, null);

		JButton button_13 = new JButton("Add");
		button_13.setVerticalAlignment(SwingConstants.TOP);
		button_13.setHorizontalAlignment(SwingConstants.LEFT);
		navCalendar.add(button_13);

		JButton button_14 = new JButton("Remove");
		button_14.setHorizontalAlignment(SwingConstants.LEFT);
		navCalendar.add(button_14);

		JButton button_15 = new JButton("Cancel");
		button_15.setVerticalAlignment(SwingConstants.TOP);
		button_15.setHorizontalAlignment(SwingConstants.LEFT);
		navCalendar.add(button_15);

		textField_1 = new JTextField();
		textField_1.setText("Saturday, June 1 2019");
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setEditable(false);
		textField_1.setColumns(25);
		navCalendar.add(textField_1);

		JPanel Contacts = new JPanel();
		tabbedPane.addTab("Contacts", null, Contacts, null);

		textField_2 = new JTextField();
		textField_2.setText("Search...");
		textField_2.setColumns(10);
		Contacts.add(textField_2);

		JButton button_16 = new JButton("Add Favoties");
		button_16.setEnabled(false);
		Contacts.add(button_16);

		JButton button_17 = new JButton("Add contact");
		Contacts.add(button_17);

		JLayeredPane pnlAccounts = new JLayeredPane();
		pnlAccounts.setBounds(9, 72, 665, 329);
		contentPane.add(pnlAccounts);
		
		JComboBox cbAccountList = new JComboBox();
		cbAccountList.setBounds(0, 0, 219, 20);
		pnlAccounts.add(cbAccountList);
		
		JPanel navAccounts = new JPanel();
		tabbedPane.addTab("Accounts", null, navAccounts, null);
				
				JButton btnAddAccount = new JButton("New Account");
				btnAddAccount.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				        JTextField addAccountEmail = new JTextField("");
				        JTextField addAccountPassword = new JTextField("");
				        JPanel panel = new JPanel(new GridLayout(0, 2));

				        panel.add(new JLabel("Email: "));
				        panel.add(addAccountEmail);

				        panel.add(new JLabel("Password: "));
				        panel.add(addAccountPassword);
				        
				        int result = JOptionPane.showConfirmDialog(null, panel, "Add account",
				            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				        
				        cbAccountList.addItem(addAccountEmail.getText());
					}
				});
				btnAddAccount.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {

					}
				});
				
				navAccounts.add(btnAddAccount);
		
				JButton btnSwitch = new JButton("Switch Accounts");
				navAccounts.add(btnSwitch);

		JPanel Settings = new JPanel();
		tabbedPane.addTab("Settings", null, Settings, null);

		JButton button_19 = new JButton("Save");
		button_19.setVerticalAlignment(SwingConstants.TOP);
		button_19.setHorizontalAlignment(SwingConstants.LEFT);
		Settings.add(button_19);

		JButton button_20 = new JButton("Restore Default");
		button_20.setHorizontalAlignment(SwingConstants.LEFT);
		Settings.add(button_20);

		JButton button_21 = new JButton("Cancel");
		button_21.setVerticalAlignment(SwingConstants.TOP);
		button_21.setHorizontalAlignment(SwingConstants.LEFT);
		button_21.setEnabled(false);
		Settings.add(button_21);

		JLayeredPane pnlContacts = new JLayeredPane();
		pnlContacts.setBounds(20, 72, 654, 329);
		contentPane.add(pnlContacts);
		pnlContacts.setLayout(null);

		JLabel lblContactList = new JLabel("Contact List");
		lblContactList.setBounds(10, 11, 78, 14);
		pnlContacts.add(lblContactList);

		JList listContactInfo = new JList();
		listContactInfo.setBounds(10, 36, 164, 265);
		listContactInfo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		listContactInfo.setModel(new AbstractListModel() {
			String[] values = new String[] { "Alison", "Andrew", "Dave", "Beth" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		pnlContacts.add(listContactInfo);

		JTextArea txtContactInfo = new JTextArea();
		txtContactInfo.setBounds(184, 36, 460, 265);
		txtContactInfo.setText(
				"First Name: Alison\r\nLast Name: Smith\r\nE-mail Address: smith.al44356@bc.edu\r\nphone-number: 555-000-0098\r\n\r\nNotes:  in CS410 group 3 project");
		pnlContacts.add(txtContactInfo);

		JLabel lblInformation = new JLabel("Information");
		lblInformation.setBounds(184, 11, 71, 14);
		pnlContacts.add(lblInformation);

		JLayeredPane pnlCalendarBody = new JLayeredPane();
		pnlCalendarBody.setBounds(20, 72, 654, 329);
		contentPane.add(pnlCalendarBody);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 301, 329);
		pnlCalendarBody.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("Select date...");
		panel.add(lblNewLabel);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(311, 0, 343, 329);
		pnlCalendarBody.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel("Schedule...");
		panel_2.add(lblNewLabel_1, BorderLayout.NORTH);

		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] { "EVENT1", "EVENT2", "EVENT3" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(list, BorderLayout.CENTER);

		JLayeredPane pnlEmailBody = new JLayeredPane();
		pnlEmailBody.setBounds(20, 72, 654, 329);
		pnlEmailBody.setVisible(false);
		contentPane.add(pnlEmailBody);

		JPanel pnlEmailMid = new JPanel();
		pnlEmailMid.setBounds(0, 0, 654, 94);
		pnlEmailBody.add(pnlEmailMid);
		GridBagLayout gbl_pnlEmailMid = new GridBagLayout();
		gbl_pnlEmailMid.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_pnlEmailMid.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_pnlEmailMid.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlEmailMid.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pnlEmailMid.setLayout(gbl_pnlEmailMid);

		JLabel lblTo = new JLabel("To");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.EAST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 0;
		gbc_lblTo.gridy = 0;
		pnlEmailMid.add(lblTo, gbc_lblTo);

		txtTo = new JTextField();
		GridBagConstraints gbc_txtTo = new GridBagConstraints();
		gbc_txtTo.fill = GridBagConstraints.VERTICAL;
		gbc_txtTo.anchor = GridBagConstraints.WEST;
		gbc_txtTo.insets = new Insets(0, 0, 5, 5);
		gbc_txtTo.gridx = 1;
		gbc_txtTo.gridy = 0;
		pnlEmailMid.add(txtTo, gbc_txtTo);
		txtTo.setColumns(40);

		JLabel lblCC = new JLabel("CC");
		GridBagConstraints gbc_lblCC = new GridBagConstraints();
		gbc_lblCC.anchor = GridBagConstraints.EAST;
		gbc_lblCC.insets = new Insets(0, 0, 5, 5);
		gbc_lblCC.gridx = 0;
		gbc_lblCC.gridy = 1;
		pnlEmailMid.add(lblCC, gbc_lblCC);

		txtCC = new JTextField();
		GridBagConstraints gbc_txtCC = new GridBagConstraints();
		gbc_txtCC.anchor = GridBagConstraints.WEST;
		gbc_txtCC.insets = new Insets(0, 0, 5, 5);
		gbc_txtCC.gridx = 1;
		gbc_txtCC.gridy = 1;
		pnlEmailMid.add(txtCC, gbc_txtCC);
		txtCC.setColumns(40);

		JLabel lblSubject = new JLabel("Subject");
		GridBagConstraints gbc_lblSubject = new GridBagConstraints();
		gbc_lblSubject.anchor = GridBagConstraints.EAST;
		gbc_lblSubject.insets = new Insets(0, 0, 0, 5);
		gbc_lblSubject.gridx = 0;
		gbc_lblSubject.gridy = 2;
		pnlEmailMid.add(lblSubject, gbc_lblSubject);

		txtSubject = new JTextField();
		GridBagConstraints gbc_txtSubject = new GridBagConstraints();
		gbc_txtSubject.anchor = GridBagConstraints.WEST;
		gbc_txtSubject.insets = new Insets(0, 0, 0, 5);
		gbc_txtSubject.gridx = 1;
		gbc_txtSubject.gridy = 2;
		pnlEmailMid.add(txtSubject, gbc_txtSubject);
		txtSubject.setColumns(40);

		JPanel pnlEmailBottom = new JPanel();
		pnlEmailBottom.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		pnlEmailBottom.setBounds(0, 319, 654, -252);
		pnlEmailBody.add(pnlEmailBottom);

		JTextArea txtEmailTextEditor = new JTextArea();
		pnlEmailBody.setLayer(txtEmailTextEditor, 0);
		txtEmailTextEditor.setBounds(0, 94, 654, 235);
		pnlEmailBody.add(txtEmailTextEditor);

		JLayeredPane pnlMainBody = new JLayeredPane();
		pnlMainBody.setBounds(20, 72, 654, 329);
		contentPane.add(pnlMainBody);
		pnlMainBody.setLayout(new BoxLayout(pnlMainBody, BoxLayout.X_AXIS));

		JPanel pnlMainBottom = new JPanel();
		pnlMainBody.add(pnlMainBottom);
		GridBagLayout gbl_pnlMainBottom = new GridBagLayout();
		gbl_pnlMainBottom.columnWidths = new int[] { 66, 149, 419, 0 };
		gbl_pnlMainBottom.rowHeights = new int[] { 322, 0 };
		gbl_pnlMainBottom.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_pnlMainBottom.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		pnlMainBottom.setLayout(gbl_pnlMainBottom);

		listEmails = new JList();
		listEmails.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

					// LIST EMAILS
			}
		});

		JList listFolders = new JList();
		listFolders.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (listFolders.getSelectedIndex() == 1) {
				}
			}
		});

		listFolders.setBorder(new LineBorder(new Color(0, 0, 0)));
		listFolders.setBackground(UIManager.getColor("Button.background"));
		listFolders.setFont(new Font("Tahoma", Font.PLAIN, 16));
		listFolders.setLayoutOrientation(JList.VERTICAL_WRAP);
		listFolders.setModel(new AbstractListModel() {
			String[] values = new String[] { "Inbox", "Sent", "Spam", "Trash", "Outbox" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		GridBagConstraints gbc_listFolders = new GridBagConstraints();
		gbc_listFolders.fill = GridBagConstraints.BOTH;
		gbc_listFolders.insets = new Insets(0, 0, 0, 5);
		gbc_listFolders.gridx = 0;
		gbc_listFolders.gridy = 0;
		pnlMainBottom.add(listFolders, gbc_listFolders);
		listFolders.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (listFolders.getSelectedIndex() == 1) {
					System.out.println("test");
				}
			}
		});
		listEmails.setBorder(new LineBorder(new Color(0, 0, 0)));
		listEmails.setBackground(UIManager.getColor("Button.background"));
		listEmails.setModel(new AbstractListModel() {
			String[] values = new String[] { "From: Sender..................." };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});

		GridBagConstraints gbc_listEmails = new GridBagConstraints();
		gbc_listEmails.fill = GridBagConstraints.BOTH;
		gbc_listEmails.insets = new Insets(0, 0, 0, 5);
		gbc_listEmails.gridx = 1;
		gbc_listEmails.gridy = 0;
		pnlMainBottom.add(listEmails, gbc_listEmails);
		txtEmailMessage = new JTextArea();
		txtEmailMessage.setEditable(false);
		GridBagConstraints gbc_txtEmailMessage = new GridBagConstraints();
		gbc_txtEmailMessage.fill = GridBagConstraints.BOTH;
		gbc_txtEmailMessage.gridx = 2;
		gbc_txtEmailMessage.gridy = 0;
		pnlMainBottom.add(txtEmailMessage, gbc_txtEmailMessage);
		pnlMainBody.setVisible(true);
		pnlEmailBody.setVisible(false);
		pnlCalendarBody.setVisible(false);
		pnlCalendarBody.setVisible(false);
		pnlContacts.setVisible(false);
		pnlSettings.setVisible(false);
		pnlAccounts.setVisible(false);
		

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();

				if (index == 0) {
					pnlMainBody.setVisible(true);
					pnlEmailBody.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlContacts.setVisible(false);
					pnlSettings.setVisible(false);
					pnlAccounts.setVisible(false);
				} else if (index == 1) {

					pnlEmailBody.setVisible(true);
					pnlMainBody.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlContacts.setVisible(false);
					pnlSettings.setVisible(false);
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
					pnlSettings.setVisible(false);
					pnlAccounts.setVisible(false);
				}

				else if (index == 3) {
					pnlContacts.setVisible(true);
					pnlCalendarBody.setVisible(false);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
					pnlSettings.setVisible(false);
					pnlAccounts.setVisible(false);
				}

				else if (index == 4) {
					pnlAccounts.setVisible(true);

					pnlContacts.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
					pnlSettings.setVisible(false);

				} else if (index == 5) {
					pnlSettings.setVisible(true);
					pnlAccounts.setVisible(false);

					pnlContacts.setVisible(false);
					pnlCalendarBody.setVisible(false);
					pnlMainBody.setVisible(false);
					pnlEmailBody.setVisible(false);
				}
			}
		});
		
		JButton button = new JButton("Reply");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnlMainBody.setVisible(false);
				pnlEmailBody.setVisible(true);
				//TODO
				//IEmail email = getSelectedEmail();
				//txtEmailTextEditor.setText(email.getMessageBodyString());
				//txtTo.setText((String) email.getToAddresses().get(0));
				//txtSubject.setText(email.getSubject());
				tabbedPane.setSelectedIndex(1);
			}
		});
		
		navMain.add(button);
		
		initializeEmailList();
		
	}

	public void initializeEmailList() {
		//IEmailManager emailMngr = emailSvc.getEmailManager();
		DefaultListModel<String> lm = new DefaultListModel<String>();
		int index = 0;
		//emails = emailMngr.getEmails("folderName");
		//for (IEmail email : emails) {
		//	lm.add(index, email.getSubject());
		//}
		this.listEmails.removeAll();
		this.listEmails.setModel(lm);
	}
}
