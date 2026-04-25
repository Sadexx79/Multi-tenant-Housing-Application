package util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Tenant;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class FileHandler {
    public static void export(List<Tenant> tenants, Window window) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save CSV");
        File file = chooser.showSaveDialog(window);

        try (FileWriter writer = new FileWriter(file)) {
            for (Tenant t : tenants) {
                writer.write(t.getName()+","+t.getRent()+","+t.getUtilities()+","+t.getLeaseMonths()+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
