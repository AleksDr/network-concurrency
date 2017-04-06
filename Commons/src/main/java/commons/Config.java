package commons;

/**
 * Created by aleks on 25.02.2017.
 */
public interface Config {

  void loadFromFileDefault();

  void loadFromFileUserDef();

  void writeToFileUserDef();

  String getProperty(String key);

  void storeProperty(String key, String value);


}
