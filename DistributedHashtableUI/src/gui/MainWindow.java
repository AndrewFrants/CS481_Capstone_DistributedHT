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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.event.ListSelectionListener;

import service.DHService;
import service.DHashEntry;
import service.DNode;

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
import javax.swing.JSplitPane;



public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	DefaultListModel<String> nodesList;
	private JList nodeList;
	DefaultListModel<String> keysList;
	JList keyList;
	DHService dhService;
	JTextArea currentValue;
	
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
		nodesList = new DefaultListModel<String>();
		keysList = new DefaultListModel<String>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.setLayout(new GridLayout(0, 1));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.setBounds(0, 0, 654, 61);
		contentPane.add(tabbedPane);
												//pnlMainBody.setBounds(20, 72, 654, 329);
												BorderLayout bl_pnlMainBody = new BorderLayout();



		JPanel navMain = new JPanel();
		tabbedPane.addTab("Main", null, navMain, null);
		tabbedPane.setEnabledAt(0, true);
		navMain.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		navMain.add(panel, BorderLayout.NORTH);
		
				JButton btnNew = new JButton("New Node");
				panel.add(btnNew);
				
				JButton btnNewButton = new JButton("Remove Node");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (nodeList.getSelectedIndex() < 0)
							return;
						
						String selectedIndex = nodesList.elementAt(nodeList.getSelectedIndex());
						selectedIndex = selectedIndex.split("\\(")[0].trim();
						
						if (selectedIndex.equalsIgnoreCase("All"))
							return;
						
						RemoveNode(selectedIndex);
					}
				});
				panel.add(btnNewButton);
				
				JButton btnNewEntry = new JButton("New Entry");
				panel.add(btnNewEntry);
				
						JButton btnSave = new JButton("Save");
						panel.add(btnSave);
						
								JButton btnDelete = new JButton("Delete");
								panel.add(btnDelete);
																												
				JLayeredPane pnlMainBody = new JLayeredPane();
				navMain.add(pnlMainBody, BorderLayout.CENTER);
				pnlMainBody.setLayout(new BorderLayout(0, 0));
				//pnlMainBody.setLayout(new BorderLayout(0, 0));
				
				JSplitPane splitPane = new JSplitPane();
				pnlMainBody.add(splitPane);
				
				currentValue = new JTextArea();
				currentValue.setText("Key value goes here");
				splitPane.setRightComponent(currentValue);
				
				JPanel panel_3 = new JPanel();
				splitPane.setLeftComponent(panel_3);
				panel_3.setLayout(new BorderLayout(0, 0));
				
				JSplitPane splitPane_1 = new JSplitPane();
				splitPane_1.setResizeWeight(0.5);
				panel_3.add(splitPane_1);
				splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
				
				JPanel panel_4 = new JPanel();
				splitPane_1.setLeftComponent(panel_4);
				panel_4.setLayout(new BorderLayout(50, 50));
				
				nodeList = new JList(nodesList);
				panel_4.add(nodeList, BorderLayout.CENTER);
				
				JPanel panel_5 = new JPanel();
				splitPane_1.setRightComponent(panel_5);
				panel_5.setLayout(new BorderLayout(0, 0));
				
				keyList = new JList(keysList);
				nodeList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent arg0) {
						PopulateKeys();
					}
				});
				panel_5.add(keyList);
				
				keyList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent arg0) {
						PopulateText();
					}
				});
				
				btnNew.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
				        JTextField replaceTxt = new JTextField("");
				        JPanel panel = new JPanel(new GridLayout(0, 1));

				        panel.add(new JLabel("Add Node: "));
				        panel.add(replaceTxt);

				        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Node",
				            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				
				        if (result == JOptionPane.OK_OPTION) {
				        		AddNode(replaceTxt.getText());
				        	}
					}
				});

		JPanel navConnection = new JPanel();
		tabbedPane.addTab("Connection", null, navConnection, null);
		navConnection.setLayout(new BorderLayout(0, 0));
										//pnlConnection.setLayout(null);
										
												JPanel pnlConnectionSettings = new JPanel();
												navConnection.add(pnlConnectionSettings, BorderLayout.CENTER);
												pnlConnectionSettings.setAlignmentX(Component.LEFT_ALIGNMENT);
												pnlConnectionSettings.setLayout(new GridLayout(3, 2, 0, 0));
												
												JLabel lblNewLabel = new JLabel("New label");
												pnlConnectionSettings.add(lblNewLabel);
												
												textField = new JTextField();
												pnlConnectionSettings.add(textField);
												textField.setColumns(10);
												
												textField_1 = new JTextField();
												pnlConnectionSettings.add(textField_1);
												textField_1.setColumns(10);
												
												JPanel panel_2 = new JPanel();
												navConnection.add(panel_2, BorderLayout.NORTH);
												panel_2.setLayout(new BorderLayout(0, 0));
												
												JPanel panel_1 = new JPanel();
												panel_2.add(panel_1, BorderLayout.NORTH);
												
												JComboBox comboBox_1 = new JComboBox();
												panel_1.add(comboBox_1);
												comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"devBox", "Dans AWS Cluster"}));
												
														JButton btnConnect = new JButton("Connect");
														panel_1.add(btnConnect);
														btnConnect.setVerticalAlignment(SwingConstants.TOP);
														btnConnect.setHorizontalAlignment(SwingConstants.LEFT);
														
																JButton btnSave_1 = new JButton("Save");
																panel_1.add(btnSave_1);
																btnSave_1.setEnabled(false);
																btnSave_1.setVerticalAlignment(SwingConstants.TOP);
																btnSave_1.setHorizontalAlignment(SwingConstants.LEFT);
																
																JButton btnMakeDefault = new JButton("Make Default");
																panel_1.add(btnMakeDefault);
																btnMakeDefault.setVerticalAlignment(SwingConstants.TOP);
																btnMakeDefault.setHorizontalAlignment(SwingConstants.LEFT);
		

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();

				if (index == 0) {
					pnlMainBody.setVisible(true);
					pnlConnectionSettings.setVisible(false);
				} else if (index == 1) {

					pnlMainBody.setVisible(false);
					pnlConnectionSettings.setVisible(true);

				}
			}
		});
		
		createDHService();
		
	}

	public void createDHService() {
		//IEmailManager emailMngr = emailSvc.getEmailManager();
		//DefaultListModel<String> lm = new DefaultListModel<String>();
		
		dhService = DHService.createFiveNodeCluster();
		
		populateNodes();
	}
	
	public void populateNodes()
	{
		nodesList.clear();
		
		nodesList.add(0, "All");
		
		for (DNode node : dhService.getAllNodes())
		{
			nodesList.add(0, node.getName() + " (" + node.getHash() + ")");
		}
	}
	
	public void PopulateKeys()
	{
		if (nodeList.getSelectedIndex() < 0)
			return;
		
		String selectedIndex = nodesList.elementAt(nodeList.getSelectedIndex());
		selectedIndex = selectedIndex.split("\\(")[0].trim();
		
		if (selectedIndex.equalsIgnoreCase("All"))
			return;
		
		keysList.clear();

		DNode node = dhService.findNodeByName(selectedIndex);
		
		for (DHashEntry en : node.getAllEntries())
		{
			keysList.add(0, en.key.toString());
		}
		
		keyList.repaint();
	}
	
	public void PopulateText()
	{
		if (keyList.getSelectedIndex() < 0)
			return;
		
		String selectedIndex = keysList.elementAt(keyList.getSelectedIndex());
		Double selectedKey = Double.valueOf(selectedIndex);
		
		if (selectedIndex.equalsIgnoreCase("All"))
			return;
		
		DNode node = dhService.findNodeByName(selectedKey);
		
		DHashEntry entry = node.getTable().getEntry(selectedKey);
		
		currentValue.setText(entry.getValue());
	}
	
	public void RefreshControls()
	{
		this.PopulateKeys();
		this.PopulateText();
		this.populateNodes();
	}
	
	public void AddNode(String text)
	{
		dhService.addNode(text);
		
		RefreshControls();
	}
	
	public void RemoveNode(String name)
	{
		dhService.removeNode(name);
		
		RefreshControls();
	}
}
