package ClientAchat.model;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class ConfigProperties {
    private static final String CONFIG_FILE = System.getProperty("user.dir") + "/config/config.properties";
    private Properties p;
    public ConfigProperties() {
        p = new Properties();
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            p.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getServeurIP() {
        return p.getProperty("serveur_ip");
    }
    public int getServeurPort() {
        String portStr = p.getProperty("serveur_port");
        try {
            return Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void main(String[] args) {
        ConfigProperties config = new ConfigProperties();
        String serveurIP = config.getServeurIP();
        int portIP = config.getServeurPort();

        if (serveurIP != null && portIP > 0) {
            System.out.println("Serveur IP : " + serveurIP);
            System.out.println("Port IP : " + portIP);
        } else {
            System.out.println("Impossible de lire les propriétés de configuration.");
        }
    }
}
