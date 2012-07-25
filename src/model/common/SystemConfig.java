package model.common;

public class SystemConfig {
	
    private static SystemConfig gInstance = new SystemConfig();

    private SystemConfig() {
        //TODO load config
    }

    public static SystemConfig instance() {
        return gInstance;
    }
}
