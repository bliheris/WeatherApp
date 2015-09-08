package mb.pl.weatherapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileAccess {

    private Context context;
    private String fileName;

    public FileAccess(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveForecast(String forecast) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(forecast);
        }finally {
            if(writer != null) {
                writer.close();
            }
        }
    }

    public String loadForecast() throws IOException {
        String result = "";
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }
}
