package model.common;

public class CommunicationConfig {
    private static CommunicationConfig gInstance = new CommunicationConfig();

    private CommunicationConfig() {
        //TODO load config
    }

    public static CommunicationConfig instance() {
        return gInstance;
    }
}
