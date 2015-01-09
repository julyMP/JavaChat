package ro.julymp.chat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ro.julymp.chat.Client;
import ro.julymp.chat.ClientAction;
import ro.julymp.chat.message.Message;
import ro.julymp.chat.message.Protocol;
import static java.awt.GridBagConstraints.*;

/**
 * @author Marius-Pop Iuliana Dec 21, 2014
 */

public class MainFrame extends JFrame implements ClientAction {

    private JTextField serverIP = new JTextField(15);
    private JTextField serverPort = new JTextField(5);
    private JTextField username = new JTextField(30);
    private JButton connect = new JButton("Connect");
    private JTabbedPane chatTabs = new JTabbedPane();
    private DefaultListModel<String> usersListModel = new DefaultListModel<String>();
    private JList<String> usersList = new JList<String>(usersListModel);
    private JTextField message = new JTextField(100);
    private JButton send = new JButton("Send");
    private boolean isConnectVisible = true;
    private Map<String, JTextArea> chatTabTextAreaMap = new HashMap<String, JTextArea>();
    private Client client;
    
    public MainFrame() {
	initGUI();
    }

    private void initGUI() {
	this.setTitle("July messanger");
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	this.add(getTopPanel(), BorderLayout.NORTH);
	this.add(getCenterPanel(), BorderLayout.CENTER);
	this.add(getBottomPanel(), BorderLayout.SOUTH);
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension newDimension = new Dimension((int) screen.getWidth() - 100, (int) screen.getHeight() - 100);
	this.setSize(newDimension);
	this.setBounds(50, 50, (int) newDimension.getWidth(), (int) newDimension.getHeight());
	this.connect.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (isConnectVisible) {
		    connect();
		} else {
		    disconnect();
		}
	    }
	});
	this.send.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		String mess = message.getText();
		putMessage(mess);
		message.setText(null);

	    }

	});
	this.serverIP.setText("127.0.0.1");
	this.serverPort.setText("12345");
	this.setVisible(true);
    }

    private void connect() {
	String ip = serverIP.getText().trim();
	String port = serverPort.getText().trim();
	String username = this.username.getText().trim();
	if (ip.isEmpty()) {
	    showErrorDialog("Invalid IP!");
	} else if (username.isEmpty()) {
	    showErrorDialog("Username cannot be empty!");
	} else {
	    try {
		int p = Integer.parseInt(port);
		if (p < 1 || p > 65000) {
		    showErrorDialog("Invalid port!");
		}
		this.client = new Client(username, ip, p, this);
		new Thread(client).start();
	    } catch (NumberFormatException e) {
		showErrorDialog("Invalid port!");
	    }
	}

    }

    private void disconnect() {
	this.client.sendMessage(new Message(Protocol.DISCONNECTED));
	connect.setText("Connect");
	isConnectVisible = true;
	serverIP.setEnabled(true);
	serverPort.setEnabled(true);
	username.setEnabled(true);
	usersListModel.removeAllElements();
    }

    private void addUser(String user) {
	usersListModel.addElement(user);
    }

    private void removeUser(String user) {
	usersListModel.removeElement(user);
    }

    private JPanel getTopPanel() {
	JPanel panel = new JPanel();
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.setBackground(Color.GREEN);
	panel.setLayout(new GridBagLayout());
	panel.add(new JLabel("Server IP:"), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, EAST, NONE, new Insets(0, 0, 0, 0), 0, 0));
	panel.add(serverIP, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, WEST, HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
	panel.add(new JLabel("Server Port:"), new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, EAST, NONE, new Insets(0, 0, 0, 0), 0, 0));
	panel.add(serverPort, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, WEST, HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
	panel.add(new JLabel("Username:"), new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0, EAST, NONE, new Insets(0, 0, 0, 0), 0, 0));
	panel.add(username, new GridBagConstraints(5, 0, 1, 1, 1.0, 1.0, WEST, HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
	panel.add(connect, new GridBagConstraints(6, 0, 1, 1, 1.0, 1.0, CENTER, NONE, new Insets(0, 0, 0, 0), 0, 0));

	serverIP.grabFocus();
	serverIP.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		serverPort.grabFocus();
	    }
	});
	serverPort.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		username.grabFocus();
	    }
	});
	username.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (isConnectVisible) {
		    connect();
		} else {
		    disconnect();
		}
	    }
	});
	return panel;
    }

    private JPanel getCenterPanel() {
	JPanel panel = new JPanel();
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.setBackground(Color.BLUE);
	panel.setLayout(new GridBagLayout());
	panel.add(chatTabs, new GridBagConstraints(0, 0, 1, 1, 0.9, 1.0, CENTER, BOTH, new Insets(0, 0, 0, 0), 0, 0));
	panel.add(new JScrollPane(usersList), new GridBagConstraints(1, 0, 1, 1, 0.1, 1.0, CENTER, BOTH, new Insets(0, 0, 0, 0), 0, 0));
	usersList.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mousePressed(MouseEvent e) {
		if (e.getClickCount() > 1) {
		    String user = usersList.getSelectedValue();
		    addTab(user);
		}
	    }

	});
	return panel;
    }

    private void addTab(String user) {
	if (!chatTabTextAreaMap.containsKey(user)) {
	    JTextArea textArea = new JTextArea();
	    textArea.setEditable(false);
	    chatTabTextAreaMap.put(user, textArea);
	    chatTabs.addTab(user, new JScrollPane(textArea));
	    chatTabs.setSelectedIndex(chatTabs.getTabCount() - 1);
	}
    }

    private void removeTab(String user) {
	chatTabTextAreaMap.remove(user);
    }

    private JPanel getBottomPanel() {
	JPanel panel = new JPanel();
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.setBackground(Color.CYAN);
	panel.setLayout(new GridBagLayout());
	panel.add(new JLabel("Message:"), new GridBagConstraints(0, 0, 1, 1, 0.1, 1.0, EAST, NONE, new Insets(0, 0, 0, 0), 0, 0));
	panel.add(message, new GridBagConstraints(1, 0, 1, 1, 0.8, 1.0, WEST, HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
	panel.add(send, new GridBagConstraints(2, 0, 1, 1, 0.1, 1.0, CENTER, NONE, new Insets(0, 0, 0, 0), 0, 0));
	return panel;
    }

    private void putMessage(String message) {
	String selectedTabTitle = chatTabs.getTitleAt(chatTabs.getSelectedIndex());
	this.client.sendMessage(new Message(Protocol.SEND_MESSAGE, "["+selectedTabTitle+"]"+message));
	putMessage(message, selectedTabTitle, this.username.getText());
    }

    private void putMessage(String message, String tabTitle, String sender) {
	JTextArea textArea = chatTabTextAreaMap.get(tabTitle);
	if (!message.isEmpty()) {
	    textArea.append(sender + ":" + message + "\n");
	}
    }

    public void showErrorDialog(String message) {
	JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
	MainFrame mainFrame = new MainFrame();
    }

    @Override
    public void getUsers(List<String> users) {
	for (String user : users) {
	    addUser(user);
	}

    }

    @Override
    public void connect(String user) {
	addUser(user);

    }

    @Override
    public void disconnect(String user) {
	removeUser(user);
    }

    @Override
    public void receiveMessage(String sender, String message) {
	if (!chatTabTextAreaMap.containsKey(sender)) {
	    addTab(sender);
	}
	putMessage(message,sender,sender);
    }

    @Override
    public void onExeption(Exception e) {
	showErrorDialog(e.getMessage());
	disconnect();
    }

    @Override
    public void afterStart() {
	connect.setText("Disconnect");
	isConnectVisible = false;
	serverIP.setEnabled(false);
	serverPort.setEnabled(false);
	username.setEnabled(false);
	this.client.sendMessage(new Message(Protocol.GET_USERS,""));
    }
}
