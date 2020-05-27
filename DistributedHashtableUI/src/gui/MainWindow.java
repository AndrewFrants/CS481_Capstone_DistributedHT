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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.event.ListSelectionListener;

import service.ChecksumDemoHashingFunction;
import service.DHService;
import service.DHashEntry;
import service.DNode;
import service.DhtLogger;

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
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

/*
 * This is the main window for the UI
 * It is built using WindowBuilder
 */

public class MainWindow extends JFrame {

	/**
	 * Serial version id (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;

	// This is the main window content pane
	private JPanel contentPane;

	/*
	 * Connection settings
	 */
	private JTextField serviceUrl;

	/*
	 * Hashtable viewer
	 */
	DefaultListModel<String> nodesList;
	private JList nodeList;
	DefaultListModel<String> keysList;
	JList keyList;
	JTextArea currentValue;

	/*
	 * Service connector
	 */
	DHService dhService;

	/**
	 * Entry point into the application
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
	 * 
	 * @throws Exception
	 */
	public MainWindow() throws Exception {

		nodesList = new DefaultListModel<String>();
		keysList = new DefaultListModel<String>();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);

		/*
		 * Creating the UI framework
		 */
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.setBounds(0, 0, 654, 61);

		contentPane.add(tabbedPane);

		BorderLayout bl_pnlMainBody = new BorderLayout();

		/*
		 * Tabbed control
		 */
		JPanel navMain = new JPanel();
		tabbedPane.addTab("Main", null, navMain, null);
		tabbedPane.setEnabledAt(0, true);
		navMain.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		navMain.add(panel, BorderLayout.NORTH);

		/*
		 * Adding node implementation
		 */
		JButton btnNew = new JButton("New Node");
		btnNew.setEnabled(false);
		panel.add(btnNew);

		/*
		 * Remove node implementation
		 */
		JButton btnNewButton = new JButton("Remove Node");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (nodeList.getSelectedIndex() < 0)
					return;

				String selectedIndex = nodesList.elementAt(nodeList.getSelectedIndex());
				selectedIndex = selectedIndex.split("\\(")[0].trim();

				if (selectedIndex.equalsIgnoreCase("All"))
					return;

				RemoveNode(selectedIndex);

				RefreshControls();
			}
		});

		panel.add(btnNewButton);

		/*
		 * New entry implementation
		 */
		JButton btnNewEntry = new JButton("New Entry");
		panel.add(btnNewEntry);

		/*
		 * Modify node button
		 */
		JButton btnNewModifyNode = new JButton("Change Node");
		btnNewModifyNode.setEnabled(false);
		panel.add(btnNewModifyNode);

		btnNewModifyNode.addActionListener(new ActionListener() {
			// removing the node
			public void actionPerformed(ActionEvent arg0) {
				if (nodeList.getSelectedIndex() < 0)
					return;
				String selectedIndex = nodesList.elementAt(nodeList.getSelectedIndex());
				selectedIndex = selectedIndex.split("\\(")[0].trim();
				if (selectedIndex.equalsIgnoreCase("All"))
					return;
				RemoveNode(selectedIndex);
				RefreshControls();

				// adding node
				JTextField replaceTxt = new JTextField("");
				JLabel label = new JLabel("Hashed value: 0");
				JPanel panel = new JPanel(new GridLayout(0, 2));
				panel.add(new JLabel("Add Node: "));
				panel.add(replaceTxt);
				replaceTxt.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						label.setText("Hashed value: " + Integer.toString(
								ChecksumDemoHashingFunction.hashValue(replaceTxt.getText() + e.getKeyChar())));
					}
				});
				panel.add(label);
				int result = JOptionPane.showConfirmDialog(null, panel, "Add New Node", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					AddNode(replaceTxt.getText());
				}
				RefreshControls();
			}
		});

		/*
		 * Update entry button
		 */
		JButton btnUpdateEntry = new JButton("Update Entry");
		panel.add(btnUpdateEntry);

		btnUpdateEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (keyList.getSelectedIndex() < 0)
					return;
				
				String selectedIndex = keysList.elementAt(keyList.getSelectedIndex());
				selectedIndex = selectedIndex.split("\\(")[0].trim();
				if (selectedIndex.equalsIgnoreCase("All"))
					return;

				JTextField replaceTxt = new JTextField("");
				JLabel label = new JLabel("Hashed value: 0");
				JPanel panel = new JPanel(new GridLayout(0, 2));
				panel.add(new JLabel("Add Entry: "));
				panel.add(replaceTxt);
				replaceTxt.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						label.setText("Hashed value: " + Integer.toString(
								ChecksumDemoHashingFunction.hashValue(replaceTxt.getText() + e.getKeyChar())));
					}
				});
				
				panel.add(label);
				int result = JOptionPane.showConfirmDialog(null, panel, "Add New Entry", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					dhService.UpdateEntry(ChecksumDemoHashingFunction.hashValue(selectedIndex), replaceTxt.getText());
				}
				
				RefreshControls();
			}
		});

		/*
		 * Save button
		 */
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RefreshControls();
			}
		});
		panel.add(btnRefresh);

		/*
		 * Delete button
		 */
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete);

		/*
		 * Main view panel
		 */
		JLayeredPane pnlMainBody = new JLayeredPane();
		navMain.add(pnlMainBody, BorderLayout.CENTER);
		pnlMainBody.setLayout(new BorderLayout(0, 0));

		// Node selection/Key value splitter
		JSplitPane splitPane = new JSplitPane();
		pnlMainBody.add(splitPane);

		currentValue = new JTextArea();
		currentValue.setText("Please select a node and key to view value");
		splitPane.setRightComponent(currentValue);

		JPanel nodeKeyViewPanel = new JPanel();
		splitPane.setLeftComponent(nodeKeyViewPanel);
		nodeKeyViewPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.5);
		nodeKeyViewPanel.add(splitPane_1);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);

		JPanel nodeListPanel = new JPanel();
		splitPane_1.setLeftComponent(nodeListPanel);
		nodeListPanel.setLayout(new BorderLayout(50, 50));

		nodeList = new JList(nodesList);
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeListPanel.add(nodeList, BorderLayout.CENTER);

		JPanel keyListPanel = new JPanel();
		splitPane_1.setRightComponent(keyListPanel);
		keyListPanel.setLayout(new BorderLayout(0, 0));

		keyList = new JList(keysList);
		keyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				PopulateKeys();
			}
		});
		JScrollPane scrollPane = new JScrollPane(keyList);
		keyListPanel.add(scrollPane, BorderLayout.CENTER);
		keyListPanel.add(keyList);
		
		keyList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				PopulateText();
			}
		});

		btnNewEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField replaceTxt = new JTextField("");
				JLabel label = new JLabel("Hashed value: 0.0000000000000");
				JPanel panel = new JPanel(new GridLayout(0, 2));

				panel.add(new JLabel("Add Entry: "));
				panel.add(replaceTxt);

				replaceTxt.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						label.setText("Hashed value: " + Integer.toString(
								ChecksumDemoHashingFunction.hashValue(replaceTxt.getText() + e.getKeyChar())));

					}
				});

				panel.add(label);

				int result = JOptionPane.showConfirmDialog(null, panel, "Add New Entry", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {
					DhtLogger.log.info("Adding entry {}", replaceTxt.getText());
					dhService.AddEntry(replaceTxt.getText());
				}

				RefreshControls();
			}
		});

		/*
		 * Add Node implementation
		 */
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JTextField replaceTxt = new JTextField("");
				JLabel label = new JLabel("Hashed value: 0");
				JPanel panel = new JPanel(new GridLayout(0, 2));

				panel.add(new JLabel("Add Node: "));
				panel.add(replaceTxt);

				replaceTxt.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						label.setText("Hashed value: " + Integer.toString(
								ChecksumDemoHashingFunction.hashValue(replaceTxt.getText() + e.getKeyChar())));
					}
				});

				panel.add(label);

				int result = JOptionPane.showConfirmDialog(null, panel, "Add New Node", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {
					AddNode(replaceTxt.getText());
				}

				RefreshControls();
			}
		});

		/*
		 * Create the connection panel
		 */
		JPanel navConnection = new JPanel();
		tabbedPane.addTab("Connection", null, navConnection, null);
		navConnection.setLayout(new BorderLayout(0, 0));

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

		JPanel tabConnectionPanel = new JPanel();
		navConnection.add(tabConnectionPanel, BorderLayout.NORTH);
		tabConnectionPanel.setLayout(new BorderLayout(0, 0));

		JPanel conectionPnl = new JPanel();
		tabConnectionPanel.add(conectionPnl, BorderLayout.NORTH);

		JComboBox comboBox_1 = new JComboBox();
		conectionPnl.add(comboBox_1);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] { "devBox", "Dans AWS Cluster" }));

		JButton btnConnect = new JButton("Connect");
		conectionPnl.add(btnConnect);
		btnConnect.setVerticalAlignment(SwingConstants.TOP);
		btnConnect.setHorizontalAlignment(SwingConstants.LEFT);

		JButton btnSave_1 = new JButton("Save");
		conectionPnl.add(btnSave_1);
		btnSave_1.setEnabled(false);
		btnSave_1.setVerticalAlignment(SwingConstants.TOP);
		btnSave_1.setHorizontalAlignment(SwingConstants.LEFT);

		JButton btnMakeDefault = new JButton("Make Default");
		conectionPnl.add(btnMakeDefault);
		btnMakeDefault.setVerticalAlignment(SwingConstants.TOP);
		btnMakeDefault.setHorizontalAlignment(SwingConstants.LEFT);

		//
		// This the the top menu tab selection logic
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

		boolean inMemoryService = false;

		if (inMemoryService) {
			createDHService();
		} else {
			dhService = new DHService(true);
			dhService.getAllNodes();

			populateNodes();
		}
	}

	/*
	 * Create the Distributed Hashtable service
	 */
	public void createDHService() throws Exception {
		dhService = DHService.createFiveNodeCluster(true);

		populateNodes();
	}

	/*
	 * Create the nodes
	 */
	public void populateNodes() {
		nodesList.clear();

		List<DNode> list = new ArrayList<DNode>(dhService.getAllNodes());

		Collections.sort(list);

		int index = 0;

		for (DNode node : list) {
			int count = node.getAllEntries().size();

			DhtLogger.log.info("Added node: {} keys count: {} ", node.nodeID, count);

			nodesList.add(nodesList.getSize(), node.getName() + " (" + index++ + "=" + "nodeID: " + node.getNodeID()
					+ ", angle: " + node.getAngle() + ", Size=" + count + ")");
		}
	}

	/*
	 * Populate the keys
	 */
	public void PopulateKeys() {
		if (nodeList.getSelectedIndex() < 0)
			return;

		String selectedIndex = nodesList.elementAt(nodeList.getSelectedIndex());
		selectedIndex = selectedIndex.split("\\(")[0].trim();
		System.out.println(nodeList.getSelectedIndex());
		if (selectedIndex.equalsIgnoreCase("All"))
			return;

		keysList.clear();

		DNode node = dhService.findNodeByName(selectedIndex);

		List<DHashEntry> list = new ArrayList<DHashEntry>(node.getAllEntries());

		Collections.sort(list);

		for (DHashEntry en : list) {
			keysList.add(keysList.getSize(), en.getValue().split("\n")[0] + " (" + en.key.toString() + ")");
		}

		DhtLogger.log.info("Retrieve keys count {}", keysList.getSize());

		keyList.repaint();
	}

	public void PopulateText() {
		if (keyList.getSelectedIndex() < 0)
			return;

		String selectedIndex = keysList.elementAt(keyList.getSelectedIndex());
		String angleVal = selectedIndex.split("\\(")[1].replace(")", "");
		Integer selectedKey = Integer.valueOf(angleVal);

		if (selectedIndex.equalsIgnoreCase("All"))
			return;

		DhtLogger.log.info("Retrieve key {} selected index: {}", selectedKey, selectedIndex);
		DHashEntry entry = dhService.getEntry(selectedKey);

		currentValue.setText(entry.getValue());
	}

	public void RefreshControls() {
		this.PopulateKeys();
		this.PopulateText();
		this.populateNodes();
	}

	public void AddNode(String text) {
		dhService.addNode(text);

		RefreshControls();
	}

	public void RemoveNode(String name) {
		dhService.removeNode(name);

		RefreshControls();
	}

}
