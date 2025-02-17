package server.client.ui;
import server.client.domain.ClientController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ClientGUI extends JFrame implements ClientView {
    private static final int WIDTH = 430;
    private static final int HEIGHT = 330;

    private JTextArea log;
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    private JPasswordField password;
    private JButton btnLogin, btnSend;
    private JPanel headerPanel;
    private ClientController clientController;
    public ClientGUI() {
        setting();
        createPanel();

        setVisible(true);
    }
    @Override
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
    private void setting() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
    @Override
    public void showMessage(String msg) {
        log.append(msg);
    }
    @Override
    public void disconnectedFromServer(){
        hideHeaderPanel(true);
    }
    public void disconnectServer(){
        clientController.disconnectServer();
    }
    public void hideHeaderPanel(boolean visible){
        headerPanel.setVisible(visible);
    }
    public void login(){
        if (clientController.connectToServer(tfLogin.getText())){
            headerPanel.setVisible(false);
        }
    }
    private void message(){
        clientController.message(tfMessage.getText());
        tfMessage.setText("");
    }
    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }
    private Component createHeaderPanel() {
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("112.3.5.8");
        tfPort = new JTextField("2810");
        tfLogin = new JTextField("Arthur Stegno");
        password = new JPasswordField("password");
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }
    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }
    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });
        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectServer();
        }
    }
}
