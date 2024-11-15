package server.client.domain;
import server.client.ui.ClientView;
import server.server.domain.ServerController;
public class ClientController {
    private boolean connected;
    private String name;
    private ClientView clientView;
    private ServerController serverController;

    public ClientController(ClientView clientView, ServerController serverController) {
        this.clientView = clientView;
        this.serverController = serverController;
        clientView.setClientController(this);
    }
    public boolean connectToServer(String name) {
        this.name = name;
        if (serverController.connectUser(this)){
            showOnWindow("Вы успешно подключились!\n");
            connected = true;
            String log = serverController.getHistory();
            if (log != null){
                showOnWindow(log);
            }
            return true;
        } else {
            showOnWindow("Подключение не удалось");
            return false;
        }
    }
    public void answerFromServer(String text) {
        showOnWindow(text);
    }
    public void disconnectedFromServer() {
        if (connected) {
            connected = false;
            clientView.disconnectedFromServer();
            showOnWindow("Вы были отключены от сервера. Хорошего дня.");
        }
    }
    public void disconnectServer() {
        serverController.disconnectUser(this);
    }
    public void message(String text) {
        if (connected) {
            if (!text.isEmpty()) {
                serverController.message(name + ": " + text);
            }
        } else {
            showOnWindow("Нет подключения");
        }
    }
    public String getName() {
        return name;
    }
    private void showOnWindow(String text) {
        clientView.showMessage(text + "\n");
    }
}
